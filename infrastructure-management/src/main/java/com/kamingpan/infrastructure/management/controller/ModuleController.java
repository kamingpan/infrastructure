package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.entity.constant.ModuleConstant;
import com.kamingpan.infrastructure.entity.model.dto.ModuleDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.vo.ModuleListVO;
import com.kamingpan.infrastructure.entity.model.vo.ModuleVO;
import com.kamingpan.infrastructure.entity.service.ModuleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * 模块 controller
 *
 * @author kamingpan
 * @since 2017-04-11
 */
@Slf4j
@RestController
@RequestMapping("/module")
public class ModuleController extends BaseController {

    @Autowired
    private ModuleService moduleService;

    /**
     * 模块查询
     *
     * @return 响应数据
     */
    @GetMapping("")
    @PreAuthorize(ModuleConstant.Authentication.LIST)
    public ResponseData list() {
        log.debug("查询模块列表...");

        List<ModuleListVO> modules = this.moduleService.listModule();

        // 创建权限对象，用于处理权限层级关系
        ModuleListVO moduleTemp = new ModuleListVO();
        List<ModuleListVO> moduleList = new LinkedList<ModuleListVO>();
        moduleTemp.setId(ModuleConstant.Superior.HIGHEST);
        moduleTemp.setChildren(moduleList);
        this.handleModuleTree(modules, moduleTemp);

        return ResponseData.build(moduleList);
    }

    /**
     * 模块详情
     *
     * @param id 模块主键
     * @return 响应数据
     */
    @GetMapping("/{id}")
    @PreAuthorize(ModuleConstant.Authentication.INFO)
    public ResponseData info(@PathVariable("id") String id) {
        log.debug("查询模块“{}”详情...", id);
        ModuleVO module = this.moduleService.getModuleById(id);
        if (null == module) {
            return ResponseData.build(ResponseStatus.DATA_IS_NOT_EXIST);
        }

        return ResponseData.build(module);
    }

    /**
     * 模块新增
     *
     * @param module 模块
     * @return 响应数据
     */
    @PostMapping("")
    @PreAuthorize(ModuleConstant.Authentication.INSERT)
    public ResponseData insert(@ModelAttribute ModuleDTO module) {
        log.debug("新增模块“{}”...", module.getName());

        this.moduleService.insert(module.toModule(), new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 模块修改
     *
     * @param id     模块主键
     * @param module 模块
     * @return 响应数据
     */
    @PutMapping("/{id}")
    @PreAuthorize(ModuleConstant.Authentication.UPDATE)
    public ResponseData update(@PathVariable("id") String id, @ModelAttribute ModuleDTO module) {
        log.debug("更新模块“{}”...", module.getName());

        module.setId(id);
        this.moduleService.update(module.toModule(), new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 模块删除
     *
     * @param id 模块主键
     * @return 响应数据
     */
    @DeleteMapping("/{id}")
    @PreAuthorize(ModuleConstant.Authentication.DELETE)
    public ResponseData delete(@PathVariable("id") String id) {
        log.debug("删除模块“{}”...", id);

        this.moduleService.delete(id, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 启用模块
     *
     * @param id 主键
     * @return 响应数据
     */
    @PatchMapping("/{id}/enable")
    @PreAuthorize(ModuleConstant.Authentication.ENABLE)
    public ResponseData enable(@PathVariable("id") String id) {
        log.debug("启用模块“{}”...", id);

        this.moduleService.updateStatusEnableById(id, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 禁用模块
     *
     * @param id 主键
     * @return 响应数据
     */
    @PatchMapping("/{id}/disable")
    @PreAuthorize(ModuleConstant.Authentication.DISABLE)
    public ResponseData disable(@PathVariable("id") String id) {
        log.debug("禁用模块“{}”...", id);

        this.moduleService.updateStatusDisableById(id, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 权限关联
     *
     * @param id          模块主键
     * @param permissions 权限主键列表
     * @return 响应数据
     */
    @PostMapping("/{id}/permission")
    @PreAuthorize(ModuleConstant.Authentication.PERMISSION)
    public ResponseData permission(@PathVariable("id") String id, @RequestParam("permissions") List<String> permissions) {
        return ResponseData.success();
    }

    /**
     * 判断模块名称是否存在
     *
     * @param id   模块主键
     * @param name 模块名称
     * @return 响应数据
     */
    @GetMapping("/name-exist")
    public ResponseData nameExist(@RequestParam(value = "id", required = false) final String id,
                                  @RequestParam("name") final String name) {
        log.debug("查询模块名称“{}”是否存在...", name);

        int count = this.moduleService.countByName(id, name);
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        result.put("result", count > 0);
        return ResponseData.build(result);
    }

    /**
     * 查询所有上级模块（排除当前模块）
     *
     * @param id 当前模块主键
     * @return 响应数据
     */
    @GetMapping("/superior")
    public ResponseData superior(@RequestParam(value = "id", required = false) String id,
                                 @RequestParam(value = "hasHighest", required = false, defaultValue = "false") boolean hasHighest) {
        log.debug("查询所有上级模块");

        // 查询列表
        List<ModuleVO> modules = this.moduleService.listSuperior(id);

        // 增加最高级模块的上级
        if (hasHighest) {
            ModuleVO highest = new ModuleVO();
            highest.setId(ModuleConstant.Superior.HIGHEST);
            highest.setName("无（即最高级)");
            modules.add(0, highest);
        }

        return ResponseData.build(modules);
    }

    /**
     * 处理模块层级关系（递归）
     *
     * @param moduleListSource 模块源列表
     * @param moduleSource     模块源
     */
    private void handleModuleTree(List<ModuleListVO> moduleListSource, ModuleListVO moduleSource) {
        // 如果源模块列表为空，即没有可在分供处理的模块了，则直接返回
        if (null == moduleListSource || moduleListSource.isEmpty()) {
            return;
        }

        // 获取子模块
        List<ModuleListVO> children = moduleSource.getChildren();

        // 浅拷贝创建一个缓存列表，用于遍历（因源列表需要移除，所以不能用于遍历）
        List<ModuleListVO> moduleListSourceTemp = new LinkedList<ModuleListVO>(moduleListSource);

        // 遍历源列表
        for (ModuleListVO moduleSourceTemp : moduleListSourceTemp) {
            // 如果结果列表的主键和结果源列表中的上级主键不一致，则开始下一轮循环
            if (!moduleSource.getId().equals(moduleSourceTemp.getSuperior())) {
                continue;
            }

            // 如果结果列表的主键和结果源列表中的上级主键一致，则将源列表中的数据挪到结果列表中
            if (null == children) {
                // 如果子权限为空，则新建对象存放
                children = new LinkedList<ModuleListVO>();
                moduleSource.setChildren(children);
            }

            // 如果结果列表的主键和结果源列表中的上级主键一致，则将源列表中的数据挪到结果列表中
            children.add(moduleSourceTemp);
            moduleListSource.remove(moduleSourceTemp);
        }

        // 如果子模块没有需要再处理的列表，则直接结束
        if (null == children || children.isEmpty()) {
            return;
        }

        // 通过递归，处理当前子权限的层级关系
        for (ModuleListVO module : children) {
            this.handleModuleTree(moduleListSource, module);
        }
    }

}
