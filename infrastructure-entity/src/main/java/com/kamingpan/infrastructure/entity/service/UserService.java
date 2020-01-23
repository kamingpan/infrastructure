package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.entity.model.dto.UserDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.User;
import com.kamingpan.infrastructure.entity.model.vo.UserVO;

import java.util.List;

/**
 * 用户 服务类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
public interface UserService extends BaseService<User> {

    /**
     * 启用用户
     *
     * @param id              主键
     * @param adminOperateLog 操作日志
     */
    void updateStatusToEnableById(String id, AdminOperateLog adminOperateLog);

    /**
     * 禁用用户
     *
     * @param id              主键
     * @param adminOperateLog 操作日志
     */
    void updateStatusToDisableById(String id, AdminOperateLog adminOperateLog);

}
