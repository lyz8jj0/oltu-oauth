package com.lxy.mapper;

import com.lxy.entity.Login;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author login
 * @since 2018-11-17
 */
public interface LoginMapper extends BaseMapper<Login> {

    /**
     * 根据登陆名获取登录用户信息
     *
     * @param username 登录名
     * @return login
     */
    Login getLoginInfoByLoginName(String username);
}
