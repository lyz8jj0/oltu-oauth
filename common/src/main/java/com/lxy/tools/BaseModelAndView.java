package com.lxy.tools;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.Map;

public class BaseModelAndView extends ModelAndView {

    public BaseModelAndView() {
        super();
        this.setConfig();
    }

    public BaseModelAndView(String viewName) {
        super(viewName);
        this.setConfig();
    }

    public BaseModelAndView(View view) {
        super(view);
        this.setConfig();
    }

    public BaseModelAndView(String viewName, Map<String, ?> model) {
        super(viewName, model);
        this.setConfig();
    }


    public BaseModelAndView(View view, Map<String, ?> model) {
        super(view, model);
        this.setConfig();
    }


    public BaseModelAndView(String viewName, HttpStatus status) {
        super(viewName, status);
        this.setConfig();
    }


    public BaseModelAndView(String viewName, Map<String, ?> model, HttpStatus status) {
        super(viewName, model, status);
        this.setConfig();
    }

    public BaseModelAndView(String viewName, String modelName, Object modelObject) {
        super(viewName, modelName, modelObject);
        this.setConfig();
    }

    public BaseModelAndView(View view, String modelName, Object modelObject) {
        super(view, modelName, modelObject);
        this.setConfig();
    }

    private void setConfig() {
        this.addObject("version", Config.webVersion);
    }
}
