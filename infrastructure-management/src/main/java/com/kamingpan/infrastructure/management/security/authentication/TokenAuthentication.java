package com.kamingpan.infrastructure.management.security.authentication;

import com.kamingpan.infrastructure.core.security.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * token认证
 *
 * @author kamingpan
 * @since 2019-01-09
 */
@Slf4j
public class TokenAuthentication extends AbstractAuthenticationToken {

    private UserDetail userDetail;

    public TokenAuthentication(String token) {
        super(null);

        UserDetail userDetail = new UserDetail();
        userDetail.setToken(token);
        this.userDetail = userDetail;
    }

    public TokenAuthentication(String userId, String token, String username, String fullName, String portrait,
                               Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        super.setAuthenticated(true);

        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(userId);
        userDetail.setToken(token);
        userDetail.setUsername(username);
        userDetail.setFullName(fullName);
        userDetail.setPortrait(portrait);
        this.userDetail = userDetail;
    }

    @Override
    public Object getCredentials() {
        return null == this.userDetail ? null : userDetail.getToken();
    }

    @Override
    public Object getPrincipal() {
        return null == this.userDetail ? null : userDetail.getUserId();
    }

    @Override
    public Object getDetails() {
        return this.userDetail;
    }

}
