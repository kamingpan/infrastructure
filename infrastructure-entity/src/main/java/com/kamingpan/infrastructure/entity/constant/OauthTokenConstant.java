package com.kamingpan.infrastructure.entity.constant;

/**
 * 授权token常量
 *
 * @author kamingpan
 * @since  2016-12-09
 */
public class OauthTokenConstant {

    /**
     * 有效时期：秒
     */
    public static final class ValidityTime {
        /**
         * 1小时
         */
        public static final long ONE_HOURS = 60L * 60L;
        /**
         * 2小时
         */
        public static final long TWO_HOURS = 60L * 60L * 2L;
        /**
         * 12小时
         */
        public static final long TWELVE_HOURS = 60L * 60L * 12L;
        /**
         * 30天（一个月）
         */
        public static final long THIRTY_DAYS = 60L * 60L * 24L * 30L;
    }

    /**
     * 秒
     */
    public static final class Second {
        /**
         * 一千秒
         */
        public static final long THOUSAND = 1000L;
    }

}
