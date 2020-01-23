package com.kamingpan.infrastructure.entity.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 模块列表 vo（用于侧边栏）
 *
 * @author kamingpan
 * @since 2019-01-22
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModuleListVO {

    /**
     * 主键id
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
     * 路由类型（container：内置容器，iframe：内嵌页面）
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
     * 子模块
     */
    private List<ModuleListVO> children;

}
