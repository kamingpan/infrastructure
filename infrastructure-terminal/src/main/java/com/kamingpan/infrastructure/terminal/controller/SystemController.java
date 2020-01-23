package com.kamingpan.infrastructure.terminal.controller;

import com.kamingpan.infrastructure.core.base.service.RedisService;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.entity.constant.OauthTokenConstant;
import com.kamingpan.infrastructure.entity.model.entity.OauthClient;
import com.kamingpan.infrastructure.entity.model.entity.OauthToken;
import com.kamingpan.infrastructure.entity.model.entity.RegisteredUser;
import com.kamingpan.infrastructure.entity.service.DataDictionaryService;
import com.kamingpan.infrastructure.entity.service.OauthClientService;
import com.kamingpan.infrastructure.entity.service.OauthTokenService;
import com.kamingpan.infrastructure.entity.service.RegisteredUserService;
import com.kamingpan.infrastructure.terminal.security.cache.UserCache;
import com.kamingpan.infrastructure.terminal.security.filter.TokenAuthenticationFilter;
import com.kamingpan.infrastructure.terminal.security.properties.SecurityProperties;
import com.kamingpan.infrastructure.terminal.security.provider.TokenAuthenticationProvider;
import com.kamingpan.infrastructure.util.ip.IP;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 系统 controller
 *
 * @author kamingpan
 * @since 2018-06-22
 */
