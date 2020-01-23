package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.entity.constant.MiniProgramUserConstant;
import com.kamingpan.infrastructure.entity.model.dto.MiniProgramUserDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.vo.MiniProgramUserVO;
import com.kamingpan.infrastructure.entity.service.MiniProgramUserService;
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
 * 小程序用户 前端控制器
 *
 * @author kamingpan
 * @since 2019-04-18
 */
@Slf4j
@RestController
@RequestMapping("/mini-program-user")
public class MiniProgramUserController extends BaseController {

    @Autowired
    private MiniProgramUserService miniProgramUserService;

    /**
     * 小程序用户查询
     *
     * @param miniProgramUser 小程序用户dto
     * @param pager           分页
     * @return 响应数据
     */
    @GetMapping("")
    @PreAuthorize(MiniProgramUserConstant.Authentication.LIST)
    public ResponseData list(@ModelAttribute MiniProgramUserDTO miniProgramUser, @ModelAttribute Pager pager) {
        log.debug("查询小程序用户列表...");

        return ResponseData.buildPagination(this.miniProgramUserService.listByMiniProgramUser(miniProgramUser, pager), pager);
    }

    /**
     * 小程序用户详情
     *
     * @param id 小程序用户主键
     * @return 响应数据
     */
    @GetMapping("/{id}")
    @PreAuthorize(MiniProgramUserConstant.Authentication.INFO)
    public ResponseData info(@PathVariable("id") final String id) {
        log.debug("查询小程序用户“{}”详情...", id);

        MiniProgramUserVO miniProgramUser = this.miniProgramUserService.getMiniProgramUserById(id);
        if (null == miniProgramUser) {
            return ResponseData.build(ResponseStatus.DATA_IS_NOT_EXIST);
        }
        return ResponseData.build(miniProgramUser);
    }

    /**
     * 小程序用户删除（只对小程序用户做标记为已删除，而不实际执行物理删除）
     *
     * @param id 小程序用户主键
     * @return 响应数据
     */
    @DeleteMapping("/{id}")
    @PreAuthorize(MiniProgramUserConstant.Authentication.DELETE)
    public ResponseData delete(@PathVariable("id") String id) {
        log.debug("删除小程序用户“{}”...", id);

        this.miniProgramUserService.delete(id, new AdminOperateLog());
        return ResponseData.success();
    }

}

