package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.entity.DataDictionary;
import com.kamingpan.infrastructure.entity.model.vo.DataDictionaryVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 数据字典（管理所有实体用键表示的值） Mapper 接口
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Repository
public interface DataDictionaryDao extends BaseDao<DataDictionary> {

    /**
     * 根据数据字典查询数据字典信息
     *
     * @param dataDictionary 数据字典
     * @param deleted        数据状态
     * @return 数据字典列表
     */
    List<DataDictionaryVO> listByDataDictionary(@Param("dataDictionary") DataDictionary dataDictionary,
                                                @Param("deleted") Integer deleted);

    /**
     * 根据类和变量查询数据字典
     *
     * @param clazz    类
     * @param variable 变量
     * @param deleted  数据状态
     * @return 数据字典列表
     */
    List<DataDictionaryVO> listByClazzAndVariable(@Param("clazz") String clazz, @Param("variable") String variable,
                                                  @Param("deleted") Integer deleted);

    /**
     * 根据类、变量和值查询数据字典
     *
     * @param clazz    类
     * @param variable 变量
     * @param value    值
     * @param deleted  数据状态
     * @return 数据字典列表
     */
    DataDictionaryVO getByClazzAndVariableAndValue(@Param("clazz") String clazz, @Param("variable") String variable,
                                                   @Param("value") String value, @Param("deleted") Integer deleted);

    /**
     * 根据主键查询数据字典详情
     *
     * @param id      主键
     * @param deleted 数据状态
     * @return 数据字典vo
     */
    DataDictionaryVO getDataDictionaryById(@Param("id") String id, @Param("deleted") Integer deleted);

    /**
     * 根据类、变量和值列表查询数据字典标签列表
     *
     * @param values   值列表
     * @param clazz    类
     * @param variable 变量
     * @return 数据字典标签列表
     */
    List<String> listLabelByClazzAndVariable(@Param("values") List<String> values, @Param("clazz") String clazz,
                                             @Param("variable") String variable);

}
