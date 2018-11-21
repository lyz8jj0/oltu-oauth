package com.lxy.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author welsee
 * @since 2018-11-17
 */
public class AccessCode extends Model<AccessCode> {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 应用id
     */
    private String clientId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 授权code
     */
    private String accessCode;

    /**
     * code过期时间
     */
    private String expires;

    /**
     * 创建时间
     */
    private LocalDateTime createdatetime;

    /**
     * 更新时间
     */
    private LocalDateTime modifydatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }
    public String getExpires() {
        return expires;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }
    public LocalDateTime getCreatedatetime() {
        return createdatetime;
    }

    public void setCreatedatetime(LocalDateTime createdatetime) {
        this.createdatetime = createdatetime;
    }
    public LocalDateTime getModifydatetime() {
        return modifydatetime;
    }

    public void setModifydatetime(LocalDateTime modifydatetime) {
        this.modifydatetime = modifydatetime;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "AccessCode{" +
        "id=" + id +
        ", clientId=" + clientId +
        ", userId=" + userId +
        ", accessCode=" + accessCode +
        ", expires=" + expires +
        ", createdatetime=" + createdatetime +
        ", modifydatetime=" + modifydatetime +
        "}";
    }
}
