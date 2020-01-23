package com.kamingpan.infrastructure.entity.service.impl;

import com.kamingpan.infrastructure.core.base.enumeration.DataStatusEnum;
import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.entity.constant.OauthTokenConstant;
import com.kamingpan.infrastructure.entity.dao.OauthTokenDao;
import com.kamingpan.infrastructure.entity.model.entity.OauthToken;
import com.kamingpan.infrastructure.entity.service.OauthTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 授权token 服务实现类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class OauthTokenServiceImpl extends BaseServiceImpl<OauthToken, OauthTokenDao> implements OauthTokenService {

    /**
     * 插入授权token
     *
     * @param oauthToken 授权token
     * @return 是否插入成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(OauthToken oauthToken) {
        if (null == oauthToken) {
            return 0;
        }

        // 设置当前时间
        Date now = new Date();
        oauthToken.setRefreshTime(now);
        oauthToken.setLoginTime(now);

        // 设置token有效时长
        oauthToken.setAccessTokenValidity(OauthTokenConstant.ValidityTime.TWO_HOURS);
        oauthToken.setRefreshTokenValidity(OauthTokenConstant.ValidityTime.THIRTY_DAYS);

        oauthToken.initId();
        return super.baseMapper.insert(oauthToken);
    }

    /**
     * 更新授权token
     *
     * @param oauthToken 授权token
     * @return 更新行数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(OauthToken oauthToken) {
        if (null == oauthToken || null == oauthToken.getId() || oauthToken.getId().isEmpty()) {
            return 0;
        }

        // 设置当前时间
        Date now = new Date();
        oauthToken.setRefreshTime(now);
        oauthToken.setLoginTime(now);

        // 设置token有效时长
        oauthToken.setAccessTokenValidity(OauthTokenConstant.ValidityTime.TWO_HOURS);
        oauthToken.setRefreshTokenValidity(OauthTokenConstant.ValidityTime.THIRTY_DAYS);

        return super.baseMapper.updateById(oauthToken);
    }

    /**
     * 根据用户主键和refreshToken删除对象
     *
     * @param userId       用户主键
     * @param refreshToken 刷新token
     * @return 删除数量
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteByUserIdAndRefreshToken(String userId, String refreshToken) {
        return super.baseMapper.deleteByUserIdAndRefreshToken(userId, refreshToken);
    }

    /**
     * 根据用户主键和客户端id查询认证token对象
     *
     * @param userId   用户主键
     * @param clientId 客户端id
     * @return 认证token
     */
    @Override
    public OauthToken getByUserId(String userId, String clientId) {
        if (null == userId || userId.isEmpty() || null == clientId || clientId.isEmpty()) {
            return null;
        }

        return super.baseMapper.getByUserId(userId, clientId, DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 根据accessToken查询对象
     *
     * @param accessToken token
     * @return 认证token
     */
    @Override
    public OauthToken getByAccessToken(String accessToken) {
        if (null == accessToken || accessToken.isEmpty()) {
            return null;
        }

        return super.baseMapper.getByAccessToken(accessToken, DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 根据refreshToken查询对象
     *
     * @param refreshToken 刷新token
     * @return 认证token
     */
    @Override
    public OauthToken getByRefreshToken(String refreshToken) {
        if (null == refreshToken || refreshToken.isEmpty()) {
            return null;
        }

        return super.baseMapper.getByRefreshToken(refreshToken, DataStatusEnum.NOT_DELETED.getValue());
    }

}
