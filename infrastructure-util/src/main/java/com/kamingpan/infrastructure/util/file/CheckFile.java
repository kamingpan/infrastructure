package com.kamingpan.infrastructure.util.file;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

/**
 * 检查文件
 *
 * @author kamingpan
 * @since 2017-08-28
 */
@Slf4j
public class CheckFile {

    /**
     * 检查文件或者目录是否存在，不存在则创建
     *
     * @param filepath 文件或目录路径
     */
    public static void checkExist(String filepath, boolean isCreateFile) throws IOException {
        log.debug("检查文件或目录‘{}’是否存在", filepath);

        File file = new File(filepath);

        // 如果文件存在
        if (!file.exists()) {
            // 创建所在文件夹
            File parent = new File(file.getParent());

            // 如果文件夹不存在
            if (!parent.exists()) {
                boolean createResult = parent.mkdirs();

                // 如果需要创建文件且文件夹创建失败
                if (isCreateFile && !createResult) {
                    throw new IOException("创建" + parent.getPath() + "文件夹失败");
                }
            }
        }

        if (!file.isDirectory() && isCreateFile) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                throw new IOException("创建" + file.getPath() + "文件失败");
            }
        }
    }

}
