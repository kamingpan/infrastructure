package com.kamingpan.infrastructure.core.injector;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

/**
 * mybatis plus自定义填充公共字段 ,即没有传的字段自动填充
 *
 * @author kamingpan
 * @since 2018-06-29
 */
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 插入元对象字段填充
     * TODO 后续需要加上判断哪些表不需要更新这些公共字段
     *
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 获取公共数据
        Object id = metaObject.getValue("id");
        Object creatorId = metaObject.getValue("creatorId");
        // Object creator = metaObject.getValue("creator");
        Object createTime = metaObject.getValue("createTime");
        Object updaterId = metaObject.getValue("updaterId");
        // Object updater = metaObject.getValue("updater");
        Object updateTime = metaObject.getValue("updateTime");

        // 获取当前登录用户主键
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String operator = null == attributes ? null : attributes.getRequest().getRemoteUser();

        // 如果主键为空，则赋值主键
        if (null == id) {
            metaObject.setValue("creatorId", operator);
        }

        // 如果创建人为空，则赋值当前登录用户主键
        if (null == creatorId) {
            metaObject.setValue("creatorId", operator);
        }

        // 如果修改人为空，则赋值当前登录用户主键
        if (null == updaterId) {
            metaObject.setValue("updaterId", operator);
        }

        // 如果创建时间或者修改时间为空，则都赋值当前当前时间
        if (null == createTime && null == updateTime) {
            Date currentDate = new Date();
            metaObject.setValue("createTime", currentDate);
            metaObject.setValue("updateTime", currentDate);
        } else if (null == createTime) {
            // 如果创建时间为空，则把修改时间赋值给创建时间
            metaObject.setValue("createTime", updateTime);
        } else if (null == updateTime) {
            // 如果修改时间为空，则把创建时间赋值给修改时间
            metaObject.setValue("updateTime", createTime);
        }
    }

    /**
     * 更新元对象字段填充（用于更新时对公共字段的填充）
     * TODO 后续需要加上判断哪些表不需要更新这些公共字段
     *
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        Object updaterId = metaObject.getValue("updaterId");
        Object updateTime = metaObject.getValue("updateTime");


        // 如果修改人为空，则赋值当前登录用户主键
        if (null == updaterId) {
            // 获取当前登录用户主键
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            String operator = null == attributes ? null : attributes.getRequest().getRemoteUser();

            metaObject.setValue("updater", operator);
        }

        // 如果修改时间为空，则把创建时间赋值给修改时间
        if (null == updateTime) {
            metaObject.setValue("updateTime", new Date());
        }
    }
}
