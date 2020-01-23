package com.kamingpan.infrastructure.entity.model.dto;

import lombok.Data;

/**
 * 注册用户 dto
 *
 * @author kamingpan
 * @since 2018-07-20
 */
@Data
public class RegisteredUserDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 状态（0：正常，1：禁用）
     */
    private Integer status;

}
