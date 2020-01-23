package com.kamingpan.infrastructure.subscription.security.provider;

import com.kamingpan.infrastructure.core.base.service.RedisService;
import com.kamingpan.infrastructure.subscription.security.authentication.TokenAuthentication;
import com.kamingpan.infrastructure.subscription.security.cache.UserCache;
import com.kamingpan.infrastructure.subscription.security.properties.SecurityProperties;
import com.kamingpan.infrastructure.util.ip.IP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * token认证 Provider
 *
 * @author kamingpan
 * @since 2019-04-16
 */
@Slf4j
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    public static final String REDIS_USER_KEY = "SUBSCRIPTION:USER:%s";

    public static final String REDIS_TOKEN_KEY = "SUBSCRIPTION:TOKEN:%s";

    public static final String REDIS_TOKEN_VALIDATE_KEY = "SUBSCRIPTION:TOKEN:VALIDATE:%s";

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private RedisService<String, UserCache> userCacheRedisService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取token
        String token = null == authentication.getCredentials() ? null : String.valueOf(authentication.getCredentials());

        // token为空，响应异常
        if (null == token || (token = token.trim()).isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("登录超时，请重新登录");
        }

        // 从redis中查询用户主键
        UserCache userCache;
        String tokenKey = String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, token);
        try {
            userCache = this.userCacheRedisService.get(tokenKey);
        } catch (Exception exception) {
            throw new InternalAuthenticationServiceException("服务器异常", exception);
        }

        // 缓存信息不存在
        if (null == userCache) {
            throw new BadCredentialsException("登录超时，请重新登录");
        }

        // 获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        // 从request中获取ip和校验
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

            String ip = IP.getIP(request);

            if (null != userCache.getIp() && !userCache.getIp().isEmpty()
                    && this.securityProperties.isValidateIp() && !userCache.getIp().equals(ip)) {
                throw new BadCredentialsException("ip不合法，请重新登录");
            }
        }

        // 判断用户refresh_token是否过期，如果已经过期，则需要重新登录，并移除该token登录缓存
        if (userCache.refreshTokenIsExpired()) {
            this.userCacheRedisService.delete(tokenKey);
            throw new CredentialsExpiredException("微信用户refresh_token已过期，请重新登录");
        }

        // 重置token有效时长
        this.userCacheRedisService.expire(tokenKey, this.securityProperties.getSessionTimeout(), TimeUnit.MINUTES);

        // 设置用户信息
        TokenAuthentication tokenAuthentication = userCache.toTokenAuthentication();
        SecurityContextHolder.getContext().setAuthentication(tokenAuthentication);

        return tokenAuthentication;
    }

    /**
     * 判断是否token验证类型
     *
     * @param clazz 验证类
     * @return 是否token验证类型
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return TokenAuthentication.class.equals(clazz);
    }

}
