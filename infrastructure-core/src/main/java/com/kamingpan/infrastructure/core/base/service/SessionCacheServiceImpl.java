package com.kamingpan.infrastructure.core.base.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * session缓存服务实现类
 *
 * @author kamingpan
 * @since 2018-07-17
 */
@Slf4j
@Component
@ConditionalOnMissingBean(name = {"sessionCacheService"})
public class SessionCacheServiceImpl implements SessionCacheService {

    @Autowired
    private OAuthIssuer oAuthIssuer;

    @Autowired
    private RedisService<String, String> redisService;

    public String insertTokenIntoRedis(String id, String prefix, long expire) throws OAuthSystemException {
        // 利用用户id判断该账户是否有已登录的token
        String oldToken = this.redisService.get(prefix + id);
        if (null != oldToken && !oldToken.isEmpty()) {
            // 如果存在旧token，则清除旧的登录凭证
            this.redisService.delete(oldToken);
        }

        // 生成accessToken
        String accessToken = this.oAuthIssuer.accessToken();

        // 将生成的accessToken作为键，用户id作为值存到redis数据库中，并设置accessToken键的数据有效时长，用于登录超时自动失效
        this.redisService.set(accessToken, id, expire, TimeUnit.MINUTES);

        // 同时将前缀+用户id作为键，accessToken作为值存到redis数据库中，用作是否有过该用户登录的判断
        this.redisService.set(prefix + id, accessToken, expire, TimeUnit.MINUTES);

        return accessToken;
    }

    public String insertTokenIntoCache(String cacheKey, String id, String prefix, long expire)
            throws OAuthSystemException {
        /*Cache<String, String> cache = this.cacheManager.getCache(cacheKey);

        // 利用用户id判断该账户是否有已登录的token
        String oldToken = cache.get(prefix + id);
        if (null != oldToken && !oldToken.isEmpty()) {
            // 如果存在旧token，则清除旧的登录凭证
            cache.remove(oldToken);
        }

        // 生成accessToken
        String accessToken = this.oAuthIssuer.accessToken();

        // 将生成的accessToken作为键，用户id作为值存到cache中
        cache.put(accessToken, id);

        // 计算有效时长存到cache中，校验token时需要同时校验是否过了有效期
        expire *= 60000; // 将过期时间转成毫秒
        String expireString = String.valueOf(System.currentTimeMillis() + expire);
        cache.put(accessToken + "@expire", expireString);

        // 同时将前缀+用户id作为键，accessToken作为值存到cache中，用作是否有过该用户登录的判断
        cache.put(prefix + id, accessToken);*/

        return null;
    }

}
