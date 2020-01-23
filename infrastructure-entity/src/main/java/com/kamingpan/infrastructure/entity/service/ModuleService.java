package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.Module;
import com.kamingpan.infrastructure.entity.model.vo.ModuleListVO;
import com.kamingpan.infrastructure.entity.model.vo.ModuleVO;

import java.util.List;
import java.util.Map;

/**
 * 模块 服务类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
public interface ModuleService extends BaseService<Module> {

    /**
     * 插入模块
     *
     * @param module          模块
     * @param adminOperateLog 操作日志
     */
    void insert(Module module, AdminOperateLog adminOperateLog);

    /**
     * 更新模块
     *
     * @param module          模块
     * @param adminOperateLog 操作日志
     */
    void update(Module module, AdminOperateLog adminOperateLog);

    /**
     * 启用模块
     *
     * @param id              模块主键
     * @param adminOperateLog 操作日志
     */
    void updateStatusEnableById(String id, AdminOperateLog adminOperateLog);

    /**
     * 禁用模块
     *
     * @param id              模块主键
     * @param adminOperateLog 操作日志
     */
    void updateStatusDisableById(String id, AdminOperateLog adminOperateLog);

    /**
     * 模块删除
     *
     * @param id              主键
     * @param adminOperateLog 操作日志
     */
    void delete(String id, AdminOperateLog adminOperateLog);

    /**
     * 查询超级管理员模块
     *
     * @return 模块列表
     */
    List<Module> listBySuperAdmin();

    /**
     * 查询超级管理员模块
     *
     * @return 模块列表
     */
    List<ModuleListVO> listModuleVOBySuperAdmin();

    /**
     * 根据管理员主键查询管理员模块
     *
     * @param adminId 管理员主键
     * @return 模块列表
     */
    List<ModuleListVO> listByAdminId(String adminId);

    /**
     * 查询模块信息
     *
     * @return 模块vo集合
     */
    List<ModuleListVO> listModule();

    /**
     * 根据模块名称查询模块数量
     *
     * @param id   当前模块主键
     * @param name 模块名称
     * @return 模块数量
     */
    int countByName(String id, String name);

    /**
     * 查询所有上级模块（排除当前模块）
     *
     * @param id 当前模块主键
     * @return 上级模块vo列表
     */
    List<ModuleVO> listSuperior(String id);

    /**
     * 根据主键查询模块详情
     *
     * @param id 主键
     * @return 模块vo
     */
    ModuleVO getModuleById(String id);

    /**
     * 查询状态正常的权限列表
     *
     * @return 权限列表
     */
    List<Map<String, Object>> listMapByEnable();

}
