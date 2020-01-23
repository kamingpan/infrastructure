package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.entity.model.dto.ConfigurationDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.Configuration;
import com.kamingpan.infrastructure.entity.model.vo.ConfigurationVO;

import java.util.List;

/**
 * 配置信息 服务类
 *
 * @author kamingpan
 * @since 2020-01-11
 */
public interface ConfigurationService extends BaseService<Configuration> {

    /**
     * 新增配置信息.
     *
     * @param configuration   配置信息
     * @param adminOperateLog 操作日志
     */
    void insert(Configuration configuration, AdminOperateLog adminOperateLog);

    /**
     * 修改配置信息
     *
     * @param configuration   配置信息
     * @param adminOperateLog 操作日志
     */
    void update(Configuration configuration, AdminOperateLog adminOperateLog);

    /**
     * 配置信息删除
     *
     * @param id              配置信息主键
     * @param adminOperateLog 操作日志
     */
    void delete(String id, AdminOperateLog adminOperateLog);

    /**
     * 配置信息批量删除
     *
     * @param ids 配置信息主键列表
     */
    void deleteByIds(List<String> ids);

    /**
     * 根据配置键查询配置信息数量（排除当前权限）
     *
     * @param id        当前配置信息主键
     * @param configKey 配置键
     * @return 配置信息数量
     */
    int countByConfigKey(String id, String configKey);

    /**
     * 根据配置信息查询配置信息信息
     *
     * @param configuration 配置信息dto
     * @param pager         分页
     * @return 配置信息vo列表
     */
    List<ConfigurationVO> listByConfiguration(ConfigurationDTO configuration, Pager pager);

    /**
     * 根据主键查询配置信息详情
     *
     * @param id 主键
     * @return 配置信息vo
     */
    ConfigurationVO getConfigurationById(String id);

}
