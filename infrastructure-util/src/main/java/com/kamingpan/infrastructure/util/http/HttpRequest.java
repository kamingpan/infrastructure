package com.kamingpan.infrastructure.util.http;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * Http请求工具类
 *
 * @author kamingpan
 * @since 2016-09-14
 */
@Slf4j
public class HttpRequest {

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url           请求地址
     * @param requestMethod 请求方法
     * @return 响应结果字符串
     */
    public static String sendHttpRequest(String url, String requestMethod) {
        return HttpRequest.sendHttpRequest(url, (String) null, requestMethod);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url           请求地址
     * @param requestMethod 请求方法
     * @return 响应结果数组
     */
    public static byte[] sendHttpRequestBytes(String url, String requestMethod) {
        return HttpRequest.sendHttpRequestBytes(url, (String) null, requestMethod);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url           请求地址
     * @param params        请求参数
     * @param requestMethod 请求方法
     * @return 响应结果字符串
     */
    public static String sendHttpRequest(String url, Object params, String requestMethod) {
        return HttpRequest.sendHttpRequest(url, params, null, requestMethod);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url           请求地址
     * @param params        请求参数
     * @param requestMethod 请求方法
     * @return 响应结果数组
     */
    public static byte[] sendHttpRequestBytes(String url, Object params, String requestMethod) {
        return HttpRequest.sendHttpRequestBytes(url, params, null, requestMethod);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url           请求地址
     * @param params        请求参数
     * @param headers       请求头
     * @param requestMethod 请求方法
     * @return 响应结果字符串
     */
    public static String sendHttpRequest(String url, Object params, Map<String, String> headers, String requestMethod) {
        return BaseHttpRequest.sendHttpRequest(url, params, headers, requestMethod, null, null, null);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url           请求地址
     * @param params        请求参数
     * @param headers       请求头
     * @param requestMethod 请求方法
     * @return 响应结果数组
     */
    public static byte[] sendHttpRequestBytes(String url, Object params, Map<String, String> headers,
                                              String requestMethod) {
        return BaseHttpRequest.sendHttpRequestBytes(url, params, headers, requestMethod, null, null, null);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url           请求地址
     * @param params        请求参数
     * @param requestMethod 请求方法
     * @return 响应结果字符串
     */
    public static String sendHttpRequest(String url, Map<String, Object> params, String requestMethod) {
        return HttpRequest.sendHttpRequest(url, params, null, requestMethod);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url           请求地址
     * @param params        请求参数
     * @param requestMethod 请求方法
     * @return 响应结果数组
     */
    public static byte[] sendHttpRequestBytes(String url, Map<String, Object> params, String requestMethod) {
        return HttpRequest.sendHttpRequestBytes(url, params, null, requestMethod);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url           请求地址
     * @param params        请求参数
     * @param headers       请求头
     * @param requestMethod 请求方法
     * @return 响应结果字符串
     */
    public static String sendHttpRequest(String url, Map<String, Object> params, Map<String, String> headers,
                                         String requestMethod) {
        return BaseHttpRequest.sendHttpRequest(url, params, headers, requestMethod, null, null, null);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url           请求地址
     * @param params        请求参数
     * @param headers       请求头
     * @param requestMethod 请求方法
     * @return 响应结果数组
     */
    public static byte[] sendHttpRequestBytes(String url, Map<String, Object> params, Map<String, String> headers,
                                              String requestMethod) {
        return BaseHttpRequest.sendHttpRequestBytes(url, params, headers, requestMethod, null, null, null);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url           请求地址
     * @param params        请求参数
     * @param requestMethod 请求方法
     * @return 响应结果字符串
     */
    public static String sendHttpRequest(String url, String params, String requestMethod) {
        return HttpRequest.sendHttpRequest(url, params, null, requestMethod);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url           请求地址
     * @param params        请求参数
     * @param requestMethod 请求方法
     * @return 响应结果数组
     */
    public static byte[] sendHttpRequestBytes(String url, String params, String requestMethod) {
        return HttpRequest.sendHttpRequestBytes(url, params, null, requestMethod);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url           请求地址
     * @param params        请求参数
     * @param headers       请求头
     * @param requestMethod 请求方法
     * @return 响应结果字符串
     */
    public static String sendHttpRequest(String url, String params, Map<String, String> headers, String requestMethod) {
        return BaseHttpRequest.sendHttpRequest(url, params, headers, requestMethod, null, null, null);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url           请求地址
     * @param params        请求参数
     * @param headers       请求头
     * @param requestMethod 请求方法
     * @return 响应结果数组
     */
    public static byte[] sendHttpRequestBytes(String url, String params, Map<String, String> headers,
                                              String requestMethod) {
        return BaseHttpRequest.sendHttpRequestBytes(url, params, headers, requestMethod, null, null, null);
    }

}
