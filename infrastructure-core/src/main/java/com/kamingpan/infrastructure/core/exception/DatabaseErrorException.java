package com.kamingpan.infrastructure.core.exception;

import com.kamingpan.infrastructure.core.response.ResponseStatus;

/**
 * 数据库异常
 *
 * @author kamingpan
 * @since 2016-04-08
 */
public class DatabaseErrorException extends CustomStatusException {

    public DatabaseErrorException() {
        super(ResponseStatus.DATABASE_ERROR);
    }

    public DatabaseErrorException(String message) {
        super(message);
    }

    public DatabaseErrorException(Throwable cause) {
        super(cause);
    }

    public DatabaseErrorException(String message, Throwable cause) {
        super(message, cause);
    }
}
