package com.lxy.service;

import com.lxy.entity.AccessToken;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author login
 * @since 2018-11-17
 */
public interface IAccessTokenService extends IService<AccessToken> {

    /**
     * @param clientId           应用id
     * @param uId                用户id
     * @param tokenType          token类型(暂时不知道有什么用,先存数据库)
     * @param authorizationToken token值
     * @param expires            token超时时间
     */
    void saveAccessToken(String clientId, String uId, String tokenType, String authorizationToken, String expires) throws Exception;
}
