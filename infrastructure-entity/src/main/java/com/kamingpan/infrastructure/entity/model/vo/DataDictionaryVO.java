package com.kamingpan.infrastructure.entity.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 数据字典 vo
 *
 * @author kamingpan
 * @since 2018-07-13
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DataDictionaryVO {

    /**
     * 主键id
     */
    private String id;

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

    /**
     * 最后修改人
     */
    private String updater;

    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
