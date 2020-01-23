package com.kamingpan.infrastructure.entity.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 配置信息 vo
 *
 * @author kamingpan
 * @since 2018-06-29
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigurationVO {

    /**
     * 主键id
     */
    public String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 配置键
     */
    private String configKey;

    /**
     * 配置值
     */
    private String configValue;

    /**
     * 描述
     */
    private String description;

    /**
     * 最后修改人
     */
    private String updater;

    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
