package com.kamingpan.infrastructure.entity.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 公众号用户 vo
 *
 * @author kamingpan
 * @since 2018-07-20
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionUserVO {

    /**
     * 主键id
     */
    private String id;

    /**
     * 用户主键
     */
    private String userId;

    /**
     * 注册用户主键
     */
    private String registeredUserId;

    /**
     * 用户状态（0：正常，1：禁用）
     */
    private Integer status;

    /**
     * 用户状态标签
     */
    private String statusLabel;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 头像
     */
    private String portrait;

    /**
     * 性别（0：女，1：男，2：未知）
     */
    private Integer gender;

    /**
     * 性别标签
     */
    private String genderLabel;

    /**
     * 国家
     */
    private String country;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * openid
     */
    private String openid;

    /**
     * union id
     */
    private String unionId;

    /**
     * 公众号关注状态（0：取消关注，1：已关注）
     */
    private Boolean subscribed;

    /**
     * 公众号关注状态标签
     */
    private String subscribedLabel;

}
