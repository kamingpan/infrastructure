package com.kamingpan.infrastructure.terminal.util;

import com.kamingpan.infrastructure.core.security.UserDetail;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 操作员工具
 *
 * @author kamingpan
 * @since 2017-02-28
 */
public class UserUtil {

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户
     */
    public static UserDetail getUser() {
        return (UserDetail) SecurityContextHolder.getContext().getAuthentication().getDetails();
    }

    /**
     * 获取当前登录用户主键
     *
     * @return 用户主键
     */
    public static String getUserId() {
        UserDetail userDetail = (UserDetail) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return userDetail.getUserId();
    }

    /**
     * 获取当前登录用户主键
     *
     * @param request 请求
     * @return 用户主键
     */
    public static String getUserId(HttpServletRequest request) {
        Principal principal = request.getUserPrincipal();
        if (!(principal instanceof Authentication)) {
            return null;
        }

        Authentication authentication = (Authentication) principal;
        if (!(authentication.getDetails() instanceof UserDetail)) {
            return null;
        }

        UserDetail userDetail = (UserDetail) authentication.getDetails();
        return null == userDetail ? null : userDetail.getUserId();
    }

}
