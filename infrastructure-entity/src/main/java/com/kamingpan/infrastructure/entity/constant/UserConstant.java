package com.kamingpan.infrastructure.entity.constant;

/**
 * 用户常量
 *
 * @author kamingpan
 * @since 2016-12-27
 */
public class UserConstant {

    public static final String CLASS_STRING = "User";

    /**
     * 状态
     */
    public static final class Status {
        /**
         * 正常
         */
        public static final Integer ENABLE = 0;

        /**
         * 禁用
         */
        public static final Integer DISABLE = 1;
    }

    /**
     * 性别
     */
    public static final class Gender {
        /**
         * 女性
         */
        public static final Integer FEMALE = 0;

        /**
         * 男性
         */
        public static final Integer MALE = 1;

        /**
         * 未知
         */
        public static final Integer UNKNOWN = 2;
    }

    /**
     * 注册来源
     */
    public static final class RegisteredSource {
        /**
         * 系统注册
         */
        public static final Integer SYSTEM = 0;

        /**
         * 公众号授权
         */
        public static final Integer SUBSCRIPTION = 1;

        /**
         * 小程序授权
         */
        public static final Integer MINI_PROGRAM = 2;
    }

    /**
     * 变量/字段
     */
    public static final class Variable {
        /**
         * 状态
         */
        public static final String STATUS = "status";
        /**
         * 注册来源
         */
        public static final String REGISTERED_SOURCE = "registered_source";
    }

    /**
     * 权限字符串
     */
    public static final class Authentication {
        /**
         * 启用
         */
        public static final String ENABLE = "hasPermission('', '" + "user:enable" + "')";
        /**
         * 禁用
         */
        public static final String DISABLE = "hasPermission('', '" + "user:disable" + "')";
    }

}
