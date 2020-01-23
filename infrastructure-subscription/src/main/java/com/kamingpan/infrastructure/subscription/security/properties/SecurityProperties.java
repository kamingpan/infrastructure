package com.kamingpan.infrastructure.subscription.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 安全配置
 *
 * @author kamingpan
 * @since 2019-04-16
 */
@Data
@ConfigurationProperties(prefix = "spring.security")
public class SecurityProperties {

    /**
     * 登录页面
     */
    private String loginPage = "/to-login";

    /**
     * 登录逻辑处理路径
     */
    private String loginUrl = "/login";

    /**
     * 登录成功跳转地址
     */
    private String loginSuccessUrl = "/login-success";

    /**
     * 登录失败跳转路径
     */
    private String loginFailureUrl = "/login-failure";

    /**
     * 登录会话超时时间（单位：分钟）
     */
    private int sessionTimeout = 60;

    /**
     * 是否验证ip
     */
    private boolean validateIp = true;

    /**
     * 允许匿名（未登录）请求路径
     */
    private List<String> anonymousUrl;

}
