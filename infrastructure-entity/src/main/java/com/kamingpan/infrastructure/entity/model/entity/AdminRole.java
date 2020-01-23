package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;

/**
 * 管理员-角色关联
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@TableName("system_admin_role")
public class AdminRole extends BaseEntity<AdminRole> {

    /**
     * 管理员主键
     */
    private String adminId;

    /**
     * 角色主键
     */
    private String roleId;

    public AdminRole() {
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    @Override
    public String toString() {
        return "AdminRole {" +
        "adminId=" + adminId +
        ", roleId=" + roleId +
        "}";
    }

}
