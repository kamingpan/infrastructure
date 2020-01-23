package com.kamingpan.infrastructure.management.security.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 安全配置
 *
 * @author kamingpan
 * @since 2019-01-17
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
     * 登录会话超时时间（单位：分钟）
     */
    private int sessionTimeout = 60;

    /**
     * 最大会话个数
     */
    private int maxLoginSession = 1;

    /**
     * 最大登录错误次数
     */
    private int maxLoginError = 5;

    /**
     * 登录错误次数缓存时长（单位：分钟）
     */
    private int loginErrorTimeout = 1440;

    /**
     * 是否验证ip
     */
    private boolean validateIp = true;

    /**
     * 允许匿名（未登录）请求路径
     */
    private List<String> anonymousUrl;

}
