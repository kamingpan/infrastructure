package com.kamingpan.infrastructure.management.security.provider;

import com.kamingpan.infrastructure.core.base.service.RedisService;
import com.kamingpan.infrastructure.entity.constant.AdminConstant;
import com.kamingpan.infrastructure.entity.constant.AdminLoginLogConstant;
import com.kamingpan.infrastructure.entity.model.entity.Admin;
import com.kamingpan.infrastructure.entity.model.entity.AdminLoginLog;
import com.kamingpan.infrastructure.entity.service.AdminLoginLogService;
import com.kamingpan.infrastructure.entity.service.AdminService;
import com.kamingpan.infrastructure.entity.service.PermissionService;
import com.kamingpan.infrastructure.management.security.cache.AdminCache;
import com.kamingpan.infrastructure.management.security.properties.SecurityProperties;
import com.kamingpan.infrastructure.util.ip.IP;
import com.kamingpan.infrastructure.util.md5.MD5;
import lombok.extern.slf4j.Slf4j;
import org.apache.oltu.oauth2.as.issuer.OAuthIssuer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 登录认证 Provider
 *
 * @author kamingpan
 * @since 2019-01-08
 */
@Slf4j
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

    public static final String REDIS_ADMIN_KEY = "MANAGEMENT:ADMIN:%s";

    private static final String REDIS_ADMIN_PASSWORD_RETRY_KEY = "MANAGEMENT:ADMIN:PASSWORD_RETRY:%s";

    @Autowired
    private OAuthIssuer oAuthIssuer;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PermissionService permissionService;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private AdminLoginLogService adminLoginLogService;

    @Autowired
    private RedisService<String, Integer> integerRedisService;

    @Autowired
    private RedisService<String, AdminCache> adminCacheRedisService;

    @Autowired
    private RedisService<String, ArrayList<String>> listRedisService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 用户名去空格并转换成小写
        String username = null == authentication.getPrincipal()
                ? null : String.valueOf(authentication.getPrincipal()).trim().toLowerCase();
        String password = null == authentication.getCredentials()
                ? null : String.valueOf(authentication.getCredentials()).trim();

        if (null == username || username.isEmpty()) {
            throw new UsernameNotFoundException("用户名不能为空");
        } else if (null == password || password.isEmpty()) {
            throw new BadCredentialsException("密码不能为空");
        }

        // 根据用户名查询用户
        Admin admin = this.adminService.getByUsername(username);

        // 验证账户是否存在
        if (null == admin) {
            throw new UsernameNotFoundException("用户不存在，请确认后重新输入");
        }

        // 获取request
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        // 定义ip变量
        String ip = null;

        // 创建登录日志对象及获取相应信息
        AdminLoginLog adminLoginLog = new AdminLoginLog();
        adminLoginLog.initId(); // 手动赋值id
        adminLoginLog.setAdminId(admin.getId());
        adminLoginLog.setLoginTime(new Date());
        adminLoginLog.setStatus(AdminLoginLogConstant.Status.FAIL);

        // 从request中获取相关信息
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

            // 设置设备信息
            adminLoginLog.setDevice(request.getParameter("device"));
            // 设置浏览器信息
            adminLoginLog.setBrowser(request.getParameter("browser"));

            // 设置ip等信息
            ip = IP.getIP(request);
            adminLoginLog.setIp(ip);
            if (null != ip && !ip.isEmpty()) {
                adminLoginLog.setAddress(IP.getAddress(ip));
                // adminLoginLog.setMac(IP.getMac(ip));
            }
        }

        // 验证状态
        if (null == admin.getStatus() || AdminConstant.Status.DELETED.equals(admin.getStatus())) {
            String message = "该用户不存在，请联系管理员确认账号信息";
            adminLoginLog.setResult(message);
            this.adminLoginLogService.insert(adminLoginLog, false);
            throw new UsernameNotFoundException(message);
        }
        if (AdminConstant.Status.DISABLE.equals(admin.getStatus())) {
            String message = "该用户已被禁用，请联系管理员";
            adminLoginLog.setResult(message);
            this.adminLoginLogService.insert(adminLoginLog, false);
            throw new DisabledException(message);
        }

        // 判断是否限制ip登录
        if (null != ip && null != admin.getRestrictIp() && !admin.getRestrictIp().isEmpty()
                && !ip.equals(admin.getRestrictIp())) {
            String message = "用户限制了ip登录，登录ip为“" + ip + "”，不符合登录限制";
            adminLoginLog.setResult(message);
            this.adminLoginLogService.insert(adminLoginLog, false);
            throw new DisabledException(message);
        }

        // 验证MD5加密密码
        String md5Password = password + AdminConstant.Password.LEFT_BRACKET + username + AdminConstant.Password.RIGHT_BRACKET;
        try {
            md5Password = MD5.encryption(md5Password);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException exception) {
            String message = "MD5加密错误，请重新输入";
            adminLoginLog.setResult(message);
            this.adminLoginLogService.insert(adminLoginLog, false);
            throw new InternalAuthenticationServiceException(message);
        }

        // 判断密码输错次数（从redis中缓存和获取）
        String loginErrorKey = String.format(LoginAuthenticationProvider.REDIS_ADMIN_PASSWORD_RETRY_KEY, admin.getId());
        int loginErrorCount = 0;
        try {
            Integer loginError = this.integerRedisService.get(loginErrorKey);
            if (null != loginError) {
                loginErrorCount = loginError;
            }
        } catch (Exception exception) {
            throw new InternalAuthenticationServiceException("服务器异常", exception);
        }

        // 如果登录错误次数到达指定错误次数，则不允许登录
        if (loginErrorCount >= this.securityProperties.getMaxLoginError()) {
            String message = "您今天登录错误次数达到" + this.securityProperties.getMaxLoginError() + "次，请明天再尝试";
            adminLoginLog.setResult(message);
            this.adminLoginLogService.insert(adminLoginLog, false);
            throw new BadCredentialsException(message);
        }

        // 验证密码
        if (!md5Password.equals(admin.getPassword())) {
            // 登录错误次数+1并修改至缓存中，返回错误提示
            loginErrorCount++;
            this.integerRedisService.set(loginErrorKey, loginErrorCount,
                    this.securityProperties.getLoginErrorTimeout(), TimeUnit.MINUTES);
            if (loginErrorCount == this.securityProperties.getMaxLoginError()) {
                String message = "您今天登录错误次数达到" + this.securityProperties.getMaxLoginError() + "次，请明天再尝试";
                adminLoginLog.setResult(message);
                this.adminLoginLogService.insert(adminLoginLog, false);
                throw new BadCredentialsException(message);
            }

            // 响应错误信息
            String message = "密码错误，今天还有" + (this.securityProperties.getMaxLoginError() - loginErrorCount) + "次尝试机会";
            adminLoginLog.setResult(message);
            this.adminLoginLogService.insert(adminLoginLog, false);
            throw new BadCredentialsException(message);

            /*String message = "密码错误，请重新输入";
            adminLoginLog.setResult(message);
            this.adminLoginLogService.insert(adminLoginLog, false);
            throw new BadCredentialsException(message);*/
        }

        // 如果登录成功，则清除登录错误次数的缓存
        this.integerRedisService.delete(loginErrorKey);

        // 创建token，更新登录缓存
        String token;
        try {
            // 查询权限列表
            List<String> permissions = !AdminConstant.Username.SUPER_ADMIN.equals(admin.getUsername())
                    ? this.permissionService.listAuthenticationByAdminId(admin.getId())
                    : this.permissionService.listAuthenticationBySuperAdmin();

            token = this.oAuthIssuer.accessToken();
            this.addLoginSession(admin, token, permissions, ip);
        } catch (Exception exception) {
            throw new InternalAuthenticationServiceException("服务器异常", exception);
        }

        // 保存登录成功的登录日志
        adminLoginLog.setResult("登录成功");
        adminLoginLog.setStatus(AdminLoginLogConstant.Status.SUCCESS);
        this.adminLoginLogService.insert(adminLoginLog, false);

        return new UsernamePasswordAuthenticationToken(token, password);
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
     * @param admin       管理员
     * @param token       登录令牌
     * @param permissions 权限列表
     * @param ip          登录ip
     */
    private synchronized void addLoginSession(Admin admin, String token, List<String> permissions, String ip) {
        // 根据主键查询当前已登录的该管理员token列表
        String adminKey = String.format(LoginAuthenticationProvider.REDIS_ADMIN_KEY, admin.getId());
        ArrayList<String> newTokens = new ArrayList<String>();

        // 遍历并移除已失效的登录管理员
        ArrayList<String> oldTokens = this.listRedisService.get(adminKey);
        if (null != oldTokens) {
            for (String tokenTemp : oldTokens) {
                // 根据token查询登录缓存
                AdminCache adminCacheTemp = this.adminCacheRedisService.get(
                        String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, tokenTemp));

                // 如果token为key的值存在，是该用户且登录状态为正常，则把该token添加到新token列表中
                if (null != adminCacheTemp && admin.getId().equals(adminCacheTemp.getAdminId())
                        && AdminCache.Status.NORMAL.equals(adminCacheTemp.getStatus())) {
                    newTokens.add(tokenTemp);
                }
            }
        }

        // 如果当前token列表长度大于最大会话个数，则从列表头开始移除token信息，并从redis中标记该token被移除
        while (newTokens.size() >= this.securityProperties.getMaxLoginSession()) {
            String tokenKey = String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, newTokens.get(0));
            newTokens.remove(0);

            // 从redis中标记该token被移除，并记录移除描述
            AdminCache adminCache = this.adminCacheRedisService.get(tokenKey);
            if (null != adminCache) {
                adminCache.setStatus(AdminCache.Status.KIC_OUT);
                adminCache.setDescription("您的账号在其它地方被登录(ip:" + ip
                        + ")，若非您本人操作，建议您尽快修改密码。");
                this.adminCacheRedisService.set(tokenKey, adminCache);
            }
        }

        // 把当前token添加到该用户的token列表中，并缓存到redis中
        newTokens.add(token);
        this.listRedisService.set(adminKey, newTokens);

        // 把token作为key，用户缓存作为值缓存到redis中
        AdminCache adminCache = new AdminCache(admin.getId(), token, admin.getUsername(), admin.getFullName(),
                admin.getPortrait(), permissions, ip);
        this.adminCacheRedisService.set(String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, token),
                adminCache, this.securityProperties.getSessionTimeout(), TimeUnit.MINUTES);
    }

}
