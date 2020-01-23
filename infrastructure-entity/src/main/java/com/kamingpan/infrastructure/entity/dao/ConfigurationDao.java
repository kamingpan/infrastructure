package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.dto.ConfigurationDTO;
import com.kamingpan.infrastructure.entity.model.entity.Configuration;
import com.kamingpan.infrastructure.entity.model.vo.ConfigurationVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 配置信息 Mapper 接口
 *
 * @author kamingpan
 * @since 2020-01-11
 */
@Repository
public interface ConfigurationDao extends BaseDao<Configuration> {

    /**
     * 根据配置信息查询配置信息列表
     *
     * @param configuration 配置信息
     * @param deleted       数据状态
     * @return 配置信息列表
     */
    List<ConfigurationVO> listByConfiguration(@Param("configuration") ConfigurationDTO configuration,
                                              @Param("deleted") Integer deleted);

    /**
     * 根据主键查询配置信息详情
     *
     * @param id      主键
     * @param deleted 数据状态
     * @return 配置信息vo
     */
    ConfigurationVO getConfigurationById(@Param("id") String id, @Param("deleted") Integer deleted);

    /**
     * 根据配置键查询配置值
     *
     * @param configKey 配置键
     * @param deleted   数据状态
     * @return 配置值
     */
    String getConfigValueByConfigKey(@Param("configKey") String configKey, @Param("deleted") Integer deleted);

}
