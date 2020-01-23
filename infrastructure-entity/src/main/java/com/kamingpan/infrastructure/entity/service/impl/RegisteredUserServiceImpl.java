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
import com.kamingpan.infrastructure.entity.constant.UserConstant;
import com.kamingpan.infrastructure.entity.dao.RegisteredUserDao;
import com.kamingpan.infrastructure.entity.model.dto.RegisteredUserDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.RegisteredUser;
import com.kamingpan.infrastructure.entity.model.vo.RegisteredUserVO;
import com.kamingpan.infrastructure.entity.service.RegisteredUserService;
import com.kamingpan.infrastructure.entity.util.DataDictionaryCache;
import com.kamingpan.infrastructure.util.sql.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 注册用户 服务实现类
 *
 * @author kamingpan
 * @since 2020-01-03
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class RegisteredUserServiceImpl extends BaseServiceImpl<RegisteredUser, RegisteredUserDao> implements RegisteredUserService {

    @Autowired
    private DataDictionaryCache dataDictionaryCache;

    /**
     * 注册用户删除（只对注册用户做标记为已删除，而不实际执行物理删除）
     *
     * @param id              主键
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
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.REGISTERED_USER);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 根据注册用户查询注册用户信息
     *
     * @param registeredUser 注册用户dto
     * @param pager          分页
     * @return 用户vo列表
     */
    @Override
    public List<RegisteredUserVO> listByRegisteredUser(RegisteredUserDTO registeredUser, Pager pager) {
        registeredUser.setUsername(SqlUtil.like(registeredUser.getUsername()));

        // 如果不需要分页
        if (null == pager) {
            List<RegisteredUserVO> registeredUsers = this.baseMapper.listByRegisteredUser(registeredUser,
                    DataStatusEnum.NOT_DELETED.getValue());

            // 遍历结果
            for (RegisteredUserVO registeredUserVO : registeredUsers) {
                // 处理状态标签
                registeredUserVO.setStatusLabel(dataDictionaryCache.getLabel(UserConstant.CLASS_STRING,
                        UserConstant.Variable.STATUS, String.valueOf(registeredUserVO.getStatus())));
            }

            return registeredUsers;
        }

        // 处理分页逻辑
        Page page = PageHelper.startPage(pager.getPageNum(), pager.getPageSize());
        if (null != pager.getOrderBy() && !pager.getOrderBy().isEmpty()) {
            page.setOrderBy(pager.getOrderBy());
        }

        // 查询数据，并设置数据总数
        List<RegisteredUserVO> registeredUsers = this.baseMapper.listByRegisteredUser(registeredUser,
                DataStatusEnum.NOT_DELETED.getValue());
        pager.setTotal(page.getTotal());

        // 遍历结果
        for (RegisteredUserVO registeredUserVO : registeredUsers) {
            // 处理状态标签
            registeredUserVO.setStatusLabel(dataDictionaryCache.getLabel(UserConstant.CLASS_STRING,
                    UserConstant.Variable.STATUS, String.valueOf(registeredUserVO.getStatus())));
        }

        return registeredUsers;
    }

    /**
     * 根据主键查询注册用户详情
     *
     * @param id 主键
     * @return 注册用户vo
     */
    @Override
    public RegisteredUserVO getRegisteredUserById(String id) {
        RegisteredUserVO registeredUser = this.baseMapper.getRegisteredUserById(id,
                DataStatusEnum.NOT_DELETED.getValue());

        if (null != registeredUser) {
            // 处理状态标签
            registeredUser.setStatusLabel(dataDictionaryCache.getLabel(UserConstant.CLASS_STRING,
                    UserConstant.Variable.STATUS, String.valueOf(registeredUser.getStatus())));
        }

        return registeredUser;
    }

    /**
     * 根据用户名查询用户对象
     *
     * @param username 用户名
     * @return 用户
     */
    @Override
    public RegisteredUser getByUsername(String username) {
        QueryWrapper<RegisteredUser> wrapper = new QueryWrapper<RegisteredUser>();
        wrapper.eq("username", username);
        return this.baseMapper.selectOne(wrapper);
    }

    /**
     * 根据用户主键查询注册用户对象
     *
     * @param userId 用户主键
     * @return 用户
     */
    @Override
    public RegisteredUser getByUserId(String userId) {
        QueryWrapper<RegisteredUser> wrapper = new QueryWrapper<RegisteredUser>();
        wrapper.eq("user_id", userId);
        return this.baseMapper.selectOne(wrapper);
    }

}
