package com.kamingpan.infrastructure.terminal.controller;

import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.terminal.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户 controller
 *
 * @author kamingpan
 * @since 2017-01-12
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    @GetMapping("/id")
    public ResponseData info() {
        return ResponseData.build(UserUtil.getUserId());
    }

}
