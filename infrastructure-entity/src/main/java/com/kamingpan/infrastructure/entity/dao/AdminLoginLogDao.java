package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.dto.AdminLoginLogDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminLoginLog;
import com.kamingpan.infrastructure.entity.model.vo.AdminLoginLogVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 管理员登录日志 Mapper 接口
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Repository
public interface AdminLoginLogDao extends BaseDao<AdminLoginLog> {

    /**
     * 根据条件查询管理员登录日志信息.
     *
     * @param adminLoginLog 管理员登录日志dto
     * @return 管理员登录日志信息列表 {@link List}
     */
    List<AdminLoginLogVO> listByAdminLoginLog(@Param("adminLoginLog") AdminLoginLogDTO adminLoginLog);

    /**
     * 根据主键查询管理员登录日志信息.
     *
     * @param id 主键
     * @return 管理员登录日志信息 {@link AdminLoginLogVO}
     */
    AdminLoginLogVO getAdminLoginLogById(@Param("id") String id);

    /**
     * 查询最后一个月登录成功的时间.
     *
     * @param lastMonth 上个月零时
     * @param lastDay   上一天末时
     * @param status    登录成功状态码
     * @return 登录时间集合 {@link List}
     */
    List<Date> listLastMonth(@Param("lastMonth") Date lastMonth, @Param("lastDay") Date lastDay,
                             @Param("status") Integer status);

}
