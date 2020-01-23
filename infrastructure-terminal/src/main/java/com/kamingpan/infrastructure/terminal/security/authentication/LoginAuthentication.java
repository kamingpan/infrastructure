package com.kamingpan.infrastructure.terminal.security.authentication;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;

/**
 * 登录认证
 *
 * @author kamingpan
 * @since 2019-01-09
 */
@Slf4j
public class LoginAuthentication extends AbstractAuthenticationToken {

    private String token;

    private String refreshToken;

    private String username;

    public LoginAuthentication(String token, String refreshToken, String username) {
        super(null);
        this.token = token;
        this.refreshToken = refreshToken;
        this.username = username;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return this.refreshToken;
    }

    @Override
    public Object getPrincipal() {
        return this.token;
    }

    @Override
    public Object getDetails() {
        return this.username;
    }
}
