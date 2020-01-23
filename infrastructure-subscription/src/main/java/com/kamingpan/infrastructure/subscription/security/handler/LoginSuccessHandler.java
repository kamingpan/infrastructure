package com.kamingpan.infrastructure.subscription.security.handler;

import com.kamingpan.infrastructure.subscription.security.properties.SecurityProperties;
import com.kamingpan.infrastructure.util.uri.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 登录成功处理（废弃类，已换成代码手动处理登录逻辑）
 *
 * @author kamingpan
 * @since 2019-04-16
 */
@Slf4j
// @Component
@Deprecated
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 登录成功逻辑
     *
     * @param request        请求
     * @param response       响应
     * @param authentication 认证异常
     * @throws IOException io异常
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String userId = (String) authentication.getPrincipal();

        // 打印登录成功
        log.info("用户“{}”登录成功", userId);

        // 从从参数中重定向地址
        String redirectURL = request.getParameter("redirect_url");
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

}
