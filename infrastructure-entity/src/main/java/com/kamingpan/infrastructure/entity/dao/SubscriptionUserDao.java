package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.dto.SubscriptionUserDTO;
import com.kamingpan.infrastructure.entity.model.entity.SubscriptionUser;
import com.kamingpan.infrastructure.entity.model.vo.SubscriptionUserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 公众号用户信息 Mapper 接口
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Repository
public interface SubscriptionUserDao extends BaseDao<SubscriptionUser> {

    /**
     * 根据主键解除手机号码绑定
     *
     * @param id 主键
     * @return 修改行数
     */
    int unbindByMobile(@Param("id") String id);

    /**
     * 根据openid更新公众号关注状态
     *
     * @param openid     openid
     * @param subscribed 公众号关注状态
     * @param updateTime 更新时间
     * @return 数据更新行数
     */
    int updateSubscribedByOpenid(@Param("openid") String openid, @Param("subscribed") boolean subscribed,
                                 @Param("updateTime") Date updateTime);

    /**
     * 根据公众号用户查询公众号用户信息
     *
     * @param subscriptionUser 公众号用户dto
     * @param deleted          数据状态
     * @return 公众号用户vo列表
     */
    List<SubscriptionUserVO> listBySubscriptionUser(@Param("subscriptionUser") SubscriptionUserDTO subscriptionUser,
                                                    @Param("deleted") Integer deleted);

    /**
     * 根据手机号查询公众号用户
     *
     * @param mobile  主键
     * @param deleted 数据状态
     * @return 公众号用户
     */
    SubscriptionUser getByMobile(@Param("mobile") String mobile, @Param("deleted") Integer deleted);

    /**
     * 根据主键查询公众号用户详情
     *
     * @param id      主键
     * @param deleted 数据状态
     * @return 公众号用户vo
     */
    SubscriptionUserVO getSubscriptionUserById(@Param("id") String id, @Param("deleted") Integer deleted);

    /**
     * 根据openid查询用户主键
     *
     * @param openid openid
     * @return 用户主键
     */
    String getUserIdByOpenid(@Param("openid") String openid);

    /**
     * 根据用户主键查询openid
     *
     * @param userId 用户主键
     * @return 用户主键
     */
    String getOpenidByUserId(@Param("userId") String userId);

}
