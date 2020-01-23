package com.kamingpan.infrastructure.entity.constant;

/**
 * 上传文件常量
 *
 * @author kamingpan
 * @since 2017-08-28
 */
public class UploadFileConstant {

    /**
     * 类型
     */
    public static final class Type {
        /**
         * 文档（未定格式）
         */
        public static final Integer FILE = 0;
        /**
         * 图片文件
         */
        public static final Integer IMAGE = 1;
    }

    /**
     * 所属对象
     */
    public static final class Belong {
        /**
         * 管理员信息
         */
        public static final String ADMIN_MESSAGE = "AdminMessage";
        /**
         * 管理端信息
         */
        public static final String MANAGEMENT_INFORMATION = "ManagementInformation";
    }

    /**
     * 文件大小（大小：K）
     */
    public static final class Size {
        /**
         * 零
         */
        public static final Long ZERO = 0L;
    }

}
