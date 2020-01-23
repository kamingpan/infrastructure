package com.kamingpan.infrastructure.entity.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 管理员 vo
 *
 * @author kamingpan
 * @since 2018-06-29
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminVO {

    /**
     * 主键id
     */
    public String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 状态（0：正常，1：禁用）
     */
    private Integer status;

    /**
     * 状态标签
     */
    private String statusLabel;

    /**
     * 真实姓名
     */
    private String fullName;

    /**
     * 头像
     */
    private String portrait;

    /**
     * 性别（0：女，1：男）
     */
    private Integer gender;

    /**
     * 性别标签
     */
    private String genderLabel;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 限制ip
     */
    private String restrictIp;

    /**
     * 最后修改人
     */
    private String updater;

    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 头像链接
     */
    private String portraitUrl;

}
