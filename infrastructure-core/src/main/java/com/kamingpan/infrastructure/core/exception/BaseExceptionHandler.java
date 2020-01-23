package com.kamingpan.infrastructure.core.exception;

import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.util.string.ExceptionString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 捕捉异常，输出异常信息至页面
 *
 * @author kamingpan
 * @since 2016-05-22
 */
@Slf4j
public class BaseExceptionHandler {

    /**
     * 参数未设置异常格式字符串
     */
    private static final String PARAMETER_NOT_SET_EXCEPTION_FORMAT = "请设置参数[%s]";

    /**
     * 参数缺少
     *
     * @param exception 异常
     * @return 响应结果
     */
    @ResponseBody
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @org.springframework.web.bind.annotation.ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseData exceptionHandler(MissingServletRequestParameterException exception) {
        log.error("参数未设置", exception);
        return ResponseData.build(ResponseStatus.PARAMETER_NOT_SET,
                String.format(PARAMETER_NOT_SET_EXCEPTION_FORMAT, exception.getParameterName()));
    }

    /**
     * 数据不允许删除异常
     *
     * @param exception 异常
     * @return 响应结果
     */
    @ResponseBody
    @ExceptionHandler({DataCanNotDeleteException.class})
    @org.springframework.web.bind.annotation.ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseData exceptionHandler(DataCanNotDeleteException exception) {
        log.warn("数据不允许删除异常", exception);
        return ResponseData.build(exception.isEmptyStatus() ? ResponseStatus.DATA_CAN_NOT_DELETE.getStatus()
                : exception.getStatus(), exception.getMessage());
    }

    /**
     * 参数缺失异常
     *
     * @param exception 异常
     * @return 响应结果
     */
    @ResponseBody
    @ExceptionHandler({ValidateException.class})
    @org.springframework.web.bind.annotation.ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseData exceptionHandler(ValidateException exception) {
        log.error("参数缺失异常", exception);
        return ResponseData.build(exception.isEmptyStatus() ? ResponseStatus.VALIDATE_ERROR.getStatus()
                : exception.getStatus(), exception.getMessage());
    }

    /**
     * 参数校验异常
     *
     * @param exception 异常
     * @return 响应结果
     */
    @ResponseBody
    @ExceptionHandler({BindException.class})
    @org.springframework.web.bind.annotation.ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseData exceptionHandler(BindException exception) {
        log.error("参数校验异常", exception);
        return ResponseData.build(ResponseStatus.VALIDATE_ERROR, exception.getAllErrors().get(0).getDefaultMessage());
    }

    /**
     * 数据库异常
     *
     * @param exception 异常
     * @return 响应结果
     */
    @ResponseBody
    @ExceptionHandler({DatabaseErrorException.class})
    @org.springframework.web.bind.annotation.ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData exceptionHandler(DatabaseErrorException exception) {
        log.error("数据库异常", exception);
        return ResponseData.build(exception.isEmptyStatus() ? ResponseStatus.DATABASE_ERROR.getStatus()
                : exception.getStatus(), exception.getMessage());
    }

    /**
     * 数据不存在异常
     *
     * @param exception 异常
     * @return 响应结果
     */
    @ResponseBody
    @ExceptionHandler({DataNotExistException.class})
    @org.springframework.web.bind.annotation.ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseData exceptionHandler(DataNotExistException exception) {
        log.error("数据不存在异常", exception);
        return ResponseData.build(exception.isEmptyStatus() ? ResponseStatus.DATA_IS_NOT_EXIST.getStatus()
                : exception.getStatus(), exception.getMessage());
    }

    /**
     * 日志异常
     *
     * @param exception 异常
     * @return 响应结果
     */
    @ResponseBody
    @ExceptionHandler({LogErrorException.class})
    @org.springframework.web.bind.annotation.ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData exceptionHandler(LogErrorException exception) {
        log.error("日志异常", exception);
        return ResponseData.build(exception.isEmptyStatus() ? ResponseStatus.LOG_ERROR.getStatus()
                : exception.getStatus(), exception.getMessage());
    }

    /**
     * 自定义异常
     *
     * @param exception 异常
     * @return 响应结果
     */
    @ResponseBody
    @ExceptionHandler({CustomStatusException.class})
    @org.springframework.web.bind.annotation.ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData exceptionHandler(CustomStatusException exception) {
        String status = null == exception.getStatus() ? ResponseStatus.ERROR.getStatus() : exception.getStatus();
        log.error("{}状态异常", status, exception);
        return ResponseData.error(status, exception.getMessage(), ExceptionString.formatByWriter(exception));
    }

    /**
     * 未知异常
     *
     * @param exception 异常
     * @return 响应结果
     */
    @ResponseBody
    @ExceptionHandler({Exception.class})
    @org.springframework.web.bind.annotation.ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseData exceptionHandler(Exception exception) {
        log.error("未知异常", exception);
        return ResponseData.error(ResponseStatus.ERROR, exception.getMessage(),
                ExceptionString.formatByWriter(exception));
    }

}
