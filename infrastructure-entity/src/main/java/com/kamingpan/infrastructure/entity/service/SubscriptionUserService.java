package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.entity.model.dto.SubscriptionUserDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.SubscriptionUser;
import com.kamingpan.infrastructure.entity.model.vo.SubscriptionUserVO;

import java.util.List;

/**
 * 公众号用户 服务类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
public interface SubscriptionUserService extends BaseService<SubscriptionUser> {

    /**
     * 新增或更新关注公众号用户（不新增系统关联用户）
     *
     * @param openid 用户openid
     */
    void insertOrUpdateByOpenid(String openid);

    /**
     * 更新公众号用户手机号码
     *
     * @param userId 用户主键
     * @param mobile 手机号码
     */
    void updateMobile(String userId, String mobile);

    /**
     * 根据openid更新公众号关注状态
     *
     * @param openid     openid
     * @param subscribed 公众号关注状态
     */
    void updateSubscribedByOpenid(String openid, boolean subscribed);

    /**
     * 公众号用户删除（只对公众号用户做标记为已删除，而不实际执行物理删除）
     *
     * @param id              公众号用户主键
     * @param adminOperateLog 操作日志
     */
    void delete(String id, AdminOperateLog adminOperateLog);

    /**
     * 根据公众号用户查询公众号用户信息
     *
     * @param subscriptionUser 公众号用户dto
     * @param pager            分页
     * @return 公众号用户vo列表
     */
    List<SubscriptionUserVO> listBySubscriptionUser(SubscriptionUserDTO subscriptionUser, Pager pager);

    /**
     * 根据主键查询公众号用户详情
     *
     * @param id 主键
     * @return 公众号用户vo
     */
    SubscriptionUserVO getSubscriptionUserById(String id);

    /**
     * 根据用户主键查询公众号用户对象
     *
     * @param userId 用户主键
     * @return 公众号用户
     */
    SubscriptionUser getByUserId(String userId);

    /**
     * 根据openid查询公众号用户对象
     *
     * @param openid openid
     * @return 公众号用户
     */
    SubscriptionUser getByOpenid(String openid);

}
