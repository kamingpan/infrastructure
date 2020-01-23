package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;

/**
 * 注册用户
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@TableName("system_registered_user")
public class RegisteredUser extends BaseEntity<RegisteredUser> {

    /**
     * 用户主键
     */
    private String userId;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    public RegisteredUser() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "RegisteredUser {" +
        "userId=" + userId +
        ", mobile=" + mobile +
        ", username=" + username +
        ", password=" + password +
        "}";
    }

}
