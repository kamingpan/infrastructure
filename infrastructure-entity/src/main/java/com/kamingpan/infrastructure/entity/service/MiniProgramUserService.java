package com.kamingpan.infrastructure.entity.service;

import com.kamingpan.infrastructure.core.base.service.BaseService;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.entity.model.dto.MiniProgramUserDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.MiniProgramUser;
import com.kamingpan.infrastructure.entity.model.vo.MiniProgramUserVO;

import java.util.List;

/**
 * 小程序用户 服务类
 *
 * @author kamingpan
 * @since 2019-04-18
 */
public interface MiniProgramUserService extends BaseService<MiniProgramUser> {

    /**
     * 更新小程序用户手机号码
     *
     * @param userId 用户主键
     * @param mobile 手机号码
     */
    void updateMobile(String userId, String mobile);

    /**
     * 小程序用户删除（只对小程序用户做标记为已删除，而不实际执行物理删除）
     *
     * @param id              小程序用户主键
     * @param adminOperateLog 操作日志
     */
    void delete(String id, AdminOperateLog adminOperateLog);

    /**
     * 根据小程序用户查询小程序用户信息
     *
     * @param miniProgramUser 小程序用户dto
     * @param pager           分页
     * @return 小程序用户vo列表
     */
    List<MiniProgramUserVO> listByMiniProgramUser(MiniProgramUserDTO miniProgramUser, Pager pager);

    /**
     * 根据主键查询小程序用户详情
     *
     * @param id 主键
     * @return 小程序用户vo
     */
    MiniProgramUserVO getMiniProgramUserById(String id);

    /**
     * 根据用户主键查询小程序用户对象
     *
     * @param userId 用户主键
     * @return 小程序用户
     */
    MiniProgramUser getByUserId(String userId);

    /**
     * 根据openid查询小程序用户对象
     *
     * @param openid openid
     * @return 小程序用户
     */
    MiniProgramUser getByOpenid(String openid);

}
