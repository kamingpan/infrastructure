package com.kamingpan.infrastructure.core.interceptor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kamingpan.infrastructure.core.response.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * ResponseInterceptor
 *
 * @author kamingpan
 * @since 2019-08-13
 */
@Slf4j
@ControllerAdvice
public class ResponseInterceptor implements ResponseBodyAdvice {

    /**
     * 直接返回true,表示对任何handler的ResponseBody都调用beforeBodyWrite方法
     *
     * @param methodParameter 方法参数
     * @param clazz           类
     * @return 是否调用beforeBodyWrite方法
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Class clazz) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object object, MethodParameter methodParameter, MediaType mediaType, Class clazz,
                                  ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {
        try {
            log.info("响应结果：{}", ResponseData.OBJECT_MAPPER.writeValueAsString(object));
        } catch (JsonProcessingException exception) {
            log.warn("响应结果转换异常", exception);
        }

        return object;
    }
}
