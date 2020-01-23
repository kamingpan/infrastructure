package com.kamingpan.infrastructure.util.conversion;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Map转换
 *
 * @author kamingpan
 * @since 2016-08-30
 */
public class MapConversion {

    /**
     * 对象转换成Map
     *
     * @param object 对象
     * @return map结果
     * @throws NoSuchMethodException     方法不存在异常
     * @throws InvocationTargetException 调用目标异常
     * @throws IllegalAccessException    非法访问异常
     */
    public static Map<String, Object> convertToMap(Object object)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Map<String, Object> result = new HashMap<String, Object>();
        Class<?> clazz = object.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            String name = field.getName();
            String getString = "get" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
            Method getMethod = clazz.getDeclaredMethod(getString);
            result.put(name, getMethod.invoke(object));
        }
        return result;
    }

    /**
     * Map转换成对象
     *
     * @param map   map对象
     * @param clazz 类
     * @return 结果对象
     * @throws IllegalAccessException    非法访问异常
     * @throws InstantiationException    实例化异常
     * @throws NoSuchMethodException     方法不存在异常
     * @throws InvocationTargetException 调用目标异常
     */
    public static Object convertToObject(Map<String, Object> map, Class<?> clazz)
            throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Field[] fields = clazz.getDeclaredFields();
        Object object = clazz.newInstance();
        for (Field field : fields) {
            field.setAccessible(true);

            Class<?> classType = field.getType();
            String name = field.getName();
            String setString = "set" + name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
            Method setMethod = clazz.getDeclaredMethod(setString, classType);
            if (map.containsKey(name)) {
                setMethod.invoke(object, map.get(name));
            }
        }
        return object;
    }

    /**
     * 把临时对象的值复制到持久化对象上
     *
     * @param persistentObject 持久化对象
     * @param temporaryObject  临时对象
     * @return 持久化对象
     * @throws NoSuchMethodException     方法不存在异常
     * @throws InvocationTargetException 调用目标异常
     * @throws IllegalAccessException    非法访问异常
     */
    public static Object mergedObject(Object persistentObject, Object temporaryObject)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<?> clazz = temporaryObject.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            Class<?> classType = field.getType();
            String name = field.getName();
            String method = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
            String getString = "get" + method;
            Method getMethod = clazz.getDeclaredMethod(getString);
            Object value = getMethod.invoke(temporaryObject);
            if (null != value) {
                String setString = "set" + method;
                Method setMethod = clazz.getDeclaredMethod(setString, classType);
                setMethod.invoke(persistentObject, value);
            }
        }
        return persistentObject;
    }

}
