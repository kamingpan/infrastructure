package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;
import com.kamingpan.infrastructure.entity.constant.OauthTokenConstant;

import java.util.Date;

/**
 * 授权token
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@TableName("system_oauth_token")
public class OauthToken extends BaseEntity<OauthToken> {

    /**
     * 用户主键
     */
    private String userId;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 登录token
     */
    private String accessToken;

    /**
     * 登录token有效时长（单位：秒）
     */
    private Long accessTokenValidity;

    /**
     * token刷新时间
     */
    private Date refreshTime;

    /**
     * 刷新token
     */
    private String refreshToken;

    /**
     * 刷新token有效时长（单位：秒）
     */
    private Long refreshTokenValidity;

    /**
     * 登录时间
     */
    private Date loginTime;

    public OauthToken() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Long accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Date getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(Date refreshTime) {
        this.refreshTime = refreshTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Long refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "OauthToken {" +
        "userId=" + userId +
        ", clientId=" + clientId +
        ", accessToken=" + accessToken +
        ", accessTokenValidity=" + accessTokenValidity +
        ", refreshTime=" + refreshTime +
        ", refreshToken=" + refreshToken +
        ", refreshTokenValidity=" + refreshTokenValidity +
        ", loginTime=" + loginTime +
        "}";
    }

    /**
     * accessToken是否过期
     *
     * @return true：过期，false：未过期
     */
    public boolean accessTokenExpired() {
        final long time = this.refreshTime.getTime() + (this.accessTokenValidity * OauthTokenConstant.Second.THOUSAND);
        return time < System.currentTimeMillis();
    }

    /**
     * refreshToken是否过期
     *
     * @return true：过期，false：未过期
     */
    public boolean refreshTokenExpired() {
        final long time = this.loginTime.getTime() + (this.refreshTokenValidity * OauthTokenConstant.Second.THOUSAND);
        return time < System.currentTimeMillis();
    }

}
