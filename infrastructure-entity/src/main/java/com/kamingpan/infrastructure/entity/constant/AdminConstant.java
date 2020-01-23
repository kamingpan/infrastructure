package com.kamingpan.infrastructure.entity.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理员常量
 *
 * @author kamingpan
 * @since 2017-02-13
 */
public class AdminConstant {

    private static Map<String, String> fieldMap;

    public static final String CLASS_STRING = "Admin";

    /**
     * 字段解析
     *
     * @return 字段解析
     */
    public static Map<String, String> getFieldMap() {
        if (null != AdminConstant.fieldMap) {
            return AdminConstant.fieldMap;
        }

        AdminConstant.fieldMap = new HashMap<String, String>();
        AdminConstant.fieldMap.put("username", "用户名");
        AdminConstant.fieldMap.put("password", "密码");
        AdminConstant.fieldMap.put("status", "状态");
        AdminConstant.fieldMap.put("fullName", "真实姓名");
        AdminConstant.fieldMap.put("phone", "手机号码");
        AdminConstant.fieldMap.put("superior", "上级管理员");
        AdminConstant.fieldMap.put("portrait", "头像");
        AdminConstant.fieldMap.put("gender", "性别");
        AdminConstant.fieldMap.put("email", "电子邮箱");
        AdminConstant.fieldMap.put("restrictIp", "限制ip");
        return AdminConstant.fieldMap;
    }

    /**
     * 用户名
     */
    public static final class Username {
        /**
         * 超级管理员
         */
        public static final String SUPER_ADMIN = "admin";
    }

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
     * 状态
     */
    public static final class Status {
        /**
         * 已删除
         */
        public static final Integer DELETED = -1;

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
     * 变量/字段
     */
    public static final class Variable {
        /**
         * 状态
         */
        public static final String STATUS = "status";

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
        public static final String LIST = "hasPermission('', '" + "admin:list" + "')";
        /**
         * 详情
         */
        public static final String INFO = "hasPermission('', '" + "admin:info" + "')";
        /**
         * 新增
         */
        public static final String INSERT = "hasPermission('', '" + "admin:insert" + "')";
        /**
         * 修改
         */
        public static final String UPDATE = "hasPermission('', '" + "admin:update" + "')";
        /**
         * 删除
         */
        public static final String DELETE = "hasPermission('', '" + "admin:delete" + "')";
        /**
         * 启用
         */
        public static final String ENABLE = "hasPermission('', '" + "admin:enable" + "')";
        /**
         * 禁用
         */
        public static final String DISABLE = "hasPermission('', '" + "admin:disable" + "')";
        /**
         * 重置密码
         */
        public static final String RESET_PASSWORD = "hasPermission('', '" + "admin:reset-password" + "')";
        /**
         * 角色分配
         */
        public static final String ROLE = "hasPermission('', '" + "admin:role" + "')";
    }

}
