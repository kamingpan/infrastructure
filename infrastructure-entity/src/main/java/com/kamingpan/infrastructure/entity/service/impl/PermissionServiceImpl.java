package com.kamingpan.infrastructure.entity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.kamingpan.infrastructure.core.base.enumeration.DataStatusEnum;
import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.core.exception.DataCanNotDeleteException;
import com.kamingpan.infrastructure.core.exception.DataNotExistException;
import com.kamingpan.infrastructure.core.exception.ValidateException;
import com.kamingpan.infrastructure.core.log.OperateLog;
import com.kamingpan.infrastructure.core.util.ChangeDetails;
import com.kamingpan.infrastructure.entity.constant.AdminOperateLogConstant;
import com.kamingpan.infrastructure.entity.constant.ModuleConstant;
import com.kamingpan.infrastructure.entity.constant.PermissionConstant;
import com.kamingpan.infrastructure.entity.dao.PermissionDao;
import com.kamingpan.infrastructure.entity.dao.RolePermissionDao;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.Permission;
import com.kamingpan.infrastructure.entity.model.vo.PermissionListVO;
import com.kamingpan.infrastructure.entity.model.vo.PermissionVO;
import com.kamingpan.infrastructure.entity.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限 服务实现类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class PermissionServiceImpl extends BaseServiceImpl<Permission, PermissionDao> implements PermissionService {

    @Autowired
    private RolePermissionDao rolePermissionDao;

    /**
     * 权限新增
     *
     * @param permission      权限
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.INSERT)
    public void insert(Permission permission, AdminOperateLog adminOperateLog) {
        QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>();
        wrapper.eq("name", permission.getName());

        long count = super.baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new ValidateException("该权限名称已存在");
        }

        wrapper = new QueryWrapper<Permission>();
        wrapper.eq("authentication", permission.getAuthentication());
        count = super.baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new ValidateException("该权限字符串已存在");
        }

        if (null != permission.getSuperior() && !permission.getSuperior().isEmpty()
                && !PermissionConstant.Superior.HIGHEST.equals(permission.getSuperior())) {
            Permission superior = super.baseMapper.selectById(permission.getSuperior());
            if (null == superior) {
                throw new ValidateException("该上级权限不存在");
            }
        } else {
            permission.setSuperior(PermissionConstant.Superior.HIGHEST);
        }

        // 设置默认图标
        if (null == permission.getIcon() || permission.getIcon().isEmpty()) {
            permission.setIcon(PermissionConstant.Icon.DEFAULT);
        }

        permission.setStatus(PermissionConstant.Status.ENABLE);
        permission.preInsert();
        super.baseMapper.insert(permission);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.PERMISSION);
        adminOperateLog.setBelongId(permission.getId());
        adminOperateLog.setContent(ChangeDetails.getByCreate(permission, PermissionConstant.getFieldMap()));
    }

    /**
     * 权限修改
     *
     * @param permission      权限
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.UPDATE)
    public void update(Permission permission, AdminOperateLog adminOperateLog) {
        Permission before = super.baseMapper.selectById(permission.getId());
        if (null == before) {
            throw new DataNotExistException();
        }

        QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>();
        wrapper.eq("authentication", permission.getAuthentication());
        wrapper.ne("id", permission.getId());
        int count = super.baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new ValidateException("该权限字符串已存在");
        }

        if (null != permission.getSuperior() && !permission.getSuperior().isEmpty()
                && !PermissionConstant.Superior.HIGHEST.equals(permission.getSuperior())) {
            if (permission.getId().equals(permission.getSuperior())) {
                throw new ValidateException("上级权限不能设置为当前权限");
            }
            Permission superior = super.baseMapper.selectById(permission.getSuperior());
            if (null == superior) {
                throw new ValidateException("该上级权限不存在");
            }
        } else {
            permission.setSuperior(PermissionConstant.Superior.HIGHEST);
        }

        // 设置默认图标
        if (null == permission.getIcon()) {
            permission.setIcon(PermissionConstant.Icon.DEFAULT);
        }

        permission.preUpdate();
        super.baseMapper.updateById(permission);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.PERMISSION);
        adminOperateLog.setBelongId(permission.getId());
        adminOperateLog.setContent(ChangeDetails.getByUpdate(before, permission, PermissionConstant.getFieldMap()));
    }

    /**
     * 权限删除
     *
     * @param id              权限主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.DELETE)
    public void delete(String id, AdminOperateLog adminOperateLog) {
        if (0 >= super.baseMapper.countById(id)) {
            throw new DataNotExistException();
        }

        // 判断是否有下级权限
        QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>();
        wrapper.eq("superior", id);
        if (0 < super.baseMapper.selectCount(wrapper)) {
            throw new DataCanNotDeleteException("该权限含有下级权限，无法删除");
        }

        // 判断该权限是否被角色关联
        if (0 < this.rolePermissionDao.countByPermissionId(id)) {
            throw new DataCanNotDeleteException("该权限已被角色关联，无法删除");
        }

        super.baseMapper.deleteById(id);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.PERMISSION);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 查询超级管理员权限
     *
     * @return 权限列表
     */
    @Override
    public List<Permission> listBySuperAdmin() {
        log.debug("查询超级管理员权限...");
        QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>();
        wrapper.eq("status", PermissionConstant.Status.ENABLE);
        return super.baseMapper.selectList(wrapper);
    }

    /**
     * 查询超级管理员权限字符串
     *
     * @return 权限字符串列表
     */
    @Override
    public List<String> listAuthenticationBySuperAdmin() {
        log.debug("查询超级管理员权限字符串...");
        return super.baseMapper.listAuthenticationBySuperAdmin(DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 根据管理员主键查询管理员权限
     *
     * @param adminId 管理员主键
     * @return 权限列表
     */
    @Override
    public List<Permission> listByAdminId(String adminId) {
        log.debug("查询管理员{}权限...", adminId);
        return super.baseMapper.listByAdminId(adminId);
    }

    /**
     * 根据管理员主键查询管理员权限字符串
     *
     * @param adminId 管理员主键
     * @return 权限字符串列表
     */
    @Override
    public List<String> listAuthenticationByAdminId(String adminId) {
        log.debug("查询管理员{}权限字符串...", adminId);
        return super.baseMapper.listAuthenticationByAdminId(adminId, DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 查询权限信息
     *
     * @return 权限vo列表
     */
    @Override
    public List<PermissionListVO> listPermission() {
        return super.baseMapper.listPermission(DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 根据主键查询权限详情
     *
     * @param id 主键
     * @return 权限vo
     */
    @Override
    public PermissionVO getPermissionById(String id) {
        return super.baseMapper.getPermissionById(id, DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 根据所属查询所有上级权限（排除当前权限）
     *
     * @param moduleId 所属模块主键
     * @param id       当前权限主键
     * @return 权限vo
     */
    @Override
    public List<PermissionVO> listSuperiorByModuleId(String moduleId, String id) {
        return super.baseMapper.listSuperiorByModuleId(moduleId, id);
    }

    /**
     * 查询可用权限
     *
     * @return 权限列表
     */
    @Override
    public List<PermissionListVO> listMapByModuleStatus() {
        return super.baseMapper.listMapByModuleStatus(ModuleConstant.Status.ENABLE);
    }

    /**
     * 查询角色已关联的权限主键
     *
     * @param roleId 角色主键
     * @return 权限主键列表
     */
    @Override
    public List<String> listIdsByRoleId(String roleId) {
        return super.baseMapper.listIdByRoleId(roleId);
    }

    /**
     * 根据权限名称查询权限数量（排除当前权限）
     *
     * @param id   当前权限主键
     * @param name 权限名称
     * @return 权限数量
     */
    @Override
    public int countByName(String id, String name) {
        if (null == name || name.isEmpty()) {
            return 0;
        }

        QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>();
        wrapper.eq("name", name);
        if (null != id && !id.isEmpty()) {
            wrapper.and(function -> function.ne("id", id));
        }

        return super.baseMapper.selectCount(wrapper);
    }

    /**
     * 根据权限字符串查询权限数量（排除当前权限）
     *
     * @param id             当前权限主键
     * @param authentication 权限字符串
     * @return 权限数量
     */
    @Override
    public int countByAuthentication(String id, String authentication) {
        if (null == authentication || authentication.isEmpty()) {
            return 0;
        }

        QueryWrapper<Permission> wrapper = new QueryWrapper<Permission>();
        wrapper.eq("authentication", authentication);
        if (null != id && !id.isEmpty()) {
            wrapper.and(function -> function.ne("id", id));
        }
        return super.baseMapper.selectCount(wrapper);
    }

}
