package com.kamingpan.infrastructure.mini.program.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import com.kamingpan.infrastructure.core.base.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * 小程序基础controller
 *
 * @author kamingpan
 * @since 2019-04-18
 */
@Slf4j
public class MiniProgramBaseController extends BaseController {

    @Autowired
    protected WxMaService wxMaService;


    /**
     * 获取小程序的access_token
     *
     * @return 小程序access_token
     */
    public String getMiniProgramAccessToken() {
        try {
            return this.wxMaService.getAccessToken();
        } catch (WxErrorException exception) {
            throw new AuthenticationServiceException("获取小程序的access_token异常", exception);
        }
    }

}
