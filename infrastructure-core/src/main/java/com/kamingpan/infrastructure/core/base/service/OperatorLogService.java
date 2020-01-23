package com.kamingpan.infrastructure.core.base.service;

/**
 * 操作日志service
 *
 * @author kamingpan
 * @since 2017-09-04
 */
public interface OperatorLogService<T> {

    /**
     * 插入数据
     *
     * @param operatorLog  操作日志
     * @param initBaseData 是否初始化基础数据
     * @return 影响的数据数量
     */
    int insert(T operatorLog, boolean initBaseData);

    /**
     * 插入管理员操作日志
     *
     * @param belong   所属对象
     * @param belongId 所属主键
     * @param type     操作类型（登录，登出，创建，修改，删除，启用，禁用，其它）
     * @param method   方法名
     * @param content  操作内容
     */
    int insert(String belong, String belongId, String type, String method, String content);

}
