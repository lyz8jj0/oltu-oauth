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
}
