package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.entity.model.dto.OauthClientDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.OauthClient;
import com.kamingpan.infrastructure.entity.model.vo.OauthClientVO;

import java.util.List;

/**
 * 授权客户端 服务类
 *
 * @author kamingpan
 * @since 2019-04-12
 */
public interface OauthClientService extends BaseService<OauthClient> {

    /**
     * 新增授权客户端.
     *
     * @param oauthClient     授权客户端
     * @param adminOperateLog 操作日志
     */
    void insert(OauthClient oauthClient, AdminOperateLog adminOperateLog);

    /**
     * 修改授权客户端
     *
     * @param oauthClient     授权客户端
     * @param adminOperateLog 操作日志
     */
    void update(OauthClient oauthClient, AdminOperateLog adminOperateLog);

    /**
     * 授权客户端删除
     *
     * @param id              授权客户端主键
     * @param adminOperateLog 操作日志
     */
    void delete(String id, AdminOperateLog adminOperateLog);

    /**
     * 授权客户端批量删除
     *
     * @param ids 授权客户端主键列表
     */
    void deleteByIds(List<String> ids);

    /**
     * 根据授权客户端查询授权客户端信息
     *
     * @param oauthClient 授权客户端dto
     * @param pager       分页
     * @return 授权客户端vo列表
     */
    List<OauthClientVO> listByOauthClient(OauthClientDTO oauthClient, Pager pager);

    /**
     * 根据主键查询授权客户端详情
     *
     * @param id 主键
     * @return 授权客户端vo
     */
    OauthClientVO getOauthClientById(String id);

    /**
     * 根据授权客户端名称查询授权客户端数量
     *
     * @param name 授权客户端名称
     * @param id   主键
     * @return 授权客户端数量
     */
    int countByName(String name, String id);

    /**
     * 根据授权客户端id查询授权客户端数量
     *
     * @param clientId 授权客户端id
     * @param id       主键
     * @return 授权客户端数量
     */
    int countByClientId(String clientId, String id);

    /**
     * 根据客户端id和客户端密钥查询授权客户端
     *
     * @param clientId     客户端id
     * @param clientSecret 客户端密钥
     * @return 授权客户端
     */
    OauthClient getByClientIdAndSecret(String clientId, String clientSecret);

}
