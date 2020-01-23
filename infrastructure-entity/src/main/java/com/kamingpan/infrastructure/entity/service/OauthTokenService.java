package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.entity.model.entity.OauthToken;

/**
 * 授权token 服务类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
public interface OauthTokenService extends BaseService<OauthToken> {

    /**
     * 根据用户主键和refreshToken删除对象
     *
     * @param userId       用户主键
     * @param refreshToken 刷新token
     * @return 删除数量
     */
    int deleteByUserIdAndRefreshToken(String userId, String refreshToken);

    /**
     * 根据用户主键和客户端id查询认证token对象
     *
     * @param userId   用户主键
     * @param clientId 客户端id
     * @return 认证token
     */
    OauthToken getByUserId(String userId, String clientId);

    /**
     * 根据accessToken查询对象
     *
     * @param accessToken token
     * @return 认证token
     */
    OauthToken getByAccessToken(String accessToken);

    /**
     * 根据refreshToken查询对象
     *
     * @param refreshToken 刷新token
     * @return 认证token
     */
    OauthToken getByRefreshToken(String refreshToken);

}
