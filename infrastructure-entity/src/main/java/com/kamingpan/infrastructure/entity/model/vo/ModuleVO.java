package com.kamingpan.infrastructure.entity.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 模块 vo
 *
 * @author kamingpan
 * @since 2018-06-30
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModuleVO {

    /**
     * 主键id
     */
    private String id;

    /**
     * 模块名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 路由类型（load：组件加载，iframe：内嵌页面）
     */
    private String type;

    /**
     * 路由类型标签
     */
    private String typeLabel;

    /**
     * 路由路径
     */
    private String route;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 上级模块
     */
    private String superior;

    /**
     * 上级模块名称
     */
    private String superiorName;

    /**
     * 上级模块图标
     */
    private String superiorIcon;

    /**
     * 状态（0：正常，1：禁用）
     */
    private Integer status;

    /**
     * 状态标签
     */
    private String statusLabel;

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
