package com.kamingpan.infrastructure.entity.service.impl;

import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.core.log.OperateLog;
import com.kamingpan.infrastructure.core.util.ChangeDetails;
import com.kamingpan.infrastructure.entity.constant.AdminOperateLogConstant;
import com.kamingpan.infrastructure.entity.constant.ManagementInformationConstant;
import com.kamingpan.infrastructure.entity.dao.ManagementInformationDao;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.ManagementInformation;
import com.kamingpan.infrastructure.entity.model.vo.ManagementInformationVO;
import com.kamingpan.infrastructure.entity.service.ManagementInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理端信息 服务实现类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class ManagementInformationServiceImpl extends BaseServiceImpl<ManagementInformation, ManagementInformationDao> implements ManagementInformationService {

    /**
     * 修改系统信息
     *
     * @param managementInformation 系统信息
     * @param adminOperateLog       操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.UPDATE)
    public void update(ManagementInformation managementInformation, AdminOperateLog adminOperateLog) {
        // 记录操作日志信息
        ManagementInformation before = this.baseMapper.selectById(managementInformation.getId());
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.MANAGEMENT_INFORMATION);
        adminOperateLog.setBelongId(managementInformation.getId());
        adminOperateLog.setContent(ChangeDetails.getByUpdate(before, managementInformation,
                ManagementInformationConstant.getFieldMap()));

        // 保存系统信息
        managementInformation.preUpdate();
        this.baseMapper.updateById(managementInformation);
    }

    /**
     * 根据主键查询管理端信息
     *
     * @param id 主键
     * @return 管理端信息
     */
    @Override
    public ManagementInformationVO getManagementInformationById(String id) {
        return this.baseMapper.getManagementInformationById(id);
    }

}
