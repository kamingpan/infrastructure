package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.vo.AdminOperateLogVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 管理端操作日志 Mapper 接口
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Repository
public interface AdminOperateLogDao extends BaseDao<AdminOperateLog> {

    /**
     * 根据所属对象和所属主键查询操作日志
     *
     * @param belong    所属对象
     * @param belongId  所属主键
     * @param orderType 排序类型
     * @return 操作日志列表
     */
    List<AdminOperateLogVO> listAdminOperateLogByBelong(@Param("belong") String belong,
                                                        @Param("belongId") String belongId,
                                                        @Param("orderType") String orderType);

}
