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
import com.kamingpan.infrastructure.entity.dao.ModuleDao;
import com.kamingpan.infrastructure.entity.dao.PermissionDao;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.Module;
import com.kamingpan.infrastructure.entity.model.entity.Permission;
import com.kamingpan.infrastructure.entity.model.vo.ModuleListVO;
import com.kamingpan.infrastructure.entity.model.vo.ModuleVO;
import com.kamingpan.infrastructure.entity.service.ModuleService;
import com.kamingpan.infrastructure.entity.util.DataDictionaryCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 模块 服务实现类
 *
 * @author kamingpan
 * @since 2017-02-27
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class ModuleServiceImpl extends BaseServiceImpl<Module, ModuleDao> implements ModuleService {

    @Autowired
    private PermissionDao permissionDao;

    @Autowired
    private DataDictionaryCache dataDictionaryCache;

    /**
     * 插入模块
     *
     * @param module          模块
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.INSERT)
    public void insert(Module module, AdminOperateLog adminOperateLog) {

        int count = this.countByName(null, module.getName());
        if (count > 0) {
            throw new ValidateException("该模块名称已存在");
        }

        if (null != module.getSuperior() && !module.getSuperior().isEmpty()
                && !ModuleConstant.Superior.HIGHEST.equals(module.getSuperior())) {
            Module superior = this.getById(module.getSuperior());
            if (null == superior) {
                throw new ValidateException("该上级模块不存在");
            }
        } else {
            module.setSuperior(ModuleConstant.Superior.HIGHEST);
        }

        // 设置默认图标
        if (null == module.getIcon()) {
            module.setIcon(ModuleConstant.Icon.DEFAULT);
        }

        // 赋值主键等信息，并插入数据库
        module.preInsert();
        this.baseMapper.insert(module);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.MODULE);
        adminOperateLog.setBelongId(module.getId());
        adminOperateLog.setContent(ChangeDetails.getByCreate(module, ModuleConstant.getFieldMap()));
    }

    /**
     * 更新模块
     *
     * @param module          模块
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.UPDATE)
    public void update(Module module, AdminOperateLog adminOperateLog) {
        Module before = this.baseMapper.selectById(module.getId());
        if (null == before) {
            throw new DataNotExistException();
        }

        if (null != module.getSuperior() && !module.getSuperior().isEmpty()
                && !ModuleConstant.Superior.HIGHEST.equals(module.getSuperior())) {
            if (module.getId().equals(module.getSuperior())) {
                throw new ValidateException("上级模块不能设置为当前模块");
            }
            Module superior = this.getById(module.getSuperior());
            if (null == superior) {
                throw new ValidateException("该上级模块不存在");
            }
        } else {
            module.setSuperior(ModuleConstant.Superior.HIGHEST);
        }

        // 设置默认图标
        if (null == module.getIcon()) {
            module.setIcon(ModuleConstant.Icon.DEFAULT);
        }

        module.preUpdate();
        this.baseMapper.updateById(module);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.MODULE);
        adminOperateLog.setBelongId(module.getId());
        adminOperateLog.setContent(ChangeDetails.getByUpdate(before, module, ModuleConstant.getFieldMap()));
    }

    /**
     * 启用模块
     *
     * @param id              模块主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.ENABLE)
    public void updateStatusEnableById(String id, AdminOperateLog adminOperateLog) {
        if (0 >= this.baseMapper.countById(id)) {
            throw new DataNotExistException();
        }

        Module module = new Module();
        module.setId(id);
        module.setStatus(ModuleConstant.Status.ENABLE);
        this.baseMapper.updateById(module);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.MODULE);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 禁用模块
     *
     * @param id              模块主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.DISABLE)
    public void updateStatusDisableById(String id, AdminOperateLog adminOperateLog) {
        if (0 >= this.baseMapper.countById(id)) {
            throw new DataNotExistException();
        }

        Module module = new Module();
        module.setId(id);
        module.setStatus(ModuleConstant.Status.DISABLE);
        this.baseMapper.updateById(module);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.MODULE);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 模块删除
     *
     * @param id              主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.DELETE)
    public void delete(String id, AdminOperateLog adminOperateLog) {
        // 判断模块是否存在
        if (0 >= this.baseMapper.countById(id)) {
            throw new DataNotExistException();
        }

        // 如果有下级模块，则不允许删除
        QueryWrapper<Module> moduleQueryWrapper = new QueryWrapper<Module>();
        moduleQueryWrapper.eq("superior", id);
        if (0 < this.baseMapper.selectCount(moduleQueryWrapper)) {
            throw new DataCanNotDeleteException("该模块含有下级模块，无法删除");
        }

        // 如果有关联的权限，则不允许删除
        QueryWrapper<Permission> permissionQueryWrapper = new QueryWrapper<Permission>();
        permissionQueryWrapper.eq("module_id", id);
        if (0 < this.permissionDao.selectCount(permissionQueryWrapper)) {
            throw new DataCanNotDeleteException("该模块含有关联的权限，无法删除");
        }

        this.baseMapper.deleteById(id);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.MODULE);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 查询超级管理员模块
     *
     * @return 模块列表
     */
    @Override
    public List<Module> listBySuperAdmin() {
        log.debug("查询超级管理员模块...");
        QueryWrapper<Module> wrapper = new QueryWrapper<Module>();
        wrapper.eq("status", ModuleConstant.Status.ENABLE);
        wrapper.orderByAsc("sort");
        return this.baseMapper.selectList(wrapper);
    }

    /**
     * 查询超级管理员模块
     *
     * @return 模块列表
     */
    @Override
    public List<ModuleListVO> listModuleVOBySuperAdmin() {
        log.debug("查询超级管理员模块...");
        return this.baseMapper.listSideBarModuleByIds(null,
                ModuleConstant.Status.ENABLE, DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 根据管理员主键查询管理员模块
     *
     * @param adminId 管理员主键
     * @return 模块列表
     */
    @Override
    public List<ModuleListVO> listByAdminId(String adminId) {
        log.debug("查询管理员{}模块...", adminId);
        List<String> ids = new ArrayList<String>();
        List<Module> modules = this.baseMapper.listByAdminId(adminId, ModuleConstant.Status.ENABLE);

        // 如果没有相关的模块，则直接返回结果，不需要后续遍历
        if (null == modules || modules.size() <= 0) {
            return new ArrayList<ModuleListVO>();
        }

        // 查询该管理员授予权限所属模块及其所有上级模块的主键集合
        for (Module module : modules) {
            ids.add(module.getId());
            ids.addAll(this.listAllSuperiorById(module.getSuperior()));
        }

        // 根据主键集合查询模块集合
        return this.baseMapper.listSideBarModuleByIds(ids, ModuleConstant.Status.ENABLE,
                DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 根据模块主键查询所有上级模块主键集合（递归）
     *
     * @param id 模块主键
     * @return 模块主键集合
     */
    private List<String> listAllSuperiorById(String id) {
        List<String> superiors = new ArrayList<String>();

        if (!ModuleConstant.Superior.HIGHEST.equals(id)) {
            Module superior = this.baseMapper.selectById(id);
            superiors.add(superior.getId());
            superiors.addAll(this.listAllSuperiorById(superior.getSuperior()));
        }

        return superiors;
    }

    /**
     * 查询模块信息
     *
     * @return 模块vo集合
     */
    @Override
    public List<ModuleListVO> listModule() {
        return this.baseMapper.listModule(DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 根据模块名称查询模块数量
     *
     * @param id   当前模块主键
     * @param name 模块名称
     * @return 模块数量
     */
    @Override
    public int countByName(String id, String name) {
        if (null == name || name.isEmpty()) {
            return 0;
        }

        QueryWrapper<Module> wrapper = new QueryWrapper<Module>();
        wrapper.eq("name", name);
        if (null != id && !id.isEmpty()) {
            wrapper.and(function -> function.ne("id", id));
        }

        return this.baseMapper.selectCount(wrapper);
    }

    /**
     * 查询所有上级模块（排除当前模块）
     *
     * @param id 当前模块主键
     * @return 上级模块vo列表
     */
    @Override
    public List<ModuleVO> listSuperior(String id) {
        return this.baseMapper.listSuperior(id);
    }

    /**
     * 根据主键查询模块详情
     *
     * @param id 主键
     * @return 模块vo
     */
    @Override
    public ModuleVO getModuleById(String id) {
        ModuleVO module = this.baseMapper.getModuleById(id, DataStatusEnum.NOT_DELETED.getValue());

        if (null != module) {
            // 处理路由类型标签
            module.setTypeLabel(dataDictionaryCache.getLabel(ModuleConstant.CLASS_STRING,
                    ModuleConstant.Variable.TYPE, module.getType()));

            // 处理状态标签
            module.setStatusLabel(dataDictionaryCache.getLabel(ModuleConstant.CLASS_STRING,
                    ModuleConstant.Variable.STATUS, String.valueOf(module.getStatus())));
        }

        return module;
    }

    /**
     * 查询状态正常的权限列表
     *
     * @return 权限列表
     */
    @Override
    public List<Map<String, Object>> listMapByEnable() {
        return this.baseMapper.listMapByStatus(ModuleConstant.Status.ENABLE);
    }

}
