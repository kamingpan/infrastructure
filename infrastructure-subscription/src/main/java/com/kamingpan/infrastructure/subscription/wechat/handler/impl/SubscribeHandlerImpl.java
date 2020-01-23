package com.kamingpan.infrastructure.subscription.wechat.handler.impl;

import com.kamingpan.infrastructure.entity.service.SubscriptionUserService;
import com.kamingpan.infrastructure.subscription.wechat.handler.SubscribeHandler;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutNewsMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 关注事件接口实现类
 *
 * @author kamingpan
 * @since 2018-07-19
 */
@Slf4j
@Component
@ConditionalOnMissingBean(name = {"subscribeHandler"})
public class SubscribeHandlerImpl implements SubscribeHandler {

    @Autowired
    private SubscriptionUserService subscriptionUserService;

    /**
     * 关注事件逻辑处理
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
        log.debug("微信服务器推送了关注事件：{}", wxMessage.toString());
        log.info("openid为“{}”的用户关注了公众号", wxMessage.getFromUser());

        // 新增或更新公众号用户的关注公众号状态
        this.subscriptionUserService.insertOrUpdateByOpenid(wxMessage.getFromUser());

        WxMpXmlOutNewsMessage.Item item = new WxMpXmlOutNewsMessage.Item();
        // 设置描述标题
        item.setTitle("欢迎关注");
        // 设置描述内容
        item.setDescription("点击进入首页");
        // 设置点击跳转地址
        item.setUrl("http://www.kamingpan.com/subscription-html/#index");

        // 组装模板信息
        return WxMpXmlOutMessage.NEWS().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .addArticle(item).build();
    }

}
