package com.lxy.controller;


import com.lxy.entity.AccessCode;
import com.lxy.service.IAccessCodeService;
import com.lxy.service.IAccessTokenService;
import com.lxy.tools.CommonUtil;
import com.lxy.tools.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 授权码token控制器(令牌)
 *
 * @author lxy
 * @since 2018-11-17
 */
@Controller
@Slf4j
@RequestMapping("/oauth2")
public class AccessTokenController {

    @Autowired
    IAccessCodeService accessCodeService;

    @Autowired
    IAccessTokenService accessTokenService;

    /**
     * 认证服务器申请令牌(AccessToken) [验证client_id、client_secret、auth code的正确性] (暂时没有使用涉及到更新令牌)
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     * @url (POST)  http://localhost:8080/oauth2/access_token?client_id={AppKey}&client_secret={AppSecret}&grant_type=authorization_code&redirect_uri={YourSiteUrl}&code={code}
     */
    @RequestMapping(value = "access_token", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult access_token(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //构建返回格式(jsonResult是自定义的)
        JsonResult jsonResult = new JsonResult();
        try {
            //构建oauth2请求
            OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

            //验证clientId是否正确
            if (!CommonUtil.validateOAuth2ClientId(oauthRequest)) {
                OAuthResponse oAuthResponse = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED) //错误请求:非法请求
                        .setError(OAuthError.CodeResponse.ACCESS_DENIED) //错误原因:拒绝访问
                        .setErrorDescription("无效的客户端Id") //错误描述:未受权的第三方应用
                        .buildJSONMessage();
                jsonResult.setSuccess(false);
                jsonResult.setMsg(oAuthResponse.getBody());
                return jsonResult;
            }

            //验证clientSecret是否正确
            if (!CommonUtil.validateOAuth2ClientSecret(oauthRequest)) {
                OAuthResponse oAuthResponse = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setError(OAuthError.CodeResponse.ACCESS_DENIED)
                        .setErrorDescription("clientSecret不合法")
                        .buildJSONMessage();
                jsonResult.setSuccess(false);
                jsonResult.setMsg(oAuthResponse.getBody());
            }

            //todo 验证第一次申请code时的redirect_uri和第这一交申请令牌的redirect_uri是否一致(必须一致才能通过) (暂时没做)

            //grant_type：表示使用的授权模式，必选项，传过来的值固定为"authorization_code",否则不通过
            if (GrantType.AUTHORIZATION_CODE.name().equalsIgnoreCase(oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE))) {

                //获取传过来的code,用于获取code的详细信息
                String authorizationCode = oauthRequest.getCode();

                //验证code是否有效
                if (!validateAuthorizationCode(oauthRequest.getClientId(), authorizationCode)) {
                    OAuthResponse oauthResponse = OAuthASResponse
                            .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                            .setError(OAuthError.CodeResponse.UNAUTHORIZED_CLIENT)
                            .setErrorDescription(OAuthError.CodeResponse.UNAUTHORIZED_CLIENT)
                            .buildJSONMessage();
                    jsonResult.setSuccess(false);
                    jsonResult.setMsg(oauthResponse.getBody());
                    return jsonResult;
                }

                //生成token
                OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
                final String authorizationToken = oauthIssuerImpl.accessToken();
                log.info("生成授权令牌为===============" + authorizationToken);

                //设置token过期时间(24小时)
                String expires = CommonUtil.tokenExpires();

                //通过clientId和code值获取该条授权码的信息,用于获取用户id
                AccessCode accessCode = accessCodeService.getAuthorizationCodeInfo(oauthRequest.getClientId(), authorizationCode);
                String uId = accessCode.getUserId();

                //将access_token信息存入数据库,token_type：表示令牌类型，该值大小写不敏感，必选项，可以是bearer类型或mac类型,
                //具体token_type有哪些区别可以看一下 https://blog.csdn.net/weixin_39973810/article/details/84673548,但具体何用还是不清楚,算了先存0吧,用到在改
                accessTokenService.saveAccessToken(oauthRequest.getClientId(), uId, "0", authorizationToken, expires);

                //删除授权码（code)保证授权码只能使用一次
                accessCodeService.delAccessCode(authorizationCode);

                //构建oauth2授权返回信息
                OAuthResponse oAuthResponse = OAuthASResponse
                        .tokenResponse(HttpServletResponse.SC_OK) //获取授权token成功
                        .setAccessToken(authorizationToken) //授权令牌(token)
                        .setExpiresIn(expires)
                        .buildJSONMessage();
                response.setStatus(oAuthResponse.getResponseStatus());

                //将token值和过期时间以json的格式返回给第三方应用

                Map<String, String> map = new HashMap<>();
                map.put("token", authorizationToken);
                map.put("expires", expires);
                jsonResult.setSuccess(true);
                jsonResult.setData(map);
                return jsonResult;
            } else {
                OAuthResponse oauthResponse = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                        .setError(OAuthError.TokenResponse.INVALID_GRANT)
                        .setErrorDescription("grant_type is not authorization_code")
                        .buildJSONMessage();
                jsonResult.setSuccess(false);
                jsonResult.setMsg(oauthResponse.getBody());
                return jsonResult;
            }
        } catch (OAuthProblemException e) {
            OAuthResponse oAuthResponse = OAuthResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED) //错误请求:非法请求
                    .error(e)
                    .buildJSONMessage();
            response.setStatus(oAuthResponse.getResponseStatus());
            jsonResult.setSuccess(false);
            jsonResult.setMsg(oAuthResponse.getBody());
            return jsonResult;
        }
    }


    /**
     * 验证code 是否正确和有效
     *
     * @param clientId          应用id
     * @param authorizationCode oauth产生的Code
     * @return
     */
    public boolean validateAuthorizationCode(String clientId, String authorizationCode) {
        boolean flag = false;
        try {
            AccessCode accessCode = accessCodeService.getAuthorizationCodeInfo(clientId, authorizationCode);
            if (accessCode.getId() != null || accessCode.getId() != "") {
                LocalDateTime localDateTime = LocalDateTime.now();
                LocalDateTime codeCreatedatetime = accessCode.getCreatedatetime(); //code创建时间
                LocalDateTime codeExpires = codeCreatedatetime.plusMinutes(10); //code失效时间(创建时间增加10分钟)
                if (localDateTime.isBefore(codeExpires)) { //如果当前时间在失效时间之前,说明code可用
                    flag = true;
                }
            }
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

}
