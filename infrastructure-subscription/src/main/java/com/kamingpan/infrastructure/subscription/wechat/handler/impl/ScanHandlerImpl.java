package com.kamingpan.infrastructure.subscription.wechat.handler.impl;

import com.kamingpan.infrastructure.subscription.wechat.handler.ScanHandler;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 扫描二维码事件接口实现类
 *
 * @author kamingpan
 * @since 2018-12-17
 */
@Slf4j
@Component
@ConditionalOnMissingBean(name = {"scanHandler"})
public class ScanHandlerImpl implements ScanHandler {

    /**
     * 默认发送信息逻辑处理
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
        log.debug("微信服务器推送了扫描二维码信息：{}", wxMessage.toString());

        String content = "暂不支持二维码信息解析，后续可能将完善此功能，敬请期待！";

        // 组装文本信息返回
        return WxMpXmlOutMessage.TEXT().fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
                .content(content).build();
    }

}
