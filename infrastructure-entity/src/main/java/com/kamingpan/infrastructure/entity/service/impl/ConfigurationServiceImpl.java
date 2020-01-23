package com.kamingpan.infrastructure.entity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kamingpan.infrastructure.core.base.enumeration.DataStatusEnum;
import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.core.exception.DataNotExistException;
import com.kamingpan.infrastructure.core.exception.ValidateException;
import com.kamingpan.infrastructure.core.log.OperateLog;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.core.util.ChangeDetails;
import com.kamingpan.infrastructure.entity.constant.AdminOperateLogConstant;
import com.kamingpan.infrastructure.entity.constant.ConfigurationConstant;
import com.kamingpan.infrastructure.entity.dao.ConfigurationDao;
import com.kamingpan.infrastructure.entity.model.dto.ConfigurationDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.Configuration;
import com.kamingpan.infrastructure.entity.model.vo.ConfigurationVO;
import com.kamingpan.infrastructure.entity.service.ConfigurationService;
import com.kamingpan.infrastructure.util.sql.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 配置信息 服务实现类
 *
 * @author kamingpan
 * @since 2020-01-11
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class ConfigurationServiceImpl extends BaseServiceImpl<Configuration, ConfigurationDao> implements ConfigurationService {

    /**
     * 新增配置信息.
     *
     * @param configuration   配置信息
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.INSERT)
    public void insert(Configuration configuration, AdminOperateLog adminOperateLog) {
        if (0 < this.countByConfigKey(null, configuration.getConfigKey())) {
            throw new ValidateException("该配置键已存在");
        }

        this.insert(configuration);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.CONFIGURATION);
        adminOperateLog.setBelongId(configuration.getId());
        adminOperateLog.setContent(ChangeDetails.getByCreate(configuration, ConfigurationConstant.getFieldMap()));
    }

    /**
     * 修改配置信息
     *
     * @param configuration   配置信息
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.UPDATE)
    public void update(Configuration configuration, AdminOperateLog adminOperateLog) {
        // 判断是否已有重复配置键的数据
        if (0 < this.countByConfigKey(configuration.getId(), configuration.getConfigKey())) {
            throw new ValidateException("该配置键已存在");
        }

        // 查询原来的配置信息
        Configuration before = this.getById(configuration.getId());
        if (null == before) {
            throw new DataNotExistException();
        }

        this.update(configuration);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.CONFIGURATION);
        adminOperateLog.setBelongId(configuration.getId());
        adminOperateLog.setContent(ChangeDetails.getByUpdate(before, configuration, ConfigurationConstant.getFieldMap()));
    }

    /**
     * 配置信息删除
     *
     * @param id              配置信息主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.DELETE)
    public void delete(String id, AdminOperateLog adminOperateLog) {
        // 判断配置信息是否存在
        Configuration configuration = this.baseMapper.selectById(id);
        if (null == configuration) {
            throw new DataNotExistException();
        }

        this.baseMapper.deleteById(id);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.CONFIGURATION);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 配置信息批量删除
     *
     * @param ids 配置信息主键列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> ids) {
        for (String id : ids) {
            if (null != id && !id.isEmpty()) {
                this.delete(id, new AdminOperateLog());
            }
        }
    }

    /**
     * 根据配置键查询配置信息数量（排除当前权限）
     *
     * @param id        当前配置信息主键
     * @param configKey 配置键
     * @return 配置信息数量
     */
    @Override
    public int countByConfigKey(String id, String configKey) {
        if (null == configKey || configKey.isEmpty()) {
            return 0;
        }

        QueryWrapper<Configuration> configurationQueryWrapper = new QueryWrapper<Configuration>();
        configurationQueryWrapper.eq("config_key", configKey);
        if (null != id && !id.isEmpty()) {
            configurationQueryWrapper.and(function -> function.ne("id", id));
        }

        return super.baseMapper.selectCount(configurationQueryWrapper);
    }

    /**
     * 根据配置信息查询配置信息信息
     *
     * @param configuration 配置信息dto
     * @param pager         分页
     * @return 配置信息vo列表
     */
    @Override
    public List<ConfigurationVO> listByConfiguration(ConfigurationDTO configuration, Pager pager) {
        configuration.setTitle(SqlUtil.like(configuration.getTitle()));
        configuration.setConfigKey(SqlUtil.like(configuration.getConfigKey()));

        // 如果不需要分页
        if (null == pager) {
            return this.baseMapper.listByConfiguration(configuration, DataStatusEnum.NOT_DELETED.getValue());
        }

        // 处理分页逻辑
        Page page = PageHelper.startPage(pager.getPageNum(), pager.getPageSize());
        if (null != pager.getOrderBy() && !pager.getOrderBy().isEmpty()) {
            page.setOrderBy(pager.getOrderBy());
        }

        // 查询数据，并设置数据总数
        List<ConfigurationVO> configurations = this.baseMapper.listByConfiguration(configuration,
                DataStatusEnum.NOT_DELETED.getValue());
        pager.setTotal(page.getTotal());

        return configurations;
    }

    /**
     * 根据主键查询配置信息详情
     *
     * @param id 主键
     * @return 配置信息vo
     */
    @Override
    public ConfigurationVO getConfigurationById(String id) {
        return this.baseMapper.getConfigurationById(id, DataStatusEnum.NOT_DELETED.getValue());
    }

}
