package com.kamingpan.infrastructure.entity.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 权限列表 vo（用于树形结构）
 *
 * @author kamingpan
 * @since 2019-02-04
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PermissionListVO {

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
     * 上级权限
     */
    private String superior;

    /**
     * 图标
     */
    private String icon;

    /**
     * 权限名称
     */
    private List<PermissionListVO> children;

}
