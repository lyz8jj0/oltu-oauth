package com.lxy.service.impl;

import com.lxy.entity.AccessCode;
import com.lxy.exception.ProgramException;
import com.lxy.mapper.AccessCodeMapper;
import com.lxy.service.IAccessCodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lxy.tools.UUIDUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author login
 * @since 2018-11-17
 */
@Service
@Transactional
public class AccessCodeServiceImpl extends ServiceImpl<AccessCodeMapper, AccessCode> implements IAccessCodeService {

    @Override
    public void saveAuthorizationCode(String clientId, String uId, String expires, String authorizationCode) throws Exception {
        if (clientId == null || uId == null || expires == null || authorizationCode == null) {
            throw new ProgramException("保存授权码参数不合法!");
        }
        AccessCode accessCode = new AccessCode();
        accessCode.setAccessCode(authorizationCode);
        accessCode.setClientId(clientId);
        accessCode.setId(UUIDUtil.getUUID());
        accessCode.setExpires(expires);
        accessCode.setCreatedatetime(LocalDateTime.now());
        accessCode.setModifydatetime(LocalDateTime.now());
        accessCode.setUserId(uId);
        baseMapper.insert(accessCode);

        //每次插入新的code时,删除已经过期的code
        LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(10);
        baseMapper.delTimeOutCode(localDateTime);
    }

    @Override
    public AccessCode getAuthorizationCodeInfo(String clientId, String authorizationCode) throws Exception {
        if (clientId == null || authorizationCode == null) {
            throw new ProgramException("获取授权码信息不合法!");
        }
        return baseMapper.getAccessCode(clientId, authorizationCode);
    }
}
