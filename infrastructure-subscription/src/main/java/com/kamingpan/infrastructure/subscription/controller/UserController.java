package com.kamingpan.infrastructure.subscription.controller;

import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.entity.model.entity.SubscriptionUser;
import com.kamingpan.infrastructure.entity.service.SubscriptionUserService;
import com.kamingpan.infrastructure.subscription.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户 controller
 *
 * @author kamingpan
 * @since 2018-02-25
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends SubscriptionBaseController {

    @Autowired
    private SubscriptionUserService subscriptionUserService;

    /**
     * 获取用户openid
     *
     * @param request 请求
     * @return 响应数据
     */
    @GetMapping("/openid")
    public ResponseData openid(HttpServletRequest request) {
        String openid = request.getUserPrincipal().getName();
        return ResponseData.build(openid);
    }

    /**
     * 获取用户信息
     *
     * @return 响应数据
     */
    @GetMapping("/message")
    public ResponseData message() {
        SubscriptionUser subscriptionUser = this.subscriptionUserService.getByUserId(UserUtil.getUserId());
        return ResponseData.build(subscriptionUser);
    }

}
