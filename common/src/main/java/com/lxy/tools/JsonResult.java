package com.lxy.tools;

public class JsonResult {

    /*返回成功与否*/
    private boolean success;

    /*返回状态码*/
    private String status;

    /*返回信息*/
    private String msg;

    /*返回数据*/
    private Object data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
