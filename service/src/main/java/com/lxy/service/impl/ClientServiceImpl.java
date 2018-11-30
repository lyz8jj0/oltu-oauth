package com.lxy.service.impl;

import com.lxy.entity.Client;
import com.lxy.exception.ProgramException;
import com.lxy.mapper.ClientMapper;
import com.lxy.service.IClientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 第三方应用接口实现类
 *
 * @author login
 * @since 2018-11-17
 */
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements IClientService {

    @Override
    public boolean validateClientByClientId(String clientId) throws Exception {
        if (clientId == null) {
            throw new ProgramException("验证第三方应用参数不合法");
        }
        boolean flag = false;
        Client client = baseMapper.getClientByClientId(clientId);
        if (client != null) {
            //如果第三应用的id不为空并且不为null
            if (client.getId() != null && !"".equals(client.getId())) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public String getClientNameByClientId(String clientId) throws Exception {
        if (clientId == null) {
            throw new ProgramException("获取第三方应用名称参数不合法");
        }
        Client client = baseMapper.getClientByClientId(clientId);
        return client.getClientName();
    }
}
