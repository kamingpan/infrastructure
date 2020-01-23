package com.kamingpan.infrastructure.entity.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kamingpan.infrastructure.core.base.model.entity.BaseEntity;

import java.util.Date;

/**
 * 公众号用户地理位置
 *
 * @author kamingpan
 * @since 2018-07-19
 */
@TableName("system_subscription_user_location")
public class SubscriptionUserLocation extends BaseEntity<SubscriptionUserLocation> {

    /**
     * 用户主键
     */
    private String userId;

    /**
     * openid
     */
    private String openid;

    /**
     * 地理位置经度
     */
    private Double longitude;

    /**
     * 地理位置纬度
     */
    private Double latitude;

    /**
     * 地理位置精度
     */
    private Double locationPrecision;

    /**
     * 记录时间
     */
    private Date recordTime;

    public SubscriptionUserLocation() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLocationPrecision() {
        return locationPrecision;
    }

    public void setLocationPrecision(Double locationPrecision) {
        this.locationPrecision = locationPrecision;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public String toString() {
        return "SubscriptionUserLocation {" +
        "userId=" + userId +
        ", openid=" + openid +
        ", longitude=" + longitude +
        ", latitude=" + latitude +
        ", locationPrecision=" + locationPrecision +
        ", recordTime=" + recordTime +
        "}";
    }

}
