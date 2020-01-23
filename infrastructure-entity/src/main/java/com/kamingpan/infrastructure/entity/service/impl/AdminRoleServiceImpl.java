package com.kamingpan.infrastructure.entity.service.impl;

import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.entity.dao.AdminRoleDao;
import com.kamingpan.infrastructure.entity.model.entity.AdminRole;
import com.kamingpan.infrastructure.entity.service.AdminRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 管理员-角色关联 服务实现类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class AdminRoleServiceImpl extends BaseServiceImpl<AdminRole, AdminRoleDao> implements AdminRoleService {

}
