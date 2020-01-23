package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.entity.constant.SubscriptionUserConstant;
import com.kamingpan.infrastructure.entity.model.dto.SubscriptionUserDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.vo.SubscriptionUserVO;
import com.kamingpan.infrastructure.entity.service.SubscriptionUserService;
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
 * 公众号用户 controller
 *
 * @author kamingpan
 * @since 2018-07-20
 */
@Slf4j
@RestController
@RequestMapping("/subscription-user")
public class SubscriptionUserController extends BaseController {

    @Autowired
    private SubscriptionUserService subscriptionUserService;

    /**
     * 公众号用户查询
     *
     * @param subscriptionUser 公众号用户dto
     * @param pager            分页
     * @return 响应数据
     */
    @GetMapping("")
    @PreAuthorize(SubscriptionUserConstant.Authentication.LIST)
    public ResponseData list(@ModelAttribute SubscriptionUserDTO subscriptionUser, @ModelAttribute Pager pager) {
        log.debug("查询公众号用户列表...");

        return ResponseData.buildPagination(this.subscriptionUserService.listBySubscriptionUser(subscriptionUser, pager), pager);
    }

    /**
     * 公众号用户详情
     *
     * @param id 公众号用户主键
     * @return 响应数据
     */
    @GetMapping("/{id}")
    @PreAuthorize(SubscriptionUserConstant.Authentication.INFO)
    public ResponseData info(@PathVariable("id") final String id) {
        log.debug("查询公众号用户“{}”详情...", id);

        SubscriptionUserVO subscriptionUser = this.subscriptionUserService.getSubscriptionUserById(id);
        if (null == subscriptionUser) {
            return ResponseData.build(ResponseStatus.DATA_IS_NOT_EXIST);
        }
        return ResponseData.build(subscriptionUser);
    }

    /**
     * 公众号用户删除（只对公众号用户做标记为已删除，而不实际执行物理删除）
     *
     * @param id 公众号用户主键
     * @return 响应数据
     */
    @DeleteMapping("/{id}")
    @PreAuthorize(SubscriptionUserConstant.Authentication.DELETE)
    public ResponseData delete(@PathVariable("id") String id) {
        log.debug("删除公众号用户“{}”...", id);

        this.subscriptionUserService.delete(id, new AdminOperateLog());
        return ResponseData.success();
    }

}
