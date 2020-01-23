package com.kamingpan.infrastructure.management.security.cache;

import com.kamingpan.infrastructure.management.security.authentication.TokenAuthentication;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理员缓存
 *
 * @author kamingpan
 * @since 2019-01-16
 */
@Data
public class AdminCache implements Serializable {

    /**
     * 管理员主键
     */
    private String adminId;

    /**
     * 登录token
     */
    private String token;

    /**
     * 管理员用户名
     */
    private String username;

    /**
     * 管理员姓名
     */
    private String fullName;

    /**
     * 管理员头像
     */
    private String portrait;

    /**
     * 管理员权限
     */
    private List<String> permissions;

    /**
     * 登录ip
     */
    private String ip;

    /**
     * 登录状态
     */
    private Integer status;

    /**
     * 描述
     */
    private String description;

    public AdminCache() {
    }

    public AdminCache(String adminId, String token, String username, String fullName, String portrait,
                      List<String> permissions, String ip) {
        this.adminId = adminId;
        this.token = token;
        this.username = username;
        this.fullName = fullName;
        this.portrait = portrait;
        this.permissions = permissions;
        this.ip = ip;
        this.status = AdminCache.Status.NORMAL;
    }

    public List<GrantedAuthority> toGrantedAuthority() {
        if (null == this.permissions || permissions.isEmpty()) {
            return new ArrayList<GrantedAuthority>();
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(permissions.size());
        for (String permission : this.permissions) {
            if (null == permission || permission.isEmpty()) {
                continue;
            }

            grantedAuthorities.add(new SimpleGrantedAuthority(permission));
        }
        return grantedAuthorities;
    }

    public TokenAuthentication toTokenAuthentication() {
        return new TokenAuthentication(this.adminId, this.token, this.username, this.fullName, this.portrait,
                this.toGrantedAuthority());
    }

    /**
     * 登录状态
     */
    public static final class Status {

        /**
         * 正常
         */
        public static final Integer NORMAL = 0;

        /**
         * 被移除
         */
        public static final Integer KIC_OUT = 1;
    }

}
