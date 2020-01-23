package com.kamingpan.infrastructure.mini.program.wechat.configuration;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.kamingpan.infrastructure.core.base.enumeration.CacheTypeEnum;
import com.kamingpan.infrastructure.mini.program.wechat.properties.MiniProgramProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

/**
 * 小程序配置
 *
 * @author kamingpan
 * @since 2019-04-18
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({MiniProgramProperties.class})
public class MiniProgramConfiguration {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private MiniProgramProperties miniProgramProperties;

    /**
     * 微信小程序基础配置
     *
     * @return {@link WxMaConfig}
     */
    @Bean
    public WxMaConfig wxMaConfig() {
        // redis缓存类型
        if (CacheTypeEnum.REDIS.equals(this.miniProgramProperties.getCacheType())) {
            WxMaRedisConfig wxMaConfig = new WxMaRedisConfig();
            wxMaConfig.setJedisPool(this.jedisPool);

            wxMaConfig.setAppid(this.miniProgramProperties.getAppId());
            wxMaConfig.setSecret(this.miniProgramProperties.getAppSecret());
            wxMaConfig.setToken(this.miniProgramProperties.getToken());
            wxMaConfig.setAesKey(this.miniProgramProperties.getAesKey());
            return wxMaConfig;
        }

        // 本地缓存类型
        WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
        wxMaConfig.setAppid(this.miniProgramProperties.getAppId());
        wxMaConfig.setSecret(this.miniProgramProperties.getAppSecret());
        wxMaConfig.setToken(this.miniProgramProperties.getToken());
        wxMaConfig.setAesKey(this.miniProgramProperties.getAesKey());
        return wxMaConfig;
    }

    /**
     * 微信小程序基础服务配置
     *
     * @return {@link WxMaService}
     */
    @Bean
    public WxMaService wxMaService() {
        WxMaService wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(this.wxMaConfig());
        return wxMaService;
    }

}
