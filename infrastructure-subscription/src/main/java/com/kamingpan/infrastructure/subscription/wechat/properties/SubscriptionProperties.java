package com.kamingpan.infrastructure.subscription.wechat.properties;

import com.kamingpan.infrastructure.core.base.enumeration.CacheTypeEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 公众号配置信息
 *
 * @author kamingpan
 * @since 2018-06-20
 */
@ConfigurationProperties(prefix = SubscriptionProperties.SUBSCRIPTION_PREFIX)
public class SubscriptionProperties {

    static final String SUBSCRIPTION_PREFIX = "subscription";

    // 公众号appID(应用ID)
    private String appId;

    // 公众号appSecret(应用密钥)
    private String appSecret;

    // 公众号开发者模式配置token(令牌)
    private String token;

    // 公众号开发者模式配置aesKey(消息加解密密钥)
    private String aesKey;

    // 缓存类型
    private CacheTypeEnum cacheType = CacheTypeEnum.REDIS;

    // 是否初始化菜单按钮(若初始化会自动将公众号转为开发者模式)
    private boolean initMenu = false;

    // 公众号菜单按钮类
    private String menuButtonClass;

    public SubscriptionProperties() {
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

    public boolean isInitMenu() {
        return initMenu;
    }

    public void setInitMenu(boolean initMenu) {
        this.initMenu = initMenu;
    }

    public String getMenuButtonClass() {
        return menuButtonClass;
    }

    public void setMenuButtonClass(String menuButtonClass) {
        this.menuButtonClass = menuButtonClass;
    }

}
