package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;

/**
 * 模块
 *
 * @author kamingpan
 * @since 2019-04-01
 */
@TableName("system_module")
public class Module extends BaseEntity<Module> {

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
     * 状态（0：正常，1：禁用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    public Module() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getSuperior() {
        return superior;
    }

    public void setSuperior(String superior) {
        this.superior = superior;
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
        return "Module {" +
        "name=" + name +
        ", icon=" + icon +
        ", type=" + type +
        ", route=" + route +
        ", sort=" + sort +
        ", superior=" + superior +
        ", status=" + status +
        ", remark=" + remark +
        "}";
    }

}
