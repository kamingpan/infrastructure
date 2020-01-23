package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.exception.ValidateException;
import com.kamingpan.infrastructure.core.properties.SystemProperties;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.entity.constant.AdminConstant;
import com.kamingpan.infrastructure.entity.group.AdminGroup;
import com.kamingpan.infrastructure.entity.model.dto.AdminDTO;
import com.kamingpan.infrastructure.entity.model.entity.Admin;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.vo.AdminVO;
import com.kamingpan.infrastructure.entity.service.AdminService;
import com.kamingpan.infrastructure.management.util.LoginSessionUtil;
import com.kamingpan.infrastructure.util.md5.MD5;
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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员 controller
 *
 * @author kamingpan
 * @since 2017-04-01
 */
@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController extends BaseController {

    @Autowired
    private SystemProperties systemProperties;

    @Autowired
    private LoginSessionUtil loginSessionUtil;

    @Autowired
    private AdminService adminService;

    /**
     * 管理员查询
     *
     * @param admin 管理员dto
     * @param pager 分页
     * @return 响应数据
     */
    @GetMapping("")
    @PreAuthorize(AdminConstant.Authentication.LIST)
    public ResponseData list(@ModelAttribute AdminDTO admin, @ModelAttribute Pager pager) {
        log.debug("查询管理员列表...");

        List<AdminVO> admins = this.adminService.listByAdmin(admin, pager);
        return ResponseData.buildPagination(admins, pager);
    }

    /**
     * 管理员详情
     *
     * @param id 管理员主键
     * @return 响应数据
     */
    @GetMapping("/{id}")
    @PreAuthorize(AdminConstant.Authentication.INFO)
    public ResponseData info(@PathVariable("id") final String id) {
        log.debug("查询管理员“{}”详情...", id);

        AdminVO admin = this.adminService.getAdminById(id);
        if (null == admin) {
            return ResponseData.build(ResponseStatus.DATA_IS_NOT_EXIST);
        }
        return ResponseData.build(admin);
    }

    /**
     * 管理员新增
     *
     * @param admin 管理员
     * @return 响应数据
     */
    @PostMapping("")
    @PreAuthorize(AdminConstant.Authentication.INSERT)
    public ResponseData insert(@ModelAttribute @Validated(AdminGroup.Insert.class) AdminDTO admin) {
        log.debug("新增管理员“{}”...", admin.getUsername());

        // 将密码首次md加密后拼合用户名及左右中括号再md加密
        try {
            String adminInitPassword = MD5.encryption(this.systemProperties.getAdminInitPassword());
            String md5Password = adminInitPassword + AdminConstant.Password.LEFT_BRACKET
                    + admin.getUsername() + AdminConstant.Password.RIGHT_BRACKET;
            admin.setPassword(MD5.encryption(md5Password));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException exception) {
            log.warn("初始密码MD5加密异常", exception);
        }

        this.adminService.insert(admin.toAdmin(), new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 管理员修改
     *
     * @param id       管理员主键
     * @param adminDTO 管理员
     * @return 响应数据
     */
    @PutMapping("/{id}")
    @PreAuthorize(AdminConstant.Authentication.UPDATE)
    public ResponseData update(@PathVariable("id") String id,
                               @ModelAttribute @Validated(AdminGroup.Update.class) AdminDTO adminDTO) {
        log.debug("更新管理员“{}”...", adminDTO.getUsername());

        Admin admin = adminDTO.toAdmin();
        admin.setId(id);
        admin.setUsername(null); // 用户名不允许做修改
        this.adminService.update(admin, new AdminOperateLog());

        // 如果该管理员被禁用了，则需要移除对应的登录缓存
        if (AdminConstant.Status.DISABLE.equals(adminDTO.getStatus())) {
            this.loginSessionUtil.removeLoginSessionByAdminId(id);
        }

        return ResponseData.success();
    }

    /**
     * 管理员删除（只对管理员做标记为已删除，而不实际执行物理删除）
     *
     * @param id 管理员主键
     * @return 响应数据
     */
    @DeleteMapping("/{id}")
    @PreAuthorize(AdminConstant.Authentication.DELETE)
    public ResponseData delete(@PathVariable("id") String id) {
        log.debug("删除管理员“{}”...", id);

        this.adminService.delete(id, new AdminOperateLog());

        // 删除管理员后移除对应管理员的登录缓存
        this.loginSessionUtil.removeLoginSessionByAdminId(id);

        return ResponseData.success();
    }

    /**
     * 管理员批量删除
     *
     * @param ids 管理员主键列表
     * @return 响应数据
     */
    @DeleteMapping("")
    @PreAuthorize(AdminConstant.Authentication.DELETE)
    public ResponseData deleteByIds(@RequestParam("ids") List<String> ids) {
        log.debug("删除管理员“{}”...", ids);
        if (null == ids || ids.isEmpty()) {
            throw new ValidateException();
        }

        this.adminService.deleteByIds(ids);

        // 删除管理员后移除对应管理员的登录缓存
        for (String id : ids) {
            this.loginSessionUtil.removeLoginSessionByAdminId(id);
        }

        return ResponseData.success();
    }

    /**
     * 启用管理员
     *
     * @param id 主键
     * @return 响应数据
     */
    @PatchMapping("/{id}/enable")
    @PreAuthorize(AdminConstant.Authentication.ENABLE)
    public ResponseData enable(@PathVariable("id") String id) {
        log.debug("启用管理员“{}”...", id);

        this.adminService.updateStatusToEnableById(id, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 禁用管理员
     *
     * @param id 主键
     * @return 响应数据
     */
    @PatchMapping("/{id}/disable")
    @PreAuthorize(AdminConstant.Authentication.DISABLE)
    public ResponseData disable(@PathVariable("id") String id) {
        log.debug("禁用管理员“{}”...", id);

        this.adminService.updateStatusToDisableById(id, new AdminOperateLog());

        // 禁用管理员后移除对应管理员的登录缓存
        this.loginSessionUtil.removeLoginSessionByAdminId(id);

        String result = "禁用管理员成功，并且已移除了该管理员的登录。";
        return ResponseData.build(result);
    }

    /**
     * 重置密码
     *
     * @param id 主键
     * @return 响应数据
     */
    @PatchMapping("/{id}/reset-password")
    @PreAuthorize(AdminConstant.Authentication.RESET_PASSWORD)
    public ResponseData resetPassword(@PathVariable("id") String id) {
        Admin temp = this.adminService.getById(id);
        if (null == temp) {
            return ResponseData.build(ResponseStatus.DATA_IS_NOT_EXIST);
        }

        log.debug("重置管理员“{}”密码...", temp.getUsername());
        Admin admin = new Admin();

        // 将密码首次md加密后拼合用户名及左右中括号再md加密
        try {
            // 验证MD5加密密码
            String md5Password = MD5.encryption(this.systemProperties.getAdminInitPassword())
                    + AdminConstant.Password.LEFT_BRACKET + temp.getUsername()
                    + AdminConstant.Password.RIGHT_BRACKET;
            admin.setPassword(MD5.encryption(md5Password));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException exception) {
            throw new ValidateException("重置密码MD5加密异常：" + exception.getMessage());
        }

        admin.setId(id);
        this.adminService.updateResetPassword(admin, new AdminOperateLog());

        // 重置密码后移除对应管理员的登录缓存
        this.loginSessionUtil.removeLoginSessionByAdminId(id);

        String result = "重置密码成功，重置后的密码为“" + this.systemProperties.getAdminInitPassword()
                + "”，该管理员需要重新登录。";
        return ResponseData.build(result);
    }

    /**
     * 管理员-角色分配
     *
     * @param id    管理员主键
     * @param roles 角色主键列表
     * @return 响应数据
     */
    @PostMapping("/{id}/role")
    @PreAuthorize(AdminConstant.Authentication.ROLE)
    public ResponseData role(@PathVariable("id") String id, @RequestParam("roles") List<String> roles) {
        log.debug("分配管理员“{}”角色...", id);

        this.adminService.updateAdminRole(id, roles, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 判断用户名是否存在
     *
     * @param username 用户名
     * @return 响应数据
     */
    @GetMapping("/username-exist")
    public ResponseData usernameExist(@RequestParam("username") final String username) {
        log.debug("查询管理员“{}”是否存在...", username);

        int count = this.adminService.countByUsername(username);
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        result.put("result", count > 0);
        return ResponseData.build(result);
    }

}
