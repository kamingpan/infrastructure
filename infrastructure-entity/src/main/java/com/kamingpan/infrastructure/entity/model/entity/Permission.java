package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;

/**
 * 权限
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@TableName("system_permission")
public class Permission extends BaseEntity<Permission> {

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
     * 状态（0：正常，1：禁用；暂时无用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    public Permission() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
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
        return "Permission {" +
        "name=" + name +
        ", moduleId=" + moduleId +
        ", superior=" + superior +
        ", icon=" + icon +
        ", authentication=" + authentication +
        ", sort=" + sort +
        ", method=" + method +
        ", status=" + status +
        ", remark=" + remark +
        "}";
    }

}
