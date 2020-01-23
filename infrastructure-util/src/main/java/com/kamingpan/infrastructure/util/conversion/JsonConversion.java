package com.kamingpan.infrastructure.util.conversion;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * json转换
 *
 * @author kamingpan
 * @since 2016-08-30
 */
@Slf4j
public class JsonConversion {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 对象转换成json字符串
     *
     * @param object 对象
     * @return json字符串
     * @throws JsonProcessingException json转换异常
     */
    public static String convertToJson(Object object) throws JsonProcessingException {
        return JsonConversion.OBJECT_MAPPER.writeValueAsString(object);
    }

    /**
     * 对象转换成json字符串
     *
     * @param object 对象
     * @return json字符串
     */
    public static String convertToJsonWithoutException(Object object) {
        try {
            return JsonConversion.convertToJson(object);
        } catch (JsonProcessingException exception) {
            log.warn("json转换异常", exception);
        }

        return null;
    }

    /**
     * json字符串转换成对象
     *
     * @param json  json字符串
     * @param clazz 类
     * @param <T>   对象泛型
     * @return 对象
     * @throws IOException io异常
     */
    public static <T> T convertToObject(String json, Class<T> clazz) throws IOException {
        return JsonConversion.OBJECT_MAPPER.readValue(json, clazz);
    }

    /**
     * json字符串转换成对象
     *
     * @param json  json字符串
     * @param clazz 类
     * @param <T>   对象泛型
     * @return 对象
     */
    public static <T> T convertToObjectWithoutException(String json, Class<T> clazz) {
        try {
            return JsonConversion.convertToObject(json, clazz);
        } catch (IOException exception) {
            log.warn("json转换异常", exception);
        }

        return null;
    }

}
