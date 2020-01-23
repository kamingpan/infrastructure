package com.kamingpan.infrastructure.mini.program.controller;

import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.entity.model.entity.MiniProgramUser;
import com.kamingpan.infrastructure.entity.service.MiniProgramUserService;
import com.kamingpan.infrastructure.mini.program.util.UserUtil;
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
 * @since 2019-04-18
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends MiniProgramBaseController {

    @Autowired
    private MiniProgramUserService miniProgramUserService;

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
        MiniProgramUser miniProgramUser = this.miniProgramUserService.getByUserId(UserUtil.getUserId());
        return ResponseData.build(miniProgramUser);
    }

}
