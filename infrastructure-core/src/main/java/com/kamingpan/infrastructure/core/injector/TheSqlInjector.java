package com.kamingpan.infrastructure.core.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 自定义基础dao类sql注入
 * 继承逻辑删除sql注入方法，使用父类方法的逻辑删除
 *
 * @author kamingpan
 * @since 2018-06-29
 */
@Slf4j
public class TheSqlInjector extends DefaultSqlInjector {

    /**
     * 如果只需增加方法，保留MP自带方法
     * 可以super.getMethodList() 再add
     *
     * @return 抽象方法列表
     */
    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
        List<AbstractMethod> abstractMethods = super.getMethodList(mapperClass); // 获取逻辑删出的所有父类方法
        abstractMethods.add(new PhysicalDeleteMethod()); // 添加物理删除方法
        abstractMethods.add(new PhysicalDeleteBatchIdsMethod()); // 添加批量物理删除方法
        abstractMethods.add(new CountByIdMethod()); // 添加根据主键查询数据数量方法
        return abstractMethods;
    }

}
