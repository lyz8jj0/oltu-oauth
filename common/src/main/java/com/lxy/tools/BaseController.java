package com.lxy.tools;

public class BaseController {
    /**
     * 渲染失败数据
     *
     * @return result
     */
    private JsonResult renderError() {
        JsonResult result = new JsonResult();
        result.setSuccess(false);
        result.setStatus("500");
        return result;
    }

    /**
     * 渲染失败数据（带消息）
     *
     * @param msg 需要返回的消息
     * @return result
     */
    protected JsonResult renderError(String msg) {
        JsonResult result = renderError();
        result.setMsg(msg);
        return result;
    }

    /**
     * 渲染成功数据
     *
     * @return result
     */
    private JsonResult renderSuccess() {
        JsonResult result = new JsonResult();
        result.setSuccess(true);
        result.setStatus("200");
        return result;
    }

    /**
     * 渲染成功数据（带信息）
     *
     * @param msg 需要返回的信息
     * @return result
     */
    protected JsonResult renderSuccess(String msg) {
        JsonResult result = renderSuccess();
        result.setMsg(msg);
        return result;
    }

    /**
     * 渲染成功数据（带数据）
     *
     * @param data 需要返回的对象
     * @return result
     */
    protected JsonResult renderSuccess(Object data) {
        JsonResult result = renderSuccess();
        result.setData(data);
        return result;
    }

    /**
     * 渲染成功数据(带信息和数据)
     *
     * @param msg  需要返回的信息
     * @param data 需要返回的对象
     * @return result
     */
    protected JsonResult renderSuccess(String msg, Object data) {
        JsonResult result = renderSuccess();
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}
