package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;

/**
 * 配置信息
 *
 * @author kamingpan
 * @since 2020-01-11
 */
@TableName("system_configuration")
public class Configuration extends BaseEntity<Configuration> {

    /**
     * 标题
     */
    private String title;

    /**
     * 配置键
     */
    private String configKey;

    /**
     * 配置值（json格式）
     */
    private String configValue;

    /**
     * 描述
     */
    private String description;

    public Configuration() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }

    public String getConfigValue() {
        return configValue;
    }

    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Configuration {" +
        "title=" + title +
        ", configKey=" + configKey +
        ", configValue=" + configValue +
        ", description=" + description +
        "}";
    }

}
