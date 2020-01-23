package com.kamingpan.infrastructure.entity.util;

import com.kamingpan.infrastructure.core.base.enumeration.DataStatusEnum;
import com.kamingpan.infrastructure.entity.dao.ConfigurationDao;
import com.kamingpan.infrastructure.util.conversion.JsonConversion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * ConfigurationUtil
 *
 * @author kamingpan
 * @since 2020-01-11
 */
@Slf4j
@Component
public class ConfigurationUtil {

    @Autowired
    private ConfigurationDao configurationDao;

    /**
     * 根据配置键查询配置对象
     *
     * @param configKey 配置键
     * @param clazz     转换对象类型
     * @param <T>       对象泛型
     * @return 配置对象
     */
    public <T> T getObjectByConfigKey(String configKey, Class<T> clazz) {
        if (null == configKey || configKey.isEmpty()) {
            throw new NullPointerException("配置键不能为空");
        } else if (null == clazz) {
            throw new NullPointerException("转换对象不能为空");
        }

        // 查询配置信息
        String configValue = this.configurationDao.getConfigValueByConfigKey(configKey,
                DataStatusEnum.NOT_DELETED.getValue());
        if (null == configValue || configValue.isEmpty()) {
            return null;
        }

        return JsonConversion.convertToObjectWithoutException(configValue, clazz);
    }

}
