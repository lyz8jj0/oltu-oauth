package com.lxy.service.impl;

import com.lxy.entity.Client;
import com.lxy.mapper.ClientMapper;
import com.lxy.service.IClientService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 第三方应用接口实现类
 *
 * @author welsee
 * @since 2018-11-17
 */
@Service
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements IClientService {

    @Override
    public boolean validateClientByClientId(String clientId) throws Exception {
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
        Client client = baseMapper.getClientByClientId(clientId);
        return client.getClientName();
    }
}
