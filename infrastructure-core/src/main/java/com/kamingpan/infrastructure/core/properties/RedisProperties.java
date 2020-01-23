package com.kamingpan.infrastructure.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * RedisProperties redis配置
 *
 * @author kamingpan
 * @since 2018-07-17
 */
@ConfigurationProperties(prefix = RedisProperties.REDIS_PREFIX)
public class RedisProperties {

    static final String REDIS_PREFIX = "redis";

    // 服务器地址
    private String hostName;

    // 服务器端口
    private int port = 6379;

    // 密码
    private String password;

    // 数据库索引
    private int database = 0;

    // 是否开启事务管理控制
    private boolean enableTransactionSupport = false;

    // 最大连接数
    private int maxTotal = 8;

    // 最大空闲连接
    private int maxIdle = 8;

    // 最小空闲连接
    private int minIdle = 0;

    // 每次最大连接数
    private int numTestsPerEvictionRun = 3;

    // 释放扫描的扫描间隔
    private long timeBetweenEvictionRunsMillis = -1L;

    // 连接的最小空闲时间
    private long minEvictableIdleTimeMillis = 1800000L;

    // 连接控歘按时间多久后释放，当空闲时间 > 该值且空闲连接 > 最大空闲连接数时直接释放
    private long softMinEvictableIdleTimeMillis = 1800000L;

    // 获得链接时的最大等待毫秒数，小于0：阻塞不确定时间，默认-1
    private long maxWaitMillis = -1L;

    // 在获得链接的时候检查有效性，默认false
    private boolean testOnBorrow = false;

    // 在空闲时检查有效性，默认false
    private boolean testWhileIdle = false;

    // 连接耗尽时是否阻塞，false报异常，true阻塞超时 默认：true
    private boolean blockWhenExhausted = true;

    public RedisProperties() {
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPassword() {
        if (null == this.password || this.password.isEmpty()) {
            return null;
        }

        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }

    public boolean isEnableTransactionSupport() {
        return enableTransactionSupport;
    }

    public void setEnableTransactionSupport(boolean enableTransactionSupport) {
        this.enableTransactionSupport = enableTransactionSupport;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public long getSoftMinEvictableIdleTimeMillis() {
        return softMinEvictableIdleTimeMillis;
    }

    public void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis) {
        this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
    }

    public long getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(long maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public boolean isBlockWhenExhausted() {
        return blockWhenExhausted;
    }

    public void setBlockWhenExhausted(boolean blockWhenExhausted) {
        this.blockWhenExhausted = blockWhenExhausted;
    }

}
