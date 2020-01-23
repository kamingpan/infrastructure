package com.kamingpan.infrastructure.util.context;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;

/**
 * Spring应用上下文工具
 *
 * @author kamingpan
 * @since 2018-12-26
 */
@Slf4j
public class SpringContextUtil implements ApplicationContextAware {

    // Spring 应用上下文环境
    private static ApplicationContext applicationContext;

    // Spring Bean 工厂
    private static DefaultListableBeanFactory defaultListableBeanFactory;

    /**
     * 实现ApplicationContextAware接口的回调方法。设置上下文环境
     *
     * @param applicationContext Spring应用上下文
     */
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;

        // 获取bean工厂
        AutowireCapableBeanFactory autowireCapableBeanFactory = applicationContext.getAutowireCapableBeanFactory();
        if (autowireCapableBeanFactory instanceof DefaultListableBeanFactory) {
            SpringContextUtil.defaultListableBeanFactory = (DefaultListableBeanFactory) autowireCapableBeanFactory;
        }
    }

    /**
     * 获取Spring应用上下文环境
     *
     * @return ApplicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return SpringContextUtil.applicationContext;
    }

    /**
     * 根据bean的id来查找对象
     *
     * @param id bean id
     * @return spring对象
     */
    public static Object getBean(String id) {
        return SpringContextUtil.applicationContext.getBean(id);
    }

    /**
     * 根据bean的class来查找对象
     *
     * @param clazz bean class
     * @return spring对象
     */
    public static <T> T getBean(Class<T> clazz) {
        return SpringContextUtil.applicationContext.getBean(clazz);
    }

    /**
     * 根据bean的id和class来查找对象
     *
     * @param id    bean id
     * @param clazz bean class
     * @return spring对象
     */
    public static <T> T getBean(String id, Class<T> clazz) {
        return SpringContextUtil.applicationContext.getBean(id, clazz);
    }

    /**
     * 动态注册bean
     *
     * @param className                类名
     * @param beanKey                  bean 键
     * @param propertyMap              属性集合
     * @param referenceMap             引用集合
     * @param propertyConstructorList  属性构造方法列表
     * @param referenceConstructorList 引用构造方法列表
     * @param destroyMethod            销毁方法
     */
    public static void setBean(String className, String beanKey, Map<String, Object> propertyMap,
                               Map<String, String> referenceMap, List<Object> propertyConstructorList,
                               List<String> referenceConstructorList, String destroyMethod) {
        // 判断Spring容器中是否存在该Bean
        if (SpringContextUtil.defaultListableBeanFactory.containsBeanDefinition(className)) {
            return;
        }

        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.rootBeanDefinition(className);
        beanDefinitionBuilder.getBeanDefinition().setAttribute("id", beanKey);

        // 设置bean参数属性
        if (propertyMap != null && !propertyMap.isEmpty()) {
            for (String keyString : propertyMap.keySet()) {
                beanDefinitionBuilder.addPropertyValue(keyString, propertyMap.get(keyString));
            }
        }

        // 设置bean参数引用
        if (referenceMap != null && !referenceMap.isEmpty()) {
            for (String keyString : referenceMap.keySet()) {
                beanDefinitionBuilder.addPropertyReference(keyString, referenceMap.get(keyString));
            }
        }

        // 设置bean构造参数属性
        if (propertyConstructorList != null && !propertyConstructorList.isEmpty()) {
            for (Object propertyConstructor : propertyConstructorList) {
                beanDefinitionBuilder.addConstructorArgValue(propertyConstructor);
            }
        }

        // 设置bean构造参数引用
        if (referenceConstructorList != null && !referenceConstructorList.isEmpty()) {
            for (String aReferenceConstructorList : referenceConstructorList) {
                beanDefinitionBuilder.addConstructorArgReference(aReferenceConstructorList);
            }
        }

        // 设置bean销毁执行方法
        if (destroyMethod != null && !destroyMethod.isEmpty()) {
            beanDefinitionBuilder.setDestroyMethodName(destroyMethod);
        }

        defaultListableBeanFactory.registerBeanDefinition(beanKey, beanDefinitionBuilder.getBeanDefinition());
    }

}
