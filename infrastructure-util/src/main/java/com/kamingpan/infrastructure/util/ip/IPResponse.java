package com.kamingpan.infrastructure.util.ip;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ip响应结果
 *
 * @author kamingpan
 * @since 2018-04-27
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IPResponse {

    // 不响应详细结果
    private static final String UNKNOWN = "XX";

    // 内网IP结果
    private static final String INTRANET_IP = "内网IP";

    // ip
    private String ip;

    // 国家
    private String country;

    // 地区
    private String area;

    // 区域（省份）
    private String region;

    // 城市
    private String city;

    // 县城
    private String county;

    // 运营商
    private String isp;

    // 国家id
    @JsonProperty("country_id")
    private String countryId;

    // 地区id
    @JsonProperty("area_id")
    private String areaId;

    // 区域（省份）id
    @JsonProperty("region_id")
    private String regionId;

    // 城市id
    @JsonProperty("city_id")
    private String cityId;

    // 县城id
    @JsonProperty("county_id")
    private String countyId;

    // 运营商id
    @JsonProperty("isp_id")
    private String ispId;

    public IPResponse() {
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getIsp() {
        return isp;
    }

    public void setIsp(String isp) {
        this.isp = isp;
    }

    public String getCountryId() {
        return countryId;
    }

    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCountyId() {
        return countyId;
    }

    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    public String getIspId() {
        return ispId;
    }

    public void setIspId(String ispId) {
        this.ispId = ispId;
    }

    /**
     * ip地址响应详情
     *
     * @return 国家 + 省份 + 城市 + 县城 + 运营商
     */
    public String getDetail() {
        if (IPResponse.INTRANET_IP.equals(this.isp)) {
            return IPResponse.INTRANET_IP;
        }

        // LOGGER.debug(country + " - " + area + " - " + region + " - " + city + " - " + county + " - " + isp);
        return this.country + this.region + this.city
                + (null == this.county || IPResponse.UNKNOWN.equals(this.county) ? "" : this.county)
                + (null != this.isp ? ("(" + this.isp + ")") : "");
    }
}
