package com.kamingpan.infrastructure.subscription.wechat.handler.impl;

import com.kamingpan.infrastructure.entity.constant.SubscriptionUserConstant;
import com.kamingpan.infrastructure.entity.service.SubscriptionUserService;
import com.kamingpan.infrastructure.subscription.wechat.handler.UnSubscribeHandler;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 取消关注事件接口实现类
 *
 * @author kamingpan
 * @since 2018-07-19
 */
@Slf4j
@Component
@ConditionalOnMissingBean(name = {"unSubscribeHandler"})
public class UnSubscribeHandlerImpl implements UnSubscribeHandler {

    @Autowired
    private SubscriptionUserService subscriptionUserService;

    /**
     * 取消关注事件逻辑处理
     *
     * @param wxMessage      微信推送消息
     * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
     * @param wxMpService    微信公众号 基础服务
     * @param sessionManager 微信session管理
     * @return 响应推送信息
     * @throws WxErrorException 微信异常
     */
    @Override
    public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
                                    WxSessionManager sessionManager) throws WxErrorException {
        log.debug("微信服务器推送了取消关注事件：{}", wxMessage.toString());
        log.info("openid为“{}”的用户取消关注公众号", wxMessage.getFromUser());

        // 将公众号用户的“公众号关注状态”改为“取消关注”
        this.subscriptionUserService.updateSubscribedByOpenid(wxMessage.getFromUser(),
                SubscriptionUserConstant.Subscribed.UN_SUBSCRIBED);
        return null;
    }

}
