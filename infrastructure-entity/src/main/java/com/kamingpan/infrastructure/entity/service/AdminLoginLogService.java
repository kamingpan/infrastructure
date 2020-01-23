package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.entity.model.dto.AdminLoginLogDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminLoginLog;
import com.kamingpan.infrastructure.entity.model.vo.AdminLoginLogVO;

import java.util.Date;
import java.util.List;

/**
 * 管理员登录日志 服务类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
public interface AdminLoginLogService extends BaseService<AdminLoginLog> {

    /**
     * 根据条件查询管理员登录日志信息.
     *
     * @param adminLoginLog 管理员登录日志dto
     * @param pager         分页
     * @return 管理员登录日志信息列表 {@link List}
     */
    List<AdminLoginLogVO> listByAdminLoginLog(AdminLoginLogDTO adminLoginLog, Pager pager);

    /**
     * 根据主键查询管理员登录日志信息.
     *
     * @param id 主键
     * @return 管理员登录日志信息 {@link AdminLoginLogVO}
     */
    AdminLoginLogVO getAdminLoginLogById(String id);

    /**
     * 查询最后一个月登录成功的时间.
     *
     * @param lastMonth 上一个月
     * @param lastDay   上一天
     * @return 登录成功的时间集合 {@link List}
     */
    List<Date> listLastMonth(Date lastMonth, Date lastDay);

}
