package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;

import java.util.Date;

/**
 * 管理员登录日志
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@TableName("system_admin_login_log")
public class AdminLoginLog extends BaseEntity<AdminLoginLog> {

    /**
     * 管理员主键
     */
    private String adminId;

    /**
     * 登录时间
     */
    private Date loginTime;

    /**
     * 登录状态（0：成功，1：失败）
     */
    private Integer status;

    /**
     * 登录结果描述
     */
    private String result;

    /**
     * 登录ip
     */
    private String ip;

    /**
     * 登录ip真实地址
     */
    private String address;

    /**
     * 登录mac
     */
    private String mac;

    /**
     * 登录设备
     */
    private String device;

    /**
     * 登录浏览器
     */
    private String browser;

    public AdminLoginLog() {
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    @Override
    public String toString() {
        return "AdminLoginLog {" +
        "adminId=" + adminId +
        ", loginTime=" + loginTime +
        ", status=" + status +
        ", result=" + result +
        ", ip=" + ip +
        ", address=" + address +
        ", mac=" + mac +
        ", device=" + device +
        ", browser=" + browser +
        "}";
    }

}
