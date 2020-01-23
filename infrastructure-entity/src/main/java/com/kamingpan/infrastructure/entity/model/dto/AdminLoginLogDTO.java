package com.kamingpan.infrastructure.entity.model.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 管理员登录日志 dto
 *
 * @author kamingpan
 * @since 2018-06-27
 */
@Data
public class AdminLoginLogDTO {

    /**
     * 管理员用户名
     */
    private String username;

    /**
     * 管理员姓名
     */
    private String fullName;

    /**
     * 登录状态（0：成功，1：失败）
     */
    private Integer status;

    /**
     * 开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date beginTime;

    /**
     * 结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

}
