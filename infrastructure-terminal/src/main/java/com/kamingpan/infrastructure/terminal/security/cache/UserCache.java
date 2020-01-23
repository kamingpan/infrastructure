package com.kamingpan.infrastructure.terminal.security.cache;

import com.kamingpan.infrastructure.terminal.security.authentication.TokenAuthentication;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户缓存
 *
 * @author kamingpan
 * @since 2019-01-16
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
    private String accessToken;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录ip
     */
    private String ip;

    public UserCache() {
    }

    public UserCache(String userId, String accessToken, String clientId, String username, String ip) {
        this.userId = userId;
        this.accessToken = accessToken;
        this.clientId = clientId;
        this.username = username;
        this.ip = ip;
    }

    public TokenAuthentication toTokenAuthentication() {
        return new TokenAuthentication(this.userId, this.accessToken, this.username);
    }

}
