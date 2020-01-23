package com.kamingpan.infrastructure.mini.program.controller;

import cn.binarywang.wx.miniapp.bean.WxMaPhoneNumberInfo;
import com.kamingpan.infrastructure.core.base.service.RedisService;
import com.kamingpan.infrastructure.core.base.service.StringRedisService;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.core.security.UserDetail;
import com.kamingpan.infrastructure.entity.service.DataDictionaryService;
import com.kamingpan.infrastructure.entity.service.MiniProgramUserService;
import com.kamingpan.infrastructure.mini.program.security.cache.UserCache;
import com.kamingpan.infrastructure.mini.program.security.properties.SecurityProperties;
import com.kamingpan.infrastructure.mini.program.security.provider.LoginAuthenticationProvider;
import com.kamingpan.infrastructure.mini.program.security.provider.TokenAuthenticationProvider;
import com.kamingpan.infrastructure.mini.program.util.UserUtil;
import com.kamingpan.infrastructure.mini.program.wechat.entity.UnifiedDecryption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 系统controller
 *
 * @author kamingpan
 * @since 2019-04-18
 */
@Slf4j
@RestController
@RequestMapping("/system")
public class SystemController extends MiniProgramBaseController {

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private StringRedisService stringRedisService;

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Autowired
    private MiniProgramUserService miniProgramUserService;

    @Autowired
    private RedisService<String, UserCache> userCacheRedisService;

    /**
     * 登录
     *
     * @param code        微信端重定向后携带的code授权码
     * @param redirectURL 登录成功后重定向地址
     * @param state       微信端重定向后携带类型参数（0：用户授权，1：刷新token）
     * @param request     请求
     * @param response    响应
     * @throws IOException io异常
     */
    // @GetMapping("/login")
    @Deprecated
    public ResponseData login(@RequestParam("code") String code, @RequestParam("state") String state,
                              @RequestParam(value = "redirect_url", required = false) String redirectURL,
                              HttpServletRequest request, HttpServletResponse response) throws IOException {
        return ResponseData.success();
    }

    /**
     * 未登录响应
     *
     * @param referer  发起请求页面
     * @param request  请求
     * @param response 响应
     * @return 响应数据
     */
    @RequestMapping("/to-login")
    public ResponseData toLogin(@RequestHeader(value = "referer", required = false) String referer,
                                HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("bad credentials", "not login");
        return ResponseData.build(ResponseStatus.NOT_LOGIN);
    }

    /**
     * 更新小程序用户信息
     *
     * @param unifiedDecryption 解密信息
     * @return 响应数据
     */
    // @PostMapping("/update-user")
    @Deprecated
    public ResponseData updateUser(@ModelAttribute @Validated(UnifiedDecryption.ValidateClass.class) UnifiedDecryption unifiedDecryption) {
        return ResponseData.success();
    }

    /**
     * 更新小程序用户手机号
     *
     * @param unifiedDecryption 解密信息
     * @return 响应数据
     */
    @PostMapping("/bind-mobile")
    public ResponseData bindMobile(@ModelAttribute @Validated(UnifiedDecryption.ValidateClass.class) UnifiedDecryption unifiedDecryption) {
        // 获取当前登录用户信息
        UserDetail userDetail = UserUtil.getUser();
        if (null == userDetail) {
            return ResponseData.build(ResponseStatus.NOT_LOGIN);
        }

        // 解密
        WxMaPhoneNumberInfo wxMaPhoneNumberInfo = this.wxMaService.getUserService().getPhoneNoInfo(
                userDetail.getSessionKey(), unifiedDecryption.getEncryptedData(), unifiedDecryption.getIv());
        if (null == wxMaPhoneNumberInfo) {
            return ResponseData.build(ResponseStatus.VALIDATE_ERROR, "用户手机号解密失败");
        }

        // 更新小程序用户手机号码
        this.miniProgramUserService.updateMobile(UserUtil.getUserId(), wxMaPhoneNumberInfo.getPurePhoneNumber());
        return ResponseData.success();
    }

    /**
     * 更新登录缓存（暂时注释掉该段代码）
     *
     * @param newUserId 新用户主键
     * @param oldUserId 旧用户主键
     */
    @Deprecated
    private void updateLoginSession(String newUserId, String oldUserId) {
        // 获取当前小程序原来用户的token，并将redis中的数据删除
        String oldUserKey = String.format(LoginAuthenticationProvider.REDIS_USER_KEY, oldUserId);
        String token = this.stringRedisService.get(oldUserKey);
        this.stringRedisService.delete(oldUserKey);

        // 将新用户主键原来的token失效，并替换为当前登录的token
        String newUserKey = String.format(LoginAuthenticationProvider.REDIS_USER_KEY, newUserId);
        String userOldToken = this.stringRedisService.get(newUserKey); // 获取当前新用户的原token
        String oldTokenKey = String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, userOldToken);
        this.userCacheRedisService.delete(oldTokenKey); // 删除原token对应的用户缓存
        this.stringRedisService.set(newUserKey, token); // 替换token为当前登录的token

        // 从redis中获取当前登录token对应的旧缓存信息
        String tokenKey = String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, token);
        UserCache userCache = this.userCacheRedisService.get(tokenKey);
        if (null == userCache) {
            return;
        }

        // 更新缓存的用户主键为新的用户主键，并更新到redis中
        userCache.setUserId(newUserId);
        this.userCacheRedisService.set(tokenKey, userCache,
                this.securityProperties.getSessionTimeout(), TimeUnit.MINUTES);
    }

    /**
     * 根据类和变量查询数据字典
     *
     * @param clazz    类
     * @param variable 变量
     * @return 响应数据
     */
    @GetMapping("/data-dictionary")
    public ResponseData list(@RequestParam("clazz") final String clazz,
                             @RequestParam("variable") final String variable) {
        log.debug("查询“{}”类中“{}”字段的数据字典...", clazz, variable);

        return ResponseData.build(this.dataDictionaryService.listByClazzAndVariable(clazz, variable));
    }

}
