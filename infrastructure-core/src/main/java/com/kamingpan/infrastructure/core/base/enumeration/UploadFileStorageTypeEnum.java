package com.kamingpan.infrastructure.core.base.enumeration;

/**
 * 上传文件存储类型
 *
 * @author kamingpan
 * @since 2018-08-22
 */
public enum UploadFileStorageTypeEnum {

    /**
     * 本地存储
     */
    LOCAL("local", "本地存储"),

    /**
     * fast-dfs存储
     */
    FAST_DFS("fast-dfs", "fast-dfs存储");

    /**
     * 存储类型
     */
    private String type;

    /**
     * 描述
     */
    private String remark;

    UploadFileStorageTypeEnum(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }

}
