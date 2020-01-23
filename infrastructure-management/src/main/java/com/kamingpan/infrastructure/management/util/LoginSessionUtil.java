package com.kamingpan.infrastructure.management.util;

import com.kamingpan.infrastructure.core.base.service.RedisService;
import com.kamingpan.infrastructure.management.security.cache.AdminCache;
import com.kamingpan.infrastructure.management.security.provider.LoginAuthenticationProvider;
import com.kamingpan.infrastructure.management.security.provider.TokenAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * 登录缓存工具
 *
 * @author kamingpan
 * @since 2020-01-15
 */
@Slf4j
@Component
public class LoginSessionUtil {

    @Autowired
    private RedisService<String, AdminCache> adminCacheRedisService;

    @Autowired
    private RedisService<String, ArrayList<String>> listRedisService;

    /**
     * 根据管理员主键移除登录缓存
     *
     * @param adminId 管理员主键
     */
    @Async
    public void removeLoginSessionByAdminId(String adminId) {
        if (null == adminId || adminId.isEmpty()) {
            return;
        }

        log.debug("准备移除管理员“{}”的登录缓存", adminId);

        // 根据主键查询当前已登录的该管理员token列表
        String adminKey = String.format(LoginAuthenticationProvider.REDIS_ADMIN_KEY, adminId);
        ArrayList<String> tokens = this.listRedisService.get(adminKey);
        this.listRedisService.delete(adminKey); // 删除管理员缓存
        if (null != tokens) {
            for (String token : tokens) {
                // 删除对应的token缓存
                this.adminCacheRedisService.delete(String.format(TokenAuthenticationProvider.REDIS_TOKEN_KEY, token));
            }
        }

        log.debug("移除管理员“{}”的登录缓存成功", adminId);
    }

}
