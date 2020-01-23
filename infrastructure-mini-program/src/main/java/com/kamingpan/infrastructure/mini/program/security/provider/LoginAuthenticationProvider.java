package com.kamingpan.infrastructure.mini.program.security.provider;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaUserInfo;
import com.kamingpan.infrastructure.core.base.service.RedisService;
import com.kamingpan.infrastructure.entity.constant.UserConstant;
import com.kamingpan.infrastructure.entity.model.entity.MiniProgramUser;
import com.kamingpan.infrastructure.entity.service.MiniProgramUserService;
import com.kamingpan.infrastructure.mini.program.security.cache.UserCache;
import com.kamingpan.infrastructure.mini.program.security.properties.SecurityProperties;
import com.kamingpan.infrastructure.util.ip.IP;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * 登录认证 Provider
 *
 * @author kamingpan
 * @since 2019-04-16
 */
@Slf4j
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

    public static final String REDIS_USER_KEY = "MINI_PROGRAM:USER:%s";

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private OAuthIssuer oAuthIssuer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private MiniProgramUserService miniProgramUserService;

    @Autowired
    private RedisService<String, String> stringRedisService;

    @Autowired
    private RedisService<String, UserCache> userCacheRedisService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            throw new AuthenticationServiceException("服务器异常");
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 从参数中获取code授权码
        String jsCode = request.getParameter("code");
        if (null == jsCode || jsCode.trim().isEmpty()) {
            throw new BadCredentialsException("code不能为空");
        }

        // 从参数中获取消息密文
        String encryptedData = request.getParameter("encryptedData");
        if (null == encryptedData || encryptedData.trim().isEmpty()) {
            throw new BadCredentialsException("消息密文不能为空");
        }

        // 从参数中获取初始向量
        String iv = request.getParameter("iv");
        if (null == iv || iv.trim().isEmpty()) {
            throw new BadCredentialsException("初始向量不能为空");
        }

        // 通过code换取微信用户授权access_token
        WxMaJscode2SessionResult wxMaJscode2SessionResult;
        try {
            wxMaJscode2SessionResult = this.wxMaService.getUserService().getSessionInfo(jsCode);
        } catch (WxErrorException exception) {
            log.error("通过js_code换取微信用户session异常", exception);
            throw new BadCredentialsException("通过code换取微信用户session异常", exception);
        }

        // 解密用户信息
        WxMaUserInfo wxMaUserInfo = this.wxMaService.getUserService()
                .getUserInfo(wxMaJscode2SessionResult.getSessionKey(), encryptedData, iv);
        if (null == wxMaUserInfo) {
            log.error("用户信息解密失败");
            throw new BadCredentialsException("用户信息解密失败");
        }

        // 查询小程序用户是否存在
        MiniProgramUser miniProgramUser = this.miniProgramUserService.getByOpenid(wxMaJscode2SessionResult.getOpenid());
        boolean isInsert = null == miniProgramUser;
        if (isInsert) {
            miniProgramUser = new MiniProgramUser();
        }

        // 赋值个人信息
        miniProgramUser.setOpenid(wxMaJscode2SessionResult.getOpenid());
        miniProgramUser.setUnionId(wxMaJscode2SessionResult.getUnionid());

        // 设置用户信息
        miniProgramUser.setNickname(wxMaUserInfo.getNickName());
        miniProgramUser.setLanguage(wxMaUserInfo.getLanguage());
        miniProgramUser.setPortrait(wxMaUserInfo.getAvatarUrl());
        miniProgramUser.setCountry(wxMaUserInfo.getCountry());
        miniProgramUser.setProvince(wxMaUserInfo.getProvince());
        miniProgramUser.setCity(wxMaUserInfo.getCity());

        // 微信用户的性别，值为1时是男性，值为2时是女性，值为0时是未知；
        // 而本地系统的性别，值为0时是女性，值为1时是男性，值为2时是未知，因此要把微信的性别类型转换为当前系统的性别类型
        Integer gender = UserConstant.Gender.UNKNOWN;
        if ("1".equals(wxMaUserInfo.getGender())) {
            gender = UserConstant.Gender.MALE;
        } else if ("2".equals(wxMaUserInfo.getGender())) {
            gender = UserConstant.Gender.FEMALE;
        }
        miniProgramUser.setGender(gender);

        if (isInsert) {
            // 小程序用户不存在时逻辑 - 新增用户和小程序用户
            this.miniProgramUserService.insert(miniProgramUser);
        } else {
            // 小程序用户存在时逻辑 - 更新小程序用户信息
            this.miniProgramUserService.update(miniProgramUser);
        }

        // 定义ip变量
        String ip = IP.getIP(request);

        // 定义用户token
        String token;

        // 创建并更新登录缓存
        try {
            token = this.oAuthIssuer.accessToken();
            UserCache userCache = new UserCache(miniProgramUser.getUserId(), token, wxMaJscode2SessionResult.getOpenid(),
                    wxMaJscode2SessionResult.getSessionKey(), ip);
            this.addLoginSession(userCache);
        } catch (Exception exception) {
            throw new InternalAuthenticationServiceException("服务器异常", exception);
        }

        return new UsernamePasswordAuthenticationToken(token, null);
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
     * @param userCache 用户缓存
     */
    private synchronized void addLoginSession(UserCache userCache) {
        // 根据主键查询当前已登录的该用户的token信息
        String userKey = String.format(LoginAuthenticationProvider.REDIS_USER_KEY, userCache.getUserId());
        String oldToken = this.stringRedisService.get(userKey);

        // 删除旧的token登录缓存
        this.userCacheRedisService.delete(String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, oldToken));

        // 保存新的token缓存信息到redis中
        this.stringRedisService.set(userKey, userCache.getToken());

        // 把token作为key，用户缓存作为值缓存到redis中
        this.userCacheRedisService.set(String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, userCache.getToken()),
                userCache, this.securityProperties.getSessionTimeout(), TimeUnit.MINUTES);
    }

}
