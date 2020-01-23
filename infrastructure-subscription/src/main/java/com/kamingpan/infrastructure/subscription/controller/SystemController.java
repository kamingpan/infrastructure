package com.kamingpan.infrastructure.subscription.controller;

import com.kamingpan.infrastructure.core.base.constant.FinalConstant;
import com.kamingpan.infrastructure.core.base.service.RedisService;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.core.security.UserDetail;
import com.kamingpan.infrastructure.entity.constant.UserConstant;
import com.kamingpan.infrastructure.entity.model.entity.SubscriptionUser;
import com.kamingpan.infrastructure.entity.service.DataDictionaryService;
import com.kamingpan.infrastructure.entity.service.SubscriptionUserService;
import com.kamingpan.infrastructure.subscription.security.cache.UserCache;
import com.kamingpan.infrastructure.subscription.security.properties.SecurityProperties;
import com.kamingpan.infrastructure.subscription.security.provider.TokenAuthenticationProvider;
import com.kamingpan.infrastructure.subscription.util.UserUtil;
import com.kamingpan.infrastructure.util.ip.IP;
import com.kamingpan.infrastructure.util.uri.URI;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 系统controller
 *
 * @author kamingpan
 * @since 2016-09-16
 */
@Slf4j
@RestController
@RequestMapping("/system")
public class SystemController extends SubscriptionBaseController {

    @Autowired
    private OAuthIssuer oAuthIssuer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private RedisService<String, String> stringRedisService;

    @Autowired
    private SubscriptionUserService subscriptionUserService;

    @Autowired
    private RedisService<String, Boolean> booleanRedisService;

    /**
     * 登录（后台主导重定向处理逻辑）
     *
     * @param code        微信端重定向后携带的code授权码
     * @param redirectURL 登录成功后重定向地址
     * @param token       微信端重定向后携带自定义token参数
     * @param request     请求
     * @param response    响应
     */
    @GetMapping("/login")
    public void login(@RequestParam("code") String code, @RequestParam("state") String token,
                      @RequestParam(value = "redirect_url", required = false) String redirectURL,
                      HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 验证该token是否合法
        try {
            Boolean validity = this.booleanRedisService.get(
                    String.format(TokenAuthenticationProvider.REDIS_TOKEN_VALIDATE_KEY, token));

            if (null == validity || !validity) {
                this.sendRedirect(request, response);
                return;
            }
        } catch (Exception exception) {
            log.warn("验证token缓存异常", exception);
            this.sendRedirect(request, response);
            return;
        }

        // 通过code换取微信用户授权access_token
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken;
        try {
            wxMpOAuth2AccessToken = this.wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException exception) {
            log.error("通过code换取微信用户授权access_token异常", exception);
            this.sendRedirect(request, response);
            return;
        }

        // 使用access_token获取用户信息
        WxMpUser wxMpUser;
        try {
            wxMpUser = this.wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
        } catch (WxErrorException exception) {
            log.error("使用access_token获取微信用户信息异常", exception);
            this.sendRedirect(request, response);
            return;
        }

        // 校验返回结果
        if (null == wxMpUser) {
            log.warn("使用access_token获取微信用户信息异常");
            this.sendRedirect(request, response);
            return;
        }

        // 登录逻辑处理
        try {
            this.handleLogin(wxMpOAuth2AccessToken, wxMpUser, token, request);
        } catch (Exception exception) {
            log.warn("添加登录缓存异常", exception);
            this.sendRedirect(request, response);
            return;
        }

        // 登录成功后，处理重定向地址到发起登录的前端页面
        if (null == redirectURL || redirectURL.trim().isEmpty()) {
            redirectURL = (this.securityProperties.getLoginSuccessUrl().startsWith("http") ?
                    "" : URI.getCurrentProject(request)) + this.securityProperties.getLoginSuccessUrl();
        } else {
            // 对重定向路径做base64解密
            redirectURL = new String(Base64.getDecoder().decode(redirectURL), StandardCharsets.UTF_8);
        }

        // 因为微信登录逻辑是重定向，因此登录重定向到发起登录请求接口的页面，而不是响应异常信息
        response.sendRedirect(redirectURL);
    }

