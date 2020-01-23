package com.kamingpan.infrastructure.core.base.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis服务实现类
 *
 * @author kamingpan
 * @since 2018-07-17
 */
@Slf4j
@Component
public class StringRedisService {

    @Resource(name = "stringRedisTemplate")
    private StringRedisTemplate redisTemplate;

    /**
     * 保存键值对
     *
     * @param key   键
     * @param value 值
     */
    public void set(String key, String value) {
        log.debug("保存对象，键：{}，值：{}", key, value);
        ValueOperations<String, String> valueOperations = this.redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    /**
     * 保存键值对
     *
     * @param key          键
     * @param value        值
     * @param expireSecond 过期时间（单位：秒）
     */
    public void set(String key, String value, long expireSecond) {
        log.debug("保存对象，键：{}，值：{}，有效时长：{}", key, value, expireSecond);
        ValueOperations<String, String> valueOperations = this.redisTemplate.opsForValue();
        valueOperations.set(key, value, expireSecond, TimeUnit.SECONDS);
    }

    /**
     * 保存键值对
     *
     * @param key      键
     * @param value    值
     * @param expire   过期时间
     * @param timeUnit 过期类型
     */
    public void set(String key, String value, long expire, TimeUnit timeUnit) {
        log.debug("保存对象，键：{}，值：{}，有效时长：{}", key, value, expire);
        ValueOperations<String, String> valueOperations = this.redisTemplate.opsForValue();
        valueOperations.set(key, value, expire, timeUnit);
    }

    /**
     * 根据键查询值
     *
     * @param key 键
     * @return 值
     */
    public String get(String key) {
        log.debug("查询对象，键：{}", key);
        ValueOperations<String, String> valueOperations = this.redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * 保存键-值列表
     *
     * @param key   键
     * @param value 值列表
     */
    public void setList(String key, String value) {
        log.debug("保存列表，键：{}，值：{}", key, value);
        ListOperations<String, String> listOperations = this.redisTemplate.opsForList();
        listOperations.leftPush(key, value);
    }

    /**
     * 根据键查询值列表
     *
     * @param key 键
     * @return 值列表
     */
    public String getList(String key) {
        log.debug("查询列表，键：{}", key);
        ListOperations<String, String> listOperations = this.redisTemplate.opsForList();
        return listOperations.leftPop(key);
    }

    /**
     * 保存键-值集合
     *
     * @param key   键
     * @param value 值集合
     */
    public void setSet(String key, String... value) {
        log.debug("保存集合，键：{}，值：{}", key, value);
        SetOperations<String, String> setOperations = this.redisTemplate.opsForSet();
        setOperations.add(key, value);
    }

    /**
     * 根据键查询值
     *
     * @param key 键
     * @return 值
     */
    public Set<String> getSet(String key) {
        log.debug("查询集合，键：{}", key);
        SetOperations<String, String> setOperations = this.redisTemplate.opsForSet();
        return setOperations.members(key);
    }

    /**
     * 保存键-Map值
     *
     * @param key   键
     * @param value Map值
     */
    public void setHash(String key, Map<String, String> value) {
        log.debug("保存Map，键：{}，值：{}", key, value);
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        hashOperations.putAll(key, value);
    }

    /**
     * 根据键查询Map值
     *
     * @param key 键
     * @return Map值
     */
    public Map<String, String> getHash(String key) {
        log.debug("查询Map，键：{}", key);
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }

    /**
     * 根据键删除值
     *
     * @param key 键
     */
    public void delete(String key) {
        log.debug("清除对象，键：{}", key);
        this.redisTemplate.delete(key);
    }

    /**
     * 设置指定键有效时长
     *
     * @param key      键
     * @param expire   有效时长
     * @param timeUnit 时长类型
     */
    public void expire(String key, long expire, TimeUnit timeUnit) {
        log.debug("设置对象有效时间，有效时长：{}", expire);
        this.redisTemplate.expire(key, expire, timeUnit);
    }

    /**
     * 清除所有键值对
     */
    public void clearAll() {
        log.debug("清除所有数据");
        this.redisTemplate.multi();
    }

}
