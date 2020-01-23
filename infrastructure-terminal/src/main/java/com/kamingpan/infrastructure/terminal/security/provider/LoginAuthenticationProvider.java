package com.kamingpan.infrastructure.terminal.security.provider;

import com.kamingpan.infrastructure.core.base.service.RedisService;
import com.kamingpan.infrastructure.entity.constant.RegisteredUserConstant;
import com.kamingpan.infrastructure.entity.constant.UserConstant;
import com.kamingpan.infrastructure.entity.model.entity.OauthClient;
import com.kamingpan.infrastructure.entity.model.entity.OauthToken;
import com.kamingpan.infrastructure.entity.model.entity.RegisteredUser;
import com.kamingpan.infrastructure.entity.model.entity.User;
import com.kamingpan.infrastructure.entity.service.OauthClientService;
import com.kamingpan.infrastructure.entity.service.OauthTokenService;
import com.kamingpan.infrastructure.entity.service.RegisteredUserService;
import com.kamingpan.infrastructure.entity.service.UserService;
import com.kamingpan.infrastructure.terminal.security.authentication.LoginAuthentication;
import com.kamingpan.infrastructure.terminal.security.cache.UserCache;
import com.kamingpan.infrastructure.terminal.security.filter.TokenAuthenticationFilter;
import com.kamingpan.infrastructure.terminal.security.properties.SecurityProperties;
import com.kamingpan.infrastructure.util.ip.IP;
import com.kamingpan.infrastructure.util.md5.MD5;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

/**
 * 登录认证 Provider
 *
 * @author kamingpan
 * @since 2019-01-08
 */
