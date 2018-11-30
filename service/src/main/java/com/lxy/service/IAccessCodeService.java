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
     * 保存code
     *
     * @param clientId          第三方应用clientId
     * @param uId               用户id
     * @param expires           code的过期时间
     * @param authorizationCode oauth2产生的code
     * @return
     */
    int addAccessCode(String clientId, String uId, String expires, String authorizationCode);
}
