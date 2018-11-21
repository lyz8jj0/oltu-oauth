package com.lxy.web;


import com.lxy.service.IAccessCodeService;
import com.lxy.service.IClientService;
import com.lxy.tools.BaseController;
import com.lxy.tools.BaseModelAndView;
import com.lxy.tools.RegexUtil;
import com.lxy.tools.UUIDUtil;
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
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author welsee
 * @since 2018-11-17
 */
@Controller
@RequestMapping("/oauth2")
@Slf4j
public class AccessCodeController extends BaseController {

    @Autowired
    private IClientService clientService; //第三方应用service

    @Autowired
    private IAccessCodeService accessCodeService; //授权码service

    @RequestMapping(value = "authorize", method = RequestMethod.GET)
    public BaseModelAndView authorize(HttpServletRequest request, HttpSession session, BaseModelAndView bmv) throws Exception {
        try {

            if (bmv == null) {
                bmv = new BaseModelAndView();
            }

            //构建OAuth请求
            OAuthAuthzRequest oAuthAuthzRequest = new OAuthAuthzRequest(request);
            //验证redirect_uri格式是否合法(8080端口测试)
            if (!RegexUtil.isUrl(oAuthAuthzRequest.getRedirectURI())) {
                OAuthResponse oAuthResponse = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED) //错误请求:非法请求
                        .setError(OAuthError.CodeResponse.INVALID_REQUEST) //错误原因:无效请求
                        .setErrorDescription(OAuthError.OAUTH_ERROR_URI) //错误描述:错误的url
                        .buildJSONMessage();
                bmv.setViewName("authz/error");
                //将错误信息渲染到页面
                bmv.addObject("errorMsg", oAuthResponse.getBody());
            }

            //验证clientId是否正确
            if (!clientService.validateClientByClientId(oAuthAuthzRequest.getClientId())) {
                OAuthResponse oAuthResponse = OAuthASResponse
                        .errorResponse(HttpServletResponse.SC_UNAUTHORIZED) //错误请求:非法请求
                        .setError(OAuthError.CodeResponse.ACCESS_DENIED) //错误原因:拒绝访问
                        .setErrorDescription(OAuthError.CodeResponse.UNAUTHORIZED_CLIENT) //错误描述:未受权的第三方应用
                        .buildJSONMessage();
                bmv.setViewName("authz/error");
                //将错误信息渲染到页面
                bmv.addObject("errorMsg", oAuthResponse.getBody());
            }


            //查询第三方应用的信息
            String clientName = clientService.getClientNameByClientId(oAuthAuthzRequest.getClientId());
            bmv.addObject("clientName", clientName); //第三方应用名称
            bmv.addObject("response_type", oAuthAuthzRequest.getResponseType()); //表示授权类型,固定值code
            bmv.addObject("client_id", oAuthAuthzRequest.getClientId()); //第三方应用id
            bmv.addObject("redirect_uri", oAuthAuthzRequest.getRedirectURI()); //重定向URI
            bmv.addObject("scope", oAuthAuthzRequest.getScopes());//申请的权限范围，可选项
            bmv.addObject("picture_uuid", UUIDUtil.getUUID());

            //验证用户是否已登录,如果未登录,跳到登录界面
            if (session.getAttribute("MEMBER_USER_KEY") == null) {
                bmv.setViewName("welsee/login");
                return bmv;
            }

            //生成授权码(authorizationCode)  使用UUIDValueGenerator或者MD5Generator
            String uId = session.getAttribute("MEMBER_USER_KEY").toString();
            String authorizationCode = new OAuthIssuerImpl(new MD5Generator()).authorizationCode();

            //设置授权码的过期时间
            Date date = new Date();
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            rightNow.add(Calendar.MINUTE, 10);
            String expires = Long.toString(rightNow.getTimeInMillis());

            try {
                //将第三方应用id,用户id,授权码的过期时间,以及成的授权码存入数据库中
                accessCodeService.addAccessCode(oAuthAuthzRequest.getClientId(), uId, expires, authorizationCode);
            } catch (Exception e) {
                bmv.setViewName("welsee/login");
                bmv.addObject("errorMsg", e.getMessage());
                log.error("保存Code异常." + e.getMessage());
                return bmv;
            }
            //将授权码存入缓存中
            //构建OAuth2授权返回信息
            OAuthResponse oAuthResponse = OAuthASResponse
                    .authorizationResponse(request, HttpServletResponse.SC_FOUND) //重定向302
                    .setCode(authorizationCode)
                    .location(oAuthAuthzRequest.getParam(OAuth.OAUTH_REDIRECT_URI)) //授权返回的重定向url
                    .buildQueryMessage();
            bmv = new BaseModelAndView("redirect:" + oAuthResponse.getLocationUri());
            return bmv;
        } catch (OAuthProblemException e) {
            OAuthResponse oAuthResponse = OAuthResponse
                    .errorResponse(HttpServletResponse.SC_UNAUTHORIZED) //错误请求:非法请求
                    .error(e)
                    .buildJSONMessage();
            bmv.setViewName("authz/error");
            bmv.addObject("errorMsg", oAuthResponse.getBody());
            log.error("/authorize请求异常." + e.getMessage());
            return bmv;
        }
    }
}
