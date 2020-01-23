package com.kamingpan.infrastructure.core.exception;

import com.kamingpan.infrastructure.core.response.ResponseStatus;

/**
 * 日志异常
 *
 * @author kamingpan
 * @since 2019-03-21
 */
public class LogErrorException extends CustomStatusException {

    public LogErrorException() {
        super(ResponseStatus.DATABASE_ERROR);
    }

    public LogErrorException(String message) {
        super(message);
    }

    public LogErrorException(Throwable cause) {
        super(cause);
    }

    public LogErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
