package com.kamingpan.infrastructure.core.base.service;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis服务接口
 *
 * @author kamingpan
 * @since 2018-07-18
 */
public interface RedisService<K extends Serializable, V extends Serializable> {

    /**
     * 保存键值对
     *
     * @param key   键
     * @param value 值
     */
    void set(K key, V value);

    /**
     * 保存键值对
     *
     * @param key          键
     * @param value        值
     * @param expireSecond 过期时间（单位：秒）
     */
    void set(K key, V value, long expireSecond);

    /**
     * 保存键值对
     *
     * @param key      键
     * @param value    值
     * @param expire   过期时间
     * @param timeUnit 过期类型
     */
    void set(K key, V value, long expire, TimeUnit timeUnit);

    /**
     * 根据键查询值
     *
     * @param key 键
     * @return 值
     */
    V get(K key);

    /**
     * 保存键-值列表
     *
     * @param key   键
     * @param value 值列表
     */
    void setList(K key, V value);

    /**
     * 根据键查询值列表
     *
     * @param key 键
     * @return 值列表
     */
    V getList(K key);

    /**
     * 保存键-值集合
     *
     * @param key   键
     * @param value 值集合
     */
    @SuppressWarnings("unchecked")
    void setSet(K key, V... value);

    /**
     * 根据键查询值
     *
     * @param key 键
     * @return 值
     */
    Set<V> getSet(K key);

    /**
     * 保存键-Map值
     *
     * @param key   键
     * @param value Map值
     */
    <HK extends Serializable, HV extends Serializable> void setHash(K key, Map<HK, HV> value);

    /**
     * 根据键查询Map值
     *
     * @param key 键
     * @return Map值
     */
    <HK extends Serializable, HV extends Serializable> Map<HK, HV> getHash(K key);

    /**
     * 根据键删除值
     *
     * @param key 键
     */
    void delete(K key);

    /**
     * 设置指定键有效时长
     *
     * @param key      键
     * @param expire   有效时长
     * @param timeUnit 时长类型
     */
    void expire(K key, long expire, TimeUnit timeUnit);

    /**
     * 清除所有键值对
     */
    void clearAll();

}
