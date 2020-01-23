package com.kamingpan.infrastructure.entity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kamingpan.infrastructure.core.base.enumeration.DataStatusEnum;
import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.core.exception.DataNotExistException;
import com.kamingpan.infrastructure.core.exception.ValidateException;
import com.kamingpan.infrastructure.core.log.OperateLog;
import com.kamingpan.infrastructure.core.response.Pager;
import com.kamingpan.infrastructure.core.util.ChangeDetails;
import com.kamingpan.infrastructure.entity.constant.AdminOperateLogConstant;
import com.kamingpan.infrastructure.entity.constant.OauthClientConstant;
import com.kamingpan.infrastructure.entity.dao.OauthClientDao;
import com.kamingpan.infrastructure.entity.model.dto.OauthClientDTO;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.entity.OauthClient;
import com.kamingpan.infrastructure.entity.model.vo.OauthClientVO;
import com.kamingpan.infrastructure.entity.service.OauthClientService;
import com.kamingpan.infrastructure.util.generator.UuidGenerator;
import com.kamingpan.infrastructure.util.sql.SqlUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 授权客户端 服务实现类
 *
 * @author kamingpan
 * @since 2019-04-12
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class OauthClientServiceImpl extends BaseServiceImpl<OauthClient, OauthClientDao> implements OauthClientService {

    /**
     * 新增授权客户端.
     *
     * @param oauthClient     授权客户端
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.INSERT)
    public void insert(OauthClient oauthClient, AdminOperateLog adminOperateLog) {
        if (0 < this.countByName(oauthClient.getName(), null)) {
            throw new ValidateException("该客户端名称已存在");
        }

        // 生成客户端id和客户端密钥
        oauthClient.preInsert();
        oauthClient.setClientId(oauthClient.getId()); // 客户端id设置为和主键一样
        oauthClient.setClientSecret(UuidGenerator.uuid()); // 客户端id设置uuid
        this.baseMapper.insert(oauthClient);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.OAUTH_CLIENT);
        adminOperateLog.setBelongId(oauthClient.getId());
        adminOperateLog.setContent(ChangeDetails.getByCreate(oauthClient, OauthClientConstant.getFieldMap()));
    }

    /**
     * 修改授权客户端
     *
     * @param oauthClient     授权客户端
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.UPDATE)
    public void update(OauthClient oauthClient, AdminOperateLog adminOperateLog) {
        if (0 < this.countByName(oauthClient.getName(), oauthClient.getId())) {
            throw new ValidateException("该客户端名称已存在");
        }

        OauthClient before = super.getById(oauthClient.getId());
        if (null == before) {
            throw new DataNotExistException();
        }
        super.updateById(oauthClient);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.OAUTH_CLIENT);
        adminOperateLog.setBelongId(oauthClient.getId());
        adminOperateLog.setContent(ChangeDetails.getByUpdate(before, oauthClient, OauthClientConstant.getFieldMap()));
    }

    /**
     * 授权客户端删除
     *
     * @param id              授权客户端主键
     * @param adminOperateLog 操作日志
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(type = AdminOperateLogConstant.Type.DELETE)
    public void delete(String id, AdminOperateLog adminOperateLog) {
        // 判断授权客户端是否存在
        if (0 > this.countById(id)) {
            throw new DataNotExistException();
        }

        super.baseMapper.deleteById(id);

        // 记录操作日志信息
        adminOperateLog.setBelong(AdminOperateLogConstant.Belong.OAUTH_CLIENT);
        adminOperateLog.setBelongId(id);
    }

    /**
     * 授权客户端批量删除
     *
     * @param ids 授权客户端主键列表
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
     * 根据授权客户端查询授权客户端信息
     *
     * @param oauthClient 授权客户端dto
     * @param pager       分页
     * @return 授权客户端vo列表
     */
    @Override
    public List<OauthClientVO> listByOauthClient(OauthClientDTO oauthClient, Pager pager) {
        oauthClient.setName(SqlUtil.like(oauthClient.getName()));
        oauthClient.setClientId(SqlUtil.like(oauthClient.getClientId()));

        if (null == pager) {
            return super.baseMapper.listByOauthClient(oauthClient, DataStatusEnum.NOT_DELETED.getValue());
        }

        Page page = PageHelper.startPage(pager.getPageNum(), pager.getPageSize());
        if (null != pager.getOrderBy() && !pager.getOrderBy().isEmpty()) {
            page.setOrderBy(pager.getOrderBy());
        }

        List<OauthClientVO> oauthClients = super.baseMapper.listByOauthClient(oauthClient, DataStatusEnum.NOT_DELETED.getValue());
        // 设置数据总数
        pager.setTotal(page.getTotal());

        return oauthClients;
    }

    /**
     * 根据主键查询授权客户端详情
     *
     * @param id 主键
     * @return 授权客户端vo
     */
    @Override
    public OauthClientVO getOauthClientById(String id) {
        return super.baseMapper.getOauthClientById(id, DataStatusEnum.NOT_DELETED.getValue());
    }

    /**
     * 根据授权客户端名称查询授权客户端数量
     *
     * @param name 授权客户端名称
     * @param id   主键
     * @return 授权客户端数量
     */
    @Override
    public int countByName(String name, String id) {
        if (null == name || "".equals(name)) {
            return 0;
        }

        QueryWrapper<OauthClient> wrapper = new QueryWrapper<OauthClient>();
        wrapper.eq("name", name);
        if (null != id && !id.isEmpty()) {
            wrapper.ne("id", id);
        }

        return super.baseMapper.selectCount(wrapper);
    }

    /**
     * 根据授权客户端id查询授权客户端数量
     *
     * @param clientId 授权客户端id
     * @param id       主键
     * @return 授权客户端数量
     */
    @Override
    public int countByClientId(String clientId, String id) {
        if (null == clientId || "".equals(clientId)) {
            return 0;
        }

        QueryWrapper<OauthClient> wrapper = new QueryWrapper<OauthClient>();
        wrapper.eq("client_id", clientId);
        wrapper.ne("id", id);

        return super.baseMapper.selectCount(wrapper);
    }

    /**
     * 根据客户端id和客户端密钥查询授权客户端
     *
     * @param clientId     客户端id
     * @param clientSecret 客户端密钥
     * @return 授权客户端
     */
    @Override
    public OauthClient getByClientIdAndSecret(String clientId, String clientSecret) {
        if (null == clientId || clientId.isEmpty() || null == clientSecret || clientSecret.isEmpty()) {
            return null;
        }

        QueryWrapper<OauthClient> queryWrapper = new QueryWrapper<OauthClient>();
        queryWrapper.eq("client_id", clientId);
        queryWrapper.eq("client_secret", clientSecret);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
