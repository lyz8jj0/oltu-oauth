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
public class AccessToken extends Model<AccessToken> {

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
     * 授权token
     */
    private String accessToken;

    /**
     * token过期时间
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

    /**
     * 0是人员token  1为设备token
     */
    private String tokenType;

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
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
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
    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "AccessToken{" +
        "id=" + id +
        ", clientId=" + clientId +
        ", userId=" + userId +
        ", accessToken=" + accessToken +
        ", expires=" + expires +
        ", createdatetime=" + createdatetime +
        ", modifydatetime=" + modifydatetime +
        ", tokenType=" + tokenType +
        "}";
    }
}
