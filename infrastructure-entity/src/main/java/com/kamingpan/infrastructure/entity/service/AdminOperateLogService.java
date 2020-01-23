package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.vo.AdminOperateLogVO;

import java.util.List;

/**
 * 管理端操作日志 服务类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
public interface AdminOperateLogService extends BaseService<AdminOperateLog> {

    /**
     * 根据所属对象和所属主键查询操作日志
     *
     * @param belong    所属对象
     * @param belongId  所属主键
     * @param orderType 排序类型
     * @return 操作日志列表
     */
    List<AdminOperateLogVO> listAdminOperateLogByBelong(String belong, String belongId, String orderType);

}
