package com.kamingpan.infrastructure.core.base.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;

/**
 * Dao基础接口
 *
 * @author kamingpan
 * @since 2016-03-01
 */
public interface BaseDao<T extends BaseEntity> extends BaseMapper<T> {

    /**
     * 根据主键对数据进行物理删除
     *
     * @param id 主键
     * @return 影响的数据数量
     */
    int physicalDelete(@Param("id") Serializable id);

    /**
     * 根据主键集合对数据进行批量物理删除
     *
     * @param ids 主键集合
     * @return 删除行数
     */
    int physicalDeleteBatchIds(@Param("ids") Collection<? extends Serializable> ids);

    /**
     * 根据主键查询数据数量
     *
     * @param id 主键
     * @return 数据数量
     */
    int countById(@Param("id") Serializable id);

}
