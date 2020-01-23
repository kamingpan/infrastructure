package com.kamingpan.infrastructure.management.exception;

import com.kamingpan.infrastructure.core.exception.BaseExceptionHandler;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 管理端统一异常捕捉
 *
 * @author kamingpan
 * @since 2017-03-14
 */
@Slf4j
@ControllerAdvice
public class ManagementExceptionHandler extends BaseExceptionHandler {

    /**
     * 无权限异常（spring security）
     *
     * @param exception 异常
     * @return 响应结果
     */
    @ResponseBody
    @ExceptionHandler({AccessDeniedException.class})
    @org.springframework.web.bind.annotation.ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ResponseData exceptionHandler(AccessDeniedException exception) {
        log.error("无操作权限异常", exception);
        return ResponseData.build(ResponseStatus.NOT_PERMISSION);
    }

}
