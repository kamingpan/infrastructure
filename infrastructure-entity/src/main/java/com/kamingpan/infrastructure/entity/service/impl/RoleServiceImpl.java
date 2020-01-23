package com.kamingpan.infrastructure.entity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kamingpan.infrastructure.core.base.enumeration.DataStatusEnum;
import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.core.exception.DataCanNotDeleteException;
import com.kamingpan.infrastructure.core.exception.DataNotExistException;
import com.kamingpan.infrastructure.core.exception.ValidateException;
import com.kamingpan.infrastructure.core.log.OperateLog;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.core.util.ChangeDetails;
import com.kamingpan.infrastructure.entity.constant.AdminOperateLogConstant;
import com.kamingpan.infrastructure.entity.constant.RoleConstant;
import com.kamingpan.infrastructure.entity.dao.AdminRoleDao;
import com.kamingpan.infrastructure.entity.dao.PermissionDao;
import com.kamingpan.infrastructure.entity.dao.RoleDao;
import com.kamingpan.infrastructure.entity.dao.RolePermissionDao;
import com.kamingpan.infrastructure.entity.model.dto.RoleDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.Permission;
import com.kamingpan.infrastructure.entity.model.entity.Role;
import com.kamingpan.infrastructure.entity.model.entity.RolePermission;
import com.kamingpan.infrastructure.entity.model.vo.RoleListVO;
import com.kamingpan.infrastructure.entity.model.vo.RoleVO;
import com.kamingpan.infrastructure.entity.service.RoleService;
import com.kamingpan.infrastructure.entity.util.DataDictionaryCache;
import com.kamingpan.infrastructure.util.sql.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色 服务实现类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class RoleServiceImpl extends BaseServiceImpl<Role, RoleDao> implements RoleService {

    @Autowired
    private AdminRoleDao adminRoleDao;

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private DataDictionaryCache dataDictionaryCache;

    /**
     * 新增角色
     *
     * @param role            角色
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.INSERT)
    public void insert(Role role, AdminOperateLog adminOperateLog) {
        int count = this.countByName(role.getName());
        if (count > 0) {
            throw new ValidateException("该角色名称已存在");
        }

        if (null != role.getSuperior() && !role.getSuperior().isEmpty()
                && !RoleConstant.Superior.HIGHEST.equals(role.getSuperior())) {
            // 判断上级是否存在
            Role superior = this.baseMapper.selectById(role.getSuperior());
            if (null == superior) {
                throw new ValidateException("该上级角色不存在");
            }

            // 设置级别
            role.setLevel(null == superior.getLevel() ? RoleConstant.Level.HIGHEST : superior.getLevel() + 1);
        } else {
            role.setSuperior(RoleConstant.Superior.HIGHEST);
            role.setLevel(RoleConstant.Level.HIGHEST);
        }

        role.preInsert();
        this.baseMapper.insert(role);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.ROLE);
        adminOperateLog.setBelongId(role.getId());
        adminOperateLog.setContent(ChangeDetails.getByCreate(role, RoleConstant.getFieldMap()));
    }

    /**
     * 角色修改
     *
     * @param role            角色
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.UPDATE)
    public void update(Role role, AdminOperateLog adminOperateLog) {
        Role before = this.baseMapper.selectById(role.getId());
        if (null == before) {
            throw new DataNotExistException();
        }

        if (null != role.getSuperior() && !role.getSuperior().isEmpty()
                && !RoleConstant.Superior.HIGHEST.equals(role.getSuperior())) {
            if (role.getId().equals(role.getSuperior())) {
                throw new ValidateException("上级角色不能设置为当前角色");
            }

            // 判断上级是否存在
            Role superior = this.baseMapper.selectById(role.getSuperior());
            if (null == superior) {
                throw new ValidateException("该上级角色不存在");
            }

            // 设置级别
            role.setLevel(null == superior.getLevel() ? RoleConstant.Level.HIGHEST : superior.getLevel() + 1);
        } else {
            role.setSuperior(RoleConstant.Superior.HIGHEST);
            role.setLevel(RoleConstant.Level.HIGHEST);
        }

        role.preUpdate();
        this.baseMapper.updateById(role);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.ROLE);
        adminOperateLog.setBelongId(role.getId());
        adminOperateLog.setContent(ChangeDetails.getByUpdate(before, role, RoleConstant.getFieldMap()));
    }

    /**
     * 根据角色主键删除角色和管理员-角色关联
     *
     * @param id              角色主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.DELETE)
    public void delete(String id, AdminOperateLog adminOperateLog) {
        // 判断角色是否存在
        if (0 >= this.baseMapper.countById(id)) {
            throw new DataNotExistException();
        }

        // 判断角色是否被分配给管理员
        if (0 < this.adminRoleDao.countByRoleId(id)) {
            throw new DataCanNotDeleteException("该角色已被分配，无法删除");
        }

        // 判断角色是否拥有下级角色
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
        queryWrapper.eq("superior", id);
        if (0 < this.baseMapper.selectCount(queryWrapper)) {
            throw new DataCanNotDeleteException("该角色拥有下级角色，无法删除");
        }

        // 删除所有权限关联和该角色
        this.rolePermissionDao.deleteByRoleId(id);
        this.baseMapper.deleteById(id);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.ROLE);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 角色批量删除
     *
     * @param ids 角色主键列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> ids) {
        for (String id : ids) {
            if (null != id && !id.isEmpty()) {
                this.delete(id, new AdminOperateLog());
            }
        }
    }

    /**
     * 修改角色-权限关联
     *
     * @param id              角色主键
     * @param permissionIds   权限主键集合
     * @param adminOperateLog 操作日志
     */
    @Override
    @OperateLog(type = "授予权限")
    @Transactional(rollbackFor = Exception.class)
    public void updateRolePermission(String id, List<String> permissionIds, AdminOperateLog adminOperateLog) {
        if (0 >= this.baseMapper.countById(id)) {
            throw new DataNotExistException();
        }

        StringBuilder content = new StringBuilder("移除了所有权限");

        // 删除旧的关联关系
        this.rolePermissionDao.deleteByRoleId(id);

        if (null != permissionIds && permissionIds.size() > 0) {
            content = new StringBuilder("权限为：");

            // 查询符合id的角色信息
            QueryWrapper<Permission> permissionWrapper = new QueryWrapper<Permission>();
            permissionWrapper.in("id", permissionIds);
            List<Permission> permissions = this.permissionDao.selectList(permissionWrapper);

            for (Permission permission : permissions) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRoleId(id);
                rolePermission.setPermissionId(permission.getId());
                rolePermission.preInsert();
                this.rolePermissionDao.insert(rolePermission);

                content.append(permission.getName()).append("，");
            }
            content.deleteCharAt(content.length() - 1);
        }

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.ROLE);
        adminOperateLog.setBelongId(id);
        adminOperateLog.setContent(content.toString());
    }

    /**
     * 启用角色
     *
     * @param id              主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.ENABLE)
    public void updateStatusToEnableById(String id, AdminOperateLog adminOperateLog) {
        if (0 >= this.baseMapper.countById(id)) {
            throw new DataNotExistException();
        }

        Role role = new Role();
        role.setId(id);
        role.setStatus(RoleConstant.Status.ENABLE);
        role.preUpdate();
        this.baseMapper.updateById(role);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.ROLE);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 禁用角色
     *
     * @param id              主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.DISABLE)
    public void updateStatusToDisableById(String id, AdminOperateLog adminOperateLog) {
        if (0 >= this.baseMapper.countById(id)) {
            throw new DataNotExistException();
        }

        // 判断角色是否被分配给管理员
        if (0 < this.adminRoleDao.countByRoleId(id)) {
            throw new DataCanNotDeleteException("该角色已被分配，无法禁用");
        }

        // 判断角色是否拥有下级角色
        QueryWrapper<Role> queryWrapper = new QueryWrapper<Role>();
        queryWrapper.eq("superior", id).eq("status", RoleConstant.Status.ENABLE);
        if (0 < this.baseMapper.selectCount(queryWrapper)) {
            throw new DataCanNotDeleteException("该角色拥有下级角色，无法禁用");
        }

        Role role = new Role();
        role.setId(id);
        role.setStatus(RoleConstant.Status.DISABLE);
        role.preUpdate();
        this.baseMapper.updateById(role);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.ROLE);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 根据角色查询角色信息
     *
     * @param role  角色dto
     * @param pager 分页
     * @return 角色vo列表
     */
    @Override
    public List<RoleVO> listRole(RoleDTO role, Pager pager) {
        role.setName(SqlUtil.like(role.getName()));

        // 如果不需要分页
        if (null == pager) {
            List<RoleVO> roles = this.baseMapper.listByRole(role, DataStatusEnum.NOT_DELETED.getValue());

            // 遍历结果
            for (RoleVO roleVO : roles) {
                // 处理状态标签
                roleVO.setStatusLabel(dataDictionaryCache.getLabel(RoleConstant.CLASS_STRING,
                        RoleConstant.Variable.STATUS, String.valueOf(roleVO.getStatus())));
            }

            return roles;
        }

        // 处理分页逻辑
        Page page = PageHelper.startPage(pager.getPageNum(), pager.getPageSize());
        if (null != pager.getOrderBy() && !pager.getOrderBy().isEmpty()) {
            page.setOrderBy(pager.getOrderBy());
        }

        // 查询数据，并设置数据总数
        List<RoleVO> roles = this.baseMapper.listByRole(role, DataStatusEnum.NOT_DELETED.getValue());
        pager.setTotal(page.getTotal());

        // 遍历结果
        for (RoleVO roleVO : roles) {
            // 处理状态标签
            roleVO.setStatusLabel(dataDictionaryCache.getLabel(RoleConstant.CLASS_STRING,
                    RoleConstant.Variable.STATUS, String.valueOf(roleVO.getStatus())));
        }

        return roles;
    }

    /**
     * 根据角色主键查询角色信息
     *
     * @param id 角色主键
     * @return 角色vo
     */
    @Override
    public RoleVO getRoleById(String id) {
        RoleVO role = this.baseMapper.getRoleById(id, DataStatusEnum.NOT_DELETED.getValue());

        if (null != role) {
            // 处理状态标签
            role.setStatusLabel(dataDictionaryCache.getLabel(RoleConstant.CLASS_STRING,
                    RoleConstant.Variable.STATUS, String.valueOf(role.getStatus())));
        }

        return role;
    }

    /**
     * 查询可用角色
     *
     * @return 角色列表
     */
    @Override
    public List<RoleListVO> listByEnable() {
        return this.baseMapper.listByStatus(RoleConstant.Status.ENABLE, DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 查询管理员已关联的角色主键
     *
     * @param adminId 管理员主键
     * @return 角色主键列表
     */
    @Override
    public List<String> listIdByAdminId(String adminId) {
        return this.baseMapper.listIdByAdminId(adminId);
    }

    /**
     * 根据角色名称查询角色数量
     *
     * @param name 角色名称
     * @return 角色数量
     */
    @Override
    public int countByName(String name) {
        if (null == name || "".equals(name)) {
            return 0;
        }

        QueryWrapper<Role> wrapper = new QueryWrapper<Role>();
        wrapper.eq("name", name);

        return this.baseMapper.selectCount(wrapper);
    }

    /**
     * 查询所有上级角色（排除当前角色）
     *
     * @param id 当前角色主键
     * @return 角色vo列表
     */
    @Override
    public List<RoleVO> listSuperior(String id) {
        return this.baseMapper.listSuperior(id);
    }

}
