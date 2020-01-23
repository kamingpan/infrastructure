package com.kamingpan.infrastructure.core.configuration.cache;

import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 缓存配置
 *
 * @author kamingpan
 * @since 2018-06-19
 */
@EnableCaching
@Configuration
@EnableConfigurationProperties({CacheProperties.class})
public class CacheConfiguration {

    @Autowired
    private CacheProperties cacheProperties;

    @Bean
    public EhCacheCacheManager cacheManager(CacheManager ehCacheCacheManager) {
        return new EhCacheCacheManager(ehCacheCacheManager);
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        cacheManagerFactoryBean.setConfigLocation(this.cacheProperties.getEhcache().getConfig());
        cacheManagerFactoryBean.setShared(true);
        return cacheManagerFactoryBean;
    }

}
