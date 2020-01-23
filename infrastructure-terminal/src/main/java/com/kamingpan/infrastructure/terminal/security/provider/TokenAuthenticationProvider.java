package com.kamingpan.infrastructure.terminal.security.provider;

import com.kamingpan.infrastructure.core.base.service.RedisService;
import com.kamingpan.infrastructure.terminal.security.authentication.TokenAuthentication;
import com.kamingpan.infrastructure.terminal.security.cache.UserCache;
import com.kamingpan.infrastructure.terminal.security.properties.SecurityProperties;
import com.kamingpan.infrastructure.util.ip.IP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
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
 * @since 2019-01-09
 */
@Slf4j
@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    public static final String REDIS_ACCESS_TOKEN_KEY = "TERMINAL:ACCESS_TOKEN:%s";

    public static final String REDIS_REFRESH_TOKEN_KEY = "TERMINAL:REFRESH_TOKEN:%s";

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private RedisService<String, UserCache> userCacheRedisService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取accessToken
        String accessToken = null == authentication.getCredentials() ?
                null : String.valueOf(authentication.getCredentials());

        // accessToken为空，响应异常
        if (null == accessToken || (accessToken = accessToken.trim()).isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("登录超时，请重新登录");
        }

        // 从redis中查询用户主键
        UserCache userCache;
        String accessTokenKey = String.format(TokenAuthenticationProvider.REDIS_ACCESS_TOKEN_KEY, accessToken);
        try {
            userCache = this.userCacheRedisService.get(accessTokenKey);
        } catch (Exception exception) {
            throw new InternalAuthenticationServiceException("服务器异常", exception);
        }
        if (null == userCache) {
            throw new BadCredentialsException("access_token已失效");
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

        // 重置accessToken有效时长
        this.userCacheRedisService.expire(accessTokenKey, this.securityProperties.getSessionTimeout(), TimeUnit.MINUTES);

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
