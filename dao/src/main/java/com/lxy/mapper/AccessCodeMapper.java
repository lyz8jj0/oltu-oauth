package com.lxy.mapper;

import com.lxy.entity.AccessCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.time.LocalDateTime;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author login
 * @since 2018-11-17
 */
public interface AccessCodeMapper extends BaseMapper<AccessCode> {

    /**
     * 删除超时的code
     *
     * @param localDateTime 超时code的一个时间结点
     */
    void delTimeOutCode(LocalDateTime localDateTime);

    /**
     * 获取授权码(code)的详细信息
     *
     * @param clientId          应用id
     * @param authorizationCode 授权码
     * @return
     */
    AccessCode getAccessCode(String clientId, String authorizationCode);

    /**
     * 删除授权码信息
     *
     * @param authorizationCode 授权码
     */
    void delAccessCode(String authorizationCode);
}
