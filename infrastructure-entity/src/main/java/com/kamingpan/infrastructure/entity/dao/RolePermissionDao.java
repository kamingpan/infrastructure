package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.entity.RolePermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 角色-权限关联 Mapper 接口
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Repository
public interface RolePermissionDao extends BaseDao<RolePermission> {

    /**
     * 根据角色主键删除关联关系
     *
     * @param roleId 角色主键
     * @return 删除数量
     */
    int deleteByRoleId(@Param("roleId") String roleId);

    /**
     * 根据权限主键查询角色-权限关联数量
     *
     * @param permissionId 权限主键
     * @return 关联数量
     */
    int countByPermissionId(@Param("permissionId") String permissionId);

}
