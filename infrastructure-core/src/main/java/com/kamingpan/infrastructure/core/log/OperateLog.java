package com.kamingpan.infrastructure.core.log;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 请求记录注解
 *
 * @author kamingpan
 * @since 2016-08-30
 */
@Documented
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLog {

    // 日志对象名
    String clazz() default "AdminOperateLog";

    // 操作类型（登录，登出，新增，修改，删除，其它）
    String type() default "";
}
