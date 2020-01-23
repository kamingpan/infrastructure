package com.kamingpan.infrastructure.entity.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 配置信息常量
 *
 * @author kamingpan
 * @since 2017-02-13
 */
public class ConfigurationConstant {

    private static Map<String, String> fieldMap;

    public static final String CLASS_STRING = "Configuration";

    /**
     * 字段解析
     *
     * @return 字段解析
     */
    public static Map<String, String> getFieldMap() {
        if (null != ConfigurationConstant.fieldMap) {
            return ConfigurationConstant.fieldMap;
        }

        ConfigurationConstant.fieldMap = new HashMap<String, String>();
        ConfigurationConstant.fieldMap.put("title", "标题");
        ConfigurationConstant.fieldMap.put("configKey", "配置键");
        ConfigurationConstant.fieldMap.put("configValue", "配置值");
        ConfigurationConstant.fieldMap.put("description", "描述");
        return ConfigurationConstant.fieldMap;
    }

    /**
     * 权限字符串
     */
    public static final class Authentication {
        /**
         * 列表
         */
        public static final String LIST = "hasPermission('', '" + "configuration:list" + "')";
        /**
         * 详情
         */
        public static final String INFO = "hasPermission('', '" + "configuration:info" + "')";
        /**
         * 新增
         */
        public static final String INSERT = "hasPermission('', '" + "configuration:insert" + "')";
        /**
         * 修改
         */
        public static final String UPDATE = "hasPermission('', '" + "configuration:update" + "')";
        /**
         * 删除
         */
        public static final String DELETE = "hasPermission('', '" + "configuration:delete" + "')";
    }

}
