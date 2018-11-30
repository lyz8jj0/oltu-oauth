package com.lxy.service;

import com.lxy.entity.Client;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 第三方应用接口
 *
 * @author login
 * @since 2018-11-17
 */
public interface IClientService extends IService<Client> {

    /**
     * 验证客户端clientId是否正确
     *
     * @param clientId 应用ID
     * @return true/false
     */
    boolean validateClientByClientId(String clientId) throws Exception;

    /**
     * 查询第三方应用名称
     *
     * @param clientId 应用ID
     * @return String
     */
    String getClientNameByClientId(String clientId) throws Exception;
}


