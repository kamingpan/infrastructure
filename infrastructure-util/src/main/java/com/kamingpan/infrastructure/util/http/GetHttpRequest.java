package com.kamingpan.infrastructure.util.http;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * GetHttp请求工具类
 *
 * @author kamingpan
 * @since 2016-09-14
 */
@Slf4j
public class GetHttpRequest {

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url 请求地址
     * @return 响应结果字符串
     */
    public static String sendGet(String url) {
        return GetHttpRequest.sendGet(url, (String) null);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url 请求地址
     * @return 响应结果
     */
    public static byte[] sendGetBytes(String url) {
        return GetHttpRequest.sendGetBytes(url, (String) null);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果字符串
     */
    public static String sendGet(String url, String params) {
        return GetHttpRequest.sendGet(url, params, null);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果
     */
    public static byte[] sendGetBytes(String url, String params) {
        return GetHttpRequest.sendGetBytes(url, params, null);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应结果字符串
     */
    public static String sendGet(String url, String params, Map<String, String> headers) {
        return HttpRequest.sendHttpRequest(url, params, headers, BaseHttpRequest.Method.GET);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应结果
     */
    public static byte[] sendGetBytes(String url, String params, Map<String, String> headers) {
        return HttpRequest.sendHttpRequestBytes(url, params, headers, BaseHttpRequest.Method.GET);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果字符串
     */
    public static String sendGet(String url, Map<String, Object> params) {
        return GetHttpRequest.sendGet(url, params, null);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果
     */
    public static byte[] sendGetBytes(String url, Map<String, Object> params) {
        return GetHttpRequest.sendGetBytes(url, params, null);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应结果字符串
     */
    public static String sendGet(String url, Map<String, Object> params, Map<String, String> headers) {
        return HttpRequest.sendHttpRequest(url, params, headers, BaseHttpRequest.Method.GET);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应结果
     */
    public static byte[] sendGetBytes(String url, Map<String, Object> params, Map<String, String> headers) {
        return HttpRequest.sendHttpRequestBytes(url, params, headers, BaseHttpRequest.Method.GET);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果字符串
     */
    public static String sendGet(String url, Object params) {
        return GetHttpRequest.sendGet(url, params, null);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 响应结果
     */
    public static byte[] sendGetBytes(String url, Object params) {
        return GetHttpRequest.sendGetBytes(url, params, null);
    }

    /**
     * 发起http请求获取响应结果字符串
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应结果字符串
     */
    public static String sendGet(String url, Object params, Map<String, String> headers) {
        return HttpRequest.sendHttpRequest(url, params, headers, BaseHttpRequest.Method.GET);
    }

    /**
     * 发起http请求获取响应结果
     *
     * @param url     请求地址
     * @param params  请求参数
     * @param headers 请求头
     * @return 响应结果
     */
    public static byte[] sendGetBytes(String url, Object params, Map<String, String> headers) {
        return HttpRequest.sendHttpRequestBytes(url, params, headers, BaseHttpRequest.Method.GET);
    }

}
