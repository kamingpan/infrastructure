package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.entity.Module;
import com.kamingpan.infrastructure.entity.model.vo.ModuleListVO;
import com.kamingpan.infrastructure.entity.model.vo.ModuleVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 模块 Mapper 接口
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Repository
public interface ModuleDao extends BaseDao<Module> {

    /**
     * 根据管理员查询模块
     *
     * @param adminId 管理员主键
     * @param status  管理员状态
     * @return 模块列表
     */
    List<Module> listByAdminId(@Param("adminId") String adminId, @Param("status") Integer status);

    /**
     * 根据主键集合查询侧边栏模块列表
     *
     * @param ids     主键集合
     * @param status  管理员状态
     * @param deleted 数据状态
     * @return 侧边栏模块列表
     */
    List<ModuleListVO> listSideBarModuleByIds(@Param("ids") List<String> ids, @Param("status") Integer status,
                                              @Param("deleted") Integer deleted);

    /**
     * 查询模块信息
     *
     * @param deleted 数据状态
     * @return 模块vo列表
     */
    List<ModuleListVO> listModule(@Param("deleted") Integer deleted);

    /**
     * 查询所有上级模块（排除当前模块）
     *
     * @param id 当前模块主键
     * @return 模块vo列表
     */
    List<ModuleVO> listSuperior(@Param("id") String id);

    /**
     * 根据主键查询模块信息
     *
     * @param id      主键
     * @param deleted 数据状态
     * @return 模块vo
     */
    ModuleVO getModuleById(@Param("id") String id, @Param("deleted") Integer deleted);

    /**
     * 根据状态查询模块
     *
     * @param status 状态
     * @return 模块集合
     */
    List<Map<String, Object>> listMapByStatus(@Param("status") Integer status);

}
