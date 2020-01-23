package com.kamingpan.infrastructure.entity.constant;

/**
 * 用户常量
 *
 * @author kamingpan
 * @since 2016-12-27
 */
public class RegisteredUserConstant {

    /**
     * 密码
     */
    public static final class Password {
        /**
         * 左括号
         */
        public static final String LEFT_BRACKET = "[";
        /**
         * 右括号
         */
        public static final String RIGHT_BRACKET = "]";
    }

    /**
     * 权限字符串
     */
    public static final class Authentication {
        /**
         * 列表
         */
        public static final String LIST = "hasPermission('', '" + "registered-user:list" + "')";
        /**
         * 详情
         */
        public static final String INFO = "hasPermission('', '" + "registered-user:info" + "')";
        /**
         * 新增
         */
        public static final String INSERT = "hasPermission('', '" + "registered-user:insert" + "')";
        /**
         * 修改
         */
        public static final String UPDATE = "hasPermission('', '" + "registered-user:update" + "')";
        /**
         * 删除
         */
        public static final String DELETE = "hasPermission('', '" + "registered-user:delete" + "')";
    }

}
