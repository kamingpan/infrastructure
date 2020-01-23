package com.kamingpan.infrastructure.entity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kamingpan.infrastructure.core.base.enumeration.DataStatusEnum;
import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.core.exception.DataCanNotDeleteException;
import com.kamingpan.infrastructure.core.exception.DataNotExistException;
import com.kamingpan.infrastructure.core.exception.ValidateException;
import com.kamingpan.infrastructure.core.log.OperateLog;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.core.util.ChangeDetails;
import com.kamingpan.infrastructure.entity.constant.AdminConstant;
import com.kamingpan.infrastructure.entity.constant.AdminOperateLogConstant;
import com.kamingpan.infrastructure.entity.dao.AdminDao;
import com.kamingpan.infrastructure.entity.dao.AdminRoleDao;
import com.kamingpan.infrastructure.entity.dao.RoleDao;
import com.kamingpan.infrastructure.entity.dao.UploadFileDao;
import com.kamingpan.infrastructure.entity.model.dto.AdminDTO;
import com.kamingpan.infrastructure.entity.model.entity.Admin;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.AdminRole;
import com.kamingpan.infrastructure.entity.model.entity.Role;
import com.kamingpan.infrastructure.entity.model.entity.UploadFile;
import com.kamingpan.infrastructure.entity.model.vo.AdminVO;
import com.kamingpan.infrastructure.entity.service.AdminService;
import com.kamingpan.infrastructure.entity.util.DataDictionaryCache;
import com.kamingpan.infrastructure.util.sql.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 管理员 服务实现类
 *
 * @author kamingpan
 * @since 2017-02-13
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class AdminServiceImpl extends BaseServiceImpl<Admin, AdminDao> implements AdminService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private AdminRoleDao adminRoleDao;

    @Autowired
    private UploadFileDao uploadFileDao;

    @Autowired
    private DataDictionaryCache dataDictionaryCache;

    /**
     * 新增管理员.
     *
     * @param admin           管理员
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.INSERT)
    public void insert(Admin admin, AdminOperateLog adminOperateLog) {
        if (0 < this.baseMapper.countByUsername(admin.getUsername())) {
            throw new ValidateException("该用户名已存在");
        }

        this.insert(admin);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.ADMIN);
        adminOperateLog.setBelongId(admin.getId());
        adminOperateLog.setContent(ChangeDetails.getByCreate(admin, AdminConstant.getFieldMap()));
    }

    /**
     * 修改管理员
     *
     * @param admin           管理员
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.UPDATE)
    public void update(Admin admin, AdminOperateLog adminOperateLog) {
        Admin before = this.getById(admin.getId());
        if (null == before) {
            throw new DataNotExistException();
        } else if (AdminConstant.Username.SUPER_ADMIN.equals(before.getUsername())) {
            // 获取当前登录用户主键
            String operatorId = admin.obtainOperatorId();
            if (null == operatorId || operatorId.isEmpty()) {
                // 超级管理员不允许修改
                throw new ValidateException("超级管理员不允许修改");
            }

            // 查询当前登录用户信息
            Admin operator = this.baseMapper.selectById(operatorId);
            if (null == operator || !AdminConstant.Username.SUPER_ADMIN.equals(operator.getUsername())) {
                // 如果当前登录用户不是超级管理员，则超级管理员不允许修改（即普通用户无法修改超级管理员信息）
                throw new ValidateException("超级管理员不允许修改");
            }
        }
        this.update(admin);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.ADMIN);
        adminOperateLog.setBelongId(admin.getId());
        adminOperateLog.setContent(ChangeDetails.getByUpdate(before, admin, AdminConstant.getFieldMap()));
    }

    /**
     * 修改管理员-角色关联
     *
     * @param id              管理员主键
     * @param roleIds         角色主键集合
     * @param adminOperateLog 操作日志
     */
    @Override
    @OperateLog(type = "分配角色")
    @Transactional(rollbackFor = Exception.class)
    public void updateAdminRole(String id, List<String> roleIds, AdminOperateLog adminOperateLog) {
        if (0 >= this.baseMapper.countById(id)) {
            throw new DataNotExistException();
        }

        StringBuilder content = new StringBuilder("移除了所有角色");

        // 删除旧的关联关系
        this.adminRoleDao.deleteByAdminId(id);

        if (null != roleIds && roleIds.size() > 0) {
            content = new StringBuilder("角色为：");

            // 查询符合id的角色信息
            QueryWrapper<Role> roleWrapper = new QueryWrapper<Role>();
            roleWrapper.in("id", roleIds);
            List<Role> roles = this.roleDao.selectList(roleWrapper);

            for (Role role : roles) {
                AdminRole adminRole = new AdminRole();
                adminRole.setAdminId(id);
                adminRole.setRoleId(role.getId());
                adminRole.preInsert();
                this.adminRoleDao.insert(adminRole);

                // 拼接角色名称
                content.append(role.getName()).append("，");
            }
            content.deleteCharAt(content.length() - 1);
        }

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.ADMIN);
        adminOperateLog.setBelongId(id);
        adminOperateLog.setContent(content.toString());
    }

    /**
     * 启用管理员
     *
     * @param id              主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.ENABLE)
    public void updateStatusToEnableById(String id, AdminOperateLog adminOperateLog) {
        if (0 >= this.baseMapper.countById(id)) {
            throw new DataNotExistException();
        }

        Admin admin = new Admin();
        admin.setId(id);
        admin.setStatus(AdminConstant.Status.ENABLE);
        admin.preUpdate();
        this.baseMapper.updateById(admin);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.ADMIN);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 禁用管理员
     *
     * @param id              主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.DISABLE)
    public void updateStatusToDisableById(String id, AdminOperateLog adminOperateLog) {
        Admin adminTemp = this.baseMapper.selectById(id);
        if (null == adminTemp) {
            throw new DataNotExistException();
        } else if (AdminConstant.Username.SUPER_ADMIN.equals(adminTemp.getUsername())) {
            // 超级管理员不允许禁用
            throw new ValidateException("超级管理员不允许禁用");
        }

        Admin admin = new Admin();
        admin.setId(id);
        admin.setStatus(AdminConstant.Status.DISABLE);
        admin.preUpdate();
        this.baseMapper.updateById(admin);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.ADMIN);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 重置密码
     *
     * @param admin           管理员
     * @param adminOperateLog 操作日志
     */
    @Override
    @OperateLog(type = "重置密码")
    @Transactional(rollbackFor = Exception.class)
    public void updateResetPassword(Admin admin, AdminOperateLog adminOperateLog) {
        Admin before = this.getById(admin.getId());
        if (null == before) {
            throw new DataNotExistException();
        } else if (AdminConstant.Username.SUPER_ADMIN.equals(before.getUsername())) {
            // 超级管理员不允许重置密码
            throw new ValidateException("超级管理员不允许重置密码");
        }

        admin.preUpdate();
        this.baseMapper.updateById(admin);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.ADMIN);
        adminOperateLog.setBelongId(admin.getId());
    }

    /**
     * 修改密码
     *
     * @param admin           管理员
     * @param adminOperateLog 操作日志
     */
    @Override
    @OperateLog(type = "修改了个人密码")
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(Admin admin, AdminOperateLog adminOperateLog) {
        admin.preUpdate();
        this.baseMapper.updateById(admin);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.ADMIN);
        adminOperateLog.setBelongId(admin.getId());
    }

    /**
     * 修改管理员头像
     *
     * @param admin           管理员
     * @param uploadFile      上传文件
     * @param adminOperateLog 操作日志
     */
    @Override
    @OperateLog(type = "修改了个人头像")
    @Transactional(rollbackFor = Exception.class)
    public void updatePortrait(Admin admin, UploadFile uploadFile, AdminOperateLog adminOperateLog) {
        // 保存上传文件信息
        this.uploadFileDao.insert(uploadFile);

        // 保存用户信息
        admin.preUpdate();
        this.baseMapper.updateById(admin);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.ADMIN);
        adminOperateLog.setBelongId(admin.getId());
    }

    /**
     * 管理员删除（只对管理员做标记为已删除，而不实际执行物理删除）
     *
     * @param id              管理员主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.DELETE)
    public void delete(String id, AdminOperateLog adminOperateLog) {
        // 判断管理员是否存在
        Admin admin = this.baseMapper.selectById(id);
        if (null == admin) {
            throw new DataNotExistException();
        } else if (AdminConstant.Username.SUPER_ADMIN.equals(admin.getUsername())) {
            // 超级管理员不允许删除
            throw new DataCanNotDeleteException("超级管理员不允许删除");
        }

        // 不允许删除当前登录的管理员
        String operatorId = admin.obtainOperatorId();
        if (id.equals(operatorId)) {
            throw new DataCanNotDeleteException("不允许删除当前登录的管理员");
        }

        // 删除所有角色关联和该管理员
        this.adminRoleDao.deleteByAdminId(id);
        this.baseMapper.deleteById(id);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.ADMIN);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 管理员批量删除
     *
     * @param ids 管理员主键列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIds(List<String> ids) {
        for (String id : ids) {
            if (null != id && !id.isEmpty()) {
                this.delete(id, new AdminOperateLog());
            }
        }
    }

    /**
     * 根据管理员名查询管理员对象
     *
     * @param username 管理员用户名
     * @return 管理员
     */
    @Override
    public Admin getByUsername(String username) {
        return this.baseMapper.getByUsername(username.toLowerCase(), DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 根据用户名查询管理员数量
     *
     * @param username 用户名
     * @return 管理员数量
     */
    @Override
    public int countByUsername(String username) {
        if (null == username || username.isEmpty()) {
            return 0;
        }

        return this.baseMapper.countByUsername(username);
    }

    /**
     * 根据管理员查询管理员信息
     *
     * @param admin 管理员dto
     * @param pager 分页
     * @return 管理员vo列表
     */
    @Override
    public List<AdminVO> listByAdmin(AdminDTO admin, Pager pager) {
        admin.setUsername(SqlUtil.like(admin.getUsername()));
        admin.setFullName(SqlUtil.like(admin.getFullName()));
        admin.setPhone(SqlUtil.like(admin.getPhone()));

        // 如果不需要分页
        if (null == pager) {
            List<AdminVO> admins = this.baseMapper.listByAdmin(admin, DataStatusEnum.NOT_DELETED.getValue());

            // 遍历结果
            for (AdminVO adminVO : admins) {
                // 处理状态标签
                adminVO.setStatusLabel(dataDictionaryCache.getLabel(AdminConstant.CLASS_STRING,
                        AdminConstant.Variable.STATUS, String.valueOf(adminVO.getStatus())));
            }

            return admins;
        }

        // 处理分页逻辑
        Page page = PageHelper.startPage(pager.getPageNum(), pager.getPageSize());
        if (null != pager.getOrderBy() && !pager.getOrderBy().isEmpty()) {
            page.setOrderBy(pager.getOrderBy());
        }

        // 查询数据，并设置数据总数
        List<AdminVO> admins = this.baseMapper.listByAdmin(admin, DataStatusEnum.NOT_DELETED.getValue());
        pager.setTotal(page.getTotal());

        // 遍历结果
        for (AdminVO adminVO : admins) {
            // 处理性别标签
            adminVO.setStatusLabel(dataDictionaryCache.getLabel(AdminConstant.CLASS_STRING,
                    AdminConstant.Variable.STATUS, String.valueOf(adminVO.getStatus())));
        }

        return admins;
    }

    /**
     * 根据主键查询管理员详情
     *
     * @param id 主键
     * @return 管理员vo
     */
    @Override
    public AdminVO getAdminById(String id) {
        AdminVO admin = this.baseMapper.getAdminById(id, DataStatusEnum.NOT_DELETED.getValue());

        if (null != admin) {
            // 处理状态标签
            admin.setStatusLabel(dataDictionaryCache.getLabel(AdminConstant.CLASS_STRING,
                    AdminConstant.Variable.STATUS, String.valueOf(admin.getStatus())));

            // 处理性别标签
            admin.setGenderLabel(dataDictionaryCache.getLabel(AdminConstant.CLASS_STRING,
                    AdminConstant.Variable.GENDER, String.valueOf(admin.getGender())));
        }

        return admin;
    }

}
