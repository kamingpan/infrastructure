package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.dto.RoleDTO;
import com.kamingpan.infrastructure.entity.model.entity.Role;
import com.kamingpan.infrastructure.entity.model.vo.RoleListVO;
import com.kamingpan.infrastructure.entity.model.vo.RoleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色 Mapper 接口
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Repository
public interface RoleDao extends BaseDao<Role> {

    /**
     * 根据角色查询角色信息
     *
     * @param role    角色dto
     * @param deleted 数据状态
     * @return 角色vo列表
     */
    List<RoleVO> listByRole(@Param("role") RoleDTO role, @Param("deleted") Integer deleted);

    /**
     * 根据角色主键查询角色信息
     *
     * @param id      角色主键
     * @param deleted 数据状态
     * @return 角色vo
     */
    RoleVO getRoleById(@Param("id") String id, @Param("deleted") Integer deleted);

    /**
     * 根据状态查询角色
     *
     * @param status  状态
     * @param deleted 数据状态
     * @return 角色列表
     */
    List<RoleListVO> listByStatus(@Param("status") Integer status, @Param("deleted") Integer deleted);

    /**
     * 查询管理员已关联的角色主键
     *
     * @param adminId 管理员主键
     * @return 角色主键列表
     */
    List<String> listIdByAdminId(@Param("adminId") String adminId);

    /**
     * 查询所有上级角色（排除当前角色）
     *
     * @param id 当前角色主键
     * @return 角色vo数量
     */
    List<RoleVO> listSuperior(@Param("id") String id);

}
