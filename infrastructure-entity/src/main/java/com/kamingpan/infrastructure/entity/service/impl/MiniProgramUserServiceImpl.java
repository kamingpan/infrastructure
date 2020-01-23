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
import com.kamingpan.infrastructure.entity.constant.MiniProgramUserConstant;
import com.kamingpan.infrastructure.entity.constant.UserConstant;
import com.kamingpan.infrastructure.entity.dao.MiniProgramUserDao;
import com.kamingpan.infrastructure.entity.dao.SubscriptionUserDao;
import com.kamingpan.infrastructure.entity.dao.UserDao;
import com.kamingpan.infrastructure.entity.model.dto.MiniProgramUserDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.MiniProgramUser;
import com.kamingpan.infrastructure.entity.model.entity.SubscriptionUser;
import com.kamingpan.infrastructure.entity.model.entity.User;
import com.kamingpan.infrastructure.entity.model.vo.MiniProgramUserVO;
import com.kamingpan.infrastructure.entity.service.MiniProgramUserService;
import com.kamingpan.infrastructure.entity.util.DataDictionaryCache;
import com.kamingpan.infrastructure.util.sql.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 小程序用户 服务实现类
 *
 * @author kamingpan
 * @since 2019-04-18
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class MiniProgramUserServiceImpl extends BaseServiceImpl<MiniProgramUser, MiniProgramUserDao> implements MiniProgramUserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SubscriptionUserDao subscriptionUserDao;

    @Autowired
    private DataDictionaryCache dataDictionaryCache;

    /**
     * 插入用户和小程序用户
     *
     * @param miniProgramUser 小程序用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insert(MiniProgramUser miniProgramUser) {
        int count = 0;

        // 如果unionId不为空，使用unionId查询一下系统中是否存在对应的小程序用户
        if (null != miniProgramUser.getUnionId() && miniProgramUser.getUnionId().isEmpty()) {
            QueryWrapper<SubscriptionUser> wrapper = new QueryWrapper<SubscriptionUser>();
            wrapper.eq("union_id", miniProgramUser.getUnionId());
            SubscriptionUser subscriptionUser = this.subscriptionUserDao.selectOne(wrapper);

            //  如果存在，则直接关联其对应的用户，并只保存小程序用户
            if (null != subscriptionUser) {
                miniProgramUser.setUserId(subscriptionUser.getUserId());
                miniProgramUser.preInsert();
                count += this.baseMapper.insert(miniProgramUser);
                return count;
            }
        }

        // 新增用户
        User user = new User();
        user.preInsert();
        user.setStatus(UserConstant.Status.ENABLE);
        user.setRegisteredTime(user.getCreateTime());
        user.setRegisteredSource(UserConstant.RegisteredSource.MINI_PROGRAM);
        count += this.userDao.insert(user);

        // 保存小程序用户
        miniProgramUser.preInsert();
        miniProgramUser.setUserId(user.getId());
        count += this.baseMapper.insert(miniProgramUser);

        return count;
    }

    /**
     * 更新小程序用户，并判断新增用户
     *
     * @param miniProgramUser 小程序用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int update(MiniProgramUser miniProgramUser) {
        // 判断要更新的小程序用户主键是否为空
        if (null == miniProgramUser || null == miniProgramUser.getId() || miniProgramUser.getId().isEmpty()) {
            log.warn("小程序用户主键为空，数据异常");
            throw new DataNotExistException();
        }

        // 更新小程序用户
        miniProgramUser.preUpdate();
        return this.baseMapper.updateById(miniProgramUser);
    }

    /**
     * 更新小程序用户手机号码
     *
     * @param userId 用户主键
     * @param mobile 手机号码
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMobile(String userId, String mobile) {
        // 查询小程序用户，如果查不到，表示小程序用户不存在
        MiniProgramUser temp = this.getByUserId(userId);
        if (null == temp) {
            throw new DataNotExistException();
        }

        // 查询该手机号原有的小程序用户，如果存在，则解除其绑定
        MiniProgramUser oldMiniProgramUser = this.baseMapper.getByMobile(mobile, DataStatusEnum.NOT_DELETED.getValue());
        if (null != oldMiniProgramUser) {
            this.baseMapper.unbindByMobile(oldMiniProgramUser.getId());
        }

        // 设置相关小程序用户手机号
        MiniProgramUser miniProgramUser = new MiniProgramUser();
        miniProgramUser.setId(temp.getId());
        miniProgramUser.setMobile(mobile);
        miniProgramUser.preUpdate();
        this.baseMapper.updateById(miniProgramUser);
    }

    /**
     * 小程序用户删除（只对小程序用户做标记为已删除，而不实际执行物理删除）
     *
     * @param id              小程序用户主键
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
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.MINI_PROGRAM_USER);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 根据小程序用户查询小程序用户信息
     *
     * @param miniProgramUser 小程序用户dto
     * @param pager           分页
     * @return 小程序用户vo列表
     */
    @Override
    public List<MiniProgramUserVO> listByMiniProgramUser(MiniProgramUserDTO miniProgramUser, Pager pager) {
        miniProgramUser.setNickname(SqlUtil.like(miniProgramUser.getNickname()));
        miniProgramUser.setOpenid(SqlUtil.like(miniProgramUser.getOpenid()));

        // 如果不需要分页
        if (null == pager) {
            List<MiniProgramUserVO> miniProgramUsers = this.baseMapper.listByMiniProgramUser(miniProgramUser,
                    DataStatusEnum.NOT_DELETED.getValue());

            // 遍历结果
            for (MiniProgramUserVO miniProgramUserVO : miniProgramUsers) {
                // 处理状态标签
                miniProgramUserVO.setStatusLabel(dataDictionaryCache.getLabel(UserConstant.CLASS_STRING,
                        UserConstant.Variable.STATUS, String.valueOf(miniProgramUserVO.getStatus())));

                // 处理性别标签
                miniProgramUserVO.setGenderLabel(dataDictionaryCache.getLabel(MiniProgramUserConstant.CLASS_STRING,
                        MiniProgramUserConstant.Variable.GENDER, String.valueOf(miniProgramUserVO.getGender())));
            }

            return miniProgramUsers;
        }

        // 处理分页逻辑
        Page page = PageHelper.startPage(pager.getPageNum(), pager.getPageSize());
        if (null != pager.getOrderBy() && !pager.getOrderBy().isEmpty()) {
            page.setOrderBy(pager.getOrderBy());
        }

        // 查询数据，并设置数据总数
        List<MiniProgramUserVO> miniProgramUsers = this.baseMapper.listByMiniProgramUser(miniProgramUser,
                DataStatusEnum.NOT_DELETED.getValue());
        pager.setTotal(page.getTotal());

        // 遍历结果
        for (MiniProgramUserVO miniProgramUserVO : miniProgramUsers) {
            // 处理性别标签
            miniProgramUserVO.setGenderLabel(dataDictionaryCache.getLabel(MiniProgramUserConstant.CLASS_STRING,
                    MiniProgramUserConstant.Variable.GENDER, String.valueOf(miniProgramUserVO.getGender())));

            // 处理状态标签
            miniProgramUserVO.setStatusLabel(dataDictionaryCache.getLabel(UserConstant.CLASS_STRING,
                    UserConstant.Variable.STATUS, String.valueOf(miniProgramUserVO.getStatus())));
        }

        return miniProgramUsers;
    }

    /**
     * 根据主键查询小程序用户详情
     *
     * @param id 主键
     * @return 小程序用户vo
     */
    @Override
    public MiniProgramUserVO getMiniProgramUserById(String id) {
        MiniProgramUserVO miniProgramUser = this.baseMapper.getMiniProgramUserById(id,
                DataStatusEnum.NOT_DELETED.getValue());

        if (null != miniProgramUser) {
            // 处理状态标签
            miniProgramUser.setStatusLabel(dataDictionaryCache.getLabel(UserConstant.CLASS_STRING,
                    UserConstant.Variable.STATUS, String.valueOf(miniProgramUser.getStatus())));

            // 处理性别标签
            miniProgramUser.setGenderLabel(dataDictionaryCache.getLabel(MiniProgramUserConstant.CLASS_STRING,
                    MiniProgramUserConstant.Variable.GENDER, String.valueOf(miniProgramUser.getGender())));
        }

        return miniProgramUser;
    }

    /**
     * 根据用户主键查询小程序用户对象
     *
     * @param userId 用户主键
     * @return 小程序用户
     */
    @Override
    public MiniProgramUser getByUserId(String userId) {
        QueryWrapper<MiniProgramUser> wrapper = new QueryWrapper<MiniProgramUser>();
        wrapper.eq("user_id", userId);
        return this.baseMapper.selectOne(wrapper);
    }

    /**
     * 根据openid查询小程序用户对象
     *
     * @param openid openid
     * @return 小程序用户
     */
    @Override
    public MiniProgramUser getByOpenid(String openid) {
        QueryWrapper<MiniProgramUser> wrapper = new QueryWrapper<MiniProgramUser>();
        wrapper.eq("openid", openid);
        return this.baseMapper.selectOne(wrapper);
    }

}
