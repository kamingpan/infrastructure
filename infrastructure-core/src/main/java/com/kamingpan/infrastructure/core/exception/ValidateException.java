package com.kamingpan.infrastructure.core.exception;

import com.kamingpan.infrastructure.core.response.ResponseStatus;

/**
 * 数据校验异常
 *
 * @author kamingpan
 * @since 2017-07-26
 */
public class ValidateException extends CustomStatusException {

    public ValidateException() {
        super(ResponseStatus.VALIDATE_ERROR);
    }

    public ValidateException(String message) {
        super(message);
    }

    public ValidateException(Throwable cause) {
        super(cause);
    }

    public ValidateException(String message, Throwable cause) {
        super(message, cause);
    }
}
