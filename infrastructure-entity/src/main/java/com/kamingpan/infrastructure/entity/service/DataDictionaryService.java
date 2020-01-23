package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.DataDictionary;
import com.kamingpan.infrastructure.entity.model.vo.DataDictionaryVO;

import java.util.List;

/**
 * 数据字典（管理所有实体用键表示的值） 服务类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
public interface DataDictionaryService extends BaseService<DataDictionary> {

    /**
     * 数据字典新增
     *
     * @param dataDictionary  数据字典
     * @param adminOperateLog 操作日志
     */
    void insert(DataDictionary dataDictionary, AdminOperateLog adminOperateLog);

    /**
     * 数据字典修改
     *
     * @param dataDictionary  数据字典
     * @param adminOperateLog 操作日志
     */
    void update(DataDictionary dataDictionary, AdminOperateLog adminOperateLog);

    /**
     * 数据字典删除
     *
     * @param id              数据字典主键
     * @param adminOperateLog 操作日志
     */
    void delete(String id, AdminOperateLog adminOperateLog);

    /**
     * 根据数据字典查询数据字典信息
     *
     * @param dataDictionary 数据字典
     * @param pager          分页
     * @return 数据字典列表
     */
    List<DataDictionaryVO> listByDataDictionary(DataDictionary dataDictionary, Pager pager);

    /**
     * 根据类和变量查询数据字典
     *
     * @param clazz    类
     * @param variable 变量
     * @return 数据字典列表
     */
    List<DataDictionaryVO> listByClazzAndVariable(String clazz, String variable);

    /**
     * 根据主键查询数据字典详情
     *
     * @param id 主键
     * @return 数据字典vo
     */
    DataDictionaryVO getDataDictionaryById(String id);

    /**
     * 根据类、变量和值列表查询数据字典标签列表
     *
     * @param values   值列表
     * @param clazz    类
     * @param variable 变量
     * @return 数据字典标签列表
     */
    List<String> listLabelByClazzAndVariable(List<String> values, String clazz, String variable);

}
