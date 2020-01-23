package com.kamingpan.infrastructure.subscription.wechat.menu;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;

/**
 * 微信公众号菜单按钮抽象类
 *
 * @author kamingpan
 * @since 2018-06-20
 */
@Slf4j
public abstract class AbstractWeChatMenuButton {

    /**
     * 创建公众号菜单按钮
     *
     * @return 微信菜单 {@link WxMenu}
     */
    public abstract WxMenu createWxMenu();

    /**
     * 初始化公众号按钮菜单
     */
    public void initMenuButton(WxMpService wxMpService) {
        if (null == wxMpService) {
            return;
        }

        log.info("初始化公众号按钮菜单");
        try {
            wxMpService.getMenuService().menuCreate(this.createWxMenu());
            log.info("初始化公众号按钮菜单成功");
        } catch (WxErrorException exception) {
            log.warn("初始化公众号按钮菜单异常", exception);
        }
    }

}
