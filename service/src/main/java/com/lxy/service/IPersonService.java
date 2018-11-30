package com.lxy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lxy.entity.Person;

/**
 * 人员接口
 *
 * @author lxy
 * @since 2018-11-27
 */
public interface IPersonService extends IService<Person> {

    /**
     * 通过登录名的id获取登陆用户的信息
     *
     * @param loginId 登录id
     * @return person
     */
    Person selectByLoginId(String loginId);
}
