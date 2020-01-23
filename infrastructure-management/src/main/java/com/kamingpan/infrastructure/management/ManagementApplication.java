package com.kamingpan.infrastructure.management;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 管理端启动入口
 *
 * @author kamingpan
 * @since 2018-06-13
 */
@ServletComponentScan
@SpringBootApplication
@EnableTransactionManagement
@ComponentScan(basePackages = {"com.kamingpan.infrastructure"})
@MapperScan(annotationClass = Repository.class, basePackages = "com.kamingpan.infrastructure")
public class ManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManagementApplication.class, args);
    }

}
