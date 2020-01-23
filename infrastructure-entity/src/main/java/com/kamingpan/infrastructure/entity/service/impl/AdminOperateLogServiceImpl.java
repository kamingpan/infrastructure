package com.kamingpan.infrastructure.entity.service.impl;

import com.kamingpan.infrastructure.core.base.service.BaseServiceImpl;
import com.kamingpan.infrastructure.core.base.service.OperatorLogService;
import com.kamingpan.infrastructure.core.exception.LogErrorException;
import com.kamingpan.infrastructure.entity.dao.AdminOperateLogDao;
import com.kamingpan.infrastructure.entity.model.entity.AdminOperateLog;
import com.kamingpan.infrastructure.entity.model.vo.AdminOperateLogVO;
import com.kamingpan.infrastructure.entity.service.AdminOperateLogService;
import com.kamingpan.infrastructure.util.ip.IP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 管理端操作日志 服务实现类
 *
 * @author kamingpan
 * @since 2017-03-03
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class AdminOperateLogServiceImpl extends BaseServiceImpl<AdminOperateLog, AdminOperateLogDao>
        implements AdminOperateLogService, OperatorLogService<AdminOperateLog> {

    /**
     * 插入管理员操作日志
     *
     * @param belong   所属对象
     * @param belongId 所属主键
     * @param type     操作类型（登录，登出，创建，修改，删除，启用，禁用，其它）
     * @param method   方法名
     * @param content  操作内容
     */
    public int insert(String belong, String belongId, String type, String method, String content) {
        if (null == belong || belong.isEmpty()) {
            throw new LogErrorException();
        } else if (null == belongId || belongId.isEmpty()) {
            throw new LogErrorException();
        } else if (null == type || type.isEmpty()) {
            throw new LogErrorException();
        }

        AdminOperateLog adminOperateLog = new AdminOperateLog();
        adminOperateLog.setBelong(belong);
        adminOperateLog.setBelongId(belongId);
        adminOperateLog.setType(type);
        adminOperateLog.setMethod(method);
        adminOperateLog.setContent(content);

        // 获取request
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
            adminOperateLog.setIp(IP.getIP(request));
            adminOperateLog.setUrl(request.getRequestURI() + "(" + request.getMethod() + ")");
        }

        adminOperateLog.initOperator();
        return super.baseMapper.insert(adminOperateLog);
    }

    /**
     * 根据所属对象和所属主键查询操作日志
     *
     * @param belong    所属对象
     * @param belongId  所属主键
     * @param orderType 排序类型
     * @return 操作日志列表
     */
    @Override
    public List<AdminOperateLogVO> listAdminOperateLogByBelong(String belong, String belongId, String orderType) {
        return super.baseMapper.listAdminOperateLogByBelong(belong, belongId, orderType);
    }

}
