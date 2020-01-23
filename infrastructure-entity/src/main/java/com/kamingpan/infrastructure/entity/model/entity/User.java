package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;

import java.util.Date;

/**
 * 用户
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@TableName("system_user")
public class User extends BaseEntity<User> {

    /**
     * 状态（0：正常，1：禁用）
     */
    private Integer status;

    /**
     * 注册时间
     */
    private Date registeredTime;

    /**
     * 注册来源（0：系统注册，1：公众号授权，2：小程序授权）
     */
    private Integer registeredSource;

    public User() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getRegisteredTime() {
        return registeredTime;
    }

    public void setRegisteredTime(Date registeredTime) {
        this.registeredTime = registeredTime;
    }

    public Integer getRegisteredSource() {
        return registeredSource;
    }

    public void setRegisteredSource(Integer registeredSource) {
        this.registeredSource = registeredSource;
    }

    @Override
    public String toString() {
        return "User {" +
        ", status=" + status +
        ", registeredTime=" + registeredTime +
        ", registeredSource=" + registeredSource +
        "}";
    }

}
