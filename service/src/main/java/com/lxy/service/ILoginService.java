package com.lxy.service;

import com.lxy.entity.Login;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 登录接口
 *
 * @author login
 * @since 2018-11-17
 */
public interface ILoginService extends IService<Login> {

    /**
     * 根据登陆名获取登录用户信息
     *
     * @param username 登录名
     * @return login
     */
    Login selectByLoginName(String username);
}
