package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.ManagementInformation;
import com.kamingpan.infrastructure.entity.model.vo.ManagementInformationVO;

/**
 * 管理端信息 服务类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
public interface ManagementInformationService extends BaseService<ManagementInformation> {

    /**
     * 修改系统信息
     *
     * @param managementInformation 系统信息
     * @param adminOperateLog       操作日志
     */
    void update(ManagementInformation managementInformation, AdminOperateLog adminOperateLog);

    /**
     * 根据主键查询管理端信息
     *
     * @param id 主键
     * @return 管理端信息
     */
    ManagementInformationVO getManagementInformationById(String id);

}
