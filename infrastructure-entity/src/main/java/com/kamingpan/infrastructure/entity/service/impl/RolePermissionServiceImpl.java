package com.kamingpan.infrastructure.entity.service.impl;

import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.entity.dao.RolePermissionDao;
import com.kamingpan.infrastructure.entity.model.entity.RolePermission;
import com.kamingpan.infrastructure.entity.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色-权限关联 服务实现类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class RolePermissionServiceImpl extends BaseServiceImpl<RolePermission, RolePermissionDao> implements RolePermissionService {

}
