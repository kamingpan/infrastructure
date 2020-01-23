package com.kamingpan.infrastructure.core.base.model;

/**
 * 操作日志
 *
 * @author kamingpan
 * @since 2017-09-04
 */
public interface OperatorLog {

    // 所属对象
    String getBelong();

    // 所属主键
    String getBelongId();

    //操作类型
    void setType(String type);

    //操作ip
    void setIp(String ip);

    // 操作链接地址
    void setUrl(String url);

    // 方法名
    void setMethod(String method);

    // 插入前初始化操作信息
    void initOperator();
}
