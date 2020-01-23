package com.kamingpan.infrastructure.subscription.wechat.handler.impl;

import com.kamingpan.infrastructure.entity.model.entity.SubscriptionUserLocation;
import com.kamingpan.infrastructure.entity.service.SubscriptionUserLocationService;
import com.kamingpan.infrastructure.subscription.wechat.handler.LocationHandler;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * 上报地理位置事件接口实现类
 *
 * @author kamingpan
 * @since 2018-07-19
 */
@Slf4j
@Component
@ConditionalOnMissingBean(name = {"locationHandler"})
public class LocationHandlerImpl implements LocationHandler {

    @Autowired
    private SubscriptionUserLocationService subscriptionUserLocationService;

    /**
     * 上报地理位置事件逻辑处理
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
        log.debug("微信服务器推送了上报地理位置事件：{}", wxMessage.toString());
        log.info("微信服务器上报了openid为“{}”用户的地理位置信息", wxMessage.getFromUser());

        // 创建公众号用户地理位置对象
        SubscriptionUserLocation subscriptionUserLocation = new SubscriptionUserLocation();
        subscriptionUserLocation.setOpenid(wxMessage.getFromUser());
        subscriptionUserLocation.setLongitude(wxMessage.getLongitude());
        subscriptionUserLocation.setLatitude(wxMessage.getLatitude());
        subscriptionUserLocation.setLocationPrecision(wxMessage.getPrecision());
        subscriptionUserLocation.setRecordTime(new Date());
        this.subscriptionUserLocationService.insert(subscriptionUserLocation);

        return null;
    }
}
