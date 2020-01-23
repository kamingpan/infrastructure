package com.kamingpan.infrastructure.core.configuration.druid;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.kamingpan.infrastructure.core.properties.DruidProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Druid监控配置
 *
 * @author kamingpan
 * @since 2018-06-12
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({DruidProperties.class})
public class DruidMonitorConfiguration {

    @Autowired
    private DruidProperties druidProperties;

    /**
     * 注册ServletRegistrationBean
     *
     * @return {@link ServletRegistrationBean}
     */
    @Bean
    public ServletRegistrationBean registrationBean() {
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<StatViewServlet>
                (new StatViewServlet(), this.druidProperties.getDruidUrl());

        if (null != this.druidProperties.getAllowIp()) {
            // 白名单（多个ip用逗号间隔）
            bean.addInitParameter("allow", this.druidProperties.getAllowIp());
        }

        if (null != this.druidProperties.getDenyIp()) {
            // IP黑名单 (与allow存在共同时，deny优先于allow)：如果满足deny的话提示：Sorry, you are not permitted to view this page.
            bean.addInitParameter("deny", this.druidProperties.getDenyIp());
        }

        if (null != this.druidProperties.getLoginUsername()) {
            //登录查看信息的账号密码.
            bean.addInitParameter("loginUsername", this.druidProperties.getLoginUsername());

            if (null != this.druidProperties.getLoginPassword()) {
                bean.addInitParameter("loginPassword", this.druidProperties.getLoginPassword());
            }
        }

        //是否能够重置数据.
        bean.addInitParameter("resetEnable", this.druidProperties.getResetEnable());
        return bean;
    }

    /**
     * 注册FilterRegistrationBean
     *
     * @return {@link FilterRegistrationBean}
     */
    @Bean
    public FilterRegistrationBean druidStatFilter() {
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<WebStatFilter>(new WebStatFilter());

        // 添加过滤规则.
        bean.addUrlPatterns("/*");

        // 添加不需要忽略的格式信息.
        bean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return bean;
    }

}
