package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;

/**
 * 管理端信息
 *
 * @author kamingpan
 * @since 2019-04-01
 */
@TableName("system_management_information")
public class ManagementInformation extends BaseEntity<ManagementInformation> {

    /**
     * 系统名称
     */
    private String name;

    /**
     * 系统logo
     */
    private String logo;

    /**
     * 系统logo文字
     */
    private String logoWord;

    /**
     * 显示类型（1：显示logo，2：显示文字）
     */
    private Integer type;

    /**
     * 主页路由
     */
    private String route;

    /**
     * 系统版本
     */
    private String version;

    /**
     * 备注
     */
    private String remark;

    public ManagementInformation() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogoWord() {
        return logoWord;
    }

    public void setLogoWord(String logoWord) {
        this.logoWord = logoWord;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "ManagementInformation {" +
        "name=" + name +
        ", logo=" + logo +
        ", logoWord=" + logoWord +
        ", type=" + type +
        ", route=" + route +
        ", version=" + version +
        ", remark=" + remark +
        "}";
    }

}
