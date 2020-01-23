package com.kamingpan.infrastructure.core.util.file;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadCallback;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.kamingpan.infrastructure.core.base.constant.FinalConstant;
import com.kamingpan.infrastructure.core.properties.SystemProperties;
import com.kamingpan.infrastructure.util.uri.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

/**
 * FastDFS服务器文件处理
 *
 * @author kamingpan
 * @since 2018-08-22
 */
@Slf4j
public class FastDFSFile implements FileStorageFactory {

    private static final DownloadCallback<byte[]> DOWNLOAD_CALLBACK = new DownloadByteArray();

    private SystemProperties systemProperties;

    private FastFileStorageClient fastFileStorageClient;

    public FastDFSFile(SystemProperties systemProperties, FastFileStorageClient fastFileStorageClient) {
        this.systemProperties = systemProperties;
        this.fastFileStorageClient = fastFileStorageClient;
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
            throw new RuntimeException("文件存储不能为空");
        }

        // 获取文件名的后缀格式
        String filename = multipartFile.getOriginalFilename();
        String fileExtName = (null != filename && !filename.isEmpty())
                ? filename.substring(filename.lastIndexOf(".") + 1) : FinalConstant.Strings.EMPTY;

        // 上传文件到文件服务器
        StorePath storePath = this.fastFileStorageClient.uploadFile(multipartFile.getInputStream(),
                multipartFile.getSize(), fileExtName, null);
        if (null == storePath) {
            throw new RuntimeException("获取FastDFS服务器存储分组和路径失败");
        }

        // 设置分组和路径，后续要持久化到数据库
        fileStorage.setTheGroup(storePath.getGroup());
        fileStorage.setPath(storePath.getPath());
        fileStorage.setUrl(URI.getCurrentServer(request)
                + this.systemProperties.getUploadFileUrl().replace(":id", fileStorage.getKeyWord()));
        log.debug("保存文件{}{}成功...", storePath.getGroup(), storePath.getPath());
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
        if (null == fileStorage) {
            throw new RuntimeException("文件存储不能为空");
        }

        response.reset(); // 设置为没有缓存
        response.setContentType(null == fileStorage.getContentType() || fileStorage.getContentType().isEmpty()
                ? MediaType.APPLICATION_OCTET_STREAM_VALUE : fileStorage.getContentType());
        // response.setCharacterEncoding("UTF-8");
        if (!preview) {
            response.setHeader("Content-Disposition", "attachment;filename=" + fileStorage.getFilename());
            response.addHeader("Content-Length",
                    String.valueOf(null == fileStorage.getSize() ? 0 : fileStorage.getSize()));
        }

        // 下载文件
        OutputStream outputStream = response.getOutputStream();
        byte[] bytes = this.fastFileStorageClient.downloadFile(fileStorage.getTheGroup(),
                fileStorage.getPath(), DOWNLOAD_CALLBACK);
        outputStream.write(bytes);
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
        // base64解码
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] bytes = decoder.decode(base64);
        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] < 0) {
                // 调整异常数据
                bytes[i] += 256;
            }
        }

        // 字节数组转输入流
        InputStream inputStream = new ByteArrayInputStream(bytes);

        // 使用ByteArrayOutputStream计算获取文件大小
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byteArrayOutputStream.write(bytes);
        long fileSize = byteArrayOutputStream.size();
        byteArrayOutputStream.close();

        // 上传文件到文件服务器
        String suffix = fileStorage.getFilename().substring(fileStorage.getFilename().lastIndexOf(".") + 1);
        StorePath storePath = this.fastFileStorageClient.uploadFile(inputStream,
                fileSize, suffix, null);
        if (null == storePath) {
            throw new RuntimeException("获取FastDFS服务器存储分组和路径失败");
        }

        // 设置分组和路径，后续要持久化到数据库
        fileStorage.setSize(fileSize);
        fileStorage.setTheGroup(storePath.getGroup());
        fileStorage.setPath(storePath.getPath());
        fileStorage.setUrl(URI.getCurrentServer(request)
                + this.systemProperties.getUploadFileUrl().replace(":id", fileStorage.getKeyWord()));
        log.debug("保存文件{}{}成功...", storePath.getGroup(), storePath.getPath());
    }
}