    /**
     * 登录（前端主导重定向处理逻辑）
     *
     * @param code    微信端重定向后携带的code授权码
     * @param state   微信端重定向后携带自定义参数
     * @param request 请求
     */
    @PostMapping("/login")
    public ResponseData login(@RequestParam("code") String code, HttpServletRequest request,
                              @RequestParam(value = "state", required = false) String state) {
        // 通过code换取微信用户授权access_token
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken;
        try {
            wxMpOAuth2AccessToken = this.wxMpService.oauth2getAccessToken(code);
        } catch (WxErrorException exception) {
            String message = "通过code“" + code + "”换取微信用户授权access_token异常";
            log.error(message, exception);
            return ResponseData.build(ResponseStatus.VALIDATE_ERROR, message);
        }

        // 使用access_token获取用户信息
        WxMpUser wxMpUser;
        try {
            wxMpUser = this.wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
        } catch (WxErrorException exception) {
            String message = "使用access_token获取微信用户信息异常";
            log.error(message, exception);
            return ResponseData.build(ResponseStatus.VALIDATE_ERROR, message);
        }

        // 校验返回结果
        if (null == wxMpUser) {
            String message = "使用access_token获取微信用户信息异常";
            log.warn(message);
            return ResponseData.build(ResponseStatus.VALIDATE_ERROR, message);
        }

        // 登录逻辑处理
        String token;
        try {
            token = this.handleLogin(wxMpOAuth2AccessToken, wxMpUser, null, request);
        } catch (Exception exception) {
            String message = "添加登录缓存异常";
            log.warn(message, exception);
            return ResponseData.build(ResponseStatus.SERVER_ERROR, message);
        }

        // 组装返回token
        Map<String, String> result = new HashMap<String, String>();
        result.put("token", token);
        return ResponseData.build(result);
    }

    /**
     * 处理登录逻辑
     *
     * @param wxMpOAuth2AccessToken 微信认证token对象
     * @param wxMpUser              微信用户信息
     * @param token                 登录凭证
     * @param request               请求
     * @return 登录凭证
     * @throws Exception 异常
     */
    private String handleLogin(WxMpOAuth2AccessToken wxMpOAuth2AccessToken, WxMpUser wxMpUser, String token,
                               HttpServletRequest request) throws Exception {
        // 定义ip变量
        String ip = IP.getIP(request);

        // 微信用户的性别，值为1时是男性，值为2时是女性，值为0时是未知；
        // 而本地系统的性别，值为0时是女性，值为1时是男性，值为2时是未知，因此要把微信的性别类型转换为当前系统的性别类型
        Integer gender = UserConstant.Gender.UNKNOWN;
        if (UserConstant.Gender.MALE.equals(wxMpUser.getSex())) {
            gender = UserConstant.Gender.MALE;
        } else if (UserConstant.Gender.UNKNOWN.equals(wxMpUser.getSex())) {
            gender = UserConstant.Gender.FEMALE;
        }

        // 查询公众号用户是否存在
        SubscriptionUser subscriptionUser = this.subscriptionUserService.getByOpenid(wxMpOAuth2AccessToken.getOpenId());
        boolean isInsert = null == subscriptionUser;
        if (null == subscriptionUser) {
            // 公众号用户不存在时逻辑 - 新建公众号用户，后续触发“新增用户和公众号用户”
            subscriptionUser = new SubscriptionUser();
        }

        // 赋值个人信息
        subscriptionUser.setNickname(wxMpUser.getNickname());
        subscriptionUser.setPortrait(wxMpUser.getHeadImgUrl());
        subscriptionUser.setGender(gender);
        subscriptionUser.setCountry(wxMpUser.getCountry());
        subscriptionUser.setProvince(wxMpUser.getProvince());
        subscriptionUser.setCity(wxMpUser.getCity());
        subscriptionUser.setLanguage(wxMpUser.getLanguage());
        subscriptionUser.setOpenid(wxMpUser.getOpenId());
        subscriptionUser.setUnionId(wxMpUser.getUnionId());
        subscriptionUser.setSubscribed(wxMpUser.getSubscribe());

        if (isInsert) {
            // 公众号用户不存在时逻辑 - 新增用户和公众号用户
            this.subscriptionUserService.insert(subscriptionUser);
        } else {
            // 公众号用户存在时逻辑 - 更新公众号用户信息
            this.subscriptionUserService.update(subscriptionUser);
        }

        // 获取当前时间戳
        long currentTime = System.currentTimeMillis();

        // 如果token为空，表示是前端主导重定向，需要生成token
        if (null == token || token.isEmpty()) {
            token = this.oAuthIssuer.accessToken();
        }

        // 创建用户缓存信息
        UserCache userCache = new UserCache(subscriptionUser.getUserId(),
                token, wxMpOAuth2AccessToken.getOpenId(), ip);
        userCache.setAccessToken(wxMpOAuth2AccessToken.getAccessToken());
        userCache.setRefreshToken(wxMpOAuth2AccessToken.getRefreshToken());

        // 赋值access_token过期时间（过期时间设置为提前5分钟，避免临界点时导致异常）
        userCache.setAccessTokenExpiresIn(new Date(currentTime
                + (wxMpOAuth2AccessToken.getExpiresIn() - 300) * 1000));

        // 赋值refresh_token过期时间（按微信文档描述，refresh_token有效时长为30天，过期时间设置为提前12小时，避免临界点时导致异常）
        userCache.setRefreshTokenExpiresIn(new Date(currentTime + (30 * 24 - 12) * 3600L * 1000));

        // 添加登录缓存
        this.addLoginSession(userCache);

        // 返回token
        return token;
    }

