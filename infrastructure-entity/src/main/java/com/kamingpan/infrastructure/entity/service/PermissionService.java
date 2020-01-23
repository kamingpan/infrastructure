package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.Permission;
import com.kamingpan.infrastructure.entity.model.vo.PermissionListVO;
import com.kamingpan.infrastructure.entity.model.vo.PermissionVO;

import java.util.List;

/**
 * 权限 服务类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
public interface PermissionService extends BaseService<Permission> {

    /**
     * 权限新增
     *
     * @param permission      权限
     * @param adminOperateLog 操作日志
     */
    void insert(Permission permission, AdminOperateLog adminOperateLog);

    /**
     * 权限修改
     *
     * @param permission      权限
     * @param adminOperateLog 操作日志
     */
    void update(Permission permission, AdminOperateLog adminOperateLog);

    /**
     * 权限删除
     *
     * @param id              权限主键
     * @param adminOperateLog 操作日志
     */
    void delete(String id, AdminOperateLog adminOperateLog);

    /**
     * 查询超级管理员权限
     *
     * @return 权限列表
     */
    List<Permission> listBySuperAdmin();

    /**
     * 查询超级管理员权限字符串
     *
     * @return 权限字符串列表
     */
    List<String> listAuthenticationBySuperAdmin();

    /**
     * 根据管理员主键查询管理员权限
     *
     * @param admin 管理员主键
     * @return 权限列表
     */
    List<Permission> listByAdminId(String admin);

    /**
     * 根据管理员主键查询管理员权限字符串
     *
     * @param adminId 管理员主键
     * @return 权限字符串列表
     */
    List<String> listAuthenticationByAdminId(String adminId);

    /**
     * 查询权限信息
     *
     * @return 权限vo列表
     */
    List<PermissionListVO> listPermission();

    /**
     * 根据主键查询权限详情
     *
     * @param id 主键
     * @return 权限vo
     */
    PermissionVO getPermissionById(String id);

    /**
     * 根据所属查询所有上级权限（排除当前权限）
     *
     * @param moduleId 所属模块主键
     * @param id       当前权限主键
     * @return 权限vo
     */
    List<PermissionVO> listSuperiorByModuleId(String moduleId, String id);

    /**
     * 查询可用权限
     *
     * @return 权限列表
     */
    List<PermissionListVO> listMapByModuleStatus();

    /**
     * 查询角色已关联的权限主键
     *
     * @param roleId 角色主键
     * @return 权限主键列表
     */
    List<String> listIdsByRoleId(String roleId);

    /**
     * 根据权限名称查询权限数量（排除当前权限）
     *
     * @param id   当前权限主键
     * @param name 权限名称
     * @return 权限数量
     */
    int countByName(String id, String name);

    /**
     * 根据权限字符串查询权限数量（排除当前权限）
     *
     * @param id             当前权限主键
     * @param authentication 权限字符串
     * @return 权限数量
     */
    int countByAuthentication(String id, String authentication);

}
