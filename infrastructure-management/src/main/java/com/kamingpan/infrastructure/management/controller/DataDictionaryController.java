package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.entity.constant.DataDictionaryConstant;
import com.kamingpan.infrastructure.entity.group.DataDictionaryGroup;
import com.kamingpan.infrastructure.entity.model.dto.DataDictionaryDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.DataDictionary;
import com.kamingpan.infrastructure.entity.model.vo.DataDictionaryVO;
import com.kamingpan.infrastructure.entity.service.DataDictionaryService;
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
import org.springframework.web.bind.annotation.RestController;

/**
 * 数据字典 controller
 *
 * @author kamingpan
 * @since 2017-06-30
 */
@Slf4j
@RestController
@RequestMapping("/data-dictionary")
public class DataDictionaryController extends BaseController {

    @Autowired
    private DataDictionaryService dataDictionaryService;

    /**
     * 数据字典查询
     *
     * @param dataDictionary 数据字典
     * @param pager          分页
     * @return 响应数据
     */
    @GetMapping("")
    @PreAuthorize(DataDictionaryConstant.Authentication.LIST)
    public ResponseData list(@ModelAttribute DataDictionaryDTO dataDictionary, @ModelAttribute Pager pager) {
        log.debug("查询数据字典列表...");

        return ResponseData.buildPagination(this.dataDictionaryService
                .listByDataDictionary(dataDictionary.toDataDictionary(), pager), pager);
    }

    /**
     * 数据字典详情
     *
     * @param id 数据字典主键
     * @return 响应数据
     */
    @GetMapping("/{id}")
    @PreAuthorize(DataDictionaryConstant.Authentication.INFO)
    public ResponseData info(@PathVariable("id") String id) {
        log.debug("查询数据字典“{}”详情...", id);

        DataDictionaryVO dataDictionary = this.dataDictionaryService.getDataDictionaryById(id);
        if (null == dataDictionary) {
            return ResponseData.build(ResponseStatus.DATA_IS_NOT_EXIST);
        }
        return ResponseData.build(dataDictionary);
    }

    /**
     * 数据字典新增
     *
     * @param dataDictionary 数据字典
     * @return 响应数据
     */
    @PostMapping("")
    @PreAuthorize(DataDictionaryConstant.Authentication.INSERT)
    public ResponseData insert(@ModelAttribute @Validated(DataDictionaryGroup.Insert.class)
                                       DataDictionaryDTO dataDictionary) {
        log.debug("新增数据字典“{}”类“{}”字段“{}”键“{}”值...", dataDictionary.getClazz(),
                dataDictionary.getVariable(), dataDictionary.getLabel(), dataDictionary.getValue());

        this.dataDictionaryService.insert(dataDictionary.toDataDictionary(), new AdminOperateLog());

        return ResponseData.success();
    }

    /**
     * 数据字典修改
     *
     * @param id                数据字典主键
     * @param dataDictionaryDTO 数据字典
     * @return 响应数据
     */
    @PutMapping("/{id}")
    @PreAuthorize(DataDictionaryConstant.Authentication.UPDATE)
    public ResponseData update(@PathVariable("id") String id,
                               @ModelAttribute @Validated(DataDictionaryGroup.Update.class)
                                       DataDictionaryDTO dataDictionaryDTO) {
        log.debug("更新数据字典“{}”...", id);

        DataDictionary dataDictionary = dataDictionaryDTO.toDataDictionary();
        dataDictionary.setId(id);
        dataDictionary.setClazz(null); // 类不允许修改
        dataDictionary.setVariable(null); // 变量不允许修改
        this.dataDictionaryService.update(dataDictionary, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 数据字典删除
     *
     * @param id 数据字典主键
     * @return 响应数据
     */
    @DeleteMapping("/{id}")
    @PreAuthorize(DataDictionaryConstant.Authentication.DELETE)
    public ResponseData delete(@PathVariable String id) {
        log.debug("删除数据字典“{}”...", id);

        this.dataDictionaryService.delete(id, new AdminOperateLog());
        return ResponseData.success();
    }

}
