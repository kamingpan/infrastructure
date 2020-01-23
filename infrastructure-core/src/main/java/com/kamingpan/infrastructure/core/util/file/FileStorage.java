package com.kamingpan.infrastructure.core.util.file;

/**
 * 文件存储接口
 *
 * @author kamingpan
 * @since 2018-08-23
 */
public interface FileStorage {

    /**
     * 获取关键字
     */
    String getKeyWord();

    /**
     * 获取文件名
     */
    String getFilename();

    /**
     * 获取文件内容类型
     */
    String getContentType();

    /**
     * 设置文件大小（单位：K）
     */
    void setSize(Long size);

    /**
     * 获取文件大小（单位：K）
     */
    Long getSize();

    /**
     * 设置请求链接
     */
    void setUrl(String url);

    /**
     * 获取分组
     */
    String getTheGroup();

    /**
     * 设置分组（FastDFS专用）
     *
     * @param theGroup 分组
     */
    void setTheGroup(String theGroup);

    /**
     * 获取路径
     */
    String getPath();

    /**
     * 设置路径
     *
     * @param path 路径
     */
    void setPath(String path);
}
