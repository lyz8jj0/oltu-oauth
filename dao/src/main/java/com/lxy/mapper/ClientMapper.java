package com.lxy.mapper;

import com.lxy.entity.Client;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author welsee
 * @since 2018-11-17
 */
public interface ClientMapper extends BaseMapper<Client> {

    /**
     * 通过clientId查询第三方应用信息
     *
     * @param clientId 第三应用clientId
     * @return
     */
    Client getClientByClientId(String clientId);
}
