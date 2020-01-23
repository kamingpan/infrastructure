package com.kamingpan.infrastructure.management.security.provider;

import com.kamingpan.infrastructure.core.base.service.RedisService;
import com.kamingpan.infrastructure.management.security.authentication.TokenAuthentication;
import com.kamingpan.infrastructure.management.security.cache.AdminCache;
import com.kamingpan.infrastructure.management.security.properties.SecurityProperties;
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

    public static final String REDIS_TOKEN_KEY = "MANAGEMENT:TOKEN:%s";

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private RedisService<String, AdminCache> adminCacheRedisService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取token
        String token = null == authentication.getCredentials() ? null : String.valueOf(authentication.getCredentials());

        // token为空，响应异常
        if (null == token || (token = token.trim()).isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("登录超时，请重新登录");
        }

        // 从redis中查询用户主键
        AdminCache adminCache;
        String tokenKey = String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, token);
        try {
            adminCache = this.adminCacheRedisService.get(tokenKey);
        } catch (Exception exception) {
            throw new InternalAuthenticationServiceException("服务器异常", exception);
        }

        // 判断是否已登录
        if (null == adminCache) {
            throw new BadCredentialsException("登录已超时，请重新登录");
        } else if (AdminCache.Status.KIC_OUT.equals(adminCache.getStatus())) {
            // 判断登录缓存状态是否异常，如果异常，则删除登录缓存，并返回异常描述
            try {
                this.adminCacheRedisService.delete(tokenKey);
            } catch (Exception exception) {
                throw new InternalAuthenticationServiceException("服务器异常", exception);
            }
            throw new BadCredentialsException(adminCache.getDescription());
        }

        // 获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        // 从request中获取ip和校验
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String ip = IP.getIP(request);
            if (null != adminCache.getIp() && !adminCache.getIp().isEmpty()
                    && this.securityProperties.isValidateIp() && !adminCache.getIp().equals(ip)) {
                throw new BadCredentialsException("ip不合法，请重新登录");
            }
        }

        // 重置token有效时长
        this.adminCacheRedisService.expire(tokenKey, this.securityProperties.getSessionTimeout(), TimeUnit.MINUTES);

        // 设置用户信息
        TokenAuthentication tokenAuthentication = adminCache.toTokenAuthentication();
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
