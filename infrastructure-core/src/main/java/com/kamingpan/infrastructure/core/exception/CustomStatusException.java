package com.kamingpan.infrastructure.core.exception;

import com.kamingpan.infrastructure.core.response.ResponseStatus;

/**
 * 自定义状态异常
 *
 * @author kamingpan
 * @since 2019-01-22
 */
public class CustomStatusException extends RuntimeException {

    private String status;

    public CustomStatusException() {
        this(ResponseStatus.ERROR);
    }

    public CustomStatusException(String message) {
        super(message);
    }

    public CustomStatusException(String message, String status) {
        super(message);
        this.status = status;
    }

    public CustomStatusException(Throwable cause) {
        super(cause);
    }

    public CustomStatusException(Throwable cause, String status) {
        super(cause);
        this.status = status;
    }

    public CustomStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomStatusException(String message, Throwable cause, String status) {
        super(message, cause);
        this.status = status;
    }

    public CustomStatusException(ResponseStatus responseStatus) {
        super(null == responseStatus ? ResponseStatus.ERROR.getMessage() : responseStatus.getMessage());
        this.status = (null == responseStatus) ? ResponseStatus.ERROR.getStatus() : responseStatus.getStatus();
    }

    public String getStatus() {
        return status;
    }

    public boolean isEmptyStatus() {
        return null == this.status || this.status.isEmpty();
    }

}
