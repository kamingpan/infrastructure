package com.kamingpan.infrastructure.entity.constant;

/**
 * 管理员登录日志常量
 *
 * @author kamingpan
 * @since 2017-02-13
 */
public class AdminLoginLogConstant {

    public static final String CLASS_STRING = "AdminLoginLog";

    /**
     * 登录状态
     */
    public static final class Status {
        /**
         * 成功
         */
        public static final Integer SUCCESS = 0;

        /**
         * 失败
         */
        public static final Integer FAIL = 1;
    }

    /**
     * 变量/字段
     */
    public static final class Variable {
        /**
         * 状态
         */
        public static final String STATUS = "status";
    }

    /**
     * 权限字符串
     */
    public static final class Authentication {
        /**
         * 列表
         */
        public static final String LIST = "hasPermission('', '" + "admin-login-log:list" + "')";

        /**
         * 详情
         */
        public static final String INFO = "hasPermission('', '" + "admin-login-log:info" + "')";

        /**
         * 导出
         */
        public static final String EXPORT = "hasPermission('', '" + "admin-login-log:export" + "')";
    }

}
