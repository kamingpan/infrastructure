package com.kamingpan.infrastructure.entity.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kamingpan.infrastructure.core.base.enumeration.DataStatusEnum;
import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.core.exception.DataNotExistException;
import com.kamingpan.infrastructure.core.log.OperateLog;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.core.util.ChangeDetails;
import com.kamingpan.infrastructure.entity.constant.AdminOperateLogConstant;
import com.kamingpan.infrastructure.entity.constant.DataDictionaryConstant;
import com.kamingpan.infrastructure.entity.dao.DataDictionaryDao;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.DataDictionary;
import com.kamingpan.infrastructure.entity.model.vo.DataDictionaryVO;
import com.kamingpan.infrastructure.entity.service.DataDictionaryService;
import com.kamingpan.infrastructure.entity.util.DataDictionaryCache;
import com.kamingpan.infrastructure.util.sql.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 数据字典（管理所有实体用键表示的值） 服务实现类
 *
 * @author kamingpan
 * @since 2017-04-11
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class DataDictionaryServiceImpl extends BaseServiceImpl<DataDictionary, DataDictionaryDao> implements DataDictionaryService {

    @Autowired
    private DataDictionaryCache dataDictionaryCache;

    /**
     * 数据字典新增
     *
     * @param dataDictionary  数据字典
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.INSERT)
    public void insert(DataDictionary dataDictionary, AdminOperateLog adminOperateLog) {
        dataDictionary.preInsert();
        this.baseMapper.insert(dataDictionary);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.DATA_DICTIONARY);
        adminOperateLog.setBelongId(dataDictionary.getId());
        adminOperateLog.setContent(ChangeDetails.getByCreate(dataDictionary, DataDictionaryConstant.getFieldMap()));
    }

    /**
     * 数据字典修改
     *
     * @param dataDictionary  数据字典
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.UPDATE)
    public void update(DataDictionary dataDictionary, AdminOperateLog adminOperateLog) {
        DataDictionary before = this.baseMapper.selectById(dataDictionary.getId());
        if (null == before) {
            throw new DataNotExistException();
        }
        dataDictionary.preUpdate();
        this.baseMapper.updateById(dataDictionary);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.DATA_DICTIONARY);
        adminOperateLog.setBelongId(dataDictionary.getId());
        adminOperateLog.setContent(ChangeDetails.getByUpdate(before, dataDictionary, DataDictionaryConstant.getFieldMap()));

        // 清除数据字典缓存
        this.dataDictionaryCache.deleteCache(before.getClazz(), before.getVariable(), before.getValue());
        this.dataDictionaryCache.deleteCache(before.getClazz(), before.getVariable(), dataDictionary.getValue());
    }

    /**
     * 数据字典删除
     *
     * @param id              数据字典主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.DELETE)
    public void delete(String id, AdminOperateLog adminOperateLog) {
        DataDictionary dataDictionary = this.baseMapper.selectById(id);
        if (null == dataDictionary) {
            throw new DataNotExistException();
        }
        this.baseMapper.deleteById(id);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.DATA_DICTIONARY);
        adminOperateLog.setBelongId(id);

        // 清除数据字典缓存
        this.dataDictionaryCache.deleteCache(dataDictionary.getClazz(),
                dataDictionary.getVariable(), dataDictionary.getValue());
    }

    /**
     * 根据数据字典查询数据字典信息
     *
     * @param dataDictionary 数据字典
     * @param pager          分页
     * @return 数据字典列表
     */
    @Override
    public List<DataDictionaryVO> listByDataDictionary(DataDictionary dataDictionary, Pager pager) {
        // 设置模糊查询
        dataDictionary.setClazz(SqlUtil.like(dataDictionary.getClazz()));
        dataDictionary.setVariable(SqlUtil.like(dataDictionary.getVariable()));
        dataDictionary.setLabel(SqlUtil.like(dataDictionary.getLabel()));
        dataDictionary.setValue(SqlUtil.like(dataDictionary.getValue()));

        // 如果不需要分页
        if (null == pager) {
            return this.baseMapper.listByDataDictionary(dataDictionary, DataStatusEnum.NOT_DELETED.getValue());
        }

        // 处理分页逻辑
        Page page = PageHelper.startPage(pager.getPageNum(), pager.getPageSize());
        if (null != pager.getOrderBy() && !pager.getOrderBy().isEmpty()) {
            page.setOrderBy(pager.getOrderBy());
        }

        // 查询数据，并设置数据总数
        List<DataDictionaryVO> dataDictionaries = this.baseMapper.listByDataDictionary(dataDictionary,
                DataStatusEnum.NOT_DELETED.getValue());
        pager.setTotal(page.getTotal());

        return dataDictionaries;
    }

    /**
     * 根据类和变量查询数据字典
     *
     * @param clazz    类
     * @param variable 变量
     * @return 数据字典列表
     */
    @Override
    public List<DataDictionaryVO> listByClazzAndVariable(String clazz, String variable) {
        return this.baseMapper.listByClazzAndVariable(clazz, variable, DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 根据主键查询数据字典详情
     *
     * @param id 主键
     * @return 数据字典vo
     */
    @Override
    public DataDictionaryVO getDataDictionaryById(String id) {
        return this.baseMapper.getDataDictionaryById(id, DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 根据类、变量和值列表查询数据字典标签列表
     *
     * @param values   值列表
     * @param clazz    类
     * @param variable 变量
     * @return 数据字典标签列表
     */
    @Override
    public List<String> listLabelByClazzAndVariable(List<String> values, String clazz, String variable) {
        return this.baseMapper.listLabelByClazzAndVariable(values, clazz, variable);
    }

}
