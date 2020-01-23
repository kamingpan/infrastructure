package com.kamingpan.infrastructure.management.security.handler;

import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录失败处理
 *
 * @author kamingpan
 * @since 2019-01-04
 */
@Slf4j
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

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

        // 设置响应头
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String responseDataString = ResponseData.build(ResponseStatus.ERROR, exception.getMessage()).toJson();
        response.getWriter().println(responseDataString);
        log.info("响应结果：{}", responseDataString);
    }

}
