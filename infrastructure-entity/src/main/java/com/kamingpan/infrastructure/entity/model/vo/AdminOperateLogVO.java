package com.kamingpan.infrastructure.entity.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * 管理端操作日志 vo
 *
 * @author kamingpan
 * @since 2018-06-29
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminOperateLogVO {

    /**
     * 主键id
     */
    private String id;

    /**
     * 操作类型（登录，登出，创建，修改，删除，启用，禁用，其它）
     */
    private String type;

    /**
     * 操作内容
     */
    private String content;

    /**
     * 操作描述
     */
    private String remark;

    /**
     * 操作人
     */
    private String operator;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operateTime;

    /**
     * 头像
     */
    private String portrait;

}
