package com.kamingpan.infrastructure.core.properties;

import com.kamingpan.infrastructure.core.base.enumeration.UploadFileStorageTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 系统配置
 *
 * @author kamingpan
 * @since 2018-06-13
 */
@Data
@ConfigurationProperties(prefix = SystemProperties.SYSTEM_PREFIX)
public class SystemProperties {

    static final String SYSTEM_PREFIX = "system";

    /**
     * 是否初始化默认数据库脚本
     */
    private boolean initDefaultSql = false;

    /**
     * 是否初始化自定义数据库脚本
     */
    private boolean initCustomSql = false;

    /**
     * 自定义数据库脚本路径(多脚本用,间隔)
     */
    private String customSqlPath;

    /**
     * 是否允许跨域
     */
    private boolean crossDomainAllowed = false;

    /**
     * 是否重定向到https（tomcat内置重定向）
     */
    private boolean redirectToHttps = false;

    /**
     * 接口超时警告（默认：5000，单位：毫秒）
     */
    private long timeoutWarning = 5000;

    /**
     * 管理员初始化密码
     */
    private String adminInitPassword = "12345";

    /**
     * 上传文件存储方式（local：本地存储，fast_dfs：FastDFS服务器存储，后续可能持续增加）
     */
    private UploadFileStorageTypeEnum uploadFileStorageType = UploadFileStorageTypeEnum.LOCAL;

    /**
     * 上传文件存储目录
     */
    private String uploadFileDirectory;

    /**
     * 上传文件请求目录
     */
    private String uploadFileUrl;

    /**
     * id生成配置
     * 当有多个web服务器时，worker_id应该设置为不同
     */
    private Long workerId = 0L;

    /**
     * 数据中心id
     */
    private Long dataCenterId = 0L;

}
