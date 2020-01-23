package com.kamingpan.infrastructure.core.configuration.system;

import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.kamingpan.infrastructure.core.injector.TheSqlInjector;
import com.kamingpan.infrastructure.core.properties.SystemProperties;
import com.kamingpan.infrastructure.util.id.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 系统配置
 *
 * @author kamingpan
 * @since 2018-06-19
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({SystemProperties.class})
public class SystemConfiguration {

    @Autowired
    private SystemProperties systemProperties;

    @Bean(initMethod = "init")
    public IdWorker idWorker() {
        return new IdWorker(this.systemProperties.getWorkerId(), this.systemProperties.getDataCenterId());
    }

    /**
     * 生成accessToken和refreshToken工具
     *
     * @return {@link OAuthIssuer}
     */
    @Bean
    public OAuthIssuer oAuthIssuer() {
        MD5Generator md5Generator = new MD5Generator();
        return new OAuthIssuerImpl(md5Generator);
    }

    @Bean
    public ISqlInjector sqlInjector() {
        return new TheSqlInjector();
    }

}
