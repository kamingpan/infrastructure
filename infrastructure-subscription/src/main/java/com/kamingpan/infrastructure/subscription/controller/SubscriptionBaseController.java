package com.kamingpan.infrastructure.subscription.controller;

import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.base.service.RedisService;
import com.kamingpan.infrastructure.core.security.UserDetail;
import com.kamingpan.infrastructure.subscription.security.authentication.TokenAuthentication;
import com.kamingpan.infrastructure.subscription.security.cache.UserCache;
import com.kamingpan.infrastructure.subscription.security.provider.TokenAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 公众号基础controller
 *
 * @author kamingpan
 * @since 2016-09-09
 */
@Slf4j
public class SubscriptionBaseController extends BaseController {

    @Autowired
    protected WxMpService wxMpService;

    @Autowired
    protected RedisService<String, UserCache> userCacheRedisService;

    /**
     * 获取公众号的access_token
     *
     * @return 公众号access_token
     */
    public String getSubscriptionAccessToken() {
        try {
            return this.wxMpService.getAccessToken();
        } catch (WxErrorException exception) {
            throw new AuthenticationServiceException("获取公众号的access_token异常", exception);
        }
    }

    /**
     * 获取当前登录用户的微信access_token
     *
     * @return 微信用户access_token
     */
    public String getSubscriptionUserAccessToken() {
        // 获取当前登录用户信息，并验证有效性
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof TokenAuthentication)) {
            throw new AuthenticationServiceException("获取登录用户信息异常");
        }

        // ******************* 该处有两种判断access_token是否还有效的逻辑（目前使用本地缓存判断） ********************** //

        // 获取登录信息
        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
        if (!(tokenAuthentication.getDetails() instanceof UserDetail)) {
            throw new AuthenticationServiceException("获取登录用户信息异常");
        }
        UserDetail userDetail = (UserDetail) tokenAuthentication.getDetails();

        // 第一种：通过本地缓存判断access_token是否有效
        if (!userDetail.accessTokenIsExpired()) {
            return userDetail.getAccessToken();
        }

        // 第二种：通过微信接口判断access_token是否有效
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = null;
        /*wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        wxMpOAuth2AccessToken.setAccessToken(tokenAuthentication.getAccessToken());
        wxMpOAuth2AccessToken.setOpenId(tokenAuthentication.getOpenid());

        // 判断access_token是否还有效
        boolean accessTokenIsExpired = this.wxMpService.oauth2validateAccessToken(wxMpOAuth2AccessToken);
        if (accessTokenIsExpired) {
            return tokenAuthentication.getAccessToken();
        }*/

        // 如果无效了，则重新刷新token，并且更新到redis中
        try {
            wxMpOAuth2AccessToken = this.wxMpService.oauth2refreshAccessToken(userDetail.getRefreshToken());
        } catch (WxErrorException exception) {
            log.warn("使用refresh_token刷新微信用户信息异常", exception);
            throw new AuthenticationServiceException("使用refresh_token刷新微信用户信息异常", exception);
        }

        // 判断刷新access_token是否异常
        if (null == wxMpOAuth2AccessToken) {
            log.warn("使用refresh_token刷新微信用户信息异常");
            throw new AuthenticationServiceException("使用refresh_token刷新微信用户信息异常");
        }

        try {
            // 根据token查询用户缓存信息
            String tokenKey = String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, userDetail.getToken());
            UserCache userCache = this.userCacheRedisService.get(tokenKey);
            if (null == userCache) {
                return wxMpOAuth2AccessToken.getAccessToken();
            }

            // 更新redis缓存
            userCache.setAccessToken(wxMpOAuth2AccessToken.getAccessToken());
            this.userCacheRedisService.set(tokenKey, userCache);
        } catch (Exception exception) {
            throw new AuthenticationServiceException("更新redis缓存异常", exception);
        }

        // 更新当前登录用户信息
        userDetail.setAccessToken(wxMpOAuth2AccessToken.getAccessToken());
        return wxMpOAuth2AccessToken.getAccessToken();
    }

}
