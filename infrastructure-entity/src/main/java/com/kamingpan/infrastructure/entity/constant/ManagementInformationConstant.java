package com.kamingpan.infrastructure.entity.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 管理端信息常量
 *
 * @author kamingpan
 * @since 2017-08-31
 */
public class ManagementInformationConstant {

    private static Map<String, String> fieldMap;

    public static final String CLASS_STRING = "ManagementInformation";

    /**
     * 字段解析
     *
     * @return 字段解析
     */
    public static Map<String, String> getFieldMap() {
        if (null != ManagementInformationConstant.fieldMap) {
            return ManagementInformationConstant.fieldMap;
        }

        ManagementInformationConstant.fieldMap = new HashMap<String, String>();
        ManagementInformationConstant.fieldMap.put("name", "系统名称");
        ManagementInformationConstant.fieldMap.put("logo", "系统logo");
        ManagementInformationConstant.fieldMap.put("logoWord", "系统logo文字");
        ManagementInformationConstant.fieldMap.put("type", "显示类型");
        ManagementInformationConstant.fieldMap.put("route", "主页路由文件");
        ManagementInformationConstant.fieldMap.put("url", "主页路由地址");
        ManagementInformationConstant.fieldMap.put("version", "系统版本");
        ManagementInformationConstant.fieldMap.put("remark", "备注");
        return ManagementInformationConstant.fieldMap;
    }

    /**
     * 主键
     */
    public static final class Id {
        /**
         * 默认
         */
        public static final String DEFAULT = "0";
    }

    /**
     * 类型
     */
    public static final class Type {
        /**
         * 显示logo
         */
        public static final Integer LOGO = 1;

        /**
         * 显示文字
         */
        public static final Integer WORD = 2;
    }

    /**
     * logo
     */
    public static final class Logo {
        /**
         * 默认
         */
        public static final String DEFAULT = "/system/logo";
    }

    /**
     * 变量/字段
     */
    public static final class Variable {
        /**
         * 类型
         */
        public static final String TYPE = "type";
    }

    /**
     * 权限字符串
     */
    public static final class Authentication {
        /**
         * 详情
         */
        public static final String INFO = "hasPermission('', '" + "management-information:info" + "')";
        /**
         * 修改
         */
        public static final String UPDATE = "hasPermission('', '" + "management-information:update" + "')";
    }

}
