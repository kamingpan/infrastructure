package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.entity.constant.RegisteredUserConstant;
import com.kamingpan.infrastructure.entity.model.dto.RegisteredUserDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.vo.RegisteredUserVO;
import com.kamingpan.infrastructure.entity.service.RegisteredUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注册用户 controller
 *
 * @author kamingpan
 * @since 2018-07-20
 */
@Slf4j
@RestController
@RequestMapping("/registered-user")
public class RegisteredUserController extends BaseController {

    @Autowired
    private RegisteredUserService registeredUserService;

    /**
     * 注册用户查询
     *
     * @param registeredUser 注册用户dto
     * @param pager          分页
     * @return 响应数据
     */
    @GetMapping("")
    @PreAuthorize(RegisteredUserConstant.Authentication.LIST)
    public ResponseData list(@ModelAttribute RegisteredUserDTO registeredUser, @ModelAttribute Pager pager) {
        log.debug("查询注册用户列表...");

        return ResponseData.buildPagination(this.registeredUserService.listByRegisteredUser(registeredUser, pager), pager);
    }

    /**
     * 注册用户详情
     *
     * @param id 注册用户主键
     * @return 响应数据
     */
    @GetMapping("/{id}")
    @PreAuthorize(RegisteredUserConstant.Authentication.INFO)
    public ResponseData info(@PathVariable("id") final String id) {
        log.debug("查询注册用户“{}”详情...", id);

        RegisteredUserVO registeredUser = this.registeredUserService.getRegisteredUserById(id);
        if (null == registeredUser) {
            return ResponseData.build(ResponseStatus.DATA_IS_NOT_EXIST);
        }
        return ResponseData.build(registeredUser);
    }

    /**
     * 注册用户删除（只对注册用户做标记为已删除，而不实际执行物理删除）
     *
     * @param id 注册用户主键
     * @return 响应数据
     */
    @DeleteMapping("/{id}")
    @PreAuthorize(RegisteredUserConstant.Authentication.DELETE)
    public ResponseData delete(@PathVariable("id") String id) {
        log.debug("删除注册用户“{}”...", id);

        this.registeredUserService.delete(id, new AdminOperateLog());
        return ResponseData.success();
    }

}
