package com.fanzhe.payhelp.model;

import org.json.JSONObject;

public class SettlementInfo {
    private String org;
    private String merchants;
    private String codeMn;
    private String code;
    private String pay_amount;
    private String status;
    private String create_time;

    private String platform;


    public SettlementInfo(JSONObject jsonObject) {
        this.org = jsonObject.optString("1");
        this.merchants = jsonObject.optString("2");
        this.code = jsonObject.optString("4");
        this.codeMn = jsonObject.optString("3");
        this.pay_amount = jsonObject.optString("pay_amount");
        this.status = jsonObject.optString("status");
        this.create_time = jsonObject.optString("create_time") + "000";
        this.platform = jsonObject.optString("platform");
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCodeMn() {
        return codeMn;
    }

    public void setCodeMn(String codeMn) {
        this.codeMn = codeMn;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public String getMerchants() {
        return merchants;
    }

    public void setMerchants(String merchants) {
        this.merchants = merchants;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
