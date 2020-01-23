package com.kamingpan.infrastructure.subscription.wechat.service;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;

/**
 * 继承WxMpService，方便获取相关成员变量
 *
 * @author kamingpan
 * @since 2016-09-12
 */
@Slf4j
public class WeChatMpService extends WxMpServiceImpl {

    public WeChatMpService() {
        super();
    }

    public WeChatMpService(WxMpConfigStorage wxMpConfigStorage) {
        super();
        super.setWxMpConfigStorage(wxMpConfigStorage);
    }

}
