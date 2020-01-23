package com.kamingpan.infrastructure.mini.program.security.cache;

import com.kamingpan.infrastructure.mini.program.security.authentication.TokenAuthentication;
import lombok.Data;

import java.io.Serializable;

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
     * 用户会话密钥
     */
    private String sessionKey;

    /**
     * 登录ip
     */
    private String ip;

    public UserCache() {
    }

    public UserCache(String userId, String token, String openid, String sessionKey, String ip) {
        this.userId = userId;
        this.token = token;
        this.openid = openid;
        this.sessionKey = sessionKey;
        this.ip = ip;
    }

    public TokenAuthentication toTokenAuthentication() {
        return new TokenAuthentication(this.userId, this.token, this.openid, this.sessionKey);
    }

}
