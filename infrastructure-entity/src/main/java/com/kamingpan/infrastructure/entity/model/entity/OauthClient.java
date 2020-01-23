package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;

/**
 * 授权客户端
 *
 * @author kamingpan
 * @since 2019-04-12
 */
@TableName("system_oauth_client")
public class OauthClient extends BaseEntity<OauthClient> {

    /**
     * 客户端名称
     */
    private String name;

    /**
     * 客户端id
     */
    private String clientId;

    /**
     * 客户端密钥
     */
    private String clientSecret;

    /**
     * 客户端地址
     */
    private String clientUri;

    /**
     * 重定向地址
     */
    private String redirectUri;

    /**
     * 备注
     */
    private String remark;

    public OauthClient() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getClientUri() {
        return clientUri;
    }

    public void setClientUri(String clientUri) {
        this.clientUri = clientUri;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "OauthClient {" +
        "name=" + name +
        ", clientId=" + clientId +
        ", clientSecret=" + clientSecret +
        ", clientUri=" + clientUri +
        ", redirectUri=" + redirectUri +
        ", remark=" + remark +
        "}";
    }

}
