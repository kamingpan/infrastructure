package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.entity.constant.PermissionConstant;
import com.kamingpan.infrastructure.entity.group.PermissionGroup;
import com.kamingpan.infrastructure.entity.model.dto.PermissionDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.Permission;
import com.kamingpan.infrastructure.entity.model.vo.PermissionListVO;
import com.kamingpan.infrastructure.entity.model.vo.PermissionVO;
import com.kamingpan.infrastructure.entity.service.ModuleService;
import com.kamingpan.infrastructure.entity.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
 * 权限 controller
 *
 * @author kamingpan
 * @since 2017-04-01
 */
@Slf4j
@RestController
@RequestMapping("/permission")
public class PermissionController extends BaseController {

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private PermissionService permissionService;

    /**
     * 权限查询
     *
     * @return 响应数据
     */
    @GetMapping("")
    @PreAuthorize(PermissionConstant.Authentication.LIST)
    public ResponseData list() {
        log.debug("查询权限列表...");

        List<PermissionListVO> permissions = this.permissionService.listPermission();

        // 创建权限对象，用于处理权限层级关系
        PermissionListVO permissionTemp = new PermissionListVO();
        List<PermissionListVO> permissionList = new LinkedList<PermissionListVO>();
        permissionTemp.setId(PermissionConstant.Superior.HIGHEST);
        permissionTemp.setChildren(permissionList);
        this.handlePermissionTree(permissions, permissionTemp);

        return ResponseData.build(permissionList);
    }

    /**
     * 权限详情
     *
     * @param id 权限主键
     * @return 响应数据
     */
    @GetMapping("/{id}")
    @PreAuthorize(PermissionConstant.Authentication.INFO)
    public ResponseData info(@PathVariable("id") String id) {
        log.debug("查询权限“{}”详情...", id);
        PermissionVO permission = this.permissionService.getPermissionById(id);

        if (null == permission) {
            return ResponseData.build(ResponseStatus.DATA_IS_NOT_EXIST);
        }
        return ResponseData.build(permission);
    }

    /**
     * 权限新增
     *
     * @param permission 权限
     * @return 响应数据
     */
    @PostMapping("")
    @PreAuthorize(PermissionConstant.Authentication.INSERT)
    public ResponseData insert(@ModelAttribute @Validated(PermissionGroup.Insert.class) PermissionDTO permission) {
        log.debug("新增权限“{}”...", permission.getName());
        this.permissionService.insert(permission.toPermission(), new AdminOperateLog());

        return ResponseData.success();
    }

    /**
     * 权限修改
     *
     * @param id            权限主键
     * @param permissionDTO 权限
     * @return 响应数据
     */
    @PutMapping("/{id}")
    @PreAuthorize(PermissionConstant.Authentication.UPDATE)
    public ResponseData update(@PathVariable("id") String id,
                               @ModelAttribute @Validated(PermissionGroup.Update.class) PermissionDTO permissionDTO) {
        log.debug("更新权限“{}”...", permissionDTO.getName());

        Permission permission = permissionDTO.toPermission();
        permission.setId(id);
        permission.setName(null);
        this.permissionService.update(permission, new AdminOperateLog());

        return ResponseData.success();
    }

    /**
     * 权限删除
     *
     * @param id 权限主键
     * @return 响应数据
     */
    @DeleteMapping("/{id}")
    @PreAuthorize(PermissionConstant.Authentication.DELETE)
    public ResponseData delete(@PathVariable("id") String id) {
        log.debug("删除权限“{}”...", id);
        this.permissionService.delete(id, new AdminOperateLog());

        return ResponseData.success();
    }

    /**
     * 查询可用权限
     *
     * @param roleId 角色主键
     * @return 响应数据
     */
    @GetMapping("/list")
    public ResponseData list(@RequestParam("roleId") String roleId) {
        log.debug("查询可用权限");

        List<PermissionListVO> permissions = this.permissionService.listMapByModuleStatus();

        // 创建权限对象，用于处理权限层级关系
        PermissionListVO permissionTemp = new PermissionListVO();
        List<PermissionListVO> permissionList = new LinkedList<PermissionListVO>();
        permissionTemp.setId(PermissionConstant.Superior.HIGHEST);
        permissionTemp.setChildren(permissionList);
        this.handlePermissionTree(permissions, permissionTemp);

        Map<String, List> result = new HashMap<String, List>();
        result.put("permissions", permissionList);
        result.put("modules", this.moduleService.listMapByEnable());
        result.put("ids", this.permissionService.listIdsByRoleId(roleId));
        return ResponseData.build(result);
    }

