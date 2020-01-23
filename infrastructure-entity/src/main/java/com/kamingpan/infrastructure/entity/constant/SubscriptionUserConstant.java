package com.kamingpan.infrastructure.entity.constant;

/**
 * 公众号用户信息常量
 *
 * @author kamingpan
 * @since 2018-07-19
 */
public class SubscriptionUserConstant {

    public static final String CLASS_STRING = "SubscriptionUser";

    /**
     * 公众号关注状态
     */
    public static final class Subscribed {
        /**
         * 取消关注
         */
        public static final boolean UN_SUBSCRIBED = false;

        /**
         * 取消关注值
         */
        public static final int UN_SUBSCRIBED_VALUE = 0;

        /**
         * 已关注
         */
        public static final boolean SUBSCRIBED = true;

        /**
         * 已关注值
         */
        public static final int SUBSCRIBED_VALUE = 1;
    }

    /**
     * 变量/字段
     */
    public static final class Variable {
        /**
         * 性别
         */
        public static final String GENDER = "gender";
        /**
         * 公众号关注状态
         */
        public static final String SUBSCRIBED = "subscribed";
    }

    /**
     * 权限字符串
     */
    public static final class Authentication {
        /**
         * 列表
         */
        public static final String LIST = "hasPermission('', '" + "subscription-user:list" + "')";
        /**
         * 详情
         */
        public static final String INFO = "hasPermission('', '" + "subscription-user:info" + "')";
        /**
         * 删除
         */
        public static final String DELETE = "hasPermission('', '" + "subscription-user:delete" + "')";
    }

}
