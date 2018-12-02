package com.lxy.controller;


import com.lxy.exception.ProgramException;
import com.lxy.tools.BaseController;
import com.lxy.tools.ConstantKey;
import com.lxy.tools.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

    /**
     * 用户登录接口
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "signIn", method = RequestMethod.POST)
    @ResponseBody
    public JsonResult login(HttpServletRequest request) {
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
     * 用户直接访问登录页面
     *
     * @return
     */
    @RequestMapping(value = "signIn", method = RequestMethod.GET)
    public ModelAndView login() {
        ModelAndView mv = new ModelAndView();
        mv.addObject("client_id", "");
        mv.addObject("redirect_uri", "");
        mv.addObject("response_type", "");
        mv.setViewName("login/login");
        return mv;
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
            } else {
                throw new ProgramException("用户名密码错误!");
            }
        } catch (Exception e) {
            log.error("validateOAuth2Pwd Exception: " + e.getMessage());
            return false;
        }
    }


}
