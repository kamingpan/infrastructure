package com.kamingpan.infrastructure.entity.constant;

/**
 * 小程序用户信息常量
 *
 * @author kamingpan
 * @since 2019-04-19
 */
public class MiniProgramUserConstant {

    public static final String CLASS_STRING = "MiniProgramUser";

    /**
     * 变量/字段
     */
    public static final class Variable {
        /**
         * 性别
         */
        public static final String GENDER = "gender";
    }

    /**
     * 权限字符串
     */
    public static final class Authentication {
        /**
         * 列表
         */
        public static final String LIST = "hasPermission('', '" + "mini-program-user:list" + "')";
        /**
         * 详情
         */
        public static final String INFO = "hasPermission('', '" + "mini-program-user:info" + "')";
        /**
         * 删除
         */
        public static final String DELETE = "hasPermission('', '" + "mini-program-user:delete" + "')";
    }

}
