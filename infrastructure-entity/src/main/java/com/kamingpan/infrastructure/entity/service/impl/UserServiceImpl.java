package com.kamingpan.infrastructure.entity.service.impl;

import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.core.exception.DataNotExistException;
import com.kamingpan.infrastructure.core.log.OperateLog;
import com.kamingpan.infrastructure.entity.constant.AdminOperateLogConstant;
import com.kamingpan.infrastructure.entity.constant.UserConstant;
import com.kamingpan.infrastructure.entity.dao.UserDao;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.User;
import com.kamingpan.infrastructure.entity.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户 服务实现类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class UserServiceImpl extends BaseServiceImpl<User, UserDao> implements UserService {

    /**
     * 启用用户
     *
     * @param id              主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.ENABLE)
    public void updateStatusToEnableById(String id, AdminOperateLog adminOperateLog) {
        if (0 >= this.baseMapper.countById(id)) {
            throw new DataNotExistException();
        }

        User admin = new User();
        admin.setId(id);
        admin.setStatus(UserConstant.Status.ENABLE);
        admin.preUpdate();
        this.baseMapper.updateById(admin);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.USER);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 禁用用户
     *
     * @param id              主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.DISABLE)
    public void updateStatusToDisableById(String id, AdminOperateLog adminOperateLog) {
        if (0 >= this.baseMapper.countById(id)) {
            throw new DataNotExistException();
        }

        User admin = new User();
        admin.setId(id);
        admin.setStatus(UserConstant.Status.DISABLE);
        admin.preUpdate();
        this.baseMapper.updateById(admin);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.USER);
        adminOperateLog.setBelongId(id);
    }

}
