package com.kamingpan.infrastructure.core.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * 分页
 *
 * @author kamingpan
 * @since 2016-03-01
 */
public class Pager implements Serializable {

    // 当前页码
    private int pageNum = 1;

    // 每页数量
    private int pageSize = 10;

    // 数据总量
    private long total = 0L;

    // 页数总量
    private long pages = 1L;

    // 起始行号
    private int offset = 0;

    // sql排序字符串
    private String orderBy;

    public Pager() {
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = (null != pageNum && pageNum > 0) ? pageNum : 1;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = (null != pageSize && pageSize >= -1) ? pageSize : 10;
    }

    public long getPages() {
        this.pages = this.total / this.pageSize;
        if (this.total % this.pageSize != 0) {
            this.pages++;
        }
        if (this.pages == 0) {
            this.pages = 1;
        }
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        if (null != total && total >= 0) {
            this.total = total;
        }
        if (this.pageNum > this.getPages()) {
            this.pageNum = (int) this.getPages();
        }
    }

    @JsonIgnore
    public Integer getOffset() {
        return this.offset < 0 ? ((pageNum - 1) * pageSize) : this.offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

}
