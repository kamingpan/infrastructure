package com.kamingpan.infrastructure.util.http;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Http请求基础工具类
 *
 * @author kamingpan
 * @since 2016-09-14
 */
@Slf4j
public class BaseHttpRequest {

    private static final String PROXY_HOST = "127.0.0.1";
    private static final int PROXY_PORT = 8080;
    private static final int CONNECT_TIMEOUT = 100000;
    private static final int READ_TIMEOUT = 100000;

    // IE 6.0
    private static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)";

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url            请求地址
     * @param params         请求参数
     * @param headers        请求头
     * @param requestMethod  请求方法
     * @param connectTimeout 连接超时时间
     * @param readTimeout    读取超时时间
     * @param isProxy        是否使用代理模式
     * @return 响应结果字符串
     */
    public static String sendHttpRequest(String url, Map<String, Object> params, Map<String, String> headers,
                                         String requestMethod, Integer connectTimeout, Integer readTimeout,
                                         Boolean isProxy) {
        String result = new String(BaseHttpRequest.sendHttpRequestBytes(url, params, headers, requestMethod,
                connectTimeout, readTimeout, isProxy), StandardCharsets.UTF_8);

        // 打印响应结果
        log.debug("http响应结果：{}", result);

        return result;
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url            请求地址
     * @param params         请求参数
     * @param headers        请求头
     * @param requestMethod  请求方法
     * @param connectTimeout 连接超时时间
     * @param readTimeout    读取超时时间
     * @param isProxy        是否使用代理模式
     * @return 响应结果
     */
    public static byte[] sendHttpRequestBytes(String url, Map<String, Object> params, Map<String, String> headers,
                                              String requestMethod, Integer connectTimeout, Integer readTimeout,
                                              Boolean isProxy) {
        // 组装请求参数（key1=value1&key2=value2格式）
        String paramsString = BaseHttpRequest.getParamsString(params);

        return BaseHttpRequest.sendHttpRequestBytes(url, paramsString, headers, requestMethod, connectTimeout,
                readTimeout, isProxy);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url            请求地址
     * @param params         请求参数
     * @param headers        请求头
     * @param requestMethod  请求方法
     * @param connectTimeout 连接超时时间
     * @param readTimeout    读取超时时间
     * @param isProxy        是否使用代理模式
     * @return 响应结果字符串
     */
    public static String sendHttpRequest(String url, Object params, Map<String, String> headers,
                                         String requestMethod, Integer connectTimeout, Integer readTimeout,
                                         Boolean isProxy) {
        return BaseHttpRequest.sendHttpRequest(url, BaseHttpRequest.getParamsMap(params), headers, requestMethod,
                connectTimeout, readTimeout, isProxy);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url            请求地址
     * @param params         请求参数
     * @param headers        请求头
     * @param requestMethod  请求方法
     * @param connectTimeout 连接超时时间
     * @param readTimeout    读取超时时间
     * @param isProxy        是否使用代理模式
     * @return 响应结果
     */
    public static byte[] sendHttpRequestBytes(String url, Object params, Map<String, String> headers,
                                              String requestMethod, Integer connectTimeout, Integer readTimeout,
                                              Boolean isProxy) {
        return BaseHttpRequest.sendHttpRequestBytes(url, BaseHttpRequest.getParamsMap(params), headers, requestMethod,
                connectTimeout, readTimeout, isProxy);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url            请求地址
     * @param params         请求参数
     * @param headers        请求头
     * @param requestMethod  请求方法
     * @param connectTimeout 连接超时时间
     * @param readTimeout    读取超时时间
     * @param isProxy        是否使用代理模式
     * @return 响应结果字符串
     */
    public static String sendHttpRequest(String url, String params, Map<String, String> headers, String requestMethod,
                                         Integer connectTimeout, Integer readTimeout,
                                         Boolean isProxy) {
        // 打印请求参数
        log.debug("http请求参数：{}", params);

        String result = new String(BaseHttpRequest.sendHttpRequestBytes(url, params, headers, requestMethod,
                connectTimeout, readTimeout, isProxy), StandardCharsets.UTF_8);

        // 打印响应结果
        log.debug("http响应结果：{}", result);

        return result;
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url            请求地址
     * @param params         请求参数
     * @param headers        请求头
     * @param requestMethod  请求方法
     * @param connectTimeout 连接超时时间
     * @param readTimeout    读取超时时间
     * @param isProxy        是否使用代理模式
     * @return 响应结果数组
     */
    public static byte[] sendHttpRequestBytes(String url, String params, Map<String, String> headers, String requestMethod,
                                              Integer connectTimeout, Integer readTimeout, Boolean isProxy) {
        // 请求方法转为大写
        if (null == requestMethod || requestMethod.isEmpty()) {
            requestMethod = BaseHttpRequest.Method.GET;
        } else {
            requestMethod = requestMethod.trim().toUpperCase();
        }

        // 判断是否get请求
        boolean isGetMethod = BaseHttpRequest.Method.GET.equals(requestMethod);

        // 空判请求参数
        if (null == params) {
            params = "";
        }

        // 组装get请求参数
        if (!params.isEmpty() && isGetMethod) {
            if (url.contains("?")) {
                url += ("&" + params);
            } else {
                url += ("?" + params);
            }
        }

        // 打印请求信息
        log.debug("http请求地址：{}", url);
        log.debug("http请求类型：{}", requestMethod);

        // 定义流对象
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        OutputStreamWriter outputStreamWriter = null;

        try {
            HttpURLConnection connection = BaseHttpRequest.getURLConnection(url, headers, isGetMethod, connectTimeout,
                    readTimeout, isProxy);

            // 设置请求方法
            connection.setRequestMethod(isGetMethod ? BaseHttpRequest.Method.GET : requestMethod);
            connection.connect();

            // 输入请求内容
            if (!isGetMethod) {
                outputStreamWriter = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
                outputStreamWriter.write(params);
                outputStreamWriter.flush();
            }

            // 将数据流转为二进制数组
            InputStream inputStream = connection.getInputStream();

            int length;
            byte[] bytes = new byte[1024];
            while (-1 != (length = inputStream.read(bytes))) {
                byteArrayOutputStream.write(bytes, 0, length);
            }

            // 释放资源
            connection.disconnect();

            return byteArrayOutputStream.toByteArray();
        } catch (Exception exception) {
            log.warn("", exception);
        } finally {
            BaseHttpRequest.closeWriter(outputStreamWriter);
            BaseHttpRequest.closeOutputStream(byteArrayOutputStream);
        }

        return new byte[0];
    }

    /**
     * 获取http地址链接对象
     *
     * @param url            请求地址
     * @param headers        请求头
     * @param isGetMethod    是否get请求
     * @param connectTimeout 连接超时时间
     * @param readTimeout    读取超时时间
     * @param isProxy        是否使用代理模式
     * @return http地址链接对象
     * @throws IOException io异常
     */
    private static HttpURLConnection getURLConnection(String url, Map<String, String> headers, boolean isGetMethod,
                                                      Integer connectTimeout, Integer readTimeout, Boolean isProxy)
            throws IOException {
        // 获取url对象
        URL theUrl = new URL(url);

        URLConnection connection;
        if (null != isProxy && isProxy) {
            Proxy proxy = new Proxy(Proxy.Type.DIRECT.HTTP, new InetSocketAddress(PROXY_HOST, PROXY_PORT));
            connection = theUrl.openConnection(proxy);
        } else {
            connection = theUrl.openConnection();
        }

        // 设置默认请求头信息
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("User-Agent", USER_AGENT);

        // 设置自定义请求头
        if (null != headers) {
            StringBuilder logString = new StringBuilder("{");
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                connection.setRequestProperty(entry.getKey(), entry.getValue());
                logString.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
            }

            // 移除最后两个字符串
            if (logString.length() > 2) {
                logString.deleteCharAt(logString.length() - 2);
            }
            logString.append("}");

            // 打印请求头
            log.debug("http请求头：{}", logString.toString());
        }

        connection.setDoOutput(!isGetMethod);
        connection.setDoInput(true);
        connection.setUseCaches(false);

        // 设置连接和读取超时时间
        if (null != connectTimeout) {
            connection.setConnectTimeout(connectTimeout);
        }
        if (null != readTimeout) {
            connection.setReadTimeout(readTimeout);
        }

        return (HttpURLConnection) connection;
    }

    /**
     * 获取请求参数字符串，格式为：name1=value1&name2=value2。
     *
     * @param params 请求参数
     * @return 请求参数字符串
     */
    private static String getParamsString(Map<String, Object> params) {
        if (null == params) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder logString = new StringBuilder("{");
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            logString.append("\"").append(entry.getKey()).append("\": ").append(entry.getValue()).append(", ");
        }

        // 移除最后两个字符串
        if (logString.length() > 2) {
            logString.delete(logString.length() - 2, logString.length());
        }
        logString.append("}");

        // 打印请求参数
        log.debug("http请求参数：{}", logString.toString());

        // 移除最后一个 &
        if (stringBuilder.length() > 1) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }
        return stringBuilder.toString();
    }

    /**
     * 获取请求参数Map（不直接转字符串的原因是防止父类和子类有重复的键，导致生成多余的入参）
     *
     * @param params 请求参数
     * @return 请求参数字符串
     */
    private static Map<String, Object> getParamsMap(Object params) {
        if (null == params) {
            return null;
        }

        // 定义相关对象
        Class clazz = params.getClass();
        Map<String, Object> paramsMap = new HashMap<>();

        // 循环往上遍历，组装键值对，直到Object为止
        while (!"java.lang.Object".equals(clazz.getName())) {
            // 遍历并通过反射获取字段名和值
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(params);
                } catch (IllegalAccessException exception) {
                    log.warn("获取对象“{}”的“{}”字段值异常", clazz.getName(), field.getName());
                }

                // 如果值为空（非空字符串），则跳过
                if (null == value) {
                    continue;
                }

                // 将键和值放到Map中
                paramsMap.putIfAbsent(field.getName(), value);
            }

            // 获取父类的class，开始下一次循环
            clazz = clazz.getSuperclass();
        }

        return paramsMap;
    }

    /**
     * 关闭writer
     *
     * @param writer writer
     */
    private static void closeWriter(Writer writer) {
        if (null != writer) {
            try {
                writer.close();
            } catch (IOException exception) {
                log.warn("", exception);
            }
        }
    }

    /**
     * 关闭OutputStream
     *
     * @param outputStream 输出流对象
     */
    private static void closeOutputStream(OutputStream outputStream) {
        if (null != outputStream) {
            try {
                outputStream.close();
            } catch (IOException exception) {
                log.warn("", exception);
            }
        }
    }

    /**
     * http请求方法
     */
    public static final class Method {
        /**
         * GET方法
         */
        public static final String GET = "GET";

        /**
         * POST方法
         */
        public static final String POST = "POST";

        /**
         * DELETE方法
         */
        public static final String DELETE = "DELETE";

        /**
         * PUT方法
         */
        public static final String PUT = "PUT";

        /**
         * PATCH方法
         */
        public static final String PATCH = "PATCH";
    }

    /**
     * http请求头
     */
    public static final class Heathers {
        /**
         * 请求内容类型
         */
        public static final String CONTENT_TYPE = "Content-Type";

        /**
         * 内容类型
         */
        public static final String POST_TYPE = "application/x-www-form-urlencoded";

        /**
         * json类型
         */
        public static final String JSON_TYPE = "application/json";
    }

}
