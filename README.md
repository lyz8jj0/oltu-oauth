
> 最近搭建基础平台涉及到oauth2和shiro，了解到这两中技术对以后的能力提升有一定的提高作用，所以尝试自己搭建个demo学习下，先把oauth2记录下，shiro过几天写写(项目源码在最下面)

> 要实现OAuth服务端，就得先理解客户端的调用流程，服务提供商实现可能也有些区别，实现OAuth服务端的方式很多,这里记录的是<font color=red>授权码模式的实现</font>,有需要同学可以参考下, 这里只涉及实现,具体流程网上有很多,推荐比较好的有:[阮一峰的网络日志:理解OAuth 2.0](http://www.ruanyifeng.com/blog/2014/05/oauth_2_0.html)


### 实现主要涉及参数配置如下:
 #####  授权码设置(code)
第三方通过code进行获取 access_token的时候需要用到，code的超时时间为10分钟，一个code只能成功换取一次access_token即失效。 
##### 令牌有效期(access_token) 
access_token是调用授权关系接口的调用凭证，由于access_token有效期（目前为2个小时）较短，当access_token超时后，可以使用refresh_token进行刷新，access_token刷新结果有两种： 
&emsp;1.若access_token已超时，那么进行refresh_token会获取一个新的access_token，新的超时时间； 
&emsp;2.若access_token未超时，那么进行refresh_token不会改变access_token，但超时时间会刷新，相当于续期access_token。 
refresh_token拥有较长的有效期（30天），当refresh_token失效的后，需要用户重新授权。
<font color=red>注意: 我这里没有写refresh_token的实现,主要是觉得意义不是很大,所以这里的token失效后需要从开始走一遍流程来获取token,后期如果需要refresh_token的话我会更改</font> 
<br>

># 项目介绍
- 项目结构如下
AccessCodeController:获取授权码控制器(用于获取code)
AccessTokenController:获取令牌控制器(用于获取token)
ClientController:客户端控制器(用于获取code后的回调页面展示)
LoginController:登录控制器(用于用户登录授权)
HomeController:默认访问页面(http://localhost:8080/)

- 项目目录结构

<img src="https://img-blog.csdnimg.cn/20181202213536999.png?x-oss-process=image/watermark,,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zOTk3MzgxMA==,size_16,color_FFFFFF,t_70 " width="300">
<br>

>#### 获取授权码(GET)
><font color=orange>http://localhost:8080/oauth2/authorize?client_id=fbed1d1b4b1449daa4bc49397cbe2350&response_type=code&redirect_uri=http://localhost:8080/client/oauth_callback</font>



```java

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
```

第一次请求
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181202212236366.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zOTk3MzgxMA==,size_16,color_FFFFFF,t_70)
<br>
用户确认登录授权,生成code存入数据库,并将code值传到第一请求的回调url(redirect_uri )中,拿到code准备获取token
![在这里插入图片描述](https://img-blog.csdnimg.cn/20181202212742552.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zOTk3MzgxMA==,size_16,color_FFFFFF,t_70)


<br>
<br>

>#### 获取令牌(POST,没有写获取令牌的页面,我用的postman请求)
><font color=orange> http://localhost:8080/oauth2/access_token?client_id={AppKey}&client_secret={AppSecret}&grant_type=authorization_code&redirect_uri={YourSiteUrl}&code={code}</font>


```java
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

```
<br>
使用生成的code来调用接口获取token,同时将token信息存入access_token表中,删除code信息,保证code只能使用一次

![在这里插入图片描述](https://img-blog.csdnimg.cn/20181202213015384.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dlaXhpbl8zOTk3MzgxMA==,size_16,color_FFFFFF,t_70)

> #### 生成的token即可用于获取资源

>#### 项目源码 [https://github.com/lyz8jj0/oltu-oauth](https://github.com/lyz8jj0/oltu-oauth)
