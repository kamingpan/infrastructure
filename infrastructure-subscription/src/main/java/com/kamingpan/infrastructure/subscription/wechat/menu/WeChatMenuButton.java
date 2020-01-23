package com.kamingpan.infrastructure.subscription.wechat.menu;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;

import java.util.ArrayList;
import java.util.List;

/**
 * 微信公众号菜单按钮
 *
 * @author kamingpan
 * @since 2016-09-09
 */
@Slf4j
public class WeChatMenuButton extends AbstractWeChatMenuButton {

    public WeChatMenuButton() {
    }

    /**
     * 创建公众号菜单按钮
     *
     * @return 微信菜单 {@link WxMenu}
     */
    @Override
    public WxMenu createWxMenu() {
        WxMenuButton firstMenuButton = new WxMenuButton(); // 测试按钮
        firstMenuButton.setKey("首页");
        firstMenuButton.setName("首页");
        firstMenuButton.setType(WxConsts.MenuButtonType.VIEW);
        firstMenuButton.setUrl("http://www.kamingpan.com/subscription-html/#index");

        List<WxMenuButton> wxMenuButtons = new ArrayList<WxMenuButton>();
        wxMenuButtons.add(firstMenuButton);

        WxMenu wxMenu = new WxMenu();
        wxMenu.setButtons(wxMenuButtons);

        return wxMenu;
    }

}
