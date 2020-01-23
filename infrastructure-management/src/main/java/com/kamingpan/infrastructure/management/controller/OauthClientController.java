package com.kamingpan.infrastructure.management.controller;

import com.kamingpan.infrastructure.core.exception.ValidateException;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.core.response.ResponseData;
import com.kamingpan.infrastructure.core.response.ResponseStatus;
import com.kamingpan.infrastructure.entity.constant.OauthClientConstant;
import com.kamingpan.infrastructure.entity.group.OauthClientGroup;
import com.kamingpan.infrastructure.entity.model.dto.OauthClientDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.OauthClient;
import com.kamingpan.infrastructure.entity.model.vo.OauthClientVO;
import com.kamingpan.infrastructure.entity.service.OauthClientService;
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
 * 授权客户端 前端控制器
 *
 * @author kamingpan
 * @since 2019-04-12
 */
@Slf4j
@RestController
@RequestMapping("/oauth-client")
public class OauthClientController {

    @Autowired
    private OauthClientService oauthClientService;

    /**
     * 授权客户端查询
     *
     * @param oauthClient 授权客户端dto
     * @param pager       分页
     * @return 响应数据
     */
    @GetMapping("")
    @PreAuthorize(OauthClientConstant.Authentication.LIST)
    public ResponseData list(@ModelAttribute OauthClientDTO oauthClient, @ModelAttribute Pager pager) {
        log.debug("查询授权客户端列表...");

        List<OauthClientVO> oauthClients = this.oauthClientService.listByOauthClient(oauthClient, pager);
        return ResponseData.buildPagination(oauthClients, pager);
    }

    /**
     * 授权客户端详情
     *
     * @param id 授权客户端主键
     * @return 响应数据
     */
    @GetMapping("/{id}")
    @PreAuthorize(OauthClientConstant.Authentication.INFO)
    public ResponseData info(@PathVariable("id") final String id) {
        log.debug("查询授权客户端“{}”详情...", id);

        OauthClientVO oauthClient = this.oauthClientService.getOauthClientById(id);
        if (null == oauthClient) {
            return ResponseData.build(ResponseStatus.DATA_IS_NOT_EXIST);
        }
        return ResponseData.build(oauthClient);
    }

    /**
     * 授权客户端新增
     *
     * @param oauthClientDTO 授权客户端
     * @return 响应数据
     */
    @PostMapping("")
    @PreAuthorize(OauthClientConstant.Authentication.INSERT)
    public ResponseData insert(@ModelAttribute @Validated(OauthClientGroup.Insert.class) OauthClientDTO oauthClientDTO) {
        log.debug("新增授权客户端“{}”...", oauthClientDTO.getName());

        OauthClient oauthClient = oauthClientDTO.toOauthClient();
        oauthClient.setClientId(null); // 授权客户端id不允许传入值初始化
        oauthClient.setClientSecret(null); // 授权客户端密钥不允许传入值初始化
        this.oauthClientService.insert(oauthClient, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 授权客户端修改
     *
     * @param id             授权客户端主键
     * @param oauthClientDTO 授权客户端
     * @return 响应数据
     */
    @PutMapping("/{id}")
    @PreAuthorize(OauthClientConstant.Authentication.UPDATE)
    public ResponseData update(@PathVariable("id") String id,
                               @ModelAttribute @Validated(OauthClientGroup.Update.class) OauthClientDTO oauthClientDTO) {
        log.debug("更新授权客户端“{}”...", oauthClientDTO.getName());

        OauthClient oauthClient = oauthClientDTO.toOauthClient();
        oauthClient.setId(id);
        this.oauthClientService.update(oauthClient, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 授权客户端删除
     *
     * @param id 授权客户端主键
     * @return 响应数据
     */
    @DeleteMapping("/{id}")
    @PreAuthorize(OauthClientConstant.Authentication.DELETE)
    public ResponseData delete(@PathVariable("id") String id) {
        log.debug("删除授权客户端“{}”...", id);

        this.oauthClientService.delete(id, new AdminOperateLog());
        return ResponseData.success();
    }

    /**
     * 授权客户端批量删除
     *
     * @param ids 授权客户端主键列表
     * @return 响应数据
     */
    @DeleteMapping("")
    @PreAuthorize(OauthClientConstant.Authentication.DELETE)
    public ResponseData deleteByIds(@RequestParam("ids") List<String> ids) {
        log.debug("删除授权客户端“{}”...", ids);
        if (null == ids || ids.isEmpty()) {
            throw new ValidateException();
        }

        this.oauthClientService.deleteByIds(ids);
        return ResponseData.success();
    }

    /**
     * 判断授权客户端名称是否存在
     *
     * @param name 授权客户端名称
     * @param id   主键
     * @return 响应数据
     */
    @GetMapping("/name-exist")
    public ResponseData nameExist(@RequestParam("name") final String name,
                                  @RequestParam(value = "id", required = false) final String id) {
        log.debug("查询授权客户端“{}”是否存在...", name);

        int count = this.oauthClientService.countByName(name, id);
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        result.put("result", count > 0);
        return ResponseData.build(result);
    }

    /**
     * 判断授权客户端id是否存在
     *
     * @param clientId 授权客户端id
     * @param id       主键
     * @return 响应数据
     */
    @GetMapping("/client-exist")
    public ResponseData clientIdExist(@RequestParam("clientId") final String clientId,
                                      @RequestParam("id") final String id) {
        log.debug("查询授权客户端id“{}”是否存在...", clientId);

        int count = this.oauthClientService.countByClientId(clientId, id);
        Map<String, Boolean> result = new HashMap<String, Boolean>();
        result.put("result", count > 0);
        return ResponseData.build(result);
    }

}

