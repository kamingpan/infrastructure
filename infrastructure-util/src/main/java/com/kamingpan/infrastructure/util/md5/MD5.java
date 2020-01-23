package com.kamingpan.infrastructure.util.md5;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 字符串MD5加密
 *
 * @author kamingpan
 * @since 2016-03-1
 */
@Slf4j
public class MD5 {

    private final static String[] hexDigits = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    private final static char[] hexDigits_char = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 字符串加密
     *
     * @param text 加密明文
     * @return 32位密文
     * @throws NoSuchAlgorithmException     算法不存在异常
     * @throws UnsupportedEncodingException 不支持编码类型异常
     */
    public static String encryption(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte bytes[] = messageDigest.digest(text.getBytes("UTF-8"));
        StringBuilder stringBuilder = new StringBuilder("");

        for (int i : bytes) {
            if (i < 0) {
                i += 256;
            }
            if (i < 16) {
                stringBuilder.append("0");
            }
            stringBuilder.append(Integer.toHexString(i));
        }
        return stringBuilder.toString();
    }

    /**
     * 文件MD5加密
     *
     * @param file 加密的文件
     * @return 返回加密的字符串
     * @throws NoSuchAlgorithmException 算法不存在异常
     * @throws IOException              io异常
     */
    public static String encryption(File file) throws NoSuchAlgorithmException, IOException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[2048];
        int length;

        while ((length = fis.read(buffer)) != -1) {
            messageDigest.update(buffer, 0, length);
        }
        fis.close();
        byte[] bytes = messageDigest.digest();
        return byteToHexString(bytes);
    }

    /**
     * 转换字节数组为16进制字串
     *
     * @param bytes 字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for (byte b : bytes) {
            stringBuilder.append(byteToHexString(b));
        }
        return stringBuilder.toString();
    }

    /**
     * 转换byte到16进制
     *
     * @param b 要转换的byte
     * @return 16进制格式
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexDigits[d1] + hexDigits[d2];
    }

    /**
     * 把byte[]数组转换成十六进制字符串表示形式
     *
     * @param bytes 要转换的byte[]
     * @return 十六进制字符串表示形式
     */
    private static String byteToHexString(byte[] bytes) {
        // 用字节表示就是 16 个字节
        char chars[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
        // 所以表示成 16 进制需要 32 个字符
        int length = 0; // 表示转换结果中对应的字符位置
        for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
            // 转换成 16 进制字符的转换
            byte byte0 = bytes[i]; // 取第 i 个字节
            chars[length++] = hexDigits_char[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
            // 为逻辑右移，将符号位一起右移
            chars[length++] = hexDigits_char[byte0 & 0xf]; // 取字节中低 4 位的数字转换
        }
        return new String(chars); // 换后的结果转换为字符串
    }

    /**
     * MD5编码
     *
     * @param origin 原始字符串
     * @return 经过MD5加密之后的结果
     * @throws UnsupportedEncodingException 不支持编码类型异常
     * @throws NoSuchAlgorithmException     算法不存在异常
     */
    public static String MD5Encode(String origin) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(origin.getBytes("UTF-8"));
        return byteArrayToHexString(messageDigest.digest());
    }
}