    /**
     * 未登录响应（该方法为公众号项目特有，返回重定向授权地址，由前端重定向）
     *
     * @param referer     发起请求页面
     * @param referrerURL 发起请求页面（前端自定义组装）
     * @param request     请求
     * @param response    响应
     * @return 响应数据
     */
    @RequestMapping("/to-login")
    public ResponseData toLogin(@RequestHeader(value = "Referer", required = false) String referer,
                                @RequestHeader(value = "Referrer-Url", required = false) String referrerURL,
                                HttpServletRequest request, HttpServletResponse response) {
        // 默认定义重定向地址
        String redirectURL = FinalConstant.Strings.EMPTY;

        // 组装重定向地址，通过微信跳转到登录
        if (null != referrerURL && !referrerURL.isEmpty()) {
            // 对重定向路径做base64加密
            redirectURL = Base64.getEncoder().encodeToString(referrerURL.getBytes());
        } else if (null != referer && !referer.isEmpty()) {
            // 如果自定义的页面地址为空，则使用h5自带请求头页面
            redirectURL = Base64.getEncoder().encodeToString(referer.getBytes());
        }

        // 生成token并存到redis中，设置有效时长为半小时
        String token;
        try {
            token = this.oAuthIssuer.accessToken();
            this.booleanRedisService.set(String.format(TokenAuthenticationProvider.REDIS_TOKEN_VALIDATE_KEY,
                    token), true, 30, TimeUnit.MINUTES);
        } catch (Exception exception) {
            return ResponseData.build(ResponseStatus.ERROR, exception.getMessage());
        }

        // 拼接登录请求地址，并做微信跳转地址处理
        redirectURL = URI.getCurrentProject(request) + "/system/login?redirect_url=" + redirectURL;
        redirectURL = this.wxMpService.oauth2buildAuthorizationUrl(redirectURL,
                WxConsts.OAuth2Scope.SNSAPI_USERINFO, token);

        // 返回重定向地址和token
        Map<String, String> result = new HashMap<String, String>();
        result.put("redirect_url", redirectURL);
        result.put("token", token);

        // 设置状态值并响应
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return ResponseData.build(ResponseStatus.NOT_LOGIN, result);
    }

