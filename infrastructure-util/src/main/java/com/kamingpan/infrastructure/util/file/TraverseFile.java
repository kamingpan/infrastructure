package com.kamingpan.infrastructure.util.file;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 遍历指定文件夹下面的所有文件（递归）
 *
 * @author kamingpan
 * @since 2019-08-01
 */
@Slf4j
public class TraverseFile {

    public static List<String> traverse(String path) {
        // 定义返回列表
        List<String> paths = new ArrayList<String>();

        // 获取文件
        File file = new File(path);

        // 判断文件是否存在
        if (!file.exists()) {
            log.debug("文件不存在！");
            return paths;
        }

        // 判断是否文件，如果是，把文件路径放到列表中，直接返回
        if (file.isFile()) {
            log.debug("该路径是文件！");
            paths.add(file.getAbsolutePath());
            return paths;
        }

        // 获取文件列表
        File[] files = file.listFiles();
        if (null == files || files.length == 0) {
            log.debug("该文件夹是空的！");
            return paths;
        }

        // 遍历文件夹下的所有文件
        for (File childrenFile : files) {
            // 如果子文件是文件类型，则把文件路径放到列表中，并跳到下一轮循环
            if (childrenFile.isFile()) {
                paths.add(childrenFile.getAbsolutePath());
                continue;
            }

            // 否则该子文件是路径，使用递归调用当前方法，并把返回的文件列表添加到当前列表中
            paths.addAll(TraverseFile.traverse(childrenFile.getAbsolutePath()));
        }

        return paths;
    }

}
