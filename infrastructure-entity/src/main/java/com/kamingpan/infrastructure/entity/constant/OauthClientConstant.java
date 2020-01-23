package com.kamingpan.infrastructure.entity.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 授权客户端常量
 *
 * @author kamingpan
 * @since 2019-04-15
 */
public class OauthClientConstant {

    private static Map<String, String> fieldMap;

    /**
     * 字段解析
     *
     * @return 字段解析
     */
    public static Map<String, String> getFieldMap() {
        if (null != OauthClientConstant.fieldMap) {
            return OauthClientConstant.fieldMap;
        }

        OauthClientConstant.fieldMap = new HashMap<String, String>();
        OauthClientConstant.fieldMap.put("name", "客户端名称");
        OauthClientConstant.fieldMap.put("clientId", "客户端id");
        OauthClientConstant.fieldMap.put("clientSecret", "客户端密钥");
        return OauthClientConstant.fieldMap;
    }

    /**
     * 权限字符串
     */
    public static final class Authentication {
        /**
         * 列表
         */
        public static final String LIST = "hasPermission('', '" + "oauth-client:list" + "')";
        /**
         * 详情
         */
        public static final String INFO = "hasPermission('', '" + "oauth-client:info" + "')";
        /**
         * 新增
         */
        public static final String INSERT = "hasPermission('', '" + "oauth-client:insert" + "')";
        /**
         * 修改
         */
        public static final String UPDATE = "hasPermission('', '" + "oauth-client:update" + "')";
        /**
         * 删除
         */
        public static final String DELETE = "hasPermission('', '" + "oauth-client:delete" + "')";
    }

}
