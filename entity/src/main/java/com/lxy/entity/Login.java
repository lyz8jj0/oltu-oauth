package com.lxy.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author login
 * @since 2018-11-17
 */
public class Login extends Model<Login> {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 第一用户名
     */
    @TableField("loginName1")
    private String loginName1;

    /**
     * 第二用户名
     */
    @TableField("loginName2")
    private String loginName2;

    /**
     * 第三用户名
     */
    @TableField("loginName3")
    private String loginName3;

    /**
     * 第四用户名
     */
    @TableField("loginName4")
    private String loginName4;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实名字
     */
    private String realname;

    /**
     * 电话
     */
    private String tel;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 是否删除,  0未删除/1已删除
     */
    private Integer del;

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
    public String getLoginName1() {
        return loginName1;
    }

    public void setLoginName1(String loginName1) {
        this.loginName1 = loginName1;
    }
    public String getLoginName2() {
        return loginName2;
    }

    public void setLoginName2(String loginName2) {
        this.loginName2 = loginName2;
    }
    public String getLoginName3() {
        return loginName3;
    }

    public void setLoginName3(String loginName3) {
        this.loginName3 = loginName3;
    }
    public String getLoginName4() {
        return loginName4;
    }

    public void setLoginName4(String loginName4) {
        this.loginName4 = loginName4;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public Integer getDel() {
        return del;
    }

    public void setDel(Integer del) {
        this.del = del;
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
        return "Login{" +
        "id=" + id +
        ", loginName1=" + loginName1 +
        ", loginName2=" + loginName2 +
        ", loginName3=" + loginName3 +
        ", loginName4=" + loginName4 +
        ", password=" + password +
        ", realname=" + realname +
        ", tel=" + tel +
        ", email=" + email +
        ", del=" + del +
        ", createdatetime=" + createdatetime +
        ", modifydatetime=" + modifydatetime +
        "}";
    }
}
