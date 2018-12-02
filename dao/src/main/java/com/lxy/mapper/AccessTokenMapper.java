package com.lxy.mapper;

import com.lxy.entity.AccessToken;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.time.LocalDateTime;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author login
 * @since 2018-11-17
 */
public interface AccessTokenMapper extends BaseMapper<AccessToken> {

    void delTimeOutToken(LocalDateTime localDateTime);
}
