package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.entity.model.dto.RegisteredUserDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.RegisteredUser;
import com.kamingpan.infrastructure.entity.model.vo.RegisteredUserVO;

import java.util.List;

/**
 * 注册用户 服务类
 *
 * @author kamingpan
 * @since 2020-01-03
 */
public interface RegisteredUserService extends BaseService<RegisteredUser> {

    /**
     * 注册用户删除（只对注册用户做标记为已删除，而不实际执行物理删除）
     *
     * @param id              主键
     * @param adminOperateLog 操作日志
     */
    void delete(String id, AdminOperateLog adminOperateLog);

    /**
     * 根据注册用户查询注册用户信息
     *
     * @param registeredUser 注册用户dto
     * @param pager          分页
     * @return 用户vo列表
     */
    List<RegisteredUserVO> listByRegisteredUser(RegisteredUserDTO registeredUser, Pager pager);

    /**
     * 根据主键查询注册用户详情
     *
     * @param id 主键
     * @return 用户vo
     */
    RegisteredUserVO getRegisteredUserById(String id);

    /**
     * 根据用户名查询注册用户对象
     *
     * @param username 用户名
     * @return 用户
     */
    RegisteredUser getByUsername(String username);

    /**
     * 根据用户主键查询注册用户对象
     *
     * @param userId 用户主键
     * @return 用户
     */
    RegisteredUser getByUserId(String userId);

}
