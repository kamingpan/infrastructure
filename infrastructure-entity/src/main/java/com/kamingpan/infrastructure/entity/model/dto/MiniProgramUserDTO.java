package com.kamingpan.infrastructure.entity.model.dto;

import lombok.Data;

/**
 * 小程序用户 dto
 *
 * @author kamingpan
 * @since 2019-04-19
 */
@Data
public class MiniProgramUserDTO {

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 状态（0：正常，1：禁用）
     */
    private Integer status;

    /**
     * openid
     */
    private String openid;

}
