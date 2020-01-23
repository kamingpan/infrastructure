package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.OperatorLog;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;
import com.kamingpan.infrastructure.core.security.UserDetail;

import java.util.Date;

/**
 * 管理端操作日志
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@TableName("system_admin_operate_log")
public class AdminOperateLog extends BaseEntity<AdminOperateLog> implements OperatorLog {

    /**
     * 所属对象
     */
    private String belong;

    /**
     * 所属主键
     */
    private String belongId;

    /**
     * 操作类型（登录，登出，创建，修改，删除，启用，禁用，其它）
     */
    private String type;

    /**
     * 操作目标
     */
    private String target;

    /**
     * 操作ip
     */
    private String ip;

    /**
     * 操作链接地址
     */
    private String url;

    /**
     * 请求参数
     */
    private String parameter;

    /**
     * 方法名
     */
    private String method;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 操作描述
     */
    private String remark;

    /**
     * 操作人主键
     */
    private String operatorId;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作人头像
     */
    private String operatorPortrait;

    /**
     * 操作时间
     */
    private Date operateTime;

    public AdminOperateLog() {
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getBelongId() {
        return belongId;
    }

    public void setBelongId(String belongId) {
        this.belongId = belongId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperatorPortrait() {
        return operatorPortrait;
    }

    public void setOperatorPortrait(String operatorPortrait) {
        this.operatorPortrait = operatorPortrait;
    }

    public Date getOperateTime() {
        return operateTime;
    }

    public void setOperateTime(Date operateTime) {
        this.operateTime = operateTime;
    }

    @Override
    public String toString() {
        return "AdminOperateLog {" +
        "belong=" + belong +
        ", belongId=" + belongId +
        ", type=" + type +
        ", target=" + target +
        ", ip=" + ip +
        ", url=" + url +
        ", parameter=" + parameter +
        ", method=" + method +
        ", content=" + content +
        ", remark=" + remark +
        ", operatorId=" + operatorId +
        ", operator=" + operator +
        ", operatorPortrait=" + operatorPortrait +
        ", operateTime=" + operateTime +
        "}";
    }

    @Override
    public void initOperator() {
        super.initId();

        UserDetail userPrincipal = this.obtainOperator();
        this.setOperatorId(null != userPrincipal ? userPrincipal.getUserId() : null);
        this.setOperator(null != userPrincipal ? userPrincipal.getFullName() : null);
        this.setOperatorPortrait(null != userPrincipal ? userPrincipal.getPortrait() : null);
        this.setOperateTime(new Date());
    }

}
