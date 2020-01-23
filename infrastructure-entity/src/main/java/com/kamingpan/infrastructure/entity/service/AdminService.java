package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.entity.model.dto.AdminDTO;
import com.kamingpan.infrastructure.entity.model.entity.Admin;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.UploadFile;
import com.kamingpan.infrastructure.entity.model.vo.AdminVO;

import java.util.List;

/**
 * 管理员 服务类
 *
 * @author kamingpan
 * @since 2018-06-28
 */
public interface AdminService extends BaseService<Admin> {

    /**
     * 新增管理员.
     *
     * @param admin           管理员
     * @param adminOperateLog 操作日志
     */
    void insert(Admin admin, AdminOperateLog adminOperateLog);

    /**
     * 修改管理员
     *
     * @param admin           管理员
     * @param adminOperateLog 操作日志
     */
    void update(Admin admin, AdminOperateLog adminOperateLog);

    /**
     * 修改管理员-角色关联
     *
     * @param id              管理员主键
     * @param roleIds         角色主键集合
     * @param adminOperateLog 操作日志
     */
    void updateAdminRole(String id, List<String> roleIds, AdminOperateLog adminOperateLog);

    /**
     * 启用管理员
     *
     * @param id              主键
     * @param adminOperateLog 操作日志
     */
    void updateStatusToEnableById(String id, AdminOperateLog adminOperateLog);

    /**
     * 禁用管理员
     *
     * @param id              主键
     * @param adminOperateLog 操作日志
     */
    void updateStatusToDisableById(String id, AdminOperateLog adminOperateLog);

    /**
     * 重置密码
     *
     * @param admin           管理员
     * @param adminOperateLog 操作日志
     */
    void updateResetPassword(Admin admin, AdminOperateLog adminOperateLog);

    /**
     * 修改密码
     *
     * @param admin           管理员
     * @param adminOperateLog 操作日志
     */
    void updatePassword(Admin admin, AdminOperateLog adminOperateLog);

    /**
     * 修改管理员头像
     *
     * @param admin           管理员
     * @param uploadFile      上传文件
     * @param adminOperateLog 操作日志
     */
    void updatePortrait(Admin admin, UploadFile uploadFile, AdminOperateLog adminOperateLog);

    /**
     * 管理员删除（只对管理员做标记为已删除，而不实际执行物理删除）
     *
     * @param id              管理员主键
     * @param adminOperateLog 操作日志
     */
    void delete(String id, AdminOperateLog adminOperateLog);

    /**
     * 管理员批量删除
     *
     * @param ids 管理员主键列表
     */
    void deleteByIds(List<String> ids);

    /**
     * 根据管理员名查询管理员对象
     *
     * @param username 管理员用户名
     * @return 管理员
     */
    Admin getByUsername(String username);

    /**
     * 根据用户名查询管理员数量
     *
     * @param username 用户名
     * @return 管理员数量
     */
    int countByUsername(String username);

    /**
     * 根据管理员查询管理员信息
     *
     * @param admin 管理员dto
     * @param pager 分页
     * @return 管理员vo列表
     */
    List<AdminVO> listByAdmin(AdminDTO admin, Pager pager);

    /**
     * 根据主键查询管理员详情
     *
     * @param id 主键
     * @return 管理员vo
     */
    AdminVO getAdminById(String id);

}
