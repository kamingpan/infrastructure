package com.kamingpan.infrastructure.util.file;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * 读取文件
 *
 * @author kamingpan
 * @since 2016-10-25
 */
@Slf4j
public class ReadFile {

    /**
     * 以一个字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     *
     * @param filepath 文件路径
     * @return 文件字符串
     * @throws IOException io异常
     */
    public static String readFileByByte(String filepath) throws IOException {
        File file = new File(filepath);
        InputStream inputStream = new FileInputStream(file);
        StringBuilder stringBuilder = new StringBuilder();

        // 一次读一个字节
        int readByte;
        while ((readByte = inputStream.read()) != -1) {
            stringBuilder.append(readByte);
        }
        inputStream.close();

        return stringBuilder.toString();
    }

    /**
     * 以多个字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     *
     * @param filepath 文件路径
     * @return 文件字符串
     * @throws IOException io异常
     */
    public static String readFileByBytes(String filepath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = new FileInputStream(filepath);

        // 一次读多个字节
        byte[] tempBytes = new byte[1024];
        int readByte;

        // 读入多个字节到字节数组中，byteRead为一次读入的字节数
        while ((readByte = inputStream.read(tempBytes)) != -1) {
            stringBuilder.append(new String(tempBytes, 0, readByte));
        }
        inputStream.close();

        return stringBuilder.toString();
    }

    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     *
     * @param filepath 文件路径
     * @return 文件字符串
     * @throws IOException io异常
     */
    public static String readFileByChar(String filepath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(filepath);
        FileInputStream fileInputStream = new FileInputStream(file);
        Reader reader = new InputStreamReader(fileInputStream);

        // 一次读一个字符
        int readChar;
        while ((readChar = reader.read()) != -1) {
            // 对于windows下，\r\n这两个字符在一起时，表示一个换行。
            // 但如果这两个字符分开显示时，会换两次行。
            // 因此，屏蔽掉\r，或者屏蔽\n。否则，将会多出很多空行。
            if (((char) readChar) != '\r') {
                stringBuilder.append((char) readChar);
            }
        }
        reader.close();

        return stringBuilder.toString();
    }

    /**
     * 以字符为单位读取文件，常用于读文本，数字等类型的文件
     *
     * @param filepath 文件路径
     * @return 文件字符串
     * @throws IOException io异常
     */
    public static String readFileByChars(String filepath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        File file = new File(filepath);
        FileInputStream fileInputStream = new FileInputStream(file);
        Reader reader = new InputStreamReader(fileInputStream);

        // 一次读多个字符
        char[] tempChars = new char[1024];
        int readChar;
        // 读入多个字符到字符数组中，readChar为一次读取字符数
        while ((readChar = reader.read(tempChars)) != -1) {
            // 同样屏蔽掉\r不显示
            if ((readChar == tempChars.length) && (tempChars[tempChars.length - 1] != '\r')) {
                stringBuilder.append(tempChars);
            } else {
                for (int i = 0; i < readChar; i++) {
                    if (tempChars[i] == '\r') {
                        continue;
                    }
                    stringBuilder.append(tempChars[i]);
                }
            }
        }

        reader.close();
        return stringBuilder.toString();
    }

    /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
     *
     * @param filepath 文件路径
     * @throws IOException io异常
     */
    public static String readFileByLines(String filepath) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();

        File file = new File(filepath);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);

        // 一次读入一行，直到读入null为文件结束
        String tempString;
        while (null != (tempString = bufferedReader.readLine())) {
            stringBuilder.append(tempString).append("\r\n");
        }

        bufferedReader.close();
        return stringBuilder.toString();
    }

}
