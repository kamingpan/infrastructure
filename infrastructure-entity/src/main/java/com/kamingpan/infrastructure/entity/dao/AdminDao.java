package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.dto.AdminDTO;
import com.kamingpan.infrastructure.entity.model.entity.Admin;
import com.kamingpan.infrastructure.entity.model.vo.AdminVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 管理员 Mapper 接口
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Repository
public interface AdminDao extends BaseDao<Admin> {

    /**
     * 根据管理员查询管理员信息
     *
     * @param admin   管理员dto
     * @param deleted 数据状态
     * @return 管理员vo列表
     */
    List<AdminVO> listByAdmin(@Param("admin") AdminDTO admin, @Param("deleted") Integer deleted);

    /**
     * 根据管理员用户名查询管理员
     *
     * @param username 管理员用户名
     * @param deleted  数据状态
     * @return 管理员
     */
    Admin getByUsername(@Param("username") String username, @Param("deleted") Integer deleted);

    /**
     * 根据主键查询管理员详情
     *
     * @param id      主键
     * @param deleted 数据状态
     * @return 管理员vo
     */
    AdminVO getAdminById(@Param("id") String id, @Param("deleted") Integer deleted);

    /**
     * 根据管理员用户名查询管理员数量.
     *
     * @param username 管理员用户名
     * @return 管理员数量
     */
    int countByUsername(@Param("username") String username);

}
