package com.kamingpan.infrastructure.entity.model.dto;

import com.kamingpan.infrastructure.entity.model.entity.Module;
import lombok.Data;

/**
 * 模块 dto
 *
 * @author kamingpan
 * @since 2019-02-14
 */
@Data
public class ModuleDTO {

    /**
     * 主键
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

    public Module toModule() {
        Module module = new Module();

        // 赋值
        module.setId(this.getId());
        module.setName(this.getName());
        module.setIcon(this.getIcon());
        module.setType(this.getType());
        module.setSuperior(this.getSuperior());
        module.setRoute(this.getRoute());
        module.setSort(this.getSort());
        module.setSuperior(this.getSuperior());
        module.setStatus(this.getStatus());
        module.setRemark(this.getRemark());

        return module;
    }

}
