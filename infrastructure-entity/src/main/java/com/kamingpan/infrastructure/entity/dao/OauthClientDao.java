package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.dto.OauthClientDTO;
import com.kamingpan.infrastructure.entity.model.entity.OauthClient;
import com.kamingpan.infrastructure.entity.model.vo.OauthClientVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 授权客户端 Mapper 接口
 *
 * @author kamingpan
 * @since 2019-04-12
 */
@Repository
public interface OauthClientDao extends BaseDao<OauthClient> {

    /**
     * 根据授权客户端查询授权客户端信息
     *
     * @param oauthClient 授权客户端dto
     * @param deleted     数据状态
     * @return 授权客户端vo列表
     */
    List<OauthClientVO> listByOauthClient(@Param("oauthClient") OauthClientDTO oauthClient,
                                          @Param("deleted") Integer deleted);

    /**
     * 根据主键查询授权客户端详情
     *
     * @param id      主键
     * @param deleted 数据状态
     * @return 管理员vo
     */
    OauthClientVO getOauthClientById(@Param("id") String id, @Param("deleted") Integer deleted);

}
