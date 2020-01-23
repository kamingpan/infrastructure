package com.kamingpan.infrastructure.core.base.constant;

/**
 * redis常量
 *
 * @author kamingpan
 * @since 2019-04-24
 */
public class RedisConstant {

    /**
     * 权限字符串
     */
    public static final class Authentication {
        /**
         * 列表
         */
        public static final String LIST = "hasPermission('', '" + "redis:list" + "')";
        /**
         * 详情
         */
        public static final String GET = "hasPermission('', '" + "redis:get" + "')";
        /**
         * 修改
         */
        public static final String UPDATE = "hasPermission('', '" + "redis:update" + "')";
        /**
         * 删除
         */
        public static final String DELETE = "hasPermission('', '" + "redis:delete" + "')";
    }

}
