package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.entity.OauthToken;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 授权token Mapper 接口
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Repository
public interface OauthTokenDao extends BaseDao<OauthToken> {

    /**
     * 根据用户主键和refreshToken删除对象
     *
     * @param userId       用户主键
     * @param refreshToken 刷新token
     * @return 删除数量
     */
    int deleteByUserIdAndRefreshToken(@Param("userId") String userId, @Param("refreshToken") String refreshToken);

    /**
     * 根据用户主键和客户端id查询认证token对象
     *
     * @param userId   用户主键
     * @param clientId 客户端id
     * @param deleted  数据类型
     * @return 认证token
     */
    OauthToken getByUserId(@Param("userId") String userId, @Param("clientId") String clientId,
                           @Param("deleted") Integer deleted);

    /**
     * 根据accessToken查询对象
     *
     * @param accessToken token
     * @param deleted     数据类型
     * @return 认证token
     */
    OauthToken getByAccessToken(@Param("accessToken") String accessToken, @Param("deleted") Integer deleted);

    /**
     * 根据refreshToken查询对象
     *
     * @param refreshToken 刷新token
     * @param deleted      数据类型
     * @return 认证token
     */
    OauthToken getByRefreshToken(@Param("refreshToken") String refreshToken, @Param("deleted") Integer deleted);

}
