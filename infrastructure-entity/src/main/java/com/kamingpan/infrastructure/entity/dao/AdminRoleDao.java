package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.entity.AdminRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 管理员-角色关联 Mapper 接口
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Repository
public interface AdminRoleDao extends BaseDao<AdminRole> {

    /**
     * 根据管理员主键删除管理员-角色关联
     *
     * @param adminId 管理员主键
     * @return 删除关联数量
     */
    int deleteByAdminId(@Param("adminId") String adminId);

    /**
     * 根据角色主键查询管理员-角色关联数量
     *
     * @param roleId 角色主键
     * @return 关联数量
     */
    int countByRoleId(@Param("roleId") String roleId);

}
