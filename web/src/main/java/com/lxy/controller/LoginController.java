package com.lxy.controller;


import com.lxy.entity.Login;
import com.lxy.entity.Person;
import com.lxy.exception.ProgramException;
import com.lxy.service.ILoginService;
import com.lxy.service.IPersonService;
import com.lxy.tools.BaseController;
import com.lxy.tools.ConstantKey;
import com.lxy.tools.JsonResult;
import com.lxy.tools.MD5Util;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import sun.security.provider.MD5;

import javax.servlet.http.HttpServletRequest;


/**
 * 登录控制器
 *
 * @author lxy
 * @since 2018-11-17
 */
@Controller
@Slf4j
public class LoginController extends BaseController {

//    @Autowired
//    private ILoginService loginService;
//
//    @Autowired
//    private IPersonService personService;
//
//    @RequestMapping(value = "login", method = RequestMethod.POST)
//    @ResponseBody
//    public JsonResult login(HttpServletRequest request) {
//        Session session = SecurityUtils.getSubject().getSession();
//        if (session.getAttribute("MEMBER_USER_KEY") == null) {
//            try {
//                if (!validateOAuth2Pwd(request)) {
//                    return renderError("用户名或密码错误!");
//                }
//            } catch (ProgramException p) {
//                return renderError(p.getMessage());
//            } catch (Exception e) {
//                log.error("登录异常." + e.getMessage());
//                return renderError("登录异常");
//            }
//        }
//        return renderSuccess("ok");
//    }

//    private boolean validateOAuth2Pwd(HttpServletRequest request) throws Exception {
//        if ("get".equalsIgnoreCase(request.getMethod())) {
//            return false;
//        }
//        String username = request.getParameter("txtUserName");
//        String password = request.getParameter("txtPassword");
//
//        //验证前台传过来的用户名密码是否为空
//        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
//            throw new ProgramException("用户名和密码不能为空!");
//        }
//
//        //TODO shiro验证
//        UsernamePasswordToken token = new UsernamePasswordToken(username, MD5Util.GetMD5Code(password));
//
//        //获取当前Subject
//        Subject currentUser = SecurityUtils.getSubject();
//        try {
//            // 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
//            // 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
//            // 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
//            log.info("对用户[" + username + "]进行登录验证..验证开始");
//            currentUser.login(token);
//            log.info("对用户[" + username + "]进行登录验证..验证通过");
//
//        } catch (Exception e) {
//            // 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
//            log.info("对用户[" + username + "]进行登录验证..验证未通过,堆栈轨迹如下");
//            e.printStackTrace();
//
//        }
//        //验证是否登录成功
//        if (currentUser.isAuthenticated()) {
//            log.info("用户[" + username + "]登录认证通过(这里可以进行一些认证通过后的一些系统参数初始化操作)");
//            Login login = loginService.selectByLoginName(username);
//            Person person = personService.selectByLoginId(login.getId());
//            Session session = currentUser.getSession();
//            session.setAttribute("MEMBER_LOGIN_KEY", login.getId());
//            session.setAttribute("MEMBER_USER_REAL_NAME", login.getRealname());
//            session.setAttribute("MEMBER_USER_KEY", person.getId());
//            return true;
//        } else {
//            token.clear();
//            throw new Exception("用户名或密码错误!");
//        }
//    }

//    @RequestMapping(value = "oauth2/login", method = RequestMethod.GET)
//    public ModelAndView loginin() {
//        ModelAndView mv = new ModelAndView();
//        mv.setViewName("oauth2/login");
//        return mv;
//    }

//    @RequestMapping(value = "login", method = RequestMethod.GET)
//    public ModelAndView login() {
//        ModelAndView mv = new ModelAndView();
//        mv.setViewName("login/login");
//        return mv;
//    }

    @RequestMapping(value = "signIn", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult login(HttpServletRequest request) {
//        String client_id = request.getParameter("client_id");
//        log.info("=============="+client_id);
        try {
            if (!validateOAuth2Pwd(request)) {
                return renderError("用户名或密码错误！");
            }
        } catch (ProgramException p) {
            return renderError(p.getMessage());
        } catch (Exception ex) {
            log.error("登录异常.", ex);
            return renderError("登录异常！");
        }
        return renderSuccess("登录成功");
    }

    /**
     * 验证登录名和密码是否正确,如正确将用户名存入session
     *
     * @param request request请求
     * @return boolean
     */
    private boolean validateOAuth2Pwd(HttpServletRequest request) throws Exception {
        if ("get".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        //验证前台传过来的用户名密码是否为空
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            throw new ProgramException("用户名或密码不可以为空!");
        }
        try {
            if (username.equalsIgnoreCase("lixinyu") && password.equalsIgnoreCase("123456")) {
                //登录成功,将用户名存入session
                request.getSession().setAttribute(ConstantKey.MEMBER_SESSION_KEY, "lixinyu");
                return true;
            }else {
                throw new ProgramException("用户名密码错误!");
            }
        } catch (Exception e) {
            log.error("validateOAuth2Pwd Exception: " + e.getMessage());
            return false;
        }
    }


}