    /**
     * js接口权限验证配置
     *
     * @param referer 当前页面地址
     * @return 响应数据
     * @throws WxErrorException 微信异常
     */
    @GetMapping("/js-config")
    public ResponseData jsConfig(@RequestHeader("referer") String referer) throws WxErrorException {
        // 初始化接口权限信息
        WxJsapiSignature wxJsapiSignature = this.wxMpService.createJsapiSignature(referer);
        return ResponseData.build(wxJsapiSignature);
    }

    /**
     * 添加redis登录缓存
     *
     * @param userCache 用户缓存
     */
    private synchronized void addLoginSession(UserCache userCache) {
        // 根据主键查询当前已登录的该用户的token信息
        String userKey = String.format(TokenAuthenticationProvider.REDIS_USER_KEY, userCache.getUserId());
        String oldToken = this.stringRedisService.get(userKey);

        // 删除旧的token登录缓存
        this.userCacheRedisService.delete(String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, oldToken));

        // 保存新的token缓存信息到redis中
        this.stringRedisService.set(userKey, userCache.getToken());

        // 把token作为key，用户缓存作为值缓存到redis中
        this.userCacheRedisService.set(String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, userCache.getToken()),
                userCache, this.securityProperties.getSessionTimeout(), TimeUnit.MINUTES);
    }

    /**
     * 登录失败后重定向处理
     *
     * @param request  请求
     * @param response 响应
     */
    private void sendRedirect(HttpServletRequest request, HttpServletResponse response) {
        // 因为微信登录逻辑是重定向，因此登录异常时重定向到错误页面，而不是响应异常信息
        try {
            response.sendRedirect((this.securityProperties.getLoginFailureUrl().startsWith("http") ?
                    "" : URI.getCurrentProject(request)) + this.securityProperties.getLoginFailureUrl());
        } catch (IOException exception) {
            log.error("重定向异常", exception);
        }
    }

    /**
     * 更新公众号用户手机号码
     *
     * @param mobile 手机号码
     * @return 响应数据
     */
    @PostMapping("/bind-phone")
    public ResponseData bindMobile(@RequestParam("mobile") String mobile) {
        // 获取当前登录用户信息
        UserDetail userDetail = UserUtil.getUser();
        if (null == userDetail) {
            return ResponseData.build(ResponseStatus.NOT_LOGIN);
        }

        // 更新公众号用户手机号码
        this.subscriptionUserService.updateMobile(UserUtil.getUserId(), mobile);
        return ResponseData.success();
    }

    /**
     * 更新登录缓存（暂时注释掉该段代码）
     *
     * @param newUserId 新用户主键
     * @param oldUserId 旧用户主键
     */
    @Deprecated
    private void updateLoginSession(String newUserId, String oldUserId) {
        // 获取当前小程序原来用户的token，并将redis中的数据删除
        String oldUserKey = String.format(TokenAuthenticationProvider.REDIS_USER_KEY, oldUserId);
        String token = this.stringRedisService.get(oldUserKey);
        this.stringRedisService.delete(oldUserKey);

        // 将新用户主键原来的token失效，并替换为当前登录的token
        String newUserKey = String.format(TokenAuthenticationProvider.REDIS_USER_KEY, newUserId);
        String userOldToken = this.stringRedisService.get(newUserKey); // 获取当前新用户的原token
        String oldTokenKey = String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, userOldToken);
        this.userCacheRedisService.delete(oldTokenKey); // 删除原token对应的用户缓存
        this.stringRedisService.set(newUserKey, token); // 替换token为当前登录的token

        // 从redis中获取当前登录token对应的旧缓存信息
        String tokenKey = String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, token);
        UserCache userCache = this.userCacheRedisService.get(tokenKey);
        if (null == userCache) {
            return;
        }

        // 更新缓存的用户主键为新的用户主键，并更新到redis中
        userCache.setUserId(newUserId);
        this.userCacheRedisService.set(tokenKey, userCache,
                this.securityProperties.getSessionTimeout(), TimeUnit.MINUTES);
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
