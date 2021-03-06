package com.kamingpan.infrastructure.mini.program.security.filter;

import com.kamingpan.infrastructure.mini.program.security.authentication.TokenAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token认证过滤器
 *
 * @author kamingpan
 * @since 2019-04-16
 */
@Slf4j
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String TOKEN_KEY = "token";

    public TokenAuthenticationFilter(RequestMatcher requestMatcher, AuthenticationFailureHandler failureHandler) {
        // 设置请求匹配路径
        super(requestMatcher);

        // 设置验证异常处理
        super.setAuthenticationFailureHandler(failureHandler);

        // 设置验证成功后继续走完后续流程
        super.setContinueChainBeforeSuccessfulAuthentication(true);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException, AuthenticationException {

        // 从请求头中获取token，如果不存在，则从参数中获取
        String token = request.getHeader(TokenAuthenticationFilter.TOKEN_KEY);
        if (null == token || token.trim().isEmpty()) {
            token = request.getParameter(TokenAuthenticationFilter.TOKEN_KEY);
        }

        // 创建token对象和返回
        TokenAuthentication tokenAuthentication = new TokenAuthentication(token);
        tokenAuthentication.setDetails(super.authenticationDetailsSource.buildDetails(request));

        return this.getAuthenticationManager().authenticate(tokenAuthentication);
    }

}
