package com.kamingpan.infrastructure.entity.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 注册用户 vo
 *
 * @author kamingpan
 * @since 2018-07-20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RegisteredUserVO {

    /**
     * 主键id
     */
    private String id;

    /**
     * 用户
     */
    private String userId;

    /**
     * 公众号用户
     */
    private String subscriptionUserId;

    /**
     * 小程序用户
     */
    private String miniProgramUserId;

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
     * 注册时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
