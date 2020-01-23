package com.kamingpan.infrastructure.core.base.dao;

import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;

/**
 * Mapper基础接口
 *
 * @author kamingpan
 * @since 2016-03-1
 */
public interface BaseMapper<T extends BaseEntity, PK extends Serializable> {

    /**
     * 插入数据
     *
     * @param bean 实体对象
     * @return 影响的数据数量
     */
    int insert(T bean);

    /**
     * 根据主键删除数据
     *
     * @param id 主键
     * @return 影响的数据数量
     */
    int delete(@Param("id") PK id);

    /**
     * 根据主键集合批量删除数据
     *
     * @param ids 主键集合
     * @return 影响的数据数量
     */
    int deletes(@Param("ids") List<PK> ids);

    /**
     * 根据实体更新数据
     *
     * @param bean 实体对象
     * @return 影响的数据数量
     */
    int update(T bean);

    /**
     * 根据主键查询数据
     *
     * @param id 主键
     * @return 数据
     */
    T get(@Param("id") PK id);

    /**
     * 根据实体和分页查询数据集合
     *
     * @param bean 实体对象
     * @return 数据集合
     */
    List<T> list(@Param("bean") T bean);

    /**
     * 根据实体查询数据集合
     *
     * @param bean 实体对象
     * @return 数据集合
     */
    List<T> listParam(@Param("bean") T bean);

    /**
     * 根据主键集合批量查询数据
     *
     * @param ids 主键集合
     * @return 数据集合
     */
    List<T> listBatch(@Param("ids") String ids);

    /**
     * 根据主键查询数据数量
     *
     * @param id 主键
     * @return 数据数量
     */
    int countById(@Param("id") PK id);

    /**
     * 根据实体查询数据数量
     *
     * @param bean 实体对象
     * @return 数据数量
     */
    long count(@Param("bean") T bean);

}
