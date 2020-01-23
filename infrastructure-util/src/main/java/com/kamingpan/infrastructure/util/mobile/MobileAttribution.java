package com.kamingpan.infrastructure.util.mobile;

import com.kamingpan.infrastructure.util.conversion.JsonConversion;
import com.kamingpan.infrastructure.util.http.GetHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 手机归属地
 *
 * @author kamingpan
 * @since 2018-03-13
 */
@Slf4j
public class MobileAttribution {

    // 匹配手机归属地的请求地址
    private static final String REQUEST_URL = "http://v.showji.com/Locating/showji.com2016234999234.aspx?output=json&m=";

    // 编码格式
    private static final String ENCODING = "UTF-8";

    // 转换工具类
    // public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 手机归属地
     *
     * @param mobile 手机号码
     * @return 归属地信息（对象）
     */
    public static MobileBean belongs(final String mobile) {
        String result = belongsString(mobile);

        try {
            return JsonConversion.OBJECT_MAPPER.readValue(result, MobileBean.class);
        } catch (IOException exception) {
            log.warn("手机归属地转换失败", exception);
        }
        return null;
    }

    /**
     * 获取手机归属地
     *
     * @param mobile 手机号码
     * @return 归属地信息（字符串）
     */
    public static String belongsString(final String mobile) {
        return GetHttpRequest.sendGet(REQUEST_URL + mobile);
    }

}
