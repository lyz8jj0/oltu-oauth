package com.lxy.controller;


import com.lxy.service.IAccessCodeService;
import com.lxy.tools.BaseController;
import com.lxy.tools.CommonUtil;
import com.lxy.tools.ConstantKey;
import com.lxy.tools.RegexUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 授权码code控制器
 *
 * @author lxy
 * @since 2018-11-17
 */
@Controller
@RequestMapping("/oauth2")
@Slf4j
public class AccessCodeController extends BaseController {

    @Autowired
    IAccessCodeService accessCodeService;

    /**
     * 构建OAuth2授权请求 [需要client_id与redirect_uri绝对地址]
     *
     * @param request
     * @param session
     * @param mv
     * @return 返回授权码(code)有效期10分钟，客户端只能使用一次[与client_id和redirect_uri一一对应关系]
     * @throws OAuthSystemException
     * @url http://localhost:8080/oauth2/authorize?client_id={AppKey}&response_type=code&redirect_uri={YourSiteUrl}
     * @test http://localhost:8080/oauth2/authorize?client_id=fbed1d1b4b1449daa4bc49397cbe2350&response_type=code&redirect_uri=http://localhost:8080/client/oauth_callback
     */
    @RequestMapping(value = "authorize", method = RequestMethod.GET)
    public ModelAndView authorize(HttpServletRequest request, HttpSession session, ModelAndView mv) throws OAuthSystemException {
        try {
            if (mv == null) {
                mv = new ModelAndView();
            }
            //构建OAuth请求
            OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
            //验证redirect_uri格式是否合法
            if (!RegexUtil.isUrl(oauthRequest.getRedirectURI())) {
                OAuthResponse oAuthResponse = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED) //错误请求:非法请求
                        .setError(OAuthError.CodeResponse.INVALID_REQUEST) //错误原因:无效请求
                        .setErrorDescription("url不合法") //错误描述:错误的url
                        .buildJSONMessage();
                mv.setViewName("oauth2/error");
                //将错误信息渲染到页面
                mv.addObject("errorMsg", oAuthResponse.getBody());
                return mv;
            }

            //验证clientId是否正确
            if (!CommonUtil.validateOAuth2ClientId(oauthRequest)) {
                OAuthResponse oAuthResponse = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED) //错误请求:非法请求
                        .setError(OAuthError.CodeResponse.ACCESS_DENIED) //错误原因:拒绝访问
                        .setErrorDescription("无效的客户端Id") //错误描述:未受权的第三方应用
                        .buildJSONMessage();
                mv.setViewName("oauth2/error");
                //将错误信息渲染到页面
                mv.addObject("errorMsg", oAuthResponse.getBody());
                return mv;
            }


            //查询第三方应用的信息
            String clientName = "Just Test Client";//clientService.getClientNameByClientId(oauthRequest.getClientId())
            mv.addObject("clientName", clientName); //第三方应用名称
            mv.addObject("response_type", oauthRequest.getResponseType()); //表示授权类型,固定值code
            mv.addObject("client_id", oauthRequest.getClientId()); //第三方应用id
            mv.addObject("redirect_uri", oauthRequest.getRedirectURI()); //重定向URI
            mv.addObject("scope", oauthRequest.getScopes());//申请的权限范围，可选项

            //验证用户是否已登录,如果未登录,跳到登录界面
            if (session.getAttribute(ConstantKey.MEMBER_SESSION_KEY) == null) {
                //登录失败跳转到登陆页
                mv.setViewName("login/login");
                return mv;
            }

            //生成授权码(authorizationCode)  使用UUIDValueGenerator或者MD5Generator
            String authorizationCode = new OAuthIssuerImpl(new MD5Generator()).authorizationCode();
            log.info("生成授权码为===============" + authorizationCode);

            //将session中的用户id取出来和code存在一起
            String uId = session.getAttribute(ConstantKey.MEMBER_SESSION_KEY).toString();

            //设置code过期时间(10分钟)
            String expires = CommonUtil.codeExpires();

            //TODO 以后可以考虑把授权码和令牌存入缓存，现在先分别存到access_code和access_token中

            try {
                //将授权的code信息存入数据库
                accessCodeService.saveAuthorizationCode(oauthRequest.getClientId(), uId, expires, authorizationCode);
            } catch (Exception e) {
                mv.setViewName("oauth2/error");
                mv.addObject("errorMsg", e.getMessage());
                log.error("保存Code异常." + e.getMessage());
                return mv;
            }

            //构建OAuth2授权返回信息
            OAuthResponse oAuthResponse = OAuthASResponse
                    .authorizationResponse(request, HttpServletResponse.SC_FOUND) //重定向302
                    .setCode(authorizationCode)
                    .location(oauthRequest.getParam(OAuth.OAUTH_REDIRECT_URI)) //授权返回的重定向url
                    .buildQueryMessage();
            //申请令牌成功重定向到客户端页
            mv = new ModelAndView("redirect:" + oAuthResponse.getLocationUri());
            return mv;
        } catch (OAuthProblemException ex) {
            OAuthResponse oAuthResponse = OAuthResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED)
                    .error(ex)
                    .buildJSONMessage();
            mv.setViewName("oauth2/error");
            mv.addObject("errorMsg", oAuthResponse.getBody());
            log.error("/authorize请求异常." + ex.getMessage());
            return mv;
        }
    }
}

