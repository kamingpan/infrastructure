package com.kamingpan.infrastructure.subscription.wechat.controller;

import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 微信开发模式服务
 *
 * @author kamingpan
 * @since 2016-09-09
 */
@Slf4j
@RestController
@RequestMapping("")
public class ServiceRootController {

    /**
     * 异常响应字符串
     */
    private static final String ERROR_RESPONSE = "ERROR";

    /**
     * 成功响应字符串
     */
    private static final String SUCCESS_RESPONSE = "SUCCESS";

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxMpConfigStorage wxMpConfigStorage;

    @Autowired
    private WxMpMessageRouter wxMpMessageRouter;

    /**
     * 服务器地址（微信服务器统一推送入口）
     *
     * @param signature   签名
     * @param nonce       随机字符串
     * @param timestamp   时间戳
     * @param echoString  输出字符串
     * @param encryptType 加密类型
     * @param request     请求
     * @param response    响应
     * @return 响应结果字符串
     * @throws IOException io异常
     */
    @RequestMapping("/service-root")
    public String serviceRoot(@RequestParam("signature") String signature,
                              @RequestParam("nonce") String nonce,
                              @RequestParam("timestamp") String timestamp,
                              @RequestParam(value = "echostr", required = false) String echoString,
                              @RequestParam(value = "encrypt_type", required = false) String encryptType,
                              HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug("接收到微信服务器信息：{}", request.getInputStream());

        // 验签
        if (!this.wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            // response.getWriter().println("ERROR");
            return ERROR_RESPONSE;
        }

        // 校验微信推送信息成功
        log.debug("校验成功");

        // 如果echostr不为空，说明是一个仅仅用来验证的请求，返回echostr
        if (StringUtils.isNotBlank(echoString)) {
            log.debug("响应成功，内容为：{}", echoString);
            return echoString;
        }

        // 加密类型
        encryptType = StringUtils.isBlank(encryptType) ? "raw" : encryptType;

        // 明文传输的消息
        if ("raw".equals(encryptType)) {
            WxMpXmlMessage wxMpXmlMessage = WxMpXmlMessage.fromXml(request.getInputStream());
            WxMpXmlOutMessage wxMpXmlOutMessage = this.wxMpMessageRouter.route(wxMpXmlMessage);
            String responseMessage = null == wxMpXmlOutMessage ? SUCCESS_RESPONSE : wxMpXmlOutMessage.toXml();

            log.debug("明文信息响应成功，内容为：{}", responseMessage);
            return responseMessage;
        }

        // aes加密的消息
        if ("aes".equals(encryptType)) {
            String msgSignature = request.getParameter("msg_signature");
            WxMpXmlMessage wxMpXmlMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(),
                    this.wxMpConfigStorage, timestamp, nonce, msgSignature);
            WxMpXmlOutMessage wxMpXmlOutMessage = this.wxMpMessageRouter.route(wxMpXmlMessage);
            String responseMessage = null == wxMpXmlOutMessage ?
                    SUCCESS_RESPONSE : wxMpXmlOutMessage.toEncryptedXml(this.wxMpConfigStorage);

            log.debug("aes加密信息响应成功，内容为：{}", responseMessage);
            return responseMessage;
        }

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);

        log.debug("响应失败");
        return ERROR_RESPONSE;
    }

    /**
     * 服务器校验文件
     * 实际上即为截取某一段url内容，而不是用纯文件url请求校验
     *
     * @return 文件内容
     */
    @RequestMapping("/MP_verify_{content}.txt")
    public String verification(@PathVariable("content") String content) {
        return content;
    }

}
