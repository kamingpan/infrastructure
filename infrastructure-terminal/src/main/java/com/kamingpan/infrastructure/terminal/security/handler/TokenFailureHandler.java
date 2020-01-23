package com.kamingpan.infrastructure.terminal.security.handler;

import com.kamingpan.infrastructure.core.base.constant.FinalConstant;
import com.kamingpan.infrastructure.core.util.ThreadMdc;
import com.kamingpan.infrastructure.terminal.security.properties.SecurityProperties;
import com.kamingpan.infrastructure.util.ip.IP;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * token认证失败处理
 *
 * @author kamingpan
 * @since 2019-01-04
 */
@Slf4j
@Component
public class TokenFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * token认证失败逻辑
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
        // 打印异常
        log.debug("token认证失败", exception);

        // 设置日志追踪id
        String traceId = ThreadMdc.getTraceId();
        MDC.put(ThreadMdc.TRACE_KEY, traceId);
        log.debug("日志追踪id“{}”设置成功...", traceId);

        String method = request.getMethod();
        log.info("请求路径：{}", IP.getIP(request) + " - " + request.getRequestURI() + "(" + method + ")");
        if (FinalConstant.HttpRequestMethod.GET.equals(method) && null != request.getQueryString()) {
            log.info("请求参数：{}", request.getQueryString());
        }

        if (FinalConstant.HttpRequestMethod.POST.equals(method) || FinalConstant.HttpRequestMethod.PUT.equals(method)
                || FinalConstant.HttpRequestMethod.PATCH.equals(method)) {
            StringBuilder params = new StringBuilder();
            params.append("{");
            Map<String, String[]> paramMap = request.getParameterMap();
            Iterator<Map.Entry<String, String[]>> iterator = paramMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String[]> entry = iterator.next();
                params.append(entry.getKey());
                params.append("=");
                params.append((entry.getValue())[0]);
                if (iterator.hasNext()) {
                    params.append(", ");
                }
            }
            params.append("}");
            log.info("请求参数：{}", params.toString());
        }

        // 转发到未登录响应
        request.getRequestDispatcher(this.securityProperties.getLoginPage()).forward(request, response);
    }

}
