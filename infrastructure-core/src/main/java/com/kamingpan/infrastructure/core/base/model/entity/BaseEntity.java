package com.kamingpan.infrastructure.core.base.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.kamingpan.infrastructure.core.base.enumeration.DataStatusEnum;
import com.kamingpan.infrastructure.core.security.UserDetail;
import com.kamingpan.infrastructure.util.id.IdWorker;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.Serializable;
import java.security.Principal;
import java.util.Date;

/**
 * Entity基础类
 *
 * @author kamingpan
 * @since 2016-08-30
 */
public class BaseEntity<T extends Model> extends Model {

    /**
     * 主键id
     */
    protected String id;

    /**
     * 创建人主键
     */
    @TableField("creator_id")
    protected String creatorId;

    /**
     * 创建人
     */
    protected String creator;

    /**
     * 创建时间
     */
    @TableField("create_time")
    protected Date createTime;

    /**
     * 最后修改人主键
     */
    @TableField("updater_id")
    protected String updaterId;

    /**
     * 最后修改人
     */
    protected String updater;

    /**
     * 最后修改时间
     */
    @TableField("update_time")
    protected Date updateTime;

    /**
     * 是否已删除（0：未删除，1：已删除）
     */
    @TableLogic
    protected Boolean deleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdaterId() {
        return updaterId;
    }

    public void setUpdaterId(String updaterId) {
        this.updaterId = updaterId;
    }

    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }

    public Date getUpdateTime() {
        return this.updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void initId() {
        this.id = IdWorker.getStringId(); // 获取纯数字可排序id
    }

    /**
     * 赋值创建人，最后创建人，最后修改人和最后修改时间
     */
    public void preInsert() {
        this.id = IdWorker.getStringId(); // 获取纯数字可排序id

        String userId = this.obtainOperatorId();
        String name = this.obtainOperatorName();
        this.preInsert(userId, name);
    }

    /**
     * 赋值创建人，最后创建人，最后修改人和最后修改时间
     *
     * @param userId 主键
     * @param name   姓名
     */
    public void preInsert(String userId, String name) {
        this.setCreatorId(userId);
        this.setCreator(name);
        this.setUpdaterId(userId);
        this.setUpdater(name);

        Date now = new Date();
        this.setCreateTime(now);
        this.setUpdateTime(now);

        // 设置为未删除状态
        this.deleted = DataStatusEnum.NOT_DELETED.getStatus();
    }

    /**
     * 赋值最后修改人和最后修改时间
     */
    public void preUpdate() {
        String userId = this.obtainOperatorId();
        String name = this.obtainOperatorName();
        this.preUpdate(userId, name);
    }

    /**
     * 赋值最后修改人和最后修改时间
     *
     * @param userId 主键
     * @param name   姓名
     */
    public void preUpdate(String userId, String name) {
        this.setUpdaterId(userId);
        this.setUpdater(name);
        this.setUpdateTime(new Date());

        this.setCreatorId(null); // 设置为空，不进行修改，前提是sql语句进行空判排除
        this.setCreator(null); // 设置为空，不进行修改，前提是sql语句进行空判排除
        this.setCreateTime(null); // 设置为空，不进行修改，前提是sql语句进行空判排除
    }

    /**
     * 获取当前登录用户详情
     *
     * @return 主键
     */
    public UserDetail obtainOperator() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null == attributes) {
            return null;
        }

        Principal principal = attributes.getRequest().getUserPrincipal();
        if (!(principal instanceof Authentication)) {
            return null;
        }

        Authentication authentication = (Authentication) principal;
        return (authentication.getDetails() instanceof UserDetail) ? (UserDetail) authentication.getDetails() : null;
    }

    /**
     * 获取当前登录用户主键
     *
     * @return 主键
     */
    public String obtainOperatorId() {
        UserDetail userDetail = this.obtainOperator();
        return null == userDetail ? null : userDetail.getUserId();
    }

    /**
     * 获取当前登录用户姓名
     *
     * @return 主键
     */
    public String obtainOperatorName() {
        UserDetail userDetail = this.obtainOperator();
        return null == userDetail ? null : userDetail.getFullName();
    }

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal() {
        return this.getId();
    }

}
