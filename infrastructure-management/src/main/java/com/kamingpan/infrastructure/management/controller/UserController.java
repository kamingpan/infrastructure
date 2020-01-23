package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.entity.constant.UserConstant;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户 controller
 *
 * @author kamingpan
 * @since 2018-07-20
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    /**
     * 启用注册用户
     *
     * @param id 主键
     * @return 响应数据
     */
    @PatchMapping("/{id}/enable")
    @PreAuthorize(UserConstant.Authentication.ENABLE)
    public ResponseData enable(@PathVariable("id") String id) {
        log.debug("启用注册用户“{}”...", id);

        this.userService.updateStatusToEnableById(id, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 禁用注册用户
     *
     * @param id 主键
     * @return 响应数据
     */
    @PatchMapping("/{id}/disable")
    @PreAuthorize(UserConstant.Authentication.DISABLE)
    public ResponseData disable(@PathVariable("id") String id) {
        log.debug("禁用注册用户“{}”...", id);

        this.userService.updateStatusToDisableById(id, new AdminOperateLog());
        return ResponseData.success();
    }

}
