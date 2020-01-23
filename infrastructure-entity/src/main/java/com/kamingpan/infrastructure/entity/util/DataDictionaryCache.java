package com.kamingpan.infrastructure.entity.util;

import com.kamingpan.infrastructure.core.base.enumeration.DataStatusEnum;
import com.kamingpan.infrastructure.entity.dao.DataDictionaryDao;
import com.kamingpan.infrastructure.entity.model.vo.DataDictionaryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据字典缓存
 *
 * @author kamingpan
 * @since 2020-01-09
 */
@Component
public class DataDictionaryCache {

    private static final String KEY_FORMAT = "%s:%s:%s";

    private Map<String, String> dataDictionaryMap = new HashMap<String, String>();

    @Autowired
    private DataDictionaryDao dataDictionaryDao;

    /**
     * 更新数据字典缓存
     *
     * @param clazz    类/表
     * @param variable 变量/字段
     * @param value    值
     * @param label    缓存标签
     * @return 缓存的键
     */
    public String updateCache(String clazz, String variable, String value, String label) {
        String key = String.format(DataDictionaryCache.KEY_FORMAT, clazz, variable, value);
        this.dataDictionaryMap.put(key, label);
        return key;
    }

    /**
     * 删除数据字典缓存
     *
     * @param clazz    类/表
     * @param variable 变量/字段
     * @param value    值
     * @return 缓存的键
     */
    public String deleteCache(String clazz, String variable, String value) {
        String key = String.format(DataDictionaryCache.KEY_FORMAT, clazz, variable, value);
        this.dataDictionaryMap.remove(key);
        return key;
    }

    /**
     * 获取数据字典标签
     *
     * @param clazz    类/表
     * @param variable 变量/字段
     * @param value    值
     * @return 数据字典标签
     */
    public String getLabel(String clazz, String variable, String value) {
        String key = String.format(DataDictionaryCache.KEY_FORMAT, clazz, variable, value);
        String label = this.dataDictionaryMap.get(key);
        if (null != label) {
            return label;
        }

        DataDictionaryVO dataDictionaryVO = this.dataDictionaryDao
                .getByClazzAndVariableAndValue(clazz, variable, value, DataStatusEnum.NOT_DELETED.getValue());
        if (null == dataDictionaryVO) {
            return null;
        }

        this.dataDictionaryMap.put(key, dataDictionaryVO.getLabel());
        return dataDictionaryVO.getLabel();
    }

}
