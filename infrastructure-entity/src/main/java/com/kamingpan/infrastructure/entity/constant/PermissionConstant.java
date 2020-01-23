package com.kamingpan.infrastructure.entity.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 权限常量
 *
 * @author kamingpan
 * @since 2017-02-27
 */
public class PermissionConstant {

    private static Map<String, String> fieldMap;

    public static final String CLASS_STRING = "Permission";

    /**
     * 字段解析
     *
     * @return 字段解析
     */
    public static Map<String, String> getFieldMap() {
        if (null != PermissionConstant.fieldMap) {
            return PermissionConstant.fieldMap;
        }

        PermissionConstant.fieldMap = new HashMap<String, String>();
        PermissionConstant.fieldMap.put("name", "权限名称");
        PermissionConstant.fieldMap.put("module_id", "所属模块");
        PermissionConstant.fieldMap.put("superior", "上级权限");
        PermissionConstant.fieldMap.put("icon", "图标");
        PermissionConstant.fieldMap.put("authentication", "权限字符串");
        PermissionConstant.fieldMap.put("sort", "排序");
        PermissionConstant.fieldMap.put("method", "请求方法");
        PermissionConstant.fieldMap.put("status", "状态");
        PermissionConstant.fieldMap.put("remark", "备注");
        return PermissionConstant.fieldMap;
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
     * 方法
     */
    public static final class Method {
        /**
         * get
         */
        public static final String GET = "get";

        /**
         * post
         */
        public static final String POST = "post";

        /**
         * delete
         */
        public static final String DELETE = "delete";

        /**
         * put
         */
        public static final String PUT = "put";

        /**
         * patch
         */
        public static final String PATCH = "patch";
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
     * 图标
     */
    public static final class Icon {
        /**
         * 默认图标
         */
        public static final String DEFAULT = "fa-globe";
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
         * 方法
         */
        public static final String METHOD = "method";
    }

    /**
     * 权限字符串
     */
    public static final class Authentication {
        /**
         * 列表
         */
        public static final String LIST = "hasPermission('', '" + "permission:list" + "')";
        /**
         * 详情
         */
        public static final String INFO = "hasPermission('', '" + "permission:info" + "')";
        /**
         * 新增
         */
        public static final String INSERT = "hasPermission('', '" + "permission:insert" + "')";
        /**
         * 修改
         */
        public static final String UPDATE = "hasPermission('', '" + "permission:update" + "')";
        /**
         * 删除
         */
        public static final String DELETE = "hasPermission('', '" + "permission:delete" + "')";
        /**
         * 角色关联
         */
        public static final String ROLE = "hasPermission('', '" + "permission:role" + "')";
    }

}
