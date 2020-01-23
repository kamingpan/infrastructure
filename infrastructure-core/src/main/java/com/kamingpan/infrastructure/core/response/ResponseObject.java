package com.kamingpan.infrastructure.core.response;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * 响应对象
 *
 * @author kamingpan
 * @since 2016-04-05
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseObject {

    /**
     * 数据对象
     */
    private Object list;

    /**
     * 分页
     */
    private Pager pager;


    public ResponseObject() {
    }

    public ResponseObject(Object list, Pager pager) {
        this.pager = pager;
        this.list = list;
    }

    ResponseObject(Object list) {
        this.list = list;
    }

    ResponseObject(Pager pager) {
        this.pager = pager;
    }

    public Object getList() {
        return list;
    }

    public void setList(Object list) {
        this.list = list;
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public static ResponseObject build(Object list, Pager pager) {
        return new ResponseObject(list, pager);
    }

}
