package com.kamingpan.infrastructure.subscription.security.handler;

import com.kamingpan.infrastructure.subscription.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理（废弃类，已换成代码手动处理登录逻辑）
 *
 * @author kamingpan
 * @since 2019-04-16
 */
@Slf4j
// @Component
@Deprecated
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 登录失败逻辑
     *
     * @param request   请求
     * @param response  响应
     * @param exception 认证异常
     * @throws IOException      io异常
     * @throws ServletException 服务异常
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        // 打印异常（父类中已有异常打印，因此该处只打印debug级别）
        log.debug("登录异常", exception);

        // 因为微信登录逻辑是重定向，因此登录异常时重定向到错误页面，而不是响应异常信息
        response.sendRedirect(this.securityProperties.getLoginFailureUrl());
    }

}