    /**
     * 角色关联
     *
     * @param id    角色主键
     * @param roles 角色主键列表
     * @return 响应数据
     */
    @PostMapping("/{id}/role")
    @PreAuthorize(PermissionConstant.Authentication.ROLE)
    public ResponseData role(@PathVariable("id") String id, @RequestParam("roles") List<String> roles) {
        return ResponseData.success();
    }

    /**
     * 判断权限名称是否存在
     *
     * @param id   当前权限主键
     * @param name 权限名称
     * @return 响应数据
     */
    @GetMapping("/name-exist")
    public ResponseData nameExist(@RequestParam(value = "id", required = false) final String id,
                                  @RequestParam("name") final String name) {
        log.debug("查询权限名称“{}”是否存在...", name);

        long count = this.permissionService.countByName(id, name);
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        result.put("result", count > 0);
        return ResponseData.build(result);
    }

    /**
     * 判断权限字符串是否存在
     *
     * @param authentication 权限字符串
     * @return 响应数据
     */
    @GetMapping("/authentication-exist")
    public ResponseData authenticationExist(@RequestParam(value = "id", required = false) final String id,
                                            @RequestParam("authentication") final String authentication) {
        log.debug("查询权限字符串“{}”是否存在...", authentication);

        long count = this.permissionService.countByAuthentication(id, authentication);
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        result.put("result", count > 0);
        return ResponseData.build(result);
    }

    /**
     * 根据所属模块查询所有上级权限（排除当前权限）
     *
     * @param moduleId 所属模块主键
     * @param id       当前权限主键
     * @return 响应数据
     */
    @GetMapping("/superior")
    public ResponseData superior(@RequestParam(value = "moduleId", required = false) String moduleId,
                                 @RequestParam(value = "id", required = false) String id) {
        log.debug("查询所有上级权限");

        List<PermissionVO> permissions = this.permissionService.listSuperiorByModuleId(moduleId, id);

        // 增加最高级权限的上级
        PermissionVO highest = new PermissionVO();
        highest.setId(PermissionConstant.Superior.HIGHEST);
        highest.setName("无（即最高级)");
        permissions.add(0, highest);

        return ResponseData.build(permissions);
    }

    /**
     * 处理权限层级关系（递归）
     *
     * @param permissionListSource 权限源列表
     * @param permissionSource     权限源
     */
    private void handlePermissionTree(List<PermissionListVO> permissionListSource, PermissionListVO permissionSource) {
        // 如果源权限列表为空，即没有可在分供处理的权限了，则直接返回
        if (null == permissionListSource || permissionListSource.isEmpty()) {
            return;
        }

        // 获取子权限
        List<PermissionListVO> children = permissionSource.getChildren();

        // 浅拷贝创建一个缓存列表，用于遍历（因源列表需要移除，所以不能用于遍历）
        List<PermissionListVO> permissionListSourceTemp = new LinkedList<PermissionListVO>(permissionListSource);

        // 遍历源列表
        for (PermissionListVO permissionSourceTemp : permissionListSourceTemp) {
            // 如果结果列表的主键和结果源列表中的上级主键不一致，则开始下一轮循环
            if (!permissionSource.getId().equals(permissionSourceTemp.getSuperior())) {
                continue;
            }

            // 如果结果列表的主键和结果源列表中的上级主键一致，则将源列表中的数据挪到结果列表中
            if (null == children) {
                // 如果子权限为空，则新建对象存放
                children = new LinkedList<PermissionListVO>();
                permissionSource.setChildren(children);
            }

            // 如果结果列表的主键和结果源列表中的上级主键一致，则将源列表中的数据挪到结果列表中
            children.add(permissionSourceTemp);
            permissionListSource.remove(permissionSourceTemp);
        }

        // 如果子权限没有需要再处理的列表，则直接结束
        if (null == children || children.isEmpty()) {
            return;
        }

        // 通过递归，处理当前子权限的层级关系
        for (PermissionListVO permission : children) {
            this.handlePermissionTree(permissionListSource, permission);
        }
    }

}
