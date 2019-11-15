package com.fanzhe.payhelp.model;

import org.json.JSONObject;

public class Order {
    private String  order_id;
    private String order_sn;
    private String order_amount;
    private String pay_amount;
    private String out_trade_no;
    private String pay_sn;
    private String pay_type;
    private String mch_id;
    private String coder_id;
    private String channel_id;
    private String channel_instance_id;
    private String sub_instance_id;
    private String device_info;
    private String spbill_create_ip;
    private String order_status;
    private String nonce_str;
    private String notify_url;
    private String callback_num;
    private String create_time;
    private String update_time;
    private String complete_time;

    public Order(JSONObject jsonObject) {
        this.order_id = jsonObject.optString("order_id");
        this.order_sn = jsonObject.optString("order_sn");
        this.order_amount = jsonObject.optString("order_amount");
        this.pay_amount = jsonObject.optString("pay_amount");
        this.out_trade_no = jsonObject.optString("out_trade_no");
        this.pay_sn = jsonObject.optString("pay_sn");
        this.pay_type = jsonObject.optString("pay_type");
        this.mch_id = jsonObject.optString("mch_id");
        this.coder_id = jsonObject.optString("coder_id");
        this.channel_id = jsonObject.optString("channel_id");
        this.channel_instance_id = jsonObject.optString("channel_instance_id");
        this.sub_instance_id = jsonObject.optString("sub_instance_id");
        this.device_info = jsonObject.optString("device_info");
        this.spbill_create_ip = jsonObject.optString("spbill_create_ip");
        this.order_status = jsonObject.optString("order_status");
        this.nonce_str = jsonObject.optString("nonce_str");
        this.notify_url = jsonObject.optString("notify_url");
        this.callback_num = jsonObject.optString("callback_num");
        this.create_time = jsonObject.optString("create_time");
        this.update_time = jsonObject.optString("update_time");
        this.complete_time = jsonObject.optString("complete_time");
    }

    public String getCallback_num() {
        return callback_num;
    }

    public void setCallback_num(String callback_num) {
        this.callback_num = callback_num;
    }


    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getComplete_time() {
        return complete_time;
    }

    public void setComplete_time(String complete_time) {
        this.complete_time = complete_time;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOrder_sn() {
        return order_sn;
    }

    public void setOrder_sn(String order_sn) {
        this.order_sn = order_sn;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getPay_amount() {
        return pay_amount;
    }

    public void setPay_amount(String pay_amount) {
        this.pay_amount = pay_amount;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getPay_sn() {
        return pay_sn;
    }

    public void setPay_sn(String pay_sn) {
        this.pay_sn = pay_sn;
    }

    public String getPay_type() {
        return pay_type;
    }

    public void setPay_type(String pay_type) {
        this.pay_type = pay_type;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getCoder_id() {
        return coder_id;
    }

    public void setCoder_id(String coder_id) {
        this.coder_id = coder_id;
    }

    public String getChannel_id() {
        return channel_id;
    }

    public void setChannel_id(String channel_id) {
        this.channel_id = channel_id;
    }

    public String getChannel_instance_id() {
        return channel_instance_id;
    }

    public void setChannel_instance_id(String channel_instance_id) {
        this.channel_instance_id = channel_instance_id;
    }

    public String getSub_instance_id() {
        return sub_instance_id;
    }

    public void setSub_instance_id(String sub_instance_id) {
        this.sub_instance_id = sub_instance_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getSpbill_create_ip() {
        return spbill_create_ip;
    }

    public void setSpbill_create_ip(String spbill_create_ip) {
        this.spbill_create_ip = spbill_create_ip;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }
}
