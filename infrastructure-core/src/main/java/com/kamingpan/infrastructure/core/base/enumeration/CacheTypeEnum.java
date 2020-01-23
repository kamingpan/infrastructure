package com.kamingpan.infrastructure.core.base.enumeration;

/**
 * CacheTypeEnum 缓存类型
 *
 * @author kamingpan
 * @since 2018-07-17
 */
public enum CacheTypeEnum {

    /**
     * cache 缓存
     */
    LOCAL("local", "本地缓存"),

    /**
     * redis 缓存
     */
    REDIS("redis", "redis缓存");

    /**
     * 缓存类型
     */
    private String type;

    /**
     * 描述
     */
    private String remark;

    CacheTypeEnum(String type, String remark) {
        this.type = type;
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public String getRemark() {
        return remark;
    }
}
