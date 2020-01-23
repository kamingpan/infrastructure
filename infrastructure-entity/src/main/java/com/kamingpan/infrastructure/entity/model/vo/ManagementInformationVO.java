package com.kamingpan.infrastructure.entity.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 管理端信息 vo
 *
 * @author kamingpan
 * @since 2018-06-30
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagementInformationVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 系统名称
     */
    private String name;

    /**
     * 系统logo
     */
    private String logo;

    /**
     * 系统版本
     */
    private String version;

    /**
     * 备注
     */
    private String remark;

    /**
     * logo链接
     */
    private String logoUrl;

    /**
     * 是否已登录
     */
    private boolean isLogin = false;

}
