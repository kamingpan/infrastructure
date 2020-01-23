package com.kamingpan.infrastructure.entity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kamingpan.infrastructure.core.base.enumeration.DataStatusEnum;
import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.core.exception.DataNotExistException;
import com.kamingpan.infrastructure.core.log.OperateLog;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.entity.constant.AdminOperateLogConstant;
import com.kamingpan.infrastructure.entity.constant.SubscriptionUserConstant;
import com.kamingpan.infrastructure.entity.constant.UserConstant;
import com.kamingpan.infrastructure.entity.dao.MiniProgramUserDao;
import com.kamingpan.infrastructure.entity.dao.SubscriptionUserDao;
import com.kamingpan.infrastructure.entity.dao.UserDao;
import com.kamingpan.infrastructure.entity.model.dto.SubscriptionUserDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.MiniProgramUser;
import com.kamingpan.infrastructure.entity.model.entity.SubscriptionUser;
import com.kamingpan.infrastructure.entity.model.entity.User;
import com.kamingpan.infrastructure.entity.model.vo.SubscriptionUserVO;
import com.kamingpan.infrastructure.entity.service.SubscriptionUserService;
import com.kamingpan.infrastructure.entity.util.DataDictionaryCache;
import com.kamingpan.infrastructure.util.sql.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 公众号用户 服务实现类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class SubscriptionUserServiceImpl extends BaseServiceImpl<SubscriptionUser, SubscriptionUserDao> implements SubscriptionUserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MiniProgramUserDao miniProgramUserDao;

    @Autowired
    private DataDictionaryCache dataDictionaryCache;

    /**
     * 插入用户和公众号用户
     *
     * @param subscriptionUser 公众号用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(SubscriptionUser subscriptionUser) {
        int count = 0;

        // 如果unionId不为空，使用unionId查询一下系统中是否存在对应的小程序用户
        if (null != subscriptionUser.getUnionId() && subscriptionUser.getUnionId().isEmpty()) {
            QueryWrapper<MiniProgramUser> wrapper = new QueryWrapper<MiniProgramUser>();
            wrapper.eq("union_id", subscriptionUser.getUnionId());
            MiniProgramUser miniProgramUser = this.miniProgramUserDao.selectOne(wrapper);

            //  如果存在，则直接关联其对应的用户，并保存公众号用户
            if (null != miniProgramUser) {
                subscriptionUser.preInsert();
                subscriptionUser.setUserId(miniProgramUser.getUserId());
                count += this.baseMapper.insert(subscriptionUser);
                return count;
            }
        }

        // 新增用户
        User user = new User();
        user.preInsert();
        user.setStatus(UserConstant.Status.ENABLE);
        user.setRegisteredTime(user.getCreateTime());
        user.setRegisteredSource(UserConstant.RegisteredSource.SUBSCRIPTION);
        count += this.userDao.insert(user);

        // 保存公众号用户
        subscriptionUser.preInsert();
        subscriptionUser.setUserId(user.getId());
        count += this.baseMapper.insert(subscriptionUser);

        return count;
    }

    /**
     * 更新公众号用户，并判断新增用户
     *
     * @param subscriptionUser 公众号用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(SubscriptionUser subscriptionUser) {
        // 判断要更新的公众号用户主键是否为空
        if (null == subscriptionUser || null == subscriptionUser.getId() || subscriptionUser.getId().isEmpty()) {
            log.warn("公众号用户主键为空，数据异常");
            throw new DataNotExistException();
        }

        // 更新公众号用户
        subscriptionUser.preUpdate();
        return this.baseMapper.updateById(subscriptionUser);
    }

    /**
     * 新增或更新关注公众号用户（不新增系统关联用户）
     *
     * @param openid 用户openid
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertOrUpdateByOpenid(String openid) {
        if (null == openid || openid.trim().isEmpty()) {
            return;
        }

        // 查询openid对应用户是否存在
        QueryWrapper<SubscriptionUser> wrapper = new QueryWrapper<SubscriptionUser>();
        wrapper.eq("openid", openid);
        int count = this.baseMapper.selectCount(wrapper);

        // 如果存在，则只更新公众号关注状态
        if (0 < count) {
            this.updateSubscribedByOpenid(openid, SubscriptionUserConstant.Subscribed.SUBSCRIBED);
            return;
        }

        // 否则新增公众号用户和系统关联用户

        // 新增系统关联用户
        User user = new User();
        user.preInsert();
        user.setStatus(UserConstant.Status.ENABLE);
        user.setRegisteredTime(user.getCreateTime());
        user.setRegisteredSource(UserConstant.RegisteredSource.SUBSCRIPTION);
        this.userDao.insert(user);

        // 新增公众号用户
        SubscriptionUser subscriptionUser = new SubscriptionUser();
        subscriptionUser.setUserId(user.getId());
        subscriptionUser.setOpenid(openid);
        subscriptionUser.setSubscribed(SubscriptionUserConstant.Subscribed.SUBSCRIBED);
        subscriptionUser.preInsert();
        this.baseMapper.insert(subscriptionUser);
    }

    /**
     * 更新公众号用户手机号码
     *
     * @param userId 用户主键
     * @param mobile 手机号码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMobile(String userId, String mobile) {
        // 查询公众号用户，如果查不到，表示公众号用户不存在
        SubscriptionUser temp = this.getByUserId(userId);
        if (null == temp) {
            throw new DataNotExistException();
        }

        // 查询该手机号原有的小程序用户，如果存在，则解除其绑定
        SubscriptionUser oldSubscriptionUser = this.baseMapper.getByMobile(mobile, DataStatusEnum.NOT_DELETED.getValue());
        if (null != oldSubscriptionUser) {
            this.baseMapper.unbindByMobile(oldSubscriptionUser.getId());
        }

        // 设置相关公众号用户手机号
        SubscriptionUser subscriptionUser = new SubscriptionUser();
        subscriptionUser.setId(temp.getId());
        subscriptionUser.setMobile(mobile);
        subscriptionUser.preUpdate();
        this.baseMapper.updateById(subscriptionUser);
    }

    /**
     * 根据openid更新公众号关注状态
     *
     * @param openid     openid
     * @param subscribed 公众号关注状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSubscribedByOpenid(String openid, boolean subscribed) {
        if (null == openid || openid.trim().isEmpty()) {
            return;
        }

        this.baseMapper.updateSubscribedByOpenid(openid, subscribed, new Date());
    }

    /**
     * 公众号用户删除（只对公众号用户做标记为已删除，而不实际执行物理删除）
     *
     * @param id              公众号用户主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.DELETE)
    public void delete(String id, AdminOperateLog adminOperateLog) {
        if (0 >= this.baseMapper.countById(id)) {
            throw new DataNotExistException();
        }

        this.baseMapper.deleteById(id);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.SUBSCRIPTION_USER);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 根据公众号用户查询公众号用户信息
     *
     * @param subscriptionUser 公众号用户dto
     * @param pager            分页
     * @return 公众号用户vo列表
     */
    @Override
    public List<SubscriptionUserVO> listBySubscriptionUser(SubscriptionUserDTO subscriptionUser, Pager pager) {
        subscriptionUser.setNickname(SqlUtil.like(subscriptionUser.getNickname()));
        subscriptionUser.setOpenid(SqlUtil.like(subscriptionUser.getOpenid()));

        // 如果不需要分页
        if (null == pager) {
            List<SubscriptionUserVO> subscriptionUsers = this.baseMapper.listBySubscriptionUser(subscriptionUser,
                    DataStatusEnum.NOT_DELETED.getValue());

            // 遍历结果
            for (SubscriptionUserVO subscriptionUserVO : subscriptionUsers) {
                // 处理状态标签
                subscriptionUserVO.setStatusLabel(dataDictionaryCache.getLabel(UserConstant.CLASS_STRING,
                        UserConstant.Variable.STATUS, String.valueOf(subscriptionUserVO.getStatus())));

                // 处理性别标签
                subscriptionUserVO.setGenderLabel(dataDictionaryCache.getLabel(SubscriptionUserConstant.CLASS_STRING,
                        SubscriptionUserConstant.Variable.GENDER, String.valueOf(subscriptionUserVO.getGender())));

                // 处理关注标签
                subscriptionUserVO.setSubscribedLabel(dataDictionaryCache.getLabel(SubscriptionUserConstant.CLASS_STRING,
                        SubscriptionUserConstant.Variable.SUBSCRIBED, String.valueOf(subscriptionUserVO.getSubscribed()
                                ? SubscriptionUserConstant.Subscribed.SUBSCRIBED_VALUE
                                : SubscriptionUserConstant.Subscribed.UN_SUBSCRIBED_VALUE)));
            }

            return subscriptionUsers;
        }

        // 处理分页逻辑
        Page page = PageHelper.startPage(pager.getPageNum(), pager.getPageSize());
        if (null != pager.getOrderBy() && !pager.getOrderBy().isEmpty()) {
            page.setOrderBy(pager.getOrderBy());
        }

        // 查询数据，并设置数据总数
        List<SubscriptionUserVO> subscriptionUsers = this.baseMapper.listBySubscriptionUser(subscriptionUser,
                DataStatusEnum.NOT_DELETED.getValue());
        pager.setTotal(page.getTotal());

        // 遍历结果
        for (SubscriptionUserVO subscriptionUserVO : subscriptionUsers) {
            // 处理状态标签
            subscriptionUserVO.setStatusLabel(dataDictionaryCache.getLabel(UserConstant.CLASS_STRING,
                    UserConstant.Variable.STATUS, String.valueOf(subscriptionUserVO.getStatus())));

            // 处理关注标签
            subscriptionUserVO.setSubscribedLabel(dataDictionaryCache.getLabel(SubscriptionUserConstant.CLASS_STRING,
                    SubscriptionUserConstant.Variable.SUBSCRIBED, String.valueOf(subscriptionUserVO.getSubscribed()
                            ? SubscriptionUserConstant.Subscribed.SUBSCRIBED_VALUE
                            : SubscriptionUserConstant.Subscribed.UN_SUBSCRIBED_VALUE)));

            // 处理性别标签
            subscriptionUserVO.setGenderLabel(dataDictionaryCache.getLabel(SubscriptionUserConstant.CLASS_STRING,
                    SubscriptionUserConstant.Variable.GENDER, String.valueOf(subscriptionUserVO.getGender())));
        }

        return subscriptionUsers;
    }

    /**
     * 根据主键查询公众号用户详情
     *
     * @param id 主键
     * @return 公众号用户vo
     */
    @Override
    public SubscriptionUserVO getSubscriptionUserById(String id) {
        SubscriptionUserVO subscriptionUser = this.baseMapper.getSubscriptionUserById(id,
                DataStatusEnum.NOT_DELETED.getValue());

        if (null != subscriptionUser) {
            // 处理状态标签
            subscriptionUser.setStatusLabel(dataDictionaryCache.getLabel(UserConstant.CLASS_STRING,
                    UserConstant.Variable.STATUS, String.valueOf(subscriptionUser.getStatus())));

            // 处理性别标签
            subscriptionUser.setGenderLabel(dataDictionaryCache.getLabel(SubscriptionUserConstant.CLASS_STRING,
                    SubscriptionUserConstant.Variable.GENDER, String.valueOf(subscriptionUser.getGender())));

            // 处理关注标签
            subscriptionUser.setSubscribedLabel(dataDictionaryCache.getLabel(SubscriptionUserConstant.CLASS_STRING,
                    SubscriptionUserConstant.Variable.SUBSCRIBED, String.valueOf(subscriptionUser.getSubscribed()
                            ? SubscriptionUserConstant.Subscribed.SUBSCRIBED_VALUE
                            : SubscriptionUserConstant.Subscribed.UN_SUBSCRIBED_VALUE)));
        }

        return subscriptionUser;
    }

    /**
     * 根据用户主键查询公众号用户对象
     *
     * @param userId 用户主键
     * @return 公众号用户
     */
    @Override
    public SubscriptionUser getByUserId(String userId) {
        QueryWrapper<SubscriptionUser> wrapper = new QueryWrapper<SubscriptionUser>();
        wrapper.eq("user_id", userId);
        return this.baseMapper.selectOne(wrapper);
    }

    /**
     * 根据openid查询公众号用户对象
     *
     * @param openid openid
     * @return 公众号用户
     */
    @Override
    public SubscriptionUser getByOpenid(String openid) {
        QueryWrapper<SubscriptionUser> wrapper = new QueryWrapper<SubscriptionUser>();
        wrapper.eq("openid", openid);
        return this.baseMapper.selectOne(wrapper);
    }

}
