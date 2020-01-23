package com.kamingpan.infrastructure.util.instantiate;

import lombok.extern.slf4j.Slf4j;

/**
 * InstantiateClass 实例化对象
 *
 * @author kamingpan
 * @since 2018-07-16
 */
@Slf4j
public class InstantiateClass {

    /**
     * 根据类的路径对类进行实例化
     *
     * @param classPath 类的路径
     * @return 指定类的实例化对象
     */
    public static Object stringToClass(String classPath) {
        if (null == classPath || classPath.trim().isEmpty()) {
            return null;
        }

        try {
            return Class.forName(classPath.trim()).newInstance();
        } catch (InstantiationException | IllegalAccessException exception) {
            log.error("类" + classPath + "实例化异常", exception);
        } catch (ClassNotFoundException exception) {
            log.error("类" + classPath + "不存在", exception);
        }

        return null;
    }

}
