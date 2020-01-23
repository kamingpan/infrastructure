package com.kamingpan.infrastructure.core.util.file;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 文件存储工厂
 *
 * @author kamingpan
 * @since 2018-08-22
 */
public interface FileStorageFactory {

    /**
     * 保存文件
     *
     * @param multipartFile 文件内容
     * @param fileStorage   文件存储接口
     * @param request       请求
     * @throws IOException io异常
     */
    void save(MultipartFile multipartFile, FileStorage fileStorage, HttpServletRequest request) throws IOException;

    /**
     * 获得文件
     *
     * @param fileStorage 文件存储接口
     * @param preview     是否预览
     * @param response    响应
     * @throws IOException io异常
     */
    void get(FileStorage fileStorage, boolean preview, HttpServletResponse response) throws IOException;

    /**
     * 保存base64文件
     *
     * @param base64      base64文件内容
     * @param fileStorage 文件存储接口
     * @param request     请求
     * @throws IOException io异常
     */
    void save(String base64, FileStorage fileStorage, HttpServletRequest request) throws IOException;

}
