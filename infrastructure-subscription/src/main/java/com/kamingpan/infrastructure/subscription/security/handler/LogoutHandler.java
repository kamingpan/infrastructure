package com.kamingpan.infrastructure.subscription.security.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * LogoutSuccessHandle
 *
 * @author kamingpan
 * @since 2019-04-16
 */
@Slf4j
public class LogoutHandler implements LogoutSuccessHandler {

    /**
     * 登录处理逻辑
     *
     * @param httpServletRequest  请求
     * @param httpServletResponse 响应
     * @param authentication      认证
     * @throws IOException      io异常
     * @throws ServletException servlet异常
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
    }
}
