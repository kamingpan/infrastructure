package com.kamingpan.infrastructure.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * druid数据源配置
 *
 * @author kamingpan
 * @since 2018-06-12
 */
@ConfigurationProperties(prefix = DruidProperties.DRUID_PREFIX)
public class DruidProperties {

    static final String DRUID_PREFIX = "spring.datasource.primary";

    // 驱动包
    private String driverClassName;

    // 数据库路径
    private String url;

    // 数据库用户名
    private String username;

    // 数据库密码
    private String password;

    // 连接池配置
    private String type;

    // 初始化大小
    private Integer initialSize;

    // 最小空闲数
    private Integer minIdle;

    // 最大活动数
    private Integer maxActive;

    // 连接等待超时时间
    private Integer maxWait;

    // 隔多久进行一次检测(检测可以关闭的空闲连接)
    private Long timeBetweenEvictionRunsMillis;

    // 连接在池中的最小生存时间
    private Long minEvictableIdleTimeMillis;

    // 验证查询
    private String validationQuery;

    // 试验空闲
    private Boolean testWhileIdle;

    // 借阅测验
    private Boolean testOnBorrow;

    // 返回测验
    private Boolean testOnReturn;

    // 是否打开PSCache
    private Boolean poolPreparedStatements;

    // 每个连接上PSCache的大小
    private Integer maxPoolPreparedStatementPerConnectionSize;

    // 配置监控统计拦截的filters，去掉后监控界面sql无法统计，'wall'用于防火墙
    private String filters;

    // 通过connectProperties属性来打开mergeSql功能；慢SQL记录
    private String connectionProperties;

    // 视图请求路径
    private String druidUrl = "/druid/*";

    // 允许访问ip（多个ip用逗号间隔）
    private String allowIp;

    // 拒绝访问ip (与allow存在共同时，deny优先于allow）
    private String denyIp;

    // 视图登录用户名
    private String loginUsername;

    // 视图登录密码
    private String loginPassword;

    // 是否能够重置数据
    private String resetEnable = "false";

    // druid切面拦截器匹配（多个包用;间隔）
    private String druidStatPointcutPatterns;

    public DruidProperties() {
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(Integer initialSize) {
        this.initialSize = initialSize;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(Integer maxActive) {
        this.maxActive = maxActive;
    }

    public Integer getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(Integer maxWait) {
        this.maxWait = maxWait;
    }

    public Long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(Long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public Long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(Long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public Boolean getTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(Boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public Boolean getTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(Boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public Boolean getTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(Boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public Boolean getPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    public void setPoolPreparedStatements(Boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public Integer getMaxPoolPreparedStatementPerConnectionSize() {
        return maxPoolPreparedStatementPerConnectionSize;
    }

    public void setMaxPoolPreparedStatementPerConnectionSize(Integer maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public String getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(String connectionProperties) {
        this.connectionProperties = connectionProperties;
    }

    public String getDruidUrl() {
        return druidUrl;
    }

    public void setDruidUrl(String druidUrl) {
        if (null == druidUrl || druidUrl.trim().isEmpty()) {
            return;
        }
        this.druidUrl = druidUrl.trim();
    }

    public String getAllowIp() {
        return allowIp;
    }

    public void setAllowIp(String allowIp) {
        this.allowIp = (null == allowIp || allowIp.trim().isEmpty() ? null : allowIp.trim());
    }

    public String getDenyIp() {
        return denyIp;
    }

    public void setDenyIp(String denyIp) {
        this.denyIp = (null == denyIp || denyIp.trim().isEmpty() ? null : denyIp.trim());
    }

    public String getLoginUsername() {
        return loginUsername;
    }

    public void setLoginUsername(String loginUsername) {
        this.loginUsername = (null == loginUsername || loginUsername.trim().isEmpty() ? null : loginUsername.trim());
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = (null == loginPassword || loginPassword.trim().isEmpty() ? null : loginPassword.trim());
    }

    public String getResetEnable() {
        return resetEnable;
    }

    public void setResetEnable(String resetEnable) {
        if ("true".equals(resetEnable)) {
            this.resetEnable = resetEnable;
        }
    }

    public String getDruidStatPointcutPatterns() {
        return druidStatPointcutPatterns;
    }

    public void setDruidStatPointcutPatterns(String druidStatPointcutPatterns) {
        this.druidStatPointcutPatterns = druidStatPointcutPatterns;
    }
}
