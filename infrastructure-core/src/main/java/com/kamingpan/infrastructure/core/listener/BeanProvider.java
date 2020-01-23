package com.kamingpan.infrastructure.core.listener;

import org.springframework.context.ApplicationContext;

/**
 * 监听器
 *
 * @author kamingpan
 * @since 2016-07-16
 */
public class BeanProvider {

    private static ApplicationContext applicationContext;

    public static void initialize(ApplicationContext applicationContext) {
        BeanProvider.applicationContext = applicationContext;
    }

    /**
     * Get Bean by clazz.
     *
     * @param clazz Class
     * @param <T>   class type
     * @return Bean instance
     */
    public static <T> T getBean(Class<T> clazz) {
        if (null == BeanProvider.applicationContext) {
            return null;
        }
        return BeanProvider.applicationContext.getBean(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanId) {
        if (null == BeanProvider.applicationContext) {
            return null;
        }
        return (T) BeanProvider.applicationContext.getBean(beanId);
    }
}
