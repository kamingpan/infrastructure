package com.kamingpan.infrastructure.subscription.security.authentication;

import com.kamingpan.infrastructure.core.security.UserDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Date;

/**
 * token认证
 *
 * @author kamingpan
 * @since 2019-04-16
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

    public TokenAuthentication(String userId, String token, String openid, String accessToken,
                               String refreshToken, Date accessTokenExpiresIn) {
        super(null);
        super.setAuthenticated(true);

        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(userId);
        userDetail.setToken(token);
        userDetail.setOpenid(openid);
        userDetail.setAccessToken(accessToken);
        userDetail.setRefreshToken(refreshToken);
        userDetail.setAccessTokenExpiresIn(accessTokenExpiresIn);
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
