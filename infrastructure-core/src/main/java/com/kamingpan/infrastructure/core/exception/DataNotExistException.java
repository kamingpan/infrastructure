package com.kamingpan.infrastructure.core.exception;

import com.kamingpan.infrastructure.core.response.ResponseStatus;

/**
 * 数据不存在异常
 *
 * @author kamingpan
 * @since 2017-03-02
 */
public class DataNotExistException extends CustomStatusException {

    public DataNotExistException() {
        super(ResponseStatus.DATA_IS_NOT_EXIST);
    }

    public DataNotExistException(String message) {
        super(message);
    }

    public DataNotExistException(Throwable cause) {
        super(cause);
    }

    public DataNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
