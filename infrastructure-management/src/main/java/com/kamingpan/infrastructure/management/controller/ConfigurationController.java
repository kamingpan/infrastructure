package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.exception.ValidateException;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.entity.constant.ConfigurationConstant;
import com.kamingpan.infrastructure.entity.group.ConfigurationGroup;
import com.kamingpan.infrastructure.entity.model.dto.ConfigurationDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.Configuration;
import com.kamingpan.infrastructure.entity.model.vo.ConfigurationVO;
import com.kamingpan.infrastructure.entity.service.ConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 配置信息 前端控制器
 *
 * @author kamingpan
 * @since 2020-01-11
 */
@Slf4j
@RestController
@RequestMapping("/configuration")
public class ConfigurationController {

    @Autowired
    private ConfigurationService configurationService;

    /**
     * 配置信息查询
     *
     * @param configuration 配置信息dto
     * @param pager         分页
     * @return 响应数据
     */
    @GetMapping("")
    @PreAuthorize(ConfigurationConstant.Authentication.LIST)
    public ResponseData list(@ModelAttribute ConfigurationDTO configuration, @ModelAttribute Pager pager) {
        log.debug("查询配置信息列表...");

        List<ConfigurationVO> configurations = this.configurationService.listByConfiguration(configuration, pager);
        return ResponseData.buildPagination(configurations, pager);
    }

    /**
     * 配置信息详情
     *
     * @param id 配置信息主键
     * @return 响应数据
     */
    @GetMapping("/{id}")
    @PreAuthorize(ConfigurationConstant.Authentication.INFO)
    public ResponseData info(@PathVariable("id") final String id) {
        log.debug("查询配置信息“{}”详情...", id);

        ConfigurationVO configuration = this.configurationService.getConfigurationById(id);
        if (null == configuration) {
            return ResponseData.build(ResponseStatus.DATA_IS_NOT_EXIST);
        }
        return ResponseData.build(configuration);
    }

    /**
     * 配置信息新增
     *
     * @param configuration 配置信息
     * @return 响应数据
     */
    @PostMapping("")
    @PreAuthorize(ConfigurationConstant.Authentication.INSERT)
    public ResponseData insert(@ModelAttribute @Validated(ConfigurationGroup.Insert.class) ConfigurationDTO configuration) {
        log.debug("新增配置信息“{}”...", configuration.getConfigKey());
        this.configurationService.insert(configuration.toConfiguration(), new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 配置信息修改
     *
     * @param id               配置信息主键
     * @param configurationDTO 配置信息
     * @return 响应数据
     */
    @PutMapping("/{id}")
    @PreAuthorize(ConfigurationConstant.Authentication.UPDATE)
    public ResponseData update(@PathVariable("id") String id,
                               @ModelAttribute @Validated(ConfigurationGroup.Update.class) ConfigurationDTO configurationDTO) {
        log.debug("更新配置信息“{}”...", configurationDTO.getConfigKey());

        Configuration configuration = configurationDTO.toConfiguration();
        configuration.setId(id);
        this.configurationService.update(configuration, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 配置信息删除
     *
     * @param id 配置信息主键
     * @return 响应数据
     */
    @DeleteMapping("/{id}")
    @PreAuthorize(ConfigurationConstant.Authentication.DELETE)
    public ResponseData delete(@PathVariable("id") String id) {
        log.debug("删除配置信息“{}”...", id);

        this.configurationService.delete(id, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 配置信息批量删除
     *
     * @param ids 配置信息主键列表
     * @return 响应数据
     */
    @DeleteMapping("")
    @PreAuthorize(ConfigurationConstant.Authentication.DELETE)
    public ResponseData deleteByIds(@RequestParam("ids") List<String> ids) {
        log.debug("删除配置信息“{}”...", ids);
        if (null == ids || ids.isEmpty()) {
            throw new ValidateException();
        }

        this.configurationService.deleteByIds(ids);
        return ResponseData.success();
    }

    /**
     * 判断配置键是否存在
     *
     * @param id        当前配置信息主键
     * @param configKey 配置键
     * @return 响应数据
     */
    @GetMapping("/config-key-exist")
    public ResponseData nameExist(@RequestParam(value = "id", required = false) final String id,
                                  @RequestParam("configKey") final String configKey) {
        log.debug("查询配置键“{}”是否存在...", configKey);

        long count = this.configurationService.countByConfigKey(id, configKey);
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        result.put("result", count > 0);
        return ResponseData.build(result);
    }

}

