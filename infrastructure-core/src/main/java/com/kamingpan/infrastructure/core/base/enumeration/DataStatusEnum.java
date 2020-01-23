package com.kamingpan.infrastructure.core.base.enumeration;

/**
 * 数据状态
 *
 * @author kamingpan
 * @since 2018-06-27
 */
public enum DataStatusEnum {

    NOT_DELETED(0, false, "未删除"),
    DELETED(1, true, "已删除");

    /**
     * 数据库值
     */
    private Integer value;

    /**
     * 状态
     */
    private Boolean status;

    /**
     * 标签描述
     */
    private String label;

    DataStatusEnum(Integer value, Boolean status, String label) {
        this.value = value;
        this.status = status;
        this.label = label;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getLabel() {
        return label;
    }
}
