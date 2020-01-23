package com.kamingpan.infrastructure.entity.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 管理员登录日志 vo
 *
 * @author kamingpan
 * @since 2017-08-18
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminLoginLogVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 管理员主键
     */
    private String adminId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 管理员姓名
     */
    private String fullName;

    /**
     * 头像
     */
    private String portrait;

    /**
     * 登录时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date loginTime;

    /**
     * 登录状态（0：成功，1：失败）
     */
    private Integer status;

    /**
     * 登录状态
     */
    private String statusLabel;

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

}