@Slf4j
@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private OAuthIssuer oAuthIssuer;

    @Autowired
    private OauthTokenService oauthTokenService;

    @Autowired
    private OauthClientService oauthClientService;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private RedisService<String, String> redisService;

    @Autowired
    private RegisteredUserService registeredUserService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private RedisService<String, UserCache> userCacheRedisService;

    /**
     * 登录获取token
     *
     * @param oauthToken 认证token
     * @param password   密码
     * @param response   响应
     * @return 响应数据
     */
    @Deprecated
    // @RequestMapping(value = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseData login(@ModelAttribute OauthToken oauthToken, HttpServletResponse response,
                              @RequestParam("username") String username, @RequestParam("password") String password) {
        return ResponseData.success();
    }

    /**
     * 刷新token
     *
     * @param request  请求
     * @param response 响应
     * @return 响应数据
     */
    @RequestMapping(value = "/refresh-token", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseData refresh(HttpServletRequest request, HttpServletResponse response) {
        // 从请求头中获取客户端id，如果不存在，则从参数中获取
        String clientId = request.getHeader(TokenAuthenticationFilter.CLIENT_ID_KEY);
        if (null == clientId || clientId.trim().isEmpty()) {
            clientId = request.getParameter(TokenAuthenticationFilter.CLIENT_ID_KEY);

            if (null == clientId || clientId.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ResponseData.build(ResponseStatus.PARAMETER_ERROR, "客户端id不能为空");
            }
        }

        // 从请求头中获取客户端密钥，如果不存在，则从参数中获取
        String clientSecret = request.getHeader(TokenAuthenticationFilter.CLIENT_SECRET_KEY);
        if (null == clientSecret || clientSecret.trim().isEmpty()) {
            clientSecret = request.getParameter(TokenAuthenticationFilter.CLIENT_SECRET_KEY);

            if (null == clientSecret || clientSecret.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return ResponseData.build(ResponseStatus.PARAMETER_ERROR, "客户端密钥不能为空");
            }
        }

        // 判断授权客户端是否存在
        OauthClient oauthClient = this.oauthClientService.getByClientIdAndSecret(clientId, clientSecret);
        if (null == oauthClient) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setHeader(OAuthError.TokenResponse.INVALID_CLIENT, "无效的客户端");
            return ResponseData.build(ResponseStatus.INVALID_CLIENT);
        }

        // 从请求头中获取refreshToken，如果不存在，则从参数中获取
        String refreshToken = request.getHeader(TokenAuthenticationFilter.REFRESH_TOKEN_KEY);
        if (null == refreshToken || refreshToken.trim().isEmpty()) {
            refreshToken = request.getParameter(TokenAuthenticationFilter.REFRESH_TOKEN_KEY);
        }

        // 校验refreshToken
        OauthToken oauthToken;
        if (null == refreshToken || null == (oauthToken = this.oauthTokenService.getByRefreshToken(refreshToken))
                || oauthToken.refreshTokenExpired()) {
            log.warn("无效的refresh token：{}", refreshToken);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            // 组装异常警告
            String refreshTokenKey = String.format(TokenAuthenticationProvider.REDIS_REFRESH_TOKEN_KEY, refreshToken);
            String message = this.redisService.get(refreshTokenKey);
            if (null != message && !message.isEmpty()) {
                this.redisService.delete(refreshTokenKey);
            } else {
                message = ResponseStatus.INVALID_REFRESH_TOKEN.getMessage();
            }

            // 响应异常
            response.setHeader(OAuthError.ResourceResponse.INVALID_TOKEN, message);
            return ResponseData.build(ResponseStatus.INVALID_REFRESH_TOKEN, message);
        }

        log.debug("refresh token未过期，生成新的access token");

        // 返回新的accessToken和旧的refreshToken
        String oldAccessToken = oauthToken.getAccessToken();
        try {
            oauthToken.setAccessToken(oAuthIssuer.accessToken());
        } catch (OAuthSystemException exception) {
            log.error("生成token失败", exception);
            return ResponseData.build(ResponseStatus.ERROR, "服务器异常");
        }

        //  将新的accessToken更新到数据库中
        oauthToken.setAccessTokenValidity(OauthTokenConstant.ValidityTime.TWO_HOURS);
        oauthToken.setRefreshTime(new Date());
        this.oauthTokenService.updateById(oauthToken);

        // 根据旧accessToken查询用户缓存信息
        String oldAccessTokenKey = String.format(TokenAuthenticationProvider.REDIS_ACCESS_TOKEN_KEY, oldAccessToken);
        UserCache userCache = this.userCacheRedisService.get(oldAccessTokenKey);
        if (null != userCache) {
            // 移除该用户旧的accessToken缓存
            this.userCacheRedisService.delete(oldAccessTokenKey);
        } else {
            // 查询用户，如果用户不存在，则返回refreshToken不存在，触发重新登录
            RegisteredUser registeredUser = this.registeredUserService.getByUserId(oauthToken.getUserId());
            if (null == registeredUser) {
                String message = "无效的refresh token：'" + refreshToken + "'";
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader(OAuthError.ResourceResponse.INVALID_TOKEN, message);
                return ResponseData.build(ResponseStatus.INVALID_REFRESH_TOKEN, message);
            }

            // 如果旧的缓存不存在，则新建缓存信息，并且赋值用户名
            userCache = new UserCache();
            userCache.setUsername(registeredUser.getUsername());
        }

        // 重新赋值用户缓存其余信息
        userCache.setUserId(oauthToken.getUserId());
        userCache.setAccessToken(oauthToken.getAccessToken());
        userCache.setClientId(clientId);
        userCache.setIp(IP.getIP(request));

        // 把accessToken作为key，用户主键作为值缓存到redis中
        this.userCacheRedisService.set(String.format(TokenAuthenticationProvider.REDIS_ACCESS_TOKEN_KEY,
                oauthToken.getAccessToken()), userCache, this.securityProperties.getSessionTimeout(), TimeUnit.MINUTES);

        // 返回新的accessToken
        Map<String, String> result = new HashMap<String, String>();
        result.put(TokenAuthenticationFilter.ACCESS_TOKEN_KEY, oauthToken.getAccessToken());
        result.put(TokenAuthenticationFilter.REFRESH_TOKEN_KEY, oauthToken.getRefreshToken());
        return ResponseData.build(ResponseStatus.SUCCESS, result);
    }

    /**
     * 跳转登录提示
     *
     * @param response 响应
     * @return 响应数据
     */
    @RequestMapping("/to-login")
    public ResponseData toLogin(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("bad credentials", "not login");
        return ResponseData.build(ResponseStatus.NOT_LOGIN);
    }

    /**
     * 根据类和变量查询数据字典
     *
     * @param clazz    类
     * @param variable 变量
     * @return 响应数据
     */
    @GetMapping("/data-dictionary")
    public ResponseData list(@RequestParam("clazz") final String clazz,
                             @RequestParam("variable") final String variable) {
        log.debug("查询“{}”类中“{}”字段的数据字典...", clazz, variable);

        return ResponseData.build(this.dataDictionaryService.listByClazzAndVariable(clazz, variable));
    }

}
