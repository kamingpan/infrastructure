package com.kamingpan.infrastructure.entity.model.dto;

import com.kamingpan.infrastructure.entity.group.ConfigurationGroup;
import com.kamingpan.infrastructure.entity.model.entity.Configuration;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 配置信息 dto
 *
 * @author kamingpan
 * @since 2018-06-29
 */
@Data
public class ConfigurationDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 标题
     */
    @NotEmpty(message = "标题不能为空", groups = {ConfigurationGroup.Insert.class, ConfigurationGroup.Update.class})
    private String title;

    /**
     * 配置键
     */
    @NotEmpty(message = "配置键不能为空", groups = {ConfigurationGroup.Insert.class, ConfigurationGroup.Update.class})
    private String configKey;

    /**
     * 配置值
     */
    @NotEmpty(message = "配置值不能为空", groups = {ConfigurationGroup.Insert.class, ConfigurationGroup.Update.class})
    private String configValue;

    /**
     * 描述
     */
    private String description;

    public Configuration toConfiguration() {
        Configuration configuration = new Configuration();

        // 赋值
        configuration.setId(this.getId());
        configuration.setTitle(this.getTitle());
        configuration.setConfigKey(this.getConfigKey());
        configuration.setConfigValue(this.getConfigValue());
        configuration.setDescription(this.getDescription());

        return configuration;
    }

}
