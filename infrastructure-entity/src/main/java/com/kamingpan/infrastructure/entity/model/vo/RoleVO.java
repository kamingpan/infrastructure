package com.kamingpan.infrastructure.entity.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 角色 vo
 *
 * @author kamingpan
 * @since 2018-06-30
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleVO {

    /**
     * 主键id
     */
    private String id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 上级角色
     */
    private String superior;

    /**
     * 上级角色名称
     */
    private String superiorName;

    /**
     * 级别
     */
    private Integer level;

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
