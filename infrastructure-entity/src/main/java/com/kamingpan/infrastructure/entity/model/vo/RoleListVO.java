package com.kamingpan.infrastructure.entity.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 角色列表 vo（用于树形结构）
 *
 * @author kamingpan
 * @since 2019-01-31
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleListVO {

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
     * 图标
     */
    private String icon;

    /**
     * 角色名称
     */
    private List<RoleListVO> children;

}
