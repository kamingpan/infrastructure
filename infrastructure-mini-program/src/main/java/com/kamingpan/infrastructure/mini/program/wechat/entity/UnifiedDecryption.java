package com.kamingpan.infrastructure.mini.program.wechat.entity;

import javax.validation.constraints.NotEmpty;

/**
 * 统一解密（用于统一接收微信需要解密的信息）
 *
 * @author kamingpan
 * @since 2019-04-18
 */
public class UnifiedDecryption {

    /**
     * 消息密文
     */
    @NotEmpty(message = "消息密文不能为空", groups = ValidateClass.class)
    private String encryptedData;

    /**
     * 加密算法的初始向量
     */
    @NotEmpty(message = "初始向量不能为空", groups = ValidateClass.class)
    private String iv;

    public UnifiedDecryption() {
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    /**
     * 校验类
     */
    public interface ValidateClass {
    }

}
