package com.kamingpan.infrastructure.core.exception;

import com.kamingpan.infrastructure.core.response.ResponseStatus;

/**
 * 数据不允许删除异常
 *
 * @author kamingpan
 * @since 2017-12-04
 */
public class DataCanNotDeleteException extends CustomStatusException {

    public DataCanNotDeleteException() {
        super(ResponseStatus.DATA_CAN_NOT_DELETE);
    }

    public DataCanNotDeleteException(String message) {
        super(message);
    }

    public DataCanNotDeleteException(Throwable cause) {
        super(cause);
    }

    public DataCanNotDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