@Slf4j
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

    public static final String REDIS_USER_KEY = "TERMINAL:USER:%s";

    private static final String REDIS_USER_PASSWORD_RETRY_KEY = "TERMINAL:USER:PASSWORD_RETRY:%s";

    @Autowired
    private OAuthIssuer oAuthIssuer;

    @Autowired
    private UserService userService;

    @Autowired
    private OauthTokenService oauthTokenService;

    @Autowired
    private OauthClientService oauthClientService;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private RegisteredUserService registeredUserService;

    @Autowired
    private RedisService<String, String> stringRedisService;

    @Autowired
    private RedisService<String, Integer> integerRedisService;

    @Autowired
    private RedisService<String, UserCache> userCacheRedisService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 用户名去空格并转换成小写
        String username = null == authentication.getPrincipal()
                ? null : String.valueOf(authentication.getPrincipal()).trim().toLowerCase();
        String password = null == authentication.getCredentials()
                ? null : String.valueOf(authentication.getCredentials()).trim();

        // 获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            throw new AuthenticationServiceException("服务器异常");
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        if (null == username || username.isEmpty()) {
            throw new UsernameNotFoundException("用户名不能为空");
        } else if (null == password || password.isEmpty()) {
            throw new BadCredentialsException("密码不能为空");
        }

        // 从请求头中获取客户端id，如果不存在，则从参数中获取
        String clientId = request.getHeader(TokenAuthenticationFilter.CLIENT_ID_KEY);
        if (null == clientId || clientId.trim().isEmpty()) {
            clientId = request.getParameter(TokenAuthenticationFilter.CLIENT_ID_KEY);

            if (null == clientId || clientId.isEmpty()) {
                throw new BadCredentialsException("客户端id不能为空");
            }
        }

        // 从请求头中获取客户端密钥，如果不存在，则从参数中获取
        String clientSecret = request.getHeader(TokenAuthenticationFilter.CLIENT_SECRET_KEY);
        if (null == clientSecret || clientSecret.trim().isEmpty()) {
            clientSecret = request.getParameter(TokenAuthenticationFilter.CLIENT_SECRET_KEY);

            if (null == clientSecret || clientSecret.isEmpty()) {
                throw new BadCredentialsException("客户端密钥不能为空");
            }
        }

        // 判断授权客户端是否存在
        OauthClient oauthClient = this.oauthClientService.getByClientIdAndSecret(clientId, clientSecret);
        if (null == oauthClient) {
            throw new BadCredentialsException("无效的客户端");
        }

        // 根据用户名查询注册用户
        RegisteredUser registeredUser = this.registeredUserService.getByUsername(username);

        // 验证账户是否存在
        if (null == registeredUser || null == registeredUser.getUserId()) {
            throw new UsernameNotFoundException("用户不存在，请确认后重新输入");
        }

        // 查询对应用户
        User user = this.userService.getById(registeredUser.getUserId());

        // 获取ip
        String ip = IP.getIP(request);

        // 验证状态
        if (UserConstant.Status.DISABLE.equals(user.getStatus())) {
            String message = "该用户已被禁用，请联系客服";
            throw new DisabledException(message);
        }

        // 验证MD5加密密码
        String md5Password = password + RegisteredUserConstant.Password.LEFT_BRACKET
                + username + RegisteredUserConstant.Password.RIGHT_BRACKET;
        try {
            md5Password = MD5.encryption(md5Password);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException exception) {
            String message = "MD5加密错误，请重新输入";
            throw new InternalAuthenticationServiceException(message);
        }

        // 判断密码输错次数（从redis中缓存和获取）
        String loginErrorKey = String.format(LoginAuthenticationProvider.REDIS_USER_PASSWORD_RETRY_KEY,
                registeredUser.getUserId());
        int loginErrorCount = 0;
        try {
            Integer loginError = this.integerRedisService.get(loginErrorKey);
            if (null != loginError) {
                loginErrorCount = loginError;
            }
        } catch (Exception exception) {
            throw new InternalAuthenticationServiceException("服务器异常", exception);
        }

        // 如果登录错误次数到达指定错误次数，则不允许登录
        if (loginErrorCount >= this.securityProperties.getMaxLoginError()) {
            String message = "您今天登录错误次数达到" + this.securityProperties.getMaxLoginError() + "次，请明天再尝试";
            throw new BadCredentialsException(message);
        }

        // 验证密码
        if (!md5Password.equals(registeredUser.getPassword())) {
            // 登录错误次数+1并修改至缓存中，返回错误提示
            loginErrorCount++;
            this.integerRedisService.set(loginErrorKey, loginErrorCount,
                    this.securityProperties.getLoginErrorTimeout(), TimeUnit.MINUTES);
            if (loginErrorCount == this.securityProperties.getMaxLoginError()) {
                String message = "您今天登录错误次数达到" + this.securityProperties.getMaxLoginError() + "次，请明天再尝试";
                throw new BadCredentialsException(message);
            }

            // 响应错误信息
            String message = "密码错误，今天还有" + (this.securityProperties.getMaxLoginError() - loginErrorCount) + "次尝试机会";
            throw new BadCredentialsException(message);

            /*String message = "密码错误，请重新输入";
            throw new BadCredentialsException(message);*/
        }

        // 如果登录成功，则清除登录错误次数的缓存
        this.integerRedisService.delete(loginErrorKey);

        // 创建accessToken和refreshToken，并定义一个旧accessToken用于缓存提醒
        String accessToken;
        String refreshToken;
        String oldAccessToken = null;
        try {
            accessToken = this.oAuthIssuer.accessToken();
            refreshToken = this.oAuthIssuer.refreshToken();
        } catch (OAuthSystemException exception) {
            throw new InternalAuthenticationServiceException("服务器异常", exception);
        }

        // 根据用户主键查询是否有已存在的该用户accessToken
        OauthToken oauthToken = this.oauthTokenService.getByUserId(registeredUser.getUserId(), clientId);
        if (null == oauthToken) {
            log.debug("未找到accessToken信息，创建新的授权token");
            oauthToken = new OauthToken();
            oauthToken.setClientId(clientId);
            oauthToken.setUserId(registeredUser.getUserId());
            oauthToken.setAccessToken(accessToken);
            oauthToken.setRefreshToken(refreshToken);

            this.oauthTokenService.insert(oauthToken);
        } else {
            log.debug("授权token已存在，生成新的refresh token和access token");
            oldAccessToken = oauthToken.getAccessToken();

            oauthToken.setAccessToken(accessToken);
            oauthToken.setRefreshToken(refreshToken);

            this.oauthTokenService.update(oauthToken);
        }

        // 更新登录缓存
        UserCache userCache = new UserCache(registeredUser.getUserId(), accessToken, clientId, username, ip);
        try {
            this.addLoginSession(userCache, refreshToken, oldAccessToken);
        } catch (Exception exception) {
            throw new InternalAuthenticationServiceException("服务器异常", exception);
        }

        // 设置登录信息
        Authentication loginAuthentication = new LoginAuthentication(accessToken, refreshToken, username);
        SecurityContextHolder.getContext().setAuthentication(loginAuthentication);
        return loginAuthentication;
    }

    /**
     * 判断是否登录验证类型
     *
     * @param clazz 验证类
     * @return 是否登录验证类型
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return UsernamePasswordAuthenticationToken.class.equals(clazz);
    }

    /**
     * 添加redis登录缓存
     *
     * @param userCache      用户缓存
     * @param refreshToken   刷新令牌
     * @param oldAccessToken 旧的认证令牌
     */
    private synchronized void addLoginSession(UserCache userCache, String refreshToken, String oldAccessToken) {
        // 先移除旧的accessToken记录
        if (null != oldAccessToken && !oldAccessToken.isEmpty()) {
            this.userCacheRedisService.delete(String.format(
                    TokenAuthenticationProvider.REDIS_ACCESS_TOKEN_KEY, oldAccessToken));
        }

        // 根据用户主键查询当前已登录的该用户缓存refreshToken
        String userKey = String.format(LoginAuthenticationProvider.REDIS_USER_KEY, userCache.getUserId());
        String oldRefreshToken = this.stringRedisService.get(userKey);

        // 如果该用户缓存refreshToken存在，则移除旧的用户缓存refreshToken信息，并且添加移除描述
        if (null != oldRefreshToken && !oldRefreshToken.isEmpty()) {
            this.stringRedisService.delete(userKey);

            // 添加一个旧refreshToken临时缓存，用于提醒被移除的用户，缓存时间为一天
            this.stringRedisService.set(String.format(TokenAuthenticationProvider.REDIS_REFRESH_TOKEN_KEY,
                    oldRefreshToken), "您的账号在其它设备被登录(ip:" + userCache.getIp()
                    + ")，若非您本人操作，建议您尽快修改密码。", 1, TimeUnit.DAYS);
        }

        // 使用用户主键作为key，用户新的refreshToken作为值保存到redis中，用于判断重复登录
        this.stringRedisService.set(userKey, refreshToken);

        // 把accessToken作为key，用户主键作为值缓存到redis中
        this.userCacheRedisService.set(String.format(TokenAuthenticationProvider.REDIS_ACCESS_TOKEN_KEY,
                userCache.getAccessToken()), userCache, this.securityProperties.getSessionTimeout(), TimeUnit.MINUTES);
    }

}
