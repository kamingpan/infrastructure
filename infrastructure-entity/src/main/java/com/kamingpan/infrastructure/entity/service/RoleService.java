package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.entity.model.dto.RoleDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.Role;
import com.kamingpan.infrastructure.entity.model.vo.RoleListVO;
import com.kamingpan.infrastructure.entity.model.vo.RoleVO;

import java.util.List;

/**
 * 角色 服务类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
public interface RoleService extends BaseService<Role> {

    /**
     * 新增角色
     *
     * @param role            角色
     * @param adminOperateLog 操作日志
     */
    void insert(Role role, AdminOperateLog adminOperateLog);

    /**
     * 角色修改
     *
     * @param role            角色
     * @param adminOperateLog 操作日志
     */
    void update(Role role, AdminOperateLog adminOperateLog);

    /**
     * 根据角色主键删除角色和管理员-角色关联
     *
     * @param id              角色主键
     * @param adminOperateLog 操作日志
     */
    void delete(String id, AdminOperateLog adminOperateLog);

    /**
     * 角色批量删除
     *
     * @param ids 角色主键列表
     */
    void deleteByIds(List<String> ids);

    /**
     * 修改角色-权限关联
     *
     * @param id              角色主键
     * @param permissionIds   权限主键集合
     * @param adminOperateLog 操作日志
     */
    void updateRolePermission(String id, List<String> permissionIds, AdminOperateLog adminOperateLog);

    /**
     * 启用角色
     *
     * @param id              主键
     * @param adminOperateLog 操作日志
     */
    void updateStatusToEnableById(String id, AdminOperateLog adminOperateLog);

    /**
     * 禁用角色
     *
     * @param id              主键
     * @param adminOperateLog 操作日志
     */
    void updateStatusToDisableById(String id, AdminOperateLog adminOperateLog);

    /**
     * 根据角色查询角色信息
     *
     * @param role  角色dto
     * @param pager 分页
     * @return 角色vo列表
     */
    List<RoleVO> listRole(RoleDTO role, Pager pager);

    /**
     * 根据角色主键查询角色信息
     *
     * @param id 角色主键
     * @return 角色vo
     */
    RoleVO getRoleById(String id);

    /**
     * 查询可用角色
     *
     * @return 角色列表
     */
    List<RoleListVO> listByEnable();

    /**
     * 查询管理员已关联的角色主键
     *
     * @param adminId 管理员主键
     * @return 角色主键列表
     */
    List<String> listIdByAdminId(String adminId);

    /**
     * 根据角色名称查询角色数量
     *
     * @param name 角色名称
     * @return 角色数量
     */
    int countByName(String name);

    /**
     * 查询所有上级角色（排除当前角色）
     *
     * @param id 当前角色主键
     * @return 角色vo列表
     */
    List<RoleVO> listSuperior(String id);

}
