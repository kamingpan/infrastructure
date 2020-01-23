package com.kamingpan.infrastructure.core.security;

import lombok.Data;

import java.util.Date;

/**
 * 用户详情
 *
 * @author kamingpan
 * @since 2019-10-21
 */
@Data
public class UserDetail {

    /**
     * 用户主键
     */
    private String userId;

    /**
     * 认证令牌
     */
    private String token;

    /**
     * 用户名
     */
    private String username;

    /**
     * 姓名
     */
    private String fullName;

    /**
     * 头像
     */
    private String portrait;

    /**
     * openid（公众号和小程序专用）
     */
    private String openid;

    /**
     * sessionKey（小程序专用）
     */
    private String sessionKey;

    /**
     * accessToken（公众号专用）
     */
    private String accessToken;

    /**
     * refreshToken（公众号专用）
     */
    private String refreshToken;

    /**
     * 用户access_token失效时间（公众号专用）
     */
    private Date accessTokenExpiresIn;

    public boolean accessTokenIsExpired() {
        return null == this.accessTokenExpiresIn || this.accessTokenExpiresIn.getTime() < System.currentTimeMillis();
    }

}
