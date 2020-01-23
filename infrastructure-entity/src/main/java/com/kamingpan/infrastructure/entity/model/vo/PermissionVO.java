package com.kamingpan.infrastructure.entity.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 权限 vo
 *
 * @author kamingpan
 * @since 2018-06-30
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionVO {

    /**
     * 主键id
     */
    private String id;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 所属模块主键
     */
    private String moduleId;

    /**
     * 所属模块名称
     */
    private String moduleName;

    /**
     * 所属模块图标
     */
    private String moduleIcon;

    /**
     * 上级权限
     */
    private String superior;

    /**
     * 上级权限名称
     */
    private String superiorName;

    /**
     * 上级权限图标
     */
    private String superiorIcon;

    /**
     * 图标
     */
    private String icon;

    /**
     * 权限字符串
     */
    private String authentication;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 请求方法
     */
    private String method;

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
