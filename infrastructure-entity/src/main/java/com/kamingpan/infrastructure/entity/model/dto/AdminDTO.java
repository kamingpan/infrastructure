package com.kamingpan.infrastructure.entity.model.dto;

import com.kamingpan.infrastructure.entity.group.AdminGroup;
import com.kamingpan.infrastructure.entity.group.OperatorGroup;
import com.kamingpan.infrastructure.entity.model.entity.Admin;
import lombok.Data;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 管理员 dto
 *
 * @author kamingpan
 * @since 2018-06-29
 */
@Data
public class AdminDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 用户名
     */
    @NotEmpty(message = "用户名不能为空", groups = AdminGroup.Insert.class)
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 状态（0：正常，1：禁用）
     */
    @NotNull(message = "状态不能为空", groups = {AdminGroup.Insert.class, AdminGroup.Update.class})
    @Digits(integer = 2, fraction = 0, message = "状态只能为整数",
            groups = {AdminGroup.Insert.class, AdminGroup.Update.class})
    private Integer status;

    /**
     * 真实姓名
     */
    @NotEmpty(message = "姓名不能为空",
            groups = {AdminGroup.Insert.class, AdminGroup.Update.class, OperatorGroup.Update.class})
    private String fullName;

    /**
     * 手机号码
     */
    @NotEmpty(message = "手机号码不能为空",
            groups = {AdminGroup.Insert.class, AdminGroup.Update.class, OperatorGroup.Update.class})
    private String phone;

    /**
     * 头像
     */
    private String portrait;

    /**
     * 性别（0：女，1：男）
     */
    @NotNull(message = "性别不能为空",
            groups = {AdminGroup.Insert.class, AdminGroup.Update.class, OperatorGroup.Update.class})
    @Digits(integer = 2, fraction = 0, message = "性别只能为整数",
            groups = {AdminGroup.Insert.class, AdminGroup.Update.class, OperatorGroup.Update.class})
    private Integer gender;

    /**
     * 电子邮箱
     */
    @Email(message = "邮箱格式错误",
            groups = {AdminGroup.Insert.class, AdminGroup.Update.class, OperatorGroup.Update.class})
    private String email;

    /**
     * 限制ip
     */
    private String restrictIp;

    public Admin toAdmin() {
        Admin admin = new Admin();

        // 赋值
        admin.setId(this.getId());
        admin.setUsername(this.getUsername());
        admin.setPassword(this.getPassword());
        admin.setStatus(this.getStatus());
        admin.setFullName(this.getFullName());
        admin.setPhone(this.getPhone());
        admin.setPortrait(this.getPortrait());
        admin.setGender(this.getGender());
        admin.setEmail(this.getEmail());
        admin.setRestrictIp(this.getRestrictIp());

        return admin;
    }

}
