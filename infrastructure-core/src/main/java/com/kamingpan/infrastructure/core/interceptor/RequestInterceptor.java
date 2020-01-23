package com.kamingpan.infrastructure.core.interceptor;

import com.kamingpan.infrastructure.core.base.constant.FinalConstant;
import com.kamingpan.infrastructure.core.properties.SystemProperties;
import com.kamingpan.infrastructure.core.util.ThreadMdc;
import com.kamingpan.infrastructure.util.ip.IP;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.Map;

/**
 * 请求拦截器，默认拦截所有路径
 *
 * @author kamingpan
 * @since 2016-03-01
 */
@Slf4j
@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

    /**
     * 请求时间键
     */
    private final static String REQUEST_TIME_KEY = "requestTime";

    @Autowired
    private SystemProperties systemProperties;

    /**
     * 拦截请求并打印请求信息
     *
     * @param request  请求
     * @param response 响应
     * @param handler  对象
     * @return 是否继续
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 设置日志追踪id
        MDC.put(ThreadMdc.TRACE_KEY, ThreadMdc.getTraceId());
        MDC.put(RequestInterceptor.REQUEST_TIME_KEY, String.valueOf(System.currentTimeMillis()));
        // log.debug("日志追踪id“{}”设置成功...", traceId);

        // 判断是否静态资源，如果是，则不打印请求参数日志
        /*if (this.isStatic(request.getRequestURI())) {
            return true;
        }*/

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

        // return super.preHandle(request, response, handler);
        return true;
    }

    /**
     * 打印请求响应信息
     *
     * @param request      请求
     * @param response     响应
     * @param handler      对象
     * @param modelAndView 视图
     * @throws Exception 异常
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        response.setCharacterEncoding("UTF-8");
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        super.afterCompletion(request, response, handler, exception);

        // 获取请求时间
        String requestTimeString = MDC.get(RequestInterceptor.REQUEST_TIME_KEY);
        if (null != requestTimeString && !requestTimeString.isEmpty()) {
            long consumeTime = System.currentTimeMillis() - Long.valueOf(requestTimeString);
            if (consumeTime >= this.systemProperties.getTimeoutWarning()) {
                log.warn("警告！！！本次请求超过预设时间：{}毫秒，请求地址：{}",
                        this.systemProperties.getTimeoutWarning(), request.getRequestURI());
            }

            log.info("消耗时间：{}毫秒", consumeTime);
        }
    }

    /**
     * 判断是否静态资源
     *
     * @param requestURI 请求地址
     * @return 是否静态资源
     */
    private boolean isStatic(String requestURI) {
        if (requestURI.contains(".js")) {
            return true;
        } else if (requestURI.contains(".css")) {
            return true;
        } else if (requestURI.contains(".map")) {
            return true;
        }

        if (requestURI.contains(".png")) {
            return true;
        } else if (requestURI.contains(".jpg")) {
            return true;
        } else if (requestURI.contains(".gif")) {
            return true;
        } else if (requestURI.contains(".ico")) {
            return true;
        } else if (requestURI.contains(".svg")) {
            return true;
        }

        if (requestURI.contains(".ttf")) {
            return true;
        } else if (requestURI.contains(".woff")) {
            return true;
        } else if (requestURI.contains(".eot")) {
            return true;
        }
        return false;
    }
}
