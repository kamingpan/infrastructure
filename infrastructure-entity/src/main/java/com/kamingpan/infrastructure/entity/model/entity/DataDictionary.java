package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;

/**
 * 数据字典（管理所有实体用键表示的值）
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@TableName("system_data_dictionary")
public class DataDictionary extends BaseEntity<DataDictionary> {

    /**
     * 标签
     */
    private String label;

    /**
     * 值
     */
    private String value;

    /**
     * 类型
     */
    private String type;

    /**
     * 类/表
     */
    private String clazz;

    /**
     * 变量/字段
     */
    private String variable;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;

    public DataDictionary() {
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "DataDictionary {" +
        "label=" + label +
        ", value=" + value +
        ", type=" + type +
        ", clazz=" + clazz +
        ", variable=" + variable +
        ", sort=" + sort +
        ", remark=" + remark +
        "}";
    }

}
