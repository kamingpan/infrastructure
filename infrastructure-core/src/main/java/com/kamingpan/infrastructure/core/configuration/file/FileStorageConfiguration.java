package com.kamingpan.infrastructure.core.configuration.file;

import com.github.tobato.fastdfs.FdfsClientConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.kamingpan.infrastructure.core.base.enumeration.UploadFileStorageTypeEnum;
import com.kamingpan.infrastructure.core.properties.SystemProperties;
import com.kamingpan.infrastructure.core.util.file.FastDFSFile;
import com.kamingpan.infrastructure.core.util.file.FileStorageFactory;
import com.kamingpan.infrastructure.core.util.file.LocalFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.IOException;

/**
 * 文件处理配置
 *
 * @author kamingpan
 * @since 2018-08-22
 */
@Slf4j
@Configuration
@Import(FdfsClientConfig.class)
@EnableConfigurationProperties({SystemProperties.class})
public class FileStorageConfiguration {

    @Autowired
    private SystemProperties systemProperties;

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    /**
     * 获取文件处理对象
     *
     * @return {@link FileStorageFactory}
     * @throws IOException io异常
     */
    @Bean
    public FileStorageFactory fileProcessFactory() throws IOException {
        // 判断配置的“上传文件存储方式”，对应返回不同的实体
        if (UploadFileStorageTypeEnum.FAST_DFS.equals(this.systemProperties.getUploadFileStorageType())) {
            return new FastDFSFile(this.systemProperties, this.fastFileStorageClient);
        }

        // 最后返回默认的本地处理
        return new LocalFile(this.systemProperties);
    }

}
