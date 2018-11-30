package com.lxy.service.impl;

import com.lxy.entity.AccessCode;
import com.lxy.mapper.AccessCodeMapper;
import com.lxy.service.IAccessCodeService;
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
public class AccessCodeServiceImpl extends ServiceImpl<AccessCodeMapper, AccessCode> implements IAccessCodeService {

    @Override
    public int addAccessCode(String clientId, String uId, String expires, String authorizationCode) {
        AccessCode accessCode = new AccessCode();
        accessCode.setId(UUIDUtil.getUUID());
        accessCode.setClientId(clientId);
        accessCode.setUserId(uId);
        accessCode.setAccessCode(authorizationCode);
        accessCode.setExpires(expires);
        accessCode.setCreatedatetime(LocalDateTime.now());
        accessCode.setModifydatetime(LocalDateTime.now());

        //将信息插入数据库
        int i = baseMapper.insert(accessCode);

        //每次插入新的code时,删除已经过期的code(minutesToSubtract - 要减去的分钟数)
        LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(10);
        baseMapper.delTimeOutCode(localDateTime);

        return i;
    }
}
