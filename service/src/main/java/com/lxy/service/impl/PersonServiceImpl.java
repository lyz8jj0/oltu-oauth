package com.lxy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.entity.Person;
import com.lxy.mapper.PersonMapper;
import com.lxy.service.IPersonService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author login
 * @since 2018-11-27
 */
@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements IPersonService {

    @Override
    public Person selectByLoginId(String loginId) {

        return baseMapper.getPersonByLoginId(loginId);
    }
}
