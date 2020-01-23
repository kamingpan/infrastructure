package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;

/**
 * 管理员
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@TableName("system_admin")
public class Admin extends BaseEntity<Admin> {

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态（0：正常，1：禁用）
     */
    private Integer status;

    /**
     * 真实姓名
     */
    private String fullName;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 上级管理员
     */
    private String superior;

    /**
     * 头像
     */
    private String portrait;

    /**
     * 性别（0：女，1：男）
     */
    private Integer gender;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 限制ip
     */
    private String restrictIp;

    public Admin() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRestrictIp() {
        return restrictIp;
    }

    public void setRestrictIp(String restrictIp) {
        this.restrictIp = restrictIp;
    }

    @Override
    public String toString() {
        return "Admin {" +
        "username=" + username +
        ", password=" + password +
        ", status=" + status +
        ", fullName=" + fullName +
        ", phone=" + phone +
        ", superior=" + superior +
        ", portrait=" + portrait +
        ", gender=" + gender +
        ", email=" + email +
        ", restrictIp=" + restrictIp +
        "}";
    }

}
