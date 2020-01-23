package com.kamingpan.infrastructure.management.security.handler;

import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.management.security.filter.TokenAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录成功处理
 *
 * @author kamingpan
 * @since 2019-01-04
 */
@Slf4j
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    /**
     * 登录成功逻辑
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 认证异常
     * @throws IOException      io异常
     * @throws ServletException 服务异常
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String admin = (String) authentication.getPrincipal();

        // 打印登录成功
        log.info("用户“{}”登录成功", admin);

        // 设置响应头
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        // 组装和响应token
        Map<String, Object> result = new HashMap<String, Object>();
        result.put(TokenAuthenticationFilter.ACCESS_TOKEN_KEY, authentication.getPrincipal());
        String responseDataString = ResponseData.build(result).toJson();
        response.getWriter().println(responseDataString);
        log.info("响应结果：{}", responseDataString);
    }

}
