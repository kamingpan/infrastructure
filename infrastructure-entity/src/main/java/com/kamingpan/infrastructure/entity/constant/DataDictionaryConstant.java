package com.kamingpan.infrastructure.entity.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据字典常量
 *
 * @author kamingpan
 * @since 2017-06-14
 */
public class DataDictionaryConstant {

    private static Map<String, String> fieldMap;

    /**
     * 字段解析
     *
     * @return 字段解析
     */
    public static Map<String, String> getFieldMap() {
        if (null != DataDictionaryConstant.fieldMap) {
            return DataDictionaryConstant.fieldMap;
        }

        DataDictionaryConstant.fieldMap = new HashMap<String, String>();
        DataDictionaryConstant.fieldMap.put("label", "标签");
        DataDictionaryConstant.fieldMap.put("value", "值");
        DataDictionaryConstant.fieldMap.put("type", "类型");
        DataDictionaryConstant.fieldMap.put("clazz", "类");
        DataDictionaryConstant.fieldMap.put("variable", "变量");
        DataDictionaryConstant.fieldMap.put("sort", "排序");
        DataDictionaryConstant.fieldMap.put("remark", "备注");
        return DataDictionaryConstant.fieldMap;
    }

    /**
     * 类型
     */
    public static final class Type {
        /**
         * 字符串
         */
        public static final String STRING = "字符串（string）";

        /**
         * 整型
         */
        public static final String INTEGER = "整型（int）";

        /**
         * 长整型
         */
        public static final String LONG = "长整型（long）";

        /**
         * 短整型
         */
        public static final String SHORT = "短整型（short）";

        /**
         * 布尔型
         */
        public static final String BOOLEAN = "布尔型（boolean）";
    }

    /**
     * 权限字符串
     */
    public static final class Authentication {
        /**
         * 列表
         */
        public static final String LIST = "hasPermission('', '" + "data-dictionary:list" + "')";
        /**
         * 详情
         */
        public static final String INFO = "hasPermission('', '" + "data-dictionary:info" + "')";
        /**
         * 新增
         */
        public static final String INSERT = "hasPermission('', '" + "data-dictionary:insert" + "')";
        /**
         * 修改
         */
        public static final String UPDATE = "hasPermission('', '" + "data-dictionary:update" + "')";
        /**
         * 删除
         */
        public static final String DELETE = "hasPermission('', '" + "data-dictionary:delete" + "')";
    }

}
