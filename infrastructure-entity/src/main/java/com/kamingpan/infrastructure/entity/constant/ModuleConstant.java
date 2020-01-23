package com.kamingpan.infrastructure.entity.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 模块常量
 *
 * @author kamingpan
 * @since 2017-02-27
 */
public class ModuleConstant {

    private static Map<String, String> fieldMap;

    public static final String CLASS_STRING = "Module";

    /**
     * 字段解析
     *
     * @return 字段解析
     */
    public static Map<String, String> getFieldMap() {
        if (null != ModuleConstant.fieldMap) {
            return ModuleConstant.fieldMap;
        }

        ModuleConstant.fieldMap = new HashMap<String, String>();
        ModuleConstant.fieldMap.put("name", "模块名称");
        ModuleConstant.fieldMap.put("icon", "图标");
        ModuleConstant.fieldMap.put("type", "路由类型");
        ModuleConstant.fieldMap.put("url", "目标链接地址");
        ModuleConstant.fieldMap.put("route", "路由路径");
        ModuleConstant.fieldMap.put("urlPrefix", "链接url前缀");
        ModuleConstant.fieldMap.put("sort", "排序");
        ModuleConstant.fieldMap.put("superior", "上级模块");
        ModuleConstant.fieldMap.put("haveSubordinate", "是否有下级");
        ModuleConstant.fieldMap.put("status", "状态");
        ModuleConstant.fieldMap.put("remark", "备注");
        return ModuleConstant.fieldMap;
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
     * 是否有下级
     */
    public static final class HaveSubordinate {
        /**
         * 无下级
         */
        public static final Boolean HAVE_NOT_SUBORDINATE = false;

        /**
         * 有下级
         */
        public static final Boolean HAVE_SUBORDINATE = true;

    }

    /**
     * 路由类型
     */
    public static final class Type {
        /**
         * load
         */
        public static final String LOAD = "load";
        /**
         * iframe
         */
        public static final String IFRAME = "iframe";
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
         * 路由类型
         */
        public static final String TYPE = "type";
    }

    /**
     * 权限字符串
     */
    public static final class Authentication {
        /**
         * 列表
         */
        public static final String LIST = "hasPermission('', '" + "module:list" + "')";
        /**
         * 详情
         */
        public static final String INFO = "hasPermission('', '" + "module:info" + "')";
        /**
         * 新增
         */
        public static final String INSERT = "hasPermission('', '" + "module:insert" + "')";
        /**
         * 修改
         */
        public static final String UPDATE = "hasPermission('', '" + "module:update" + "')";
        /**
         * 删除
         */
        public static final String DELETE = "hasPermission('', '" + "module:delete" + "')";
        /**
         * 启用
         */
        public static final String ENABLE = "hasPermission('', '" + "module:enable" + "')";
        /**
         * 禁用
         */
        public static final String DISABLE = "hasPermission('', '" + "module:disable" + "')";
        /**
         * 权限关联
         */
        public static final String PERMISSION = "hasPermission('', '" + "module:permission" + "')";
    }

}
