package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.entity.ManagementInformation;
import com.kamingpan.infrastructure.entity.model.vo.ManagementInformationVO;
import org.springframework.stereotype.Repository;

/**
 * 管理端信息 Mapper 接口
 *
 * @author kamingpan
 * @since 2018-06-28
 */
@Repository
public interface ManagementInformationDao extends BaseDao<ManagementInformation> {

    /**
     * 根据主键查询管理端信息
     *
     * @param id 主键
     * @return 管理端信息
     */
    ManagementInformationVO getManagementInformationById(String id);

}
