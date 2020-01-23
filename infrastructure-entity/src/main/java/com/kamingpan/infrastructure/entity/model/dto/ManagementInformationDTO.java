package com.kamingpan.infrastructure.entity.model.dto;

import com.kamingpan.infrastructure.entity.group.ManagementInformationGroup;
import com.kamingpan.infrastructure.entity.model.entity.ManagementInformation;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 管理端信息 dto
 *
 * @author kamingpan
 * @since 2019-03-26
 */
@Data
public class ManagementInformationDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 系统名称
     */
    @NotEmpty(message = "系统名称", groups = {ManagementInformationGroup.Update.class})
    private String name;

    /**
     * 系统logo
     */
    private String logo;

    /**
     * 系统版本
     */
    @NotEmpty(message = "系统版本", groups = {ManagementInformationGroup.Update.class})
    private String version;

    /**
     * 备注
     */
    private String remark;

    public ManagementInformation toManagementInformation() {
        ManagementInformation managementInformation = new ManagementInformation();

        // 赋值
        managementInformation.setId(this.getId());
        managementInformation.setName(this.getName());
        managementInformation.setLogo(this.getLogo());
        managementInformation.setVersion(this.getVersion());
        managementInformation.setRemark(this.getRemark());

        return managementInformation;
    }
}
