package com.kamingpan.infrastructure.subscription.security.cache;

import com.kamingpan.infrastructure.subscription.security.authentication.TokenAuthentication;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户缓存
 *
 * @author kamingpan
 * @since 2019-04-16
 */
@Data
public class UserCache implements Serializable {

    /**
     * 用户主键
     */
    private String userId;

    /**
     * 登录token
     */
    private String token;

    /**
     * 用户openid
     */
    private String openid;

    /**
     * 登录ip
     */
    private String ip;

    /**
     * 用户access_token
     */
    private String accessToken;

    /**
     * 用户access_token失效时间
     */
    private Date accessTokenExpiresIn;

    /**
     * 用户refresh_token
     */
    private String refreshToken;

    /**
     * 用户refresh_token失效时间
     */
    private Date refreshTokenExpiresIn;

    public UserCache() {
    }

    public UserCache(String userId, String token, String openid, String ip) {
        this.userId = userId;
        this.token = token;
        this.openid = openid;
        this.ip = ip;
    }

    public TokenAuthentication toTokenAuthentication() {
        return new TokenAuthentication(this.userId, this.token, this.openid,
                this.accessToken, this.refreshToken, this.accessTokenExpiresIn);
    }

    public boolean refreshTokenIsExpired() {
        return null == this.refreshTokenExpiresIn || this.refreshTokenExpiresIn.getTime() < System.currentTimeMillis();
    }

}
