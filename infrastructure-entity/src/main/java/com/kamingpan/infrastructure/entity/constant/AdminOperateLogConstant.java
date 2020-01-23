package com.kamingpan.infrastructure.entity.constant;

/**
 * 管理端操作日志常量
 *
 * @author kamingpan
 * @since 2017-09-04
 */
public class AdminOperateLogConstant {

    /**
     * 对象
     */
    public static final class Belong {
        /**
         * 管理员
         */
        public static final String ADMIN = "Admin";
        /**
         * 数据字典
         */
        public static final String DATA_DICTIONARY = "DataDictionary";
        /**
         * 模块
         */
        public static final String MODULE = "Module";
        /**
         * 权限
         */
        public static final String PERMISSION = "Permission";
        /**
         * 角色
         */
        public static final String ROLE = "Role";
        /**
         * 管理端信息
         */
        public static final String MANAGEMENT_INFORMATION = "ManagementInformation";
        /**
         * 用户
         */
        public static final String USER = "User";
        /**
         * 注册用户
         */
        public static final String REGISTERED_USER = "RegisteredUser";
        /**
         * 公众号用户
         */
        public static final String SUBSCRIPTION_USER = "SubscriptionUser";
        /**
         * 小程序用户
         */
        public static final String MINI_PROGRAM_USER = "MiniProgramUser";
        /**
         * 授权客户端
         */
        public static final String OAUTH_CLIENT = "OauthClient";
        /**
         * 配置信息
         */
        public static final String CONFIGURATION = "Configuration";
    }

    /**
     * 类型
     */
    public static final class Type {
        /**
         * 登录
         */
        public static final String LOGIN = "登录";
        /**
         * 登出
         */
        public static final String LOGOUT = "登出";
        /**
         * 新增
         */
        public static final String INSERT = "新增";
        /**
         * 修改
         */
        public static final String UPDATE = "修改";
        /**
         * 删除
         */
        public static final String DELETE = "删除";
        /**
         * 启用
         */
        public static final String ENABLE = "启用";
        /**
         * 禁用
         */
        public static final String DISABLE = "禁用";
        /**
         * 发布
         */
        public static final String PUBLISH = "发布";
        /**
         * 撤销
         */
        public static final String REVOKE = "撤销";
        /**
         * 其它
         */
        public static final String OTHER = "other";
    }

}
