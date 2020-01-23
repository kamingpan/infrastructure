package com.kamingpan.infrastructure.util.generator;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * uuid生成器
 *
 * @author kamingpan
 * @since 2016-08-25
 */
public class UuidGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 封装JDK自带的UUID, 通过Random数字生成, 中间无-分割.
     */
    public static synchronized String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 使用SecureRandom随机生成Long.
     */
    public static synchronized long randomLong() {
        return Math.abs(RANDOM.nextLong());
    }
}

