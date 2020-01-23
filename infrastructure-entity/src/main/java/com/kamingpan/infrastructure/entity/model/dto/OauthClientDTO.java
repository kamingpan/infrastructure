package com.kamingpan.infrastructure.entity.model.dto;

import com.kamingpan.infrastructure.entity.group.OauthClientGroup;
import com.kamingpan.infrastructure.entity.model.entity.OauthClient;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 授权客户端 dto
 *
 * @author kamingpan
 * @since 2018-06-29
 */
@Data
public class OauthClientDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 客户端名称
     */
    @NotEmpty(message = "客户端名称不能为空", groups = {OauthClientGroup.Insert.class, OauthClientGroup.Update.class})
    private String name;

    /**
     * 客户端id
     */
    @NotEmpty(message = "客户端id不能为空", groups = {OauthClientGroup.Update.class})
    private String clientId;

    /**
     * 客户端密钥
     */
    @NotEmpty(message = "客户端密钥不能为空", groups = {OauthClientGroup.Update.class})
    private String clientSecret;

    public OauthClient toOauthClient() {
        OauthClient oauthClient = new OauthClient();

        // 赋值
        oauthClient.setId(this.getId());
        oauthClient.setName(this.getName());
        oauthClient.setClientId(this.getClientId());
        oauthClient.setClientSecret(this.getClientSecret());

        return oauthClient;
    }

}
