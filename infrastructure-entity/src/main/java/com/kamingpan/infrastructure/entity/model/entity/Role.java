package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;

/**
 * 角色
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@TableName("system_role")
public class Role extends BaseEntity<Role> {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 上级角色
     */
    private String superior;

    /**
     * 级别
     */
    private Integer level;

    /**
     * 状态（0：正常，1：禁用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    public Role() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "Role {" +
        "name=" + name +
        ", superior=" + superior +
        ", level=" + level +
        ", status=" + status +
        ", remark=" + remark +
        "}";
    }

}
