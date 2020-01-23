package com.kamingpan.infrastructure.core.base.constant;

/**
 * 定义最终常量
 *
 * @author kamingpan
 * @since 2016-07-29
 */
public class FinalConstant {

    /**
     * http请求方法
     */
    public static final class HttpRequestMethod {
        /**
         * GET方法
         */
        public static final String GET = "GET";

        /**
         * POST方法
         */
        public static final String POST = "POST";

        /**
         * DELETE方法
         */
        public static final String DELETE = "DELETE";

        /**
         * PUT方法
         */
        public static final String PUT = "PUT";

        /**
         * PATCH方法
         */
        public static final String PATCH = "PATCH";
    }

    /**
     * 字符串常量
     */
    public static final class Strings {
        /**
         * 空字符串
         */
        public static final String EMPTY = "";

        /**
         * 空数组
         */
        public static final String EMPTY_ARRAY = "[]";

        /**
         * 连字符（横杠）
         */
        public static final String HYPHEN = "-";

        /**
         * 下划线
         */
        public static final String UNDERSCORE = "_";

        /**
         * 斜杠
         */
        public static final String SLASH = "/";

        /**
         * 反斜杠
         */
        public static final String BACKSLASH = "\\";
    }

    /**
     * 数字常量
     */
    public static final class Integers {
        /**
         * 0
         */
        public static final Integer ZERO = 0;
    }

    /**
     * 字符编码常量
     */
    public static final class Charset {
        /**
         * utf-8
         */
        public static final String UTF_8 = "UTF-8";

        /**
         * gbk
         */
        public static final String GBK = "GBK";
    }

    /**
     * 关键字
     */
    public static final class Keyword {
        /**
         * 状态
         */
        public static final String STATUS = "status";
        /**
         * 状态
         */
        public static final String MESSAGE = "message";
        /**
         * 异常
         */
        public static final String EXCEPTION = "exception";

        /**
         * 操作员
         */
        public static final String OPERATOR = "operator";
    }

    /**
     * 公众号
     */
    public static final class Subscription {

        public static final String USER = "user";

        public static final String OPENID = "openid";
    }

    /**
     * scheme
     */
    public static final class Scheme {

        public static final String HTTP = "http";

        public static final String HTTPS = "https";
    }

    /**
     * 默认端口
     */
    public static final class DefaultPort {

        public static final int HTTP = 80;

        public static final int HTTPS = 443;
    }

}
