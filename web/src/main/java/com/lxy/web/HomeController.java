package com.lxy.web;

import com.lxy.tools.BaseController;
import com.lxy.tools.BaseModelAndView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Slf4j
@Controller
public class HomeController extends BaseController {

    /**
     * 默认页面
     *
     * @return bmv
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public BaseModelAndView index() {
        BaseModelAndView bmv = new BaseModelAndView();
        bmv.setViewName("home/index");
        return bmv;
    }
}
