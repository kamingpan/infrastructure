package com.kamingpan.infrastructure.management.security.configuration;

import com.kamingpan.infrastructure.management.security.filter.TokenAuthenticationFilter;
import com.kamingpan.infrastructure.management.security.handler.LoginFailureHandler;
import com.kamingpan.infrastructure.management.security.handler.LoginSuccessHandler;
import com.kamingpan.infrastructure.management.security.handler.TokenFailureHandler;
import com.kamingpan.infrastructure.management.security.matcher.SkipJoinPathRequestMatcher;
import com.kamingpan.infrastructure.management.security.properties.SecurityProperties;
import com.kamingpan.infrastructure.management.security.provider.LoginAuthenticationProvider;
import com.kamingpan.infrastructure.management.security.provider.TokenAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

/**
 * SecurityConfiguration
 *
 * @author kamingpan
 * @since 2019-01-04
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableConfigurationProperties({SecurityProperties.class})
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Autowired
    private TokenFailureHandler tokenFailureHandler;

    @Autowired
    private LoginAuthenticationProvider loginAuthenticationProvider;

    @Autowired
    private TokenAuthenticationProvider tokenAuthenticationProvider;

    /**
     * 配置认证管理器
     *
     * @param authenticationManagerBuilder 认证管理器
     */
    @Override
    protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) {
        // 设置登录验证
        authenticationManagerBuilder.authenticationProvider(this.loginAuthenticationProvider);

        // 设置token验证
        authenticationManagerBuilder.authenticationProvider(this.tokenAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 关闭csrf防护
        http.csrf().disable();
        // 跨域的问题
        http.headers().frameOptions().disable();

        http.formLogin()
                .successHandler(this.loginSuccessHandler)
                .failureHandler(this.loginFailureHandler)
                .loginProcessingUrl(this.securityProperties.getLoginUrl());

        http.addFilterBefore(this.tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling();
    }

    /**
     * 初始化自定义token过滤器
     *
     * @return 自定义token过滤器
     * @throws Exception 异常
     */
    private TokenAuthenticationFilter tokenAuthenticationFilter() throws Exception {
        // 获取允许匿名登录url
        List<String> skipPaths = this.securityProperties.getAnonymousUrl();

        // 把登录地址放到允许匿名登录列表中
        if (null == skipPaths) {
            skipPaths = new ArrayList<String>();
        }
        skipPaths.add(this.securityProperties.getLoginUrl());

        // 组装路径匹配对象
        SkipJoinPathRequestMatcher skipJoinPathRequestMatcher
                = new SkipJoinPathRequestMatcher(skipPaths, "/**");

        // 创建token过滤器对象
        TokenAuthenticationFilter tokenAuthenticationFilter
                = new TokenAuthenticationFilter(skipJoinPathRequestMatcher, this.tokenFailureHandler);

        // 设置认证管理器
        tokenAuthenticationFilter.setAuthenticationManager(super.authenticationManager());
        return tokenAuthenticationFilter;
    }

}
