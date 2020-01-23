package com.kamingpan.infrastructure.util.http;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * PostHttp请求工具类
 *
 * @author kamingpan
 * @since 2016-09-14
 */
@Slf4j
public class PostStringHttpRequest {

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url 请求地址
     * @return 响应结果字符串
     */
    public static String sendPost(String url) {
        return PostStringHttpRequest.sendPost(url, null);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url 请求地址
     * @return 响应结果
     */
    public static byte[] sendPostBytes(String url) {
        return PostStringHttpRequest.sendPostBytes(url, null);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果字符串
     */
    public static String sendPost(String url, String params) {
        return PostStringHttpRequest.sendPost(url, params, null);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果
     */
    public static byte[] sendPostBytes(String url, String params) {
        return PostStringHttpRequest.sendPostBytes(url, params, null);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应结果字符串
     */
    public static String sendPost(String url, String params, Map<String, String> headers) {
        // 初始化请求头
        if (null == headers) {
            headers = new HashMap<>();
        }

        PostStringHttpRequest.initHeaders(headers);
        return HttpRequest.sendHttpRequest(url, params, headers, BaseHttpRequest.Method.POST);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应结果
     */
    public static byte[] sendPostBytes(String url, String params, Map<String, String> headers) {
        // 初始化请求头
        if (null == headers) {
            headers = new HashMap<>();
        }

        PostStringHttpRequest.initHeaders(headers);
        return HttpRequest.sendHttpRequestBytes(url, params, headers, BaseHttpRequest.Method.POST);
    }

    /**
     * 初始化请求头
     *
     * @param headers 请求头
     */
    private static void initHeaders(Map<String, String> headers) {
        // 设置请求头内容
        String contentType = headers.get(BaseHttpRequest.Heathers.CONTENT_TYPE);
        if (null == contentType || contentType.isEmpty()) {
            headers.put(BaseHttpRequest.Heathers.CONTENT_TYPE, BaseHttpRequest.Heathers.JSON_TYPE);
        }
    }

}
