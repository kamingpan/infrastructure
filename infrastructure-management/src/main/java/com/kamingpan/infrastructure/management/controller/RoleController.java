package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.exception.ValidateException;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.entity.constant.RoleConstant;
import com.kamingpan.infrastructure.entity.group.RoleGroup;
import com.kamingpan.infrastructure.entity.model.dto.RoleDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.Role;
import com.kamingpan.infrastructure.entity.model.vo.RoleListVO;
import com.kamingpan.infrastructure.entity.model.vo.RoleVO;
import com.kamingpan.infrastructure.entity.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
 * 角色 controller
 *
 * @author kamingpan
 * @since 2017-04-01
 */
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    /**
     * 角色查询
     *
     * @param role  角色dto
     * @param pager 分页
     * @return 响应数据
     */
    @GetMapping("")
    @PreAuthorize(RoleConstant.Authentication.LIST)
    public ResponseData list(@ModelAttribute RoleDTO role, @ModelAttribute Pager pager) {
        log.debug("查询角色列表...");

        return ResponseData.buildPagination(this.roleService.listRole(role, pager), pager);
    }

    /**
     * 角色详情
     *
     * @param id 角色主键
     * @return 响应数据
     */
    @GetMapping("/{id}")
    @PreAuthorize(RoleConstant.Authentication.INFO)
    public ResponseData info(@PathVariable String id) {
        log.debug("查询角色“{}”详情...", id);

        RoleVO role = this.roleService.getRoleById(id);
        if (null == role) {
            return ResponseData.build(ResponseStatus.DATA_IS_NOT_EXIST);
        }
        return ResponseData.build(role);
    }

    /**
     * 角色新增
     *
     * @param role 角色
     * @return 响应数据
     */
    @PostMapping("")
    @PreAuthorize(RoleConstant.Authentication.INSERT)
    public ResponseData insert(@ModelAttribute @Validated(RoleGroup.Insert.class) RoleDTO role) {
        log.debug("新增角色“{}”...", role.getName());

        this.roleService.insert(role.toRole(), new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 角色修改
     *
     * @param id      角色主键
     * @param roleDTO 角色
     * @return 响应数据
     */
    @PutMapping("/{id}")
    @PreAuthorize(RoleConstant.Authentication.UPDATE)
    public ResponseData update(@PathVariable("id") String id,
                               @ModelAttribute @Validated(RoleGroup.Update.class) RoleDTO roleDTO) {
        log.debug("更新角色“{}”...", roleDTO.getName());

        Role role = roleDTO.toRole();
        role.setId(id);
        role.setName(null); // 角色名称不允许做修改
        this.roleService.update(role, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 角色删除
     *
     * @param id 角色主键
     * @return 响应数据
     */
    @DeleteMapping("/{id}")
    @PreAuthorize(RoleConstant.Authentication.DELETE)
    public ResponseData delete(@PathVariable("id") String id) {
        log.debug("删除角色“{}”...", id);

        this.roleService.delete(id, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 角色批量删除
     *
     * @param ids 角色主键列表
     * @return 响应数据
     */
    @DeleteMapping("")
    @PreAuthorize(RoleConstant.Authentication.DELETE)
    public ResponseData deleteByIds(@RequestParam("ids") List<String> ids) {
        log.debug("删除角色“{}”...", ids);
        if (null == ids || ids.isEmpty()) {
            throw new ValidateException();
        }

        this.roleService.deleteByIds(ids);

        return ResponseData.success();
    }

    /**
     * 查询可用角色
     *
     * @param adminId 管理员主键
     * @return 响应数据
     */
    @GetMapping("/list")
    public ResponseData list(@RequestParam("adminId") String adminId) {
        log.debug("查询可用角色");

        // 查询所有角色
        List<RoleListVO> roles = this.roleService.listByEnable();

        // 创建角色对象，用于处理角色层级关系
        RoleListVO roleTemp = new RoleListVO();
        List<RoleListVO> roleList = new LinkedList<RoleListVO>();
        roleTemp.setId(RoleConstant.Superior.HIGHEST);
        roleTemp.setChildren(roleList);
        this.handleRoleTree(roles, roleTemp);

        Map<String, List> result = new HashMap<String, List>();
        result.put("roles", roleList);
        result.put("ids", this.roleService.listIdByAdminId(adminId));
        return ResponseData.build(result);
    }

    /**
     * 权限授予
     *
     * @param id          角色主键
     * @param permissions 权限主键集合
     * @return 响应数据
     */
    @PostMapping("/{id}/permission")
    @PreAuthorize(RoleConstant.Authentication.PERMISSION)
    public ResponseData permission(@PathVariable("id") String id,
                                   @RequestParam("permissions") List<String> permissions) {
        log.debug("授予角色“{}”权限...", id);

        this.roleService.updateRolePermission(id, permissions, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 管理员关联
     *
     * @param id     角色主键
     * @param admins 管理员主键列表
     * @return 响应数据
     */
    @PostMapping("/{id}/admin")
    @PreAuthorize(RoleConstant.Authentication.ADMIN)
    public ResponseData admin(@PathVariable("id") String id, @RequestParam("admins") List<String> admins) {
        return ResponseData.success();
    }

    /**
     * 启用角色
     *
     * @param id 主键
     * @return 响应数据
     */
    @PatchMapping("/{id}/enable")
    @PreAuthorize(RoleConstant.Authentication.ENABLE)
    public ResponseData enable(@PathVariable("id") String id) {
        log.debug("启用角色“{}”...", id);

        this.roleService.updateStatusToEnableById(id, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 禁用角色
     *
     * @param id 主键
     * @return 响应数据
     */
    @PatchMapping("/{id}/disable")
    @PreAuthorize(RoleConstant.Authentication.DISABLE)
    public ResponseData disable(@PathVariable("id") String id) {
        log.debug("禁用角色“{}”...", id);

        this.roleService.updateStatusToDisableById(id, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 判断角色名称是否存在
     *
     * @param name 角色名称
     * @return 响应数据
     */
    @GetMapping("/name-exist")
    public ResponseData nameExist(@RequestParam("name") final String name) {
        log.debug("查询角色“{}”是否存在...", name);

        int count = this.roleService.countByName(name);
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        result.put("result", count > 0);
        return ResponseData.build(result);
    }

    /**
     * 查询所有上级角色（排除当前角色）
     *
     * @param id 当前角色主键
     * @return 响应数据
     */
    @GetMapping("/superior")
    public ResponseData superior(@RequestParam(value = "id", required = false) String id) {
        log.debug("查询所有上级角色");

        List<RoleVO> result = this.roleService.listSuperior(id);

        // 增加最高级角色的上级
        RoleVO highest = new RoleVO();
        highest.setId(RoleConstant.Superior.HIGHEST);
        highest.setName("无（即最高级)");
        result.add(0, highest);

        return ResponseData.build(result);
    }

    /**
     * 处理角色层级关系（递归）
     *
     * @param roleListSource 权限源列表
     * @param roleSource     权限源
     */
    private void handleRoleTree(List<RoleListVO> roleListSource, RoleListVO roleSource) {
        // 如果源权限列表为空，即没有可在分供处理的权限了，则直接返回
        if (null == roleListSource || roleListSource.isEmpty()) {
            return;
        }

        // 获取子权限
        List<RoleListVO> children = roleSource.getChildren();

        // 浅拷贝创建一个缓存列表，用于遍历（因源列表需要移除，所以不能用于遍历）
        List<RoleListVO> roleListSourceTemp = new LinkedList<RoleListVO>(roleListSource);

        // 遍历源列表
        for (RoleListVO roleSourceTemp : roleListSourceTemp) {
            // 如果结果列表的主键和结果源列表中的上级主键不一致，则开始下一轮循环
            if (!roleSource.getId().equals(roleSourceTemp.getSuperior())) {
                continue;
            }

            // 如果结果列表的主键和结果源列表中的上级主键一致，则将源列表中的数据挪到结果列表中
            if (null == children) {
                // 如果子权限为空，则新建对象存放
                children = new LinkedList<RoleListVO>();
                roleSource.setChildren(children);
            }

            // 如果结果列表的主键和结果源列表中的上级主键一致，则将源列表中的数据挪到结果列表中
            children.add(roleSourceTemp);
            roleListSource.remove(roleSourceTemp);
        }

        // 如果子权限没有需要再处理的列表，则直接结束
        if (null == children || children.isEmpty()) {
            return;
        }

        // 通过递归，处理当前子权限的层级关系
        for (RoleListVO role : children) {
            this.handleRoleTree(roleListSource, role);
        }
    }

}
