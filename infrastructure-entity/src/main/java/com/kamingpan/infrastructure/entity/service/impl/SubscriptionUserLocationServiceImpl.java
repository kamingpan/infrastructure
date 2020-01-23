package com.kamingpan.infrastructure.entity.service.impl;

import com.kamingpan.infrastructure.core.base.enumeration.DataStatusEnum;
import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.entity.dao.SubscriptionUserDao;
import com.kamingpan.infrastructure.entity.dao.SubscriptionUserLocationDao;
import com.kamingpan.infrastructure.entity.model.entity.SubscriptionUserLocation;
import com.kamingpan.infrastructure.entity.service.SubscriptionUserLocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 公众号用户地理位置 服务实现类
 *
 * @author kamingpan
 * @since 2018-07-19
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class SubscriptionUserLocationServiceImpl extends BaseServiceImpl<SubscriptionUserLocation, SubscriptionUserLocationDao> implements SubscriptionUserLocationService {

    @Autowired
    private SubscriptionUserDao subscriptionUserDao;

    /**
     * 新增公众号用户地理位置
     *
     * @param subscriptionUserLocation 公众号用户地理位置
     * @return 是否插入成功
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SubscriptionUserLocation subscriptionUserLocation) {
        if (null == subscriptionUserLocation) {
            return 0;
        }

        // 查询并设置用户主键
        String userId = this.subscriptionUserDao.getUserIdByOpenid(subscriptionUserLocation.getOpenid());
        subscriptionUserLocation.setUserId(userId);

        // 设置基础信息并新增
        subscriptionUserLocation.initId();
        subscriptionUserLocation.setDeleted(DataStatusEnum.NOT_DELETED.getStatus());
        return super.baseMapper.insert(subscriptionUserLocation);
    }
}
