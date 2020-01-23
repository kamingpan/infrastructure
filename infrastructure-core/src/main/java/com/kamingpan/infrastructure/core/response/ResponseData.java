package com.kamingpan.infrastructure.core.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 响应数据
 *
 * @author kamingpan
 * @since 2016-04-05
 */
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class ResponseData {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 状态码
     */
    private String status;

    /**
     * 信息
     */
    private String message;

    /**
     * 异常
     */
    private String error;

    /**
     * 响应数据
     */
    private Object data;

    public ResponseData() {
        this.status = ResponseStatus.SUCCESS.getStatus();
        this.message = ResponseStatus.SUCCESS.getMessage();
    }

    public ResponseData(ResponseStatus responseStatus) {
        this.status = responseStatus.getStatus();
        this.message = responseStatus.getMessage();
    }

    public ResponseData(ResponseStatus responseStatus, String message) {
        this.status = responseStatus.getStatus();
        this.message = message;
    }

    public ResponseData(String error, ResponseStatus responseStatus) {
        this.status = responseStatus.getStatus();
        this.message = responseStatus.getMessage();
        this.error = error;
    }

    public ResponseData(ResponseStatus responseStatus, String message, String error) {
        this.status = responseStatus.getStatus();
        this.message = message;
        this.error = error;
    }

    public ResponseData(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public ResponseData(String status, String message, String error) {
        this.status = status;
        this.message = message;
        this.error = error;
    }

    public ResponseData(Object data) {
        this.status = ResponseStatus.SUCCESS.getStatus();
        this.message = ResponseStatus.SUCCESS.getMessage();
        this.data = data;
    }

    public ResponseData(ResponseStatus responseStatus, Object data) {
        this.status = responseStatus.getStatus();
        this.message = responseStatus.getMessage();
        this.data = data;
    }

    public ResponseData(String status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ResponseData(Object data, Pager pager) {
        this.status = ResponseStatus.SUCCESS.getStatus();
        this.message = ResponseStatus.SUCCESS.getMessage();
        this.data = ResponseObject.build(data, pager);
    }

    public ResponseData(ResponseStatus responseStatus, Object data, Pager pager) {
        this.status = responseStatus.getStatus();
        this.message = responseStatus.getMessage();
        this.data = ResponseObject.build(data, pager);
    }

    public ResponseData(String status, String message, Object data, Pager pager) {
        this.status = status;
        this.message = message;
        this.data = ResponseObject.build(data, pager);
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public String getError() {
        return error;
    }

    public void log() {
        log.debug("响应对象：{}", this.toJson());
    }

    public String toJson() {
        try {
            return ResponseData.OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException exception) {
            return null;
        }
    }

    public static ResponseData success() {
        return new ResponseData();
    }

    public static ResponseData error(String error, ResponseStatus responseStatus) {
        return new ResponseData(error, responseStatus);
    }

    public static ResponseData error(ResponseStatus responseStatus, String message, String error) {
        return new ResponseData(responseStatus, message, error);
    }

    public static ResponseData error(String status, String message, String error) {
        return new ResponseData(status, message, error);
    }

    public static ResponseData build(ResponseStatus responseStatus) {
        return new ResponseData(responseStatus);
    }

    public static ResponseData build(ResponseStatus responseStatus, String message) {
        return new ResponseData(responseStatus, message);
    }

    public static ResponseData build(String status, String message) {
        return new ResponseData(status, message);
    }

    public static ResponseData build(Object data) {
        return new ResponseData(data);
    }

    public static ResponseData build(ResponseStatus responseStatus, Object data) {
        return new ResponseData(responseStatus, data);
    }

    public static ResponseData build(String status, String message, Object data) {
        return new ResponseData(status, message, data);
    }

    public static ResponseData buildPagination(Object data, Pager pager) {
        return new ResponseData(data, pager);
    }

    public static ResponseData buildPagination(ResponseStatus responseStatus, Object data, Pager pager) {
        return new ResponseData(responseStatus, data, pager);
    }

    public static ResponseData buildPagination(String status, String message, Object data, Pager pager) {
        return new ResponseData(status, message, data, pager);
    }

}
