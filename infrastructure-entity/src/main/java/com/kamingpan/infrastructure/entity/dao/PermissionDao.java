package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.entity.Permission;
import com.kamingpan.infrastructure.entity.model.vo.PermissionListVO;
import com.kamingpan.infrastructure.entity.model.vo.PermissionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 权限 Mapper 接口
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Repository
public interface PermissionDao extends BaseDao<Permission> {

    /**
     * 查询超级管理员权限字符串
     *
     * @param deleted 数据状态
     * @return 权限字符串列表
     */
    List<String> listAuthenticationBySuperAdmin(@Param("deleted") Integer deleted);

    /**
     * 根据管理员主键查询管理员权限
     *
     * @param adminId 管理员主键
     * @return 权限列表
     */
    List<Permission> listByAdminId(@Param("adminId") String adminId);

    /**
     * 根据管理员主键查询管理员权限字符串
     *
     * @param adminId 管理员主键
     * @param deleted 数据状态
     * @return 权限字符串列表
     */
    List<String> listAuthenticationByAdminId(@Param("adminId") String adminId, @Param("deleted") Integer deleted);

    /**
     * 查询权限信息
     *
     * @param deleted 数据状态
     * @return 权限vo列表
     */
    List<PermissionListVO> listPermission(@Param("deleted") Integer deleted);

    /**
     * 根据主键查询权限信息
     *
     * @param id      主键
     * @param deleted 数据状态
     * @return 权限vo
     */
    PermissionVO getPermissionById(@Param("id") String id, @Param("deleted") Integer deleted);

    /**
     * 根据所属模块查询所有上级权限（排除当前权限）
     *
     * @param moduleId 所属模块主键
     * @param id       当前权限主键
     * @return 权限vo
     */
    List<PermissionVO> listSuperiorByModuleId(@Param("moduleId") String moduleId, @Param("id") String id);

    /**
     * 查询可用权限
     *
     * @param status 状态
     * @return 权限列表
     */
    List<PermissionListVO> listMapByModuleStatus(@Param("status") Integer status);

    /**
     * 查询角色已关联的权限主键
     *
     * @param roleId 角色主键
     * @return 权限主键列表
     */
    List<String> listIdByRoleId(@Param("roleId") String roleId);

}
