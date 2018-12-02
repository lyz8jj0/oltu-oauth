package com.lxy.service;

import com.lxy.entity.AccessCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * 授权码service
 *
 * @author login
 * @since 2018-11-17
 */
public interface IAccessCodeService extends IService<AccessCode> {

    /**
     * 保存授权code
     *
     * @param clientId          应用id
     * @param uId               用户id
     * @param expires           code 过期时间
     * @param authorizationCode oauth生成的code
     */
    void saveAuthorizationCode(String clientId, String uId, String expires, String authorizationCode) throws Exception;


    /**
     * 获取code的信息
     * @param clientId      应用ID
     * @param authorizationCode     oauth产生的Code
     * @return
     */
    AccessCode getAuthorizationCodeInfo(String clientId,String authorizationCode) throws Exception;

    /**
     * 删除授权码信息
     * @param authorizationCode 授权码
     */
    void delAccessCode(String authorizationCode) throws Exception;
}
