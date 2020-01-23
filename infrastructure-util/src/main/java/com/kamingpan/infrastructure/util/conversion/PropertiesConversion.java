package com.kamingpan.infrastructure.util.conversion;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 配置文件装换，通过静态内部类实现“单例模型”，满足线程安全
 *
 * @author kamingpan
 * @since 2016-08-25
 */
public class PropertiesConversion {

    /**
     * 配置文件转换为Properties对象
     *
     * @param filename 文件名
     * @return Properties对象
     */
    public static Properties convertToProperties(String filename) throws IOException {
        Properties properties = new Properties();
        InputStreamReader inputStreamReader = new InputStreamReader(PropertiesConversion
                .class.getClassLoader().getResourceAsStream(filename), "UTF-8");
        properties.load(inputStreamReader);
        inputStreamReader.close();

        return properties;
    }

    /**
     * 配置文件转换为Map对象
     *
     * @param filename 文件名
     * @return Map对象
     */
    public static Map<String, String> convertToMap(String filename) throws IOException {
        Properties properties = convertToProperties(filename);

        Map<String, String> result = new HashMap<String, String>();

        // 获取属性key的集合
        Set<Map.Entry<Object, Object>> objects = properties.entrySet();
        for (Map.Entry<Object, Object> object : objects) {
            result.put(String.valueOf(object.getKey()), String.valueOf(object.getValue()));
        }

        return result;
    }
}
