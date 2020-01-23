package com.kamingpan.infrastructure.entity.model.vo;

import lombok.Data;

/**
 * 上传文件 VO
 *
 * @author kamingpan
 * @since 2019-03-26
 */
@Data
public class UploadFileVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 链接
     */
    private String url;

    public UploadFileVO(String id, String filename, String url) {
        this.id = id;
        this.filename = filename;
        this.url = url;
    }

}
