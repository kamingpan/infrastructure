package com.kamingpan.infrastructure.core.base.service;

import com.baomidou.mybatisplus.extension.service.IService;

import java.io.Serializable;
import java.util.Collection;

/**
 * 基础服务类 接口
 *
 * @author kamingpan
 * @since 2018-06-26
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 插入数据
     *
     * @param entity       实体对象
     * @return 影响的数据数量
     */
    int insert(T entity);

    /**
     * 插入数据
     *
     * @param entity       实体对象
     * @param initBaseData 是否初始化基础数据
     * @return 影响的数据数量
     */
    int insert(T entity, boolean initBaseData);

    /**
     * 根据主键对数据进行物理删除
     *
     * @param id 主键
     * @return 删除行数
     */
    int physicalDelete(Serializable id);

    /**
     * 根据主键集合对数据进行批量物理删除
     *
     * @param ids 主键集合
     * @return 删除行数
     */
    int physicalDeleteBatchIds(Collection<? extends Serializable> ids);

    /**
     * 根据主键更新实体数据
     *
     * @param entity 实体对象
     * @return 影响的数据数量
     */
    int update(T entity);

    /**
     * 根据主键查询数据数量
     *
     * @param id 主键
     * @return 数据数量
     */
    int countById(Serializable id);

}
