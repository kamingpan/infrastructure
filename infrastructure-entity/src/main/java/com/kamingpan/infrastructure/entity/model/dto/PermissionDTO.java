package com.kamingpan.infrastructure.entity.model.dto;

import com.kamingpan.infrastructure.entity.group.PermissionGroup;
import com.kamingpan.infrastructure.entity.model.entity.Permission;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 权限 dto
 *
 * @author kamingpan
 * @since 2019-02-14
 */
@Data
public class PermissionDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 权限名称
     */
    @NotEmpty(message = "权限名称不能为空", groups = PermissionGroup.Insert.class)
    private String name;

    /**
     * 所属模块
     */
    @NotEmpty(message = "所属模块不能为空", groups = {PermissionGroup.Insert.class, PermissionGroup.Update.class})
    private String moduleId;

    /**
     * 上级权限
     */
    @NotEmpty(message = "上级权限不能为空", groups = {PermissionGroup.Insert.class, PermissionGroup.Update.class})
    private String superior;

    /**
     * 图标
     */
    @NotEmpty(message = "图标不能为空", groups = {PermissionGroup.Insert.class, PermissionGroup.Update.class})
    private String icon;

    /**
     * 权限字符串
     */
    @NotEmpty(message = "权限字符串不能为空", groups = {PermissionGroup.Insert.class, PermissionGroup.Update.class})
    private String authentication;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {PermissionGroup.Insert.class, PermissionGroup.Update.class})
    @Digits(integer = 4, fraction = 0, message = "排序只能为整数",
            groups = {PermissionGroup.Insert.class, PermissionGroup.Update.class})
    private Integer sort;

    /**
     * 请求方法
     */
    @NotEmpty(message = "请求方法不能为空", groups = {PermissionGroup.Insert.class, PermissionGroup.Update.class})
    private String method;

    /**
     * 状态（0：正常，1：禁用；暂时无用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    public Permission toPermission() {
        Permission permission = new Permission();

        // 赋值
        permission.setId(this.getId());
        permission.setName(this.getName());
        permission.setModuleId(this.getModuleId());
        permission.setSuperior(this.getSuperior());
        permission.setIcon(this.getIcon());
        permission.setAuthentication(this.getAuthentication());
        permission.setSort(this.getSort());
        permission.setMethod(this.getMethod());
        permission.setStatus(this.getStatus());
        permission.setRemark(this.getRemark());

        return permission;
    }

}
