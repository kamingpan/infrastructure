package com.kamingpan.infrastructure.subscription.runner;

import com.kamingpan.infrastructure.subscription.wechat.menu.AbstractWeChatMenuButton;
import com.kamingpan.infrastructure.subscription.wechat.properties.SubscriptionProperties;
import com.kamingpan.infrastructure.util.instantiate.InstantiateClass;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * InitMenuRunner 按钮菜单初始化
 *
 * @author kamingpan
 * @since 2018-07-19
 */
@Slf4j
@Component
public class InitMenuRunner implements CommandLineRunner {

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private SubscriptionProperties subscriptionProperties;

    @Override
    public void run(String... args) throws Exception {
        this.initMenuButton();
    }

    private void initMenuButton() {
        if (!this.subscriptionProperties.isInitMenu()) {
            return;
        }

        String menuButtonClass = this.subscriptionProperties.getMenuButtonClass();
        // 实例化抽象实现类
        AbstractWeChatMenuButton menuButton = (AbstractWeChatMenuButton) InstantiateClass.stringToClass(menuButtonClass);
        if (null != menuButton && null != this.wxMpService) {
            // 初始化公众号按钮菜单
            menuButton.initMenuButton(this.wxMpService);
        }
    }
}
