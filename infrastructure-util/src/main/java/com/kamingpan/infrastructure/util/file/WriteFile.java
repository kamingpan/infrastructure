package com.kamingpan.infrastructure.util.file;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * 写入文件
 *
 * @author kamingpan
 * @since 2016-10-25
 */
@Slf4j
public class WriteFile {

    /**
     * 写入内容到指定文件(BufferedWriter)
     *
     * @param filepath 文件路径
     * @param content  写入内容
     * @throws IOException io异常
     */
    public static void bufferedWriter(String filepath, String content) throws IOException {
        WriteFile.bufferedWriter(filepath, content, true);
    }

    /**
     * 写入内容到指定文件(BufferedWriter)
     *
     * @param filepath 文件路径
     * @param content  写入内容
     * @param append   是否追加
     * @throws IOException io异常
     */
    public static void bufferedWriter(String filepath, String content, boolean append) throws IOException {
        CheckFile.checkExist(filepath, true);
        File file = new File(filepath);

        FileOutputStream fileOutputStream = new FileOutputStream(file, append);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
        bufferedWriter.write(content + "\r\n");
        bufferedWriter.flush();
        bufferedWriter.close();
    }

    /**
     * 写入内容到指定文件(FileWriter)
     *
     * @param filepath 文件路径
     * @param content  写入内容
     * @throws IOException io异常
     */
    public static void fileWriter(String filepath, String content) throws IOException {
        WriteFile.fileWriter(filepath, content, true);
    }

    /**
     * 写入内容到指定文件(FileWriter)
     *
     * @param filepath 文件路径
     * @param content  写入内容
     * @param append   是否追加
     * @throws IOException io异常
     */
    public static void fileWriter(String filepath, String content, boolean append) throws IOException {
        CheckFile.checkExist(filepath, true);
        File file = new File(filepath);

        FileWriter fileWriter = new FileWriter(file, append);
        fileWriter.write(content + "\r\n");
        fileWriter.flush();
        fileWriter.close();
    }

}
