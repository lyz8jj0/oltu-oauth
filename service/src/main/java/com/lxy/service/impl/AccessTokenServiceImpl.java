package com.lxy.service.impl;

import com.lxy.entity.AccessToken;
import com.lxy.mapper.AccessTokenMapper;
import com.lxy.service.IAccessTokenService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.tools.UUIDUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author login
 * @since 2018-11-17
 */
@Service
public class AccessTokenServiceImpl extends ServiceImpl<AccessTokenMapper, AccessToken> implements IAccessTokenService {

    @Override
    public void saveAccessToken(String clientId, String uId, String tokenType, String authorizationToken, String expires) throws Exception {
        AccessToken accessToken = new AccessToken();
        accessToken.setAccessToken(authorizationToken);
        accessToken.setClientId(clientId);
        accessToken.setExpires(expires);
        accessToken.setUserId(uId);
        accessToken.setId(UUIDUtil.getUUID());
        accessToken.setTokenType(tokenType);
        accessToken.setCreatedatetime(LocalDateTime.now());
        accessToken.setModifydatetime(LocalDateTime.now());
        baseMapper.insert(accessToken);

        //每次插入新的token时,删除已经过期的token
        LocalDateTime localDateTime = LocalDateTime.now().minusHours(24);
        baseMapper.delTimeOutToken(localDateTime);





    }
}
