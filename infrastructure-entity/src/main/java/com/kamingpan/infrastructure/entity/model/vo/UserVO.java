package com.kamingpan.infrastructure.entity.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 用户 vo
 *
 * @author kamingpan
 * @since 2018-07-20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO {

    /**
     * 主键id
     */
    private String id;

    /**
     * 公众号用户
     */
    private String subscriptionUser;

    /**
     * 小程序用户
     */
    private String miniProgramUser;

    /**
     * 用户名
     */
    // private String username;

    /**
     * 状态（0：正常，1：禁用）
     */
    private Integer status;

    /**
     * 状态标签
     */
    private String statusLabel;

    /**
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date registeredTime;

    /**
     * 注册来源（0：系统注册，1：公众号授权，2：小程序授权）
     */
    private Integer registeredSource;

    /**
     * 注册来源标签
     */
    private String registeredSourceLabel;

    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
