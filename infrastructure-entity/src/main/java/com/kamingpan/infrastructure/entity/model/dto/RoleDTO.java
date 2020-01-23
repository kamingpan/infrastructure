package com.kamingpan.infrastructure.entity.model.dto;

import com.kamingpan.infrastructure.entity.group.RoleGroup;
import com.kamingpan.infrastructure.entity.model.entity.Role;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 角色 dto
 *
 * @author kamingpan
 * @since 2018-06-30
 */
@Data
public class RoleDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 角色名称
     */
    @NotEmpty(message = "角色名称不能为空", groups = RoleGroup.Insert.class)
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
    @NotNull(message = "角色名称不能为空", groups = {RoleGroup.Insert.class, RoleGroup.Update.class})
    @Digits(integer = 2, fraction = 0, message = "状态只能为整数",
            groups = {RoleGroup.Insert.class, RoleGroup.Update.class})
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    public Role toRole() {
        Role role = new Role();

        // 赋值
        role.setId(this.getId());
        role.setName(this.getName());
        role.setSuperior(this.getSuperior());
        role.setLevel(this.getLevel());
        role.setStatus(this.getStatus());
        role.setRemark(this.getRemark());

        return role;
    }

}
