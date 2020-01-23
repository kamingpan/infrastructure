package com.kamingpan.infrastructure.entity.dao;

import com.kamingpan.infrastructure.core.base.dao.BaseDao;
import com.kamingpan.infrastructure.entity.model.dto.MiniProgramUserDTO;
import com.kamingpan.infrastructure.entity.model.entity.MiniProgramUser;
import com.kamingpan.infrastructure.entity.model.vo.MiniProgramUserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 小程序用户 Mapper 接口
 *
 * @author kamingpan
 * @since 2019-04-18
 */
@Repository
public interface MiniProgramUserDao extends BaseDao<MiniProgramUser> {

    /**
     * 根据主键解除手机号码绑定
     *
     * @param id 主键
     * @return 修改行数
     */
    int unbindByMobile(@Param("id") String id);

    /**
     * 根据小程序用户查询小程序用户信息
     *
     * @param miniProgramUser 小程序用户dto
     * @param deleted         数据状态
     * @return 小程序用户vo列表
     */
    List<MiniProgramUserVO> listByMiniProgramUser(@Param("miniProgramUser") MiniProgramUserDTO miniProgramUser,
                                                  @Param("deleted") Integer deleted);

    /**
     * 根据手机号查询小程序用户
     *
     * @param mobile  主键
     * @param deleted 数据状态
     * @return 小程序用户
     */
    MiniProgramUser getByMobile(@Param("mobile") String mobile, @Param("deleted") Integer deleted);

    /**
     * 根据主键查询小程序用户详情
     *
     * @param id      主键
     * @param deleted 数据状态
     * @return 小程序用户vo
     */
    MiniProgramUserVO getMiniProgramUserById(@Param("id") String id, @Param("deleted") Integer deleted);

    /**
     * 根据openid查询用户主键
     *
     * @param openid openid
     * @return 用户主键
     */
    String getUserIdByOpenid(@Param("openid") String openid);

    /**
     * 根据用户主键查询openid
     *
     * @param userId 用户主键
     * @return 用户主键
     */
    String getOpenidByUserId(@Param("userId") String userId);

}
