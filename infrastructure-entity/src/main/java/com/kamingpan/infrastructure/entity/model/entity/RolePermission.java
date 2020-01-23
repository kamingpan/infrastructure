package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;

/**
 * 角色-权限关联
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@TableName("system_role_permission")
public class RolePermission extends BaseEntity<RolePermission> {

    /**
     * 角色主键
     */
    private String roleId;

    /**
     * 模块权限主键
     */
    private String permissionId;

    public RolePermission() {
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    @Override
    public String toString() {
        return "RolePermission {" +
        "roleId=" + roleId +
        ", permissionId=" + permissionId +
        "}";
    }

}
