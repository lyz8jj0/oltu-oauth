package com.lxy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lxy.entity.Person;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author login
 * @since 2018-11-27
 */
public interface PersonMapper extends BaseMapper<Person> {

    /**
     * 通过登录名的id获取登陆用户的信息
     *
     * @param loginId 登录id
     * @return person
     */
    Person getPersonByLoginId(String loginId);
}
