package com.kamingpan.infrastructure.subscription.wechat.configuration;

import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.enums.TicketType;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * 基于Redis的微信配置provider.
 * <pre>
 *    使用说明：本实现仅供参考，并不完整，
 *    比如为减少项目依赖，未加入redis分布式锁的实现，如有需要请自行实现。
 * </pre>
 *
 * @author kamingpan
 * @since 2019-09-24
 */
public class WxMpRedisConfig extends WxMpDefaultConfigImpl {

    public static final String ACCESS_TOKEN_KEY = "SUBSCRIPTION:%s:ACCESS_TOKEN";

    public static final String TICKET_KEY = "SUBSCRIPTION:%s:TICKET_KEY:%s";

    /**
     * 使用连接池保证线程安全.
     */
    private final JedisPool jedisPool;

    public WxMpRedisConfig(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    public String getAccessTokenKey() {
        return String.format(WxMpRedisConfig.ACCESS_TOKEN_KEY, this.appId);
    }

    public String getTicketRedisKey(TicketType type) {
        return String.format(WxMpRedisConfig.TICKET_KEY, this.appId, type.getCode());
    }

    @Override
    public String getAccessToken() {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.get(this.getAccessTokenKey());
        }
    }

    @Override
    public boolean isAccessTokenExpired() {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.ttl(this.getAccessTokenKey()) < 2;
        }
    }

    @Override
    public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.setex(this.getAccessTokenKey(), expiresInSeconds - 200, accessToken);
        }
    }

    @Override
    public void expireAccessToken() {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.expire(this.getAccessTokenKey(), 0);
        }
    }

    @Override
    public String getTicket(TicketType type) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.get(this.getTicketRedisKey(type));
        }
    }

    @Override
    public boolean isTicketExpired(TicketType type) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            return jedis.ttl(this.getTicketRedisKey(type)) < 2;
        }
    }

    @Override
    public synchronized void updateTicket(TicketType type, String jsapiTicket, int expiresInSeconds) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.setex(this.getTicketRedisKey(type), expiresInSeconds - 200, jsapiTicket);
        }
    }

    @Override
    public void expireTicket(TicketType type) {
        try (Jedis jedis = this.jedisPool.getResource()) {
            jedis.expire(this.getTicketRedisKey(type), 0);
        }
    }

}
