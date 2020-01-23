package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.base.controller.BaseController;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.entity.constant.ManagementInformationConstant;
import com.kamingpan.infrastructure.entity.group.ManagementInformationGroup;
import com.kamingpan.infrastructure.entity.model.dto.ManagementInformationDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.vo.ManagementInformationVO;
import com.kamingpan.infrastructure.entity.service.ManagementInformationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * 管理端信息 前端控制器
 *
 * @author kamingpan
 * @since 2017-08-31
 */
@Slf4j
@RestController
@RequestMapping("/management-information")
public class ManagementInformationController extends BaseController {

    @Autowired
    private ManagementInformationService managementInformationService;

    /**
     * 查询管理端信息详情
     *
     * @return 响应数据
     */
    @GetMapping("")
    @PreAuthorize(ManagementInformationConstant.Authentication.INFO)
    public ResponseData info() {
        log.debug("查询管理端信息详情");

        ManagementInformationVO managementInformation
                = this.managementInformationService.getManagementInformationById(ManagementInformationConstant.Id.DEFAULT);
        return ResponseData.build(managementInformation);
    }

    /**
     * 修改管理端信息
     *
     * @param managementInformation 管理端信息
     * @return 响应数据
     * @throws IOException io异常
     */
    @PutMapping("")
    @PreAuthorize(ManagementInformationConstant.Authentication.UPDATE)
    public ResponseData update(@ModelAttribute @Validated(ManagementInformationGroup.Update.class)
                                       ManagementInformationDTO managementInformation) throws IOException {
        log.debug("更新管理端信息...");

        managementInformation.setId(ManagementInformationConstant.Id.DEFAULT);
        this.managementInformationService.update(managementInformation.toManagementInformation(), new AdminOperateLog());

        return ResponseData.success();
    }

}
