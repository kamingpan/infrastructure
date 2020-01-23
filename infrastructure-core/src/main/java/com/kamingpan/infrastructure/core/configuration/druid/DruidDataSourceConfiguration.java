package com.kamingpan.infrastructure.core.configuration.druid;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.kamingpan.infrastructure.core.properties.DruidProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.Advisor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * druid主数据源配置
 *
 * @author kamingpan
 * @since 2018-06-12
 */
@Slf4j
@Primary // 在同样的DataSource中，首先使用被标注的DataSource
@Configuration
@EnableConfigurationProperties({DruidProperties.class})
public class DruidDataSourceConfiguration {

    @Autowired
    private DruidProperties druidProperties;

    /**
     * 声明DataSource bean实例
     *
     * @return {@link DataSource}
     */
    @Bean("dataSource")
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();

        datasource.setDriverClassName(this.druidProperties.getDriverClassName());
        datasource.setUrl(this.druidProperties.getUrl());
        datasource.setUsername(this.druidProperties.getUsername());
        datasource.setPassword(this.druidProperties.getPassword());

        // 连接池配置
        datasource.setInitialSize(this.druidProperties.getInitialSize());
        datasource.setMinIdle(this.druidProperties.getMinIdle());
        datasource.setMaxActive(this.druidProperties.getMaxActive());
        datasource.setMaxWait(this.druidProperties.getMaxWait());
        datasource.setTimeBetweenEvictionRunsMillis(this.druidProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(this.druidProperties.getMinEvictableIdleTimeMillis());
        datasource.setValidationQuery(this.druidProperties.getValidationQuery());
        datasource.setTestWhileIdle(this.druidProperties.getTestWhileIdle());
        datasource.setTestOnBorrow(this.druidProperties.getTestOnBorrow());
        datasource.setTestOnReturn(this.druidProperties.getTestOnReturn());
        datasource.setPoolPreparedStatements(this.druidProperties.getPoolPreparedStatements());
        datasource.setMaxPoolPreparedStatementPerConnectionSize(this.druidProperties.getMaxPoolPreparedStatementPerConnectionSize());
        try {
            datasource.setFilters(this.druidProperties.getFilters());
        } catch (SQLException exception) {
            log.error("druid configuration initialization filter", exception);
        }
        datasource.setConnectionProperties(this.druidProperties.getConnectionProperties());

        return datasource;
    }

    @Bean
    public WallFilter wallFilter() {
        WallFilter wallFilter = new WallFilter();
        wallFilter.setConfig(this.wallConfig());
        return wallFilter;
    }

    @Bean
    public WallConfig wallConfig() {
        WallConfig config = new WallConfig();
        // 允许一次执行多条语句
        config.setMultiStatementAllow(true);
        // 允许非基本语句的其他语句
        config.setNoneBaseStatementAllow(true);
        return config;
    }

    /**
     * 定义拦截器
     *
     * @return {@link DruidStatInterceptor}
     */
    @Bean
    public DruidStatInterceptor druidStatInterceptor() {
        return new DruidStatInterceptor();
    }

    /**
     * 定义切入点
     *
     * @return {@link JdkRegexpMethodPointcut}
     */
    @Bean
    public JdkRegexpMethodPointcut druidStatPointcut() {
        JdkRegexpMethodPointcut druidStatPointcut = new JdkRegexpMethodPointcut();

        // 解析并匹配patterns的切面设置
        String druidStatPointcutPatterns = this.druidProperties.getDruidStatPointcutPatterns();
        if (null != druidStatPointcutPatterns && !druidStatPointcutPatterns.isEmpty()) {
            String[] patterns = druidStatPointcutPatterns.split(";");
            druidStatPointcut.setPatterns(patterns);
        }
        return druidStatPointcut;
    }

    /**
     * 定义通知类
     *
     * @return {@link Advisor}
     */
    @Bean
    public Advisor druidStatAdvisor() {
        return new DefaultPointcutAdvisor(druidStatPointcut(), druidStatInterceptor());
    }

}
