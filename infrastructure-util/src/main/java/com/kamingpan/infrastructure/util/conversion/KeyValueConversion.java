package com.kamingpan.infrastructure.util.conversion;

import java.lang.reflect.Field;

/**
 * 键值对转换
 *
 * @author kamingpan
 * @since 2016-08-25
 */
public class KeyValueConversion {

    /**
     * 对象转换为键值对
     *
     * @param object 对象
     * @return 键值对字符串
     * @throws IllegalAccessException 非法访问异常
     */
    public static String convertToKeyValue(Object object) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();

        // 遍历object对象
        Class clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            // 如果成员变量不为空，则添加到StringBuilder中
            if (null != field.get(object) && !"".equals(field.get(object))) {
                stringBuilder.append(field.getName()).append("=").append(field.get(object)).append("&");
            }
        }

        // 移除最后一个字符"&"
        if (stringBuilder.length() > 0) {
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        }

        return stringBuilder.toString();
    }

}
