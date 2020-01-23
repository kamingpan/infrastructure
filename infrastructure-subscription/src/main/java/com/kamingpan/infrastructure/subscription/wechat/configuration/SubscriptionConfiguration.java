package com.kamingpan.infrastructure.subscription.wechat.configuration;

import com.kamingpan.infrastructure.core.base.enumeration.CacheTypeEnum;
import com.kamingpan.infrastructure.subscription.wechat.handler.LocationHandler;
import com.kamingpan.infrastructure.subscription.wechat.handler.ScanHandler;
import com.kamingpan.infrastructure.subscription.wechat.handler.SubscribeHandler;
import com.kamingpan.infrastructure.subscription.wechat.handler.TextHandler;
import com.kamingpan.infrastructure.subscription.wechat.handler.UnSubscribeHandler;
import com.kamingpan.infrastructure.subscription.wechat.properties.SubscriptionProperties;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * 公众号配置
 *
 * @author kamingpan
 * @since 2018-06-20
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({SubscriptionProperties.class})
public class SubscriptionConfiguration {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private SubscriptionProperties subscriptionProperties;

    /**
     * 微信公众号基础配置
     *
     * @return {@link WxMpConfigStorage}
     */
    @Bean
    public WxMpConfigStorage wxMpConfigStorage() {
        // redis缓存类型
        if (CacheTypeEnum.REDIS.equals(this.subscriptionProperties.getCacheType())) {
            WxMpRedisConfig wxMpConfigStorage = new WxMpRedisConfig(this.jedisPool);
            wxMpConfigStorage.setAppId(this.subscriptionProperties.getAppId());
            wxMpConfigStorage.setSecret(this.subscriptionProperties.getAppSecret());
            wxMpConfigStorage.setToken(this.subscriptionProperties.getToken());
            wxMpConfigStorage.setAesKey(this.subscriptionProperties.getAesKey());
            return wxMpConfigStorage;
        }

        // 本地缓存类型
        WxMpDefaultConfigImpl wxMpConfigStorage = new WxMpDefaultConfigImpl();
        wxMpConfigStorage.setAppId(this.subscriptionProperties.getAppId());
        wxMpConfigStorage.setSecret(this.subscriptionProperties.getAppSecret());
        wxMpConfigStorage.setToken(this.subscriptionProperties.getToken());
        wxMpConfigStorage.setAesKey(this.subscriptionProperties.getAesKey());
        return wxMpConfigStorage;
    }

    /**
     * 微信公众号基础服务配置
     *
     * @return {@link WxMpService}
     */
    @Bean
    public WxMpService wxMpService() {
        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(this.wxMpConfigStorage());
        return wxMpService;
    }

    /**
     * 微信公众号消息路由器配置
     *
     * @return {@link WxMpMessageRouter}
     */
    @Bean
    @ConditionalOnMissingBean(WxMpMessageRouter.class)
    public WxMpMessageRouter wxMpMessageRouter(LocationHandler locationHandler, TextHandler textHandler,
                                               ScanHandler scanHandler, SubscribeHandler subscribeHandler,
                                               UnSubscribeHandler unSubscribeHandler) {
        WxMpMessageRouter wxMpMessageRouter = new WxMpMessageRouter(this.wxMpService());

        // 上报地理位置事件处理
        if (null != locationHandler) {
            wxMpMessageRouter.rule().msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.LOCATION)
                    .async(false).handler(locationHandler).end();
        }

        // 文本消息处理
        if (null != textHandler) {
            wxMpMessageRouter.rule().async(false).msgType(WxConsts.XmlMsgType.TEXT).handler(textHandler).end();
        }

        // 扫描二维码事件处理
        if (null != scanHandler) {
            wxMpMessageRouter.rule().msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.SCAN).async(false)
                    .handler(scanHandler).end();
        }

        // 关注事件处理
        if (null != subscribeHandler) {
            wxMpMessageRouter.rule().msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.SUBSCRIBE).async(false)
                    .handler(subscribeHandler).end();
        }

        // 取消关注事件处理
        if (null != unSubscribeHandler) {
            wxMpMessageRouter.rule().msgType(WxConsts.XmlMsgType.EVENT).event(WxConsts.EventType.UNSUBSCRIBE)
                    .async(false).handler(unSubscribeHandler).end();
        }

        return wxMpMessageRouter;
    }

}
