package com.kamingpan.infrastructure.core.util.file;

import com.kamingpan.infrastructure.core.properties.SystemProperties;
import com.kamingpan.infrastructure.util.conversion.ImageConversion;
import com.kamingpan.infrastructure.util.uri.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;

/**
 * 本地文件处理
 *
 * @author kamingpan
 * @since 2018-08-22
 */
@Slf4j
public class LocalFile implements FileStorageFactory {

    private SystemProperties systemProperties;

    public LocalFile(SystemProperties systemProperties) {
        this.systemProperties = systemProperties;
    }

    /**
     * 保存文件
     *
     * @param multipartFile 文件内容
     * @param fileStorage   文件存储接口
     * @param request       请求
     * @throws IOException io异常
     */
    @Override
    public void save(MultipartFile multipartFile, FileStorage fileStorage, HttpServletRequest request)
            throws IOException {
        if (null == multipartFile) {
            throw new RuntimeException("文件不能为空");
        }
        if (null == fileStorage) {
            throw new RuntimeException("保存路径不能为空");
        }

        // 获取文件保存路径
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("/yyyy/MM/dd/");
        String datePath = simpleDateFormat.format(System.currentTimeMillis());
        String path = this.systemProperties.getUploadFileDirectory() + datePath + fileStorage.getKeyWord();

        // 判断文件是否存在，不存在则创建新文件
        File file = new File(path);
        if (!file.exists()) {
            if (file.isDirectory()) {
                file.mkdirs();
            } else {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        }

        // 把文件存储至文件服务器或硬盘
        multipartFile.transferTo(file);

        // 设置路径和链接，后续要持久化到数据库
        fileStorage.setPath(path);
        fileStorage.setUrl(URI.getCurrentServer(request)
                + this.systemProperties.getUploadFileUrl().replace(":id", fileStorage.getKeyWord()));
        log.debug("保存文件{}成功...", path);
    }

    /**
     * 获得文件
     *
     * @param fileStorage 文件存储接口
     * @param preview     是否预览
     * @param response    响应
     * @throws IOException io异常
     */
    @Override
    public void get(FileStorage fileStorage, boolean preview, HttpServletResponse response) throws IOException {
        File file = new File(fileStorage.getPath());
        FileInputStream fileInputStream = new FileInputStream(file);

        response.reset(); // 设置为没有缓存
        response.setContentType(null == fileStorage.getContentType() || fileStorage.getContentType().isEmpty()
                ? MediaType.APPLICATION_OCTET_STREAM_VALUE : fileStorage.getContentType());
        // response.setCharacterEncoding("UTF-8");
        if (!preview) {
            response.setHeader("Content-Disposition", "attachment;filename=" + fileStorage.getFilename());
            response.addHeader("Content-Length",
                    String.valueOf(null == fileStorage.getSize() ? 0 : fileStorage.getSize()));
        }

        OutputStream outputStream = response.getOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = fileInputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        fileInputStream.close();
        outputStream.flush();
        outputStream.close();
    }

    /**
     * 保存base64文件
     *
     * @param base64      base64文件内容
     * @param fileStorage 文件存储接口
     * @param request     请求
     * @throws IOException io异常
     */
    @Override
    public void save(String base64, FileStorage fileStorage, HttpServletRequest request) throws IOException {
        // 获取文件保存路径
        String path = this.systemProperties.getUploadFileDirectory() + "/" + fileStorage.getKeyWord();

        // 保存base64文件
        ImageConversion.base64ToImage(base64, path);

        // 设置文件大小
        File file = new File(fileStorage.getPath());
        if (file.exists() && file.isFile()) {
            fileStorage.setSize(file.length());
        } else {
            fileStorage.setSize(0L);
        }

        // 设置路径，后续要持久化到数据库
        fileStorage.setPath(path);
        fileStorage.setUrl(URI.getCurrentServer(request)
                + this.systemProperties.getUploadFileUrl().replace(":id", fileStorage.getKeyWord()));
        log.debug("保存文件{}成功...", path);
    }
}
