package com.kamingpan.infrastructure.core.base.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * 基础服务类
 *
 * @author kamingpan
 * @since 2018-06-26
 */
@Slf4j
public class BaseServiceImpl<T extends BaseEntity<T>, DAO extends BaseDao<T>>
        extends ServiceImpl<DAO, T> implements BaseService<T> {

    /**
     * 插入数据
     *
     * @param entity 实体对象
     * @return 是否插入成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(T entity) {
        if (null == entity) {
            return 0;
        }

        entity.preInsert();
        return super.baseMapper.insert(entity);
    }

    /**
     * 插入数据
     *
     * @param entity       实体对象
     * @param initBaseData 是否初始化基础数据
     * @return 影响的数据数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(T entity, boolean initBaseData) {
        if (null == entity) {
            return 0;
        }

        if (initBaseData) {
            entity.preInsert();
        }
        return super.baseMapper.insert(entity);
    }

    /**
     * 根据主键对数据进行物理删除
     *
     * @param id 主键
     * @return 删除行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int physicalDelete(Serializable id) {
        if (null == id) {
            return 0;
        }

        return super.baseMapper.physicalDelete(id);
    }

    /**
     * 根据主键集合对数据进行批量物理删除
     *
     * @param ids 主键集合
     * @return 删除行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int physicalDeleteBatchIds(Collection<? extends Serializable> ids) {
        if (null == ids || ids.size() <= 0) {
            return 0;
        }

        return super.baseMapper.physicalDeleteBatchIds(ids);
    }

    /**
     * 根据主键更新实体数据
     *
     * @param entity 实体对象
     * @return 影响的数据数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(T entity) {
        if (null == entity || null == entity.getId() || entity.getId().trim().isEmpty()) {
            return 0;
        }

        entity.preUpdate();
        return super.baseMapper.updateById(entity);
    }

    /**
     * 根据主键查询数据数量
     *
     * @param id 主键
     * @return 数据数量
     */
    @Override
    public int countById(Serializable id) {
        if (null == id) {
            return 0;
        }

        return super.baseMapper.countById(id);
    }

}
