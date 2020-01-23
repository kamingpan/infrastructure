package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;
import com.kamingpan.infrastructure.core.util.file.FileStorage;

/**
 * 上传文件
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@TableName("system_upload_file")
public class UploadFile extends BaseEntity<UploadFile> implements FileStorage {

    /**
     * 关键字
     */
    private String keyWord;

    /**
     * 所属对象
     */
    private String belong;

    /**
     * 所属主键
     */
    private String belongId;

    /**
     * 所属字段
     */
    private String belongVariable;

    /**
     * 文件名
     */
    private String filename;

    /**
     * 文件类型
     */
    private Integer type;

    /**
     * 文件内容类型
     */
    private String contentType;

    /**
     * 文件大小（单位：K）
     */
    private Long size;

    /**
     * 链接
     */
    private String url;

    /**
     * 分组（FastDFS专用）
     */
    private String theGroup;

    /**
     * 路径
     */
    private String path;

    /**
     * 备注
     */
    private String remark;

    public UploadFile() {
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getBelong() {
        return belong;
    }

    public void setBelong(String belong) {
        this.belong = belong;
    }

    public String getBelongId() {
        return belongId;
    }

    public void setBelongId(String belongId) {
        this.belongId = belongId;
    }

    public String getBelongVariable() {
        return belongVariable;
    }

    public void setBelongVariable(String belongVariable) {
        this.belongVariable = belongVariable;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTheGroup() {
        return theGroup;
    }

    public void setTheGroup(String theGroup) {
        this.theGroup = theGroup;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "UploadFile {" +
        "keyWord=" + keyWord +
        ", belong=" + belong +
        ", belongId=" + belongId +
        ", belongVariable=" + belongVariable +
        ", filename=" + filename +
        ", type=" + type +
        ", contentType=" + contentType +
        ", size=" + size +
        ", url=" + url +
        ", theGroup=" + theGroup +
        ", path=" + path +
        ", remark=" + remark +
        "}";
    }

}
