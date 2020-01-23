package com.kamingpan.infrastructure.mini.program.wechat.properties;

import com.kamingpan.infrastructure.core.base.enumeration.CacheTypeEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 小程序配置信息
 *
 * @author kamingpan
 * @since 2018-06-20
 */
@ConfigurationProperties(prefix = MiniProgramProperties.SUBSCRIPTION_PREFIX)
public class MiniProgramProperties {

    static final String SUBSCRIPTION_PREFIX = "mini-program";

    // 小程序appID(应用ID)
    private String appId;

    // 小程序appSecret(应用密钥)
    private String appSecret;

    // 小程序开发者模式配置token(令牌)
    private String token;

    // 小程序开发者模式配置aesKey(消息加解密密钥)
    private String aesKey;

    // 缓存类型
    private CacheTypeEnum cacheType = CacheTypeEnum.REDIS;

    public MiniProgramProperties() {
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public CacheTypeEnum getCacheType() {
        return cacheType;
    }

    public void setCacheType(CacheTypeEnum cacheType) {
        this.cacheType = cacheType;
    }

}
