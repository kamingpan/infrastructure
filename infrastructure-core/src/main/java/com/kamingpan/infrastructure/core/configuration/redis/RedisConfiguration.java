package com.kamingpan.infrastructure.core.configuration.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * RedisConfiguration redis配置
 *
 * @author kamingpan
 * @since 2018-07-17
 */
@Slf4j
@Configuration
public class RedisConfiguration {

    @Autowired
    private RedisProperties redisProperties;

    /**
     * redis连接池
     *
     * @return {@link JedisPool}
     */
    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        RedisProperties.Pool pool = this.redisProperties.getJedis().getPool();

        // 判断连接池是否为空
        if (null != pool) {
            jedisPoolConfig.setMaxTotal(pool.getMaxActive());
            jedisPoolConfig.setMaxIdle(pool.getMaxIdle());
            jedisPoolConfig.setMinIdle(pool.getMinIdle());
            if (pool.getMaxWait() != null) {
                jedisPoolConfig.setMaxWaitMillis(pool.getMaxWait().toMillis());
            }
        }

        // 设置连接超时时间
        int timeout = 0;
        if (null != this.redisProperties.getTimeout()) {
            timeout = (int) this.redisProperties.getTimeout().toMillis();
        }

        String password = (null == this.redisProperties.getPassword() || this.redisProperties.getPassword().isEmpty())
                ? null : this.redisProperties.getPassword();

        return new JedisPool(jedisPoolConfig, this.redisProperties.getHost(), this.redisProperties.getPort(), timeout,
                password, this.redisProperties.getDatabase());
    }

    /**
     * redis操作模板
     *
     * @return {@link RedisTemplate}
     */
    @Bean("redisTemplate")
    @SuppressWarnings("unchecked")
    public <K, V> RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<K, V>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 是否开启redis事务管理控制
        // redisTemplate.setEnableTransactionSupport(this.redisProperties.isEnableTransactionSupport());

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder().allowIfSubType(Object.class).build();
        ObjectMapper objectMapper = JsonMapper.builder()
                .activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL).build();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);

        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        RedisSerializer<String> redisSerializer = new StringRedisSerializer();

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setKeySerializer(redisSerializer);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashKeySerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    /**
     * redis操作模板
     *
     * @return {@link RedisTemplate}
     */
    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }

}
