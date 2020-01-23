package com.kamingpan.infrastructure.util.mobile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 手机归属地实体
 *
 * @author kamingpan
 * @since 2018-03-13
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MobileBean {

    /**
     * 手机号码
     */
    @JsonProperty("Mobile")
    private String mobile;

    /**
     * 查询结果
     */
    @JsonProperty("QueryResult")
    private String queryResult;

    /**
     *
     */
    @JsonProperty("TO")
    private String to;

    /**
     * 运营商
     */
    @JsonProperty("Corp")
    private String corp;

    /**
     * 所属省份
     */
    @JsonProperty("Province")
    private String province;

    /**
     * 所属城市
     */
    @JsonProperty("City")
    private String city;

    /**
     * 地区编码
     */
    @JsonProperty("AreaCode")
    private String areaCode;

    /**
     * 邮政编码
     */
    @JsonProperty("PostCode")
    private String postCode;

    /**
     *
     */
    @JsonProperty("VNO")
    private String vno;

    /**
     *
     */
    @JsonProperty("Card")
    private String card;

    public MobileBean() {
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(String queryResult) {
        this.queryResult = queryResult;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCorp() {
        return corp;
    }

    public void setCorp(String corp) {
        this.corp = corp;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getVno() {
        return vno;
    }

    public void setVno(String vno) {
        this.vno = vno;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
