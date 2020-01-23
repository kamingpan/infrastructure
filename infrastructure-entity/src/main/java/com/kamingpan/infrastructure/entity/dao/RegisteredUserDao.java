package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.dto.RegisteredUserDTO;
import com.kamingpan.infrastructure.entity.model.entity.RegisteredUser;
import com.kamingpan.infrastructure.entity.model.vo.RegisteredUserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 注册用户 Mapper 接口
 *
 * @author kamingpan
 * @since 2020-01-03
 */
@Repository
public interface RegisteredUserDao extends BaseDao<RegisteredUser> {

    /**
     * 根据注册用户查询注册用户信息
     *
     * @param registeredUser 注册用户dto
     * @param deleted        数据状态
     * @return 用户vo列表
     */
    List<RegisteredUserVO> listByRegisteredUser(@Param("registeredUser") RegisteredUserDTO registeredUser,
                                                @Param("deleted") Integer deleted);

    /**
     * 根据主键查询注册用户详情
     *
     * @param id      主键
     * @param deleted 数据状态
     * @return 用户vo
     */
    RegisteredUserVO getRegisteredUserById(@Param("id") String id, @Param("deleted") Integer deleted);

    /**
     * 根据手机号查询注册用户
     *
     * @param mobile  主键
     * @param deleted 数据状态
     * @return 注册用户
     */
    RegisteredUser getByMobile(@Param("mobile") String mobile, @Param("deleted") Integer deleted);

}
