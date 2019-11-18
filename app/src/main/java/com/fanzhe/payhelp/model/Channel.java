package com.fanzhe.payhelp.model;

import org.json.JSONObject;

public class Channel {
    private String id;
    private String channel_name;
    private String channel_code;
    private String logo;
    private String min_money;
    private String max_money;
    private String total_money;
    private String start_time;
    private String end_time;
    private String status;
    private String update_time;

    private String instance_id;

    public Channel(JSONObject jsonObject) {
        this.id = jsonObject.optString("id");
        this.instance_id = jsonObject.optString("instance_id");
        this.channel_name = jsonObject.optString("channel_name");
        this.channel_code = jsonObject.optString("channel_code");
        this.logo = jsonObject.optString("logo");
        this.min_money = jsonObject.optString("min_money");
        this.max_money = jsonObject.optString("max_money");
        this.total_money = jsonObject.optString("total_money");
        this.start_time = jsonObject.optString("start_time");
        this.end_time = jsonObject.optString("end_time");
        this.status = jsonObject.optString("status");
        this.update_time = jsonObject.optString("update_time");
    }

    public String getInstance_id() {
        return instance_id;
    }

    public void setInstance_id(String instance_id) {
        this.instance_id = instance_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMin_money() {
        return min_money;
    }

    public void setMin_money(String min_money) {
        this.min_money = min_money;
    }

    public String getMax_money() {
        return max_money;
    }

    public void setMax_money(String max_money) {
        this.max_money = max_money;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
