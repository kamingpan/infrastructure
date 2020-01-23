package com.kamingpan.infrastructure.entity.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.entity.constant.AdminLoginLogConstant;
import com.kamingpan.infrastructure.entity.dao.AdminLoginLogDao;
import com.kamingpan.infrastructure.entity.model.dto.AdminLoginLogDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminLoginLog;
import com.kamingpan.infrastructure.entity.model.vo.AdminLoginLogVO;
import com.kamingpan.infrastructure.entity.service.AdminLoginLogService;
import com.kamingpan.infrastructure.entity.util.DataDictionaryCache;
import com.kamingpan.infrastructure.util.sql.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 管理员登录日志 服务实现类
 *
 * @author kamingpan
 * @since 2017-02-13
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class AdminLoginLogServiceImpl extends BaseServiceImpl<AdminLoginLog, AdminLoginLogDao> implements AdminLoginLogService {

    @Autowired
    private DataDictionaryCache dataDictionaryCache;

    /**
     * 根据条件查询管理员登录日志信息.
     *
     * @param adminLoginLog 管理员登录日志dto
     * @param pager         分页
     * @return 管理员登录日志信息列表 {@link List}
     */
    @Override
    public List<AdminLoginLogVO> listByAdminLoginLog(AdminLoginLogDTO adminLoginLog, Pager pager) {
        adminLoginLog.setUsername(SqlUtil.like(adminLoginLog.getUsername()));
        adminLoginLog.setFullName(SqlUtil.like(adminLoginLog.getFullName()));
        adminLoginLog.setBeginTime(SqlUtil.getBeginTimeOfDay(adminLoginLog.getBeginTime()));
        adminLoginLog.setEndTime(SqlUtil.getEndTimeOfDay(adminLoginLog.getEndTime()));

        // 如果不需要分页
        if (null == pager) {
            List<AdminLoginLogVO> adminLoginLogs = this.baseMapper.listByAdminLoginLog(adminLoginLog);

            // 遍历结果
            for (AdminLoginLogVO adminLoginLogVO : adminLoginLogs) {
                // 处理状态标签
                adminLoginLogVO.setStatusLabel(dataDictionaryCache.getLabel(AdminLoginLogConstant.CLASS_STRING,
                        AdminLoginLogConstant.Variable.STATUS, String.valueOf(adminLoginLogVO.getStatus())));
            }

            return adminLoginLogs;
        }

        // 处理分页逻辑
        Page page = PageHelper.startPage(pager.getPageNum(), pager.getPageSize());
        if (null != pager.getOrderBy() && !pager.getOrderBy().isEmpty()) {
            page.setOrderBy(pager.getOrderBy());
        }

        // 查询数据，并设置数据总数
        List<AdminLoginLogVO> adminLoginLogs = this.baseMapper.listByAdminLoginLog(adminLoginLog);
        pager.setTotal(page.getTotal());

        // 遍历结果
        for (AdminLoginLogVO adminLoginLogVO : adminLoginLogs) {
            // 处理状态标签
            adminLoginLogVO.setStatusLabel(dataDictionaryCache.getLabel(AdminLoginLogConstant.CLASS_STRING,
                    AdminLoginLogConstant.Variable.STATUS, String.valueOf(adminLoginLogVO.getStatus())));
        }

        return adminLoginLogs;
    }

    /**
     * 根据主键查询管理员登录日志信息.
     *
     * @param id 主键
     * @return 管理员登录日志信息 {@link AdminLoginLogVO}
     */
    @Override
    public AdminLoginLogVO getAdminLoginLogById(String id) {
        AdminLoginLogVO adminLoginLog = this.baseMapper.getAdminLoginLogById(id);

        if (null != adminLoginLog && null != adminLoginLog.getStatus()) {
            // 处理状态标签
            adminLoginLog.setStatusLabel(dataDictionaryCache.getLabel(AdminLoginLogConstant.CLASS_STRING,
                    AdminLoginLogConstant.Variable.STATUS, String.valueOf(adminLoginLog.getStatus())));
        }
        return adminLoginLog;
    }

    /**
     * 查询最后一个月登录成功的时间.
     *
     * @param lastMonth 上一个月
     * @param lastDay   上一天
     * @return 登录成功的时间集合 {@link List}
     */
    @Override
    public List<Date> listLastMonth(Date lastMonth, Date lastDay) {
        return this.baseMapper.listLastMonth(SqlUtil.getBeginTimeOfDay(lastMonth), SqlUtil.getEndTimeOfDay(lastDay),
                AdminLoginLogConstant.Status.SUCCESS);
    }

}
