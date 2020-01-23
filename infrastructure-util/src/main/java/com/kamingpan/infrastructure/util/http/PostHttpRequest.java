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
public class PostHttpRequest {

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url 请求地址
     * @return 响应结果字符串
     */
    public static String sendPost(String url) {
        return PostHttpRequest.sendPost(url, (String) null);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url 请求地址
     * @return 响应结果
     */
    public static byte[] sendPostBytes(String url) {
        return PostHttpRequest.sendPostBytes(url, (String) null);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果字符串
     */
    public static String sendPost(String url, String params) {
        return PostHttpRequest.sendPost(url, params, null);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果
     */
    public static byte[] sendPostBytes(String url, String params) {
        return PostHttpRequest.sendPostBytes(url, params, null);
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
        // 处理请求参数
        if (null != params && !params.isEmpty()) {
            if (url.contains("?")) {
                url += ("&" + params);
            } else {
                url += ("?" + params);
            }
        }

        // 初始化请求头
        if (null == headers) {
            headers = new HashMap<>();
        }

        PostHttpRequest.initHeaders(headers);
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
        // 处理请求参数
        if (null != params && !params.isEmpty()) {
            if (url.contains("?")) {
                url += ("&" + params);
            } else {
                url += ("?" + params);
            }
        }

        return PostHttpRequest.sendPostBytes(url, (Map<String, Object>) null, headers);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果字符串
     */
    public static String sendPost(String url, Map<String, Object> params) {
        return PostHttpRequest.sendPost(url, params, null);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果
     */
    public static byte[] sendPostBytes(String url, Map<String, Object> params) {
        return PostHttpRequest.sendPostBytes(url, params, null);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应结果字符串
     */
    public static String sendPost(String url, Map<String, Object> params, Map<String, String> headers) {
        // 初始化请求头
        if (null == headers) {
            headers = new HashMap<>();
        }

        PostHttpRequest.initHeaders(headers);
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
    public static byte[] sendPostBytes(String url, Map<String, Object> params, Map<String, String> headers) {
        // 初始化请求头
        if (null == headers) {
            headers = new HashMap<>();
        }

        PostHttpRequest.initHeaders(headers);
        return HttpRequest.sendHttpRequestBytes(url, params, headers, BaseHttpRequest.Method.POST);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果字符串
     */
    public static String sendPost(String url, Object params) {
        return PostHttpRequest.sendPost(url, params, null);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果
     */
    public static byte[] sendPostBytes(String url, Object params) {
        return PostHttpRequest.sendPostBytes(url, params, null);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应结果字符串
     */
    public static String sendPost(String url, Object params, Map<String, String> headers) {
        // 初始化请求头
        if (null == headers) {
            headers = new HashMap<>();
        }

        PostHttpRequest.initHeaders(headers);
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
    public static byte[] sendPostBytes(String url, Object params, Map<String, String> headers) {
        // 初始化请求头
        if (null == headers) {
            headers = new HashMap<>();
        }

        PostHttpRequest.initHeaders(headers);
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
            headers.put(BaseHttpRequest.Heathers.CONTENT_TYPE, BaseHttpRequest.Heathers.POST_TYPE);
        }
    }

}
