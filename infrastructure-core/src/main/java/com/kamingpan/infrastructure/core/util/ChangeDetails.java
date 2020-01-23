package com.kamingpan.infrastructure.core.util;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 对象变更详情
 *
 * @author kamingpan
 * @since 2017-09-04
 */
public class ChangeDetails {

    /**
     * 新增对象详情（字符串）
     *
     * @param object   对象
     * @param fieldMap 字段集合
     * @return 新增字符串
     */
    public static String getByCreate(Object object, Map<String, String> fieldMap) {
        StringBuilder stringBuilder = new StringBuilder();

        // 获取对象的所有属性，返回Field数组
        Field[] fields = object.getClass().getDeclaredFields();

        // 遍历所有属性
        for (Field field : fields) {
            field.setAccessible(true);

            Object value;
            try {
                value = field.get(object);
            } catch (IllegalAccessException exception) {
                continue;
            }

            if (null == value) {
                continue;
            }

            String name = field.getName();
            stringBuilder.append("\"");
            stringBuilder.append(null == fieldMap || null == fieldMap.get(name) ? name : fieldMap.get(name));
            stringBuilder.append("\"，值为 \"");
            stringBuilder.append(value);
            stringBuilder.append("\"。<br/>");
        }
        return stringBuilder.toString();
    }

    /**
     * 修改对象详情（字符串）
     *
     * @param before   修改前对象
     * @param after    修改后对象
     * @param fieldMap 字段集合
     * @return 修改字符串
     */
    public static String getByUpdate(Object before, Object after, Map<String, String> fieldMap) {
        if (null == before || null == after) {
            return null;
        }

        // 如果不是同样类型对象
        if (!before.getClass().getName().equals(after.getClass().getName())) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        // 获取对象的所有属性，返回Field数组
        Field[] beforeFields = before.getClass().getDeclaredFields();
        Field[] afterFields = after.getClass().getDeclaredFields();

        // 遍历所有属性
        for (int i = 0; i < afterFields.length; i++) {
            Field afterField = afterFields[i];
            afterField.setAccessible(true);

            Field beforeField = beforeFields[i];
            beforeField.setAccessible(true);

            Object afterValue;
            Object beforeValue;
            try {
                afterValue = afterField.get(after);
                beforeValue = beforeField.get(before);
            } catch (IllegalAccessException exception) {
                continue;
            }

            // 如果修改后的值是null或者修改前后的值都相同，则代表不修改
            if (null == afterValue || afterValue.equals(beforeValue)) {
                continue;
            }

            String name = afterField.getName();
            stringBuilder.append("\"");
            stringBuilder.append(null == fieldMap || null == fieldMap.get(name) ? name : fieldMap.get(name));
            stringBuilder.append("\"，由 \"");
            stringBuilder.append(beforeValue);
            stringBuilder.append("\" 更改为 \"");
            stringBuilder.append(afterValue);
            stringBuilder.append("\"。<br/>");
        }

        return stringBuilder.toString();
    }


}
