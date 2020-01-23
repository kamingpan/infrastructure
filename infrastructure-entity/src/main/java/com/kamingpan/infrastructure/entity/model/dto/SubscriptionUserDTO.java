package com.kamingpan.infrastructure.entity.model.dto;

import lombok.Data;

/**
 * 公众号用户 dto
 *
 * @author kamingpan
 * @since 2018-07-20
 */
@Data
public class SubscriptionUserDTO {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 状态（0：正常，1：禁用）
     */
    private Integer status;

    /**
     * openid
     */
    private String openid;

    /**
     * 公众号关注状态（0：取消关注，1：已关注）
     */
    private Boolean subscribed;

}
