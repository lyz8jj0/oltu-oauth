package com.lxy.service.impl;

import com.lxy.entity.Login;
import com.lxy.mapper.LoginMapper;
import com.lxy.service.ILoginService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author welsee
 * @since 2018-11-17
 */
@Service
public class LoginServiceImpl extends ServiceImpl<LoginMapper, Login> implements ILoginService {

}
