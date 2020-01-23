package com.kamingpan.infrastructure.util.uri;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * uri转义
 *
 * @author kamingpan
 * @since 2016-09-13
 */
@Slf4j
public class URI {

    private static final class ENCODE {
        private static final String UTF8 = "UTF-8";
        private static final String GBK = "GBK";
    }

    private static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*\'()";

    public URI() {
    }

    /**
     * URI转码(utf-8)
     *
     * @param uri 链接地址
     * @return utf-8转码地址
     */
    public static String encode(String uri) {
        if (null == uri || uri.isEmpty()) {
            return "";
        }

        try {
            return URLEncoder.encode(uri, URI.ENCODE.UTF8);
        } catch (UnsupportedEncodingException exception) {
            log.warn("", exception);
        }
        return uri;
    }

    /**
     * URI解码(utf-8)
     *
     * @param uri 链接地址
     * @return utf-8解码地址
     */
    public static String decode(String uri) {
        if (null == uri || uri.isEmpty()) {
            return "";
        }

        try {
            return URLDecoder.decode(uri, URI.ENCODE.UTF8);
        } catch (UnsupportedEncodingException exception) {
            log.warn("", exception);
        }
        return uri;
    }

    /**
     * URI转码(utf-8)
     *
     * @param uri 链接地址
     * @return utf-8转码地址
     */
    public static String encodeURIComponent(String uri) {
        if (null == uri || uri.isEmpty()) {
            return "";
        }

        int length = uri.length();
        StringBuilder stringBuilder = new StringBuilder(length * 3);
        for (int i = 0; i < length; ++i) {
            String string = uri.substring(i, i + 1);
            if (!ALLOWED_CHARS.contains(string)) {
                byte[] b = string.getBytes(StandardCharsets.UTF_8);
                stringBuilder.append(getHex(b));
            } else {
                stringBuilder.append(string);
            }
        }

        return stringBuilder.toString();
    }

    private static String getHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder(bytes.length * 3);

        for (byte b : bytes) {
            long length = b & 255;
            stringBuilder.append("%");
            if (length < 16) {
                stringBuilder.append("0");
            }

            stringBuilder.append(Long.toString(length, 16).toUpperCase());
        }

        return stringBuilder.toString();
    }

    /**
     * 获取当前请求的服务器（协议://ip:端口）
     *
     * @param request 请求
     * @return 请求地址字符串
     */
    public static String getCurrentServer(HttpServletRequest request) {
        // 拼装请求路径和请求参数
        return request.getScheme() + "://" // 当前链接使用的协议
                + request.getServerName() // 服务器地址
                + (URI.isDefaultHttpPort(request) ? "" : (":" + request.getServerPort())); // 服务器端口
    }

    /**
     * 获取当前请求的项目（协议://ip:端口/项目名）
     *
     * @param request 请求
     * @return 请求项目地址字符串
     */
    public static String getCurrentProject(HttpServletRequest request) {
        // 拼装请求路径和请求参数
        return request.getScheme() + "://" // 当前链接使用的协议
                + request.getServerName() // 服务器地址
                + (URI.isDefaultHttpPort(request) ? "" : (":" + request.getServerPort())) // 服务器端口
                + request.getContextPath(); // 项目名称
    }

    /**
     * 获取当前请求的链接地址（协议://ip:端口/项目名/页面地址?请求参数）
     *
     * @param request 请求
     * @return 请求链接字符串
     */
    public static String getCurrentURL(HttpServletRequest request) {
        // 拼装请求路径和请求参数
        return request.getScheme() + "://" // 当前链接使用的协议
                + request.getServerName() // 服务器地址
                + (URI.isDefaultHttpPort(request) ? "" : (":" + request.getServerPort())) // 服务器端口
                + request.getContextPath() // 项目名称
                + request.getServletPath() // 请求页面或其他地址
                + (request.getQueryString() == null ? "" : "?" + request.getQueryString()); // 参数
    }

    /**
     * 是否默认的http端口
     *
     * @param request 请求
     * @return 判断结果
     */
    private static boolean isDefaultHttpPort(HttpServletRequest request) {
        return request.getServerPort() == 80 || request.getServerPort() == 443;
    }

}
