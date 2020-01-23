package com.kamingpan.infrastructure.util.conversion;

import com.kamingpan.infrastructure.util.file.CheckFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;

/**
 * 图片转换
 *
 * @author kamingpan
 * @since 2016-08-25
 */
public class ImageConversion {

    /**
     * 图片转码为base64
     *
     * @param path         文件路径
     * @param isDeleteFile 是否删除文件
     * @return base64字符串
     */
    public static String imageToBase64(String path, boolean isDeleteFile) throws IOException {
        InputStream inputStream = null;

        try {
            // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
            inputStream = new FileInputStream(path);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            if (isDeleteFile) {
                ImageConversion.deleteFile(path);
            }

            // 返回Base64编码过的字节数组字符串
            Base64.Encoder encoder = Base64.getEncoder();
            return encoder.encodeToString(bytes);
        } finally {
            if (null != inputStream) {
                inputStream.close();
            }
        }

    }

    /**
     * base64转码为图片
     *
     * @param base64   内容
     * @param filepath 文件路径
     * @throws IOException io异常
     */
    public static void base64ToImage(String base64, String filepath) throws IOException {
        if (null == base64) {
            return;
        }

        Base64.Decoder decoder = Base64.getDecoder();
        OutputStream outputStream = null;

        try {
            CheckFile.checkExist(filepath, true);

            // base64解码
            byte[] bytes = decoder.decode(base64);
            for (int i = 0; i < bytes.length; i++) {
                if (bytes[i] < 0) {
                    // 调整异常数据
                    bytes[i] += 256;
                }
            }

            // 生成图片文件
            outputStream = new FileOutputStream(filepath);
            outputStream.write(bytes);
        } finally {
            if (null != outputStream) {
                outputStream.flush();
                outputStream.close();
            }
        }
    }

    /**
     * 删除图片
     *
     * @param path 图片路径
     */
    private static boolean deleteFile(String path) {
        File file = new File(path);
        return !file.exists() || file.delete();
    }
}
