package com.fanzhe.payhelp.model;

import org.json.JSONObject;

public class Rate {
    private String channel_name;
    private String channel_code;
    private String id;
    private String rate;
    private String conf_id;

    public Rate(JSONObject jsonObject) {
        this.channel_name = jsonObject.optString("channel_name");
        this.channel_code = jsonObject.optString("channel_code");
        this.id = jsonObject.optString("id");
        this.rate = jsonObject.optString("rate");
        this.conf_id = jsonObject.optString("conf_id");
    }

    public String getChannel_name() {
        return channel_name;
    }

    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    public String getChannel_code() {
        return channel_code;
    }

    public void setChannel_code(String channel_code) {
        this.channel_code = channel_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getConf_id() {
        return conf_id;
    }

    public void setConf_id(String conf_id) {
        this.conf_id = conf_id;
    }
}
