package com.lxy.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author login
 * @since 2018-11-27
 */
public class Person extends Model<Person> {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 登录id
     */
    private String loginId;

    /**
     * 真实姓名
     */
    private String realname;

    /**
     * 性别
     */
    private String sex;

    /**
     * 人员类型(家长/老师)
     */
    private String personType;

    /**
     * 机构id
     */
    private String orgId;

    /**
     * 电话
     */
    private String tel;

    /**
     * 编号
     */
    private String number;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 身份证号
     */
    private String identityNo;

    /**
     * 卡号
     */
    private String cardNo;

    /**
     * 孩子ID
     */
    private String studentId;

    /**
     * 入学年份
     */
    private String startYear;

    /**
     * 头像
     */
    private String pic;

    private Integer del;

    private LocalDateTime createdatetime;

    private LocalDateTime modifydatetime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }
    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
    public String getPersonType() {
        return personType;
    }

    public void setPersonType(String personType) {
        this.personType = personType;
    }
    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getIdentityNo() {
        return identityNo;
    }

    public void setIdentityNo(String identityNo) {
        this.identityNo = identityNo;
    }
    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }
    public String getStartYear() {
        return startYear;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }
    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
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
        return "Person{" +
        "id=" + id +
        ", loginId=" + loginId +
        ", realname=" + realname +
        ", sex=" + sex +
        ", personType=" + personType +
        ", orgId=" + orgId +
        ", tel=" + tel +
        ", number=" + number +
        ", email=" + email +
        ", identityNo=" + identityNo +
        ", cardNo=" + cardNo +
        ", studentId=" + studentId +
        ", startYear=" + startYear +
        ", pic=" + pic +
        ", del=" + del +
        ", createdatetime=" + createdatetime +
        ", modifydatetime=" + modifydatetime +
        "}";
    }
}
