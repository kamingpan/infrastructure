package com.kamingpan.infrastructure.entity.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 角色常量
 *
 * @author kamingpan
 * @since 2017-02-27
 */
public class RoleConstant {

    private static Map<String, String> fieldMap;

    public static final String CLASS_STRING = "Role";

    /**
     * 字段解析
     *
     * @return 字段解析
     */
    public static Map<String, String> getFieldMap() {
        if (null != RoleConstant.fieldMap) {
            return RoleConstant.fieldMap;
        }

        RoleConstant.fieldMap = new HashMap<String, String>();
        RoleConstant.fieldMap.put("name", "角色名称");
        RoleConstant.fieldMap.put("superior", "上级角色");
        RoleConstant.fieldMap.put("level", "级别");
        RoleConstant.fieldMap.put("status", "状态");
        RoleConstant.fieldMap.put("remark", "备注");
        return RoleConstant.fieldMap;
    }

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
     * 上级
     */
    public static final class Superior {
        /**
         * 最高级
         */
        public static final String HIGHEST = "0";
    }

    /**
     * 级别
     */
    public static final class Level {
        /**
         * 最高级
         */
        public static final Integer HIGHEST = 1;
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
        public static final String LIST = "hasPermission('', '" + "role:list" + "')";
        /**
         * 详情
         */
        public static final String INFO = "hasPermission('', '" + "role:info" + "')";
        /**
         * 新增
         */
        public static final String INSERT = "hasPermission('', '" + "role:insert" + "')";
        /**
         * 修改
         */
        public static final String UPDATE = "hasPermission('', '" + "role:update" + "')";
        /**
         * 删除
         */
        public static final String DELETE = "hasPermission('', '" + "role:delete" + "')";
        /**
         * 启用
         */
        public static final String ENABLE = "hasPermission('', '" + "role:enable" + "')";
        /**
         * 禁用
         */
        public static final String DISABLE = "hasPermission('', '" + "role:disable" + "')";
        /**
         * 权限授予
         */
        public static final String PERMISSION = "hasPermission('', '" + "role:permission" + "')";
        /**
         * 管理员关联
         */
        public static final String ADMIN = "hasPermission('', '" + "role:admin" + "')";
    }

}
