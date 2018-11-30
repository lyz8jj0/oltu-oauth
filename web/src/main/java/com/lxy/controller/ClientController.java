package com.lxy.controller;


import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author login
 * @since 2018-11-17
 */
@Controller
@RequestMapping("/client")
public class ClientController {

    @RequestMapping("oauth_callback")
    public ModelAndView oauth_callback(HttpServletRequest request) {
        String code = request.getParameter("code");

        ModelAndView mv = new ModelAndView();
        mv.addObject("code",code);
        mv.setViewName("client/oauth_callback");
        return mv;
    }


}
