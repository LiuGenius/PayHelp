package com.fanzhe.payhelp.model;

public class Order {
    private String orderId;
    private String outOrderId;
    private String createTime;
    private String payCodeName;
    private String payChannel;
    private String payEqui;
    private String payCompleteTime;
    private String orderPrice;
    private String payPrice;
    private String payState;

    public Order(String orderId, String outOrderId, String createTime, String payCodeName, String payChannel, String payEqui, String payCompleteTime, String orderPrice, String payPrice, String payState) {
        this.orderId = orderId;
        this.outOrderId = outOrderId;
        this.createTime = createTime;
        this.payCodeName = payCodeName;
        this.payChannel = payChannel;
        this.payEqui = payEqui;
        this.payCompleteTime = payCompleteTime;
        this.orderPrice = orderPrice;
        this.payPrice = payPrice;
        this.payState = payState;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOutOrderId() {
        return outOrderId;
    }

    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayCodeName() {
        return payCodeName;
    }

    public void setPayCodeName(String payCodeName) {
        this.payCodeName = payCodeName;
    }

    public String getPayChannel() {
        return payChannel;
    }

    public void setPayChannel(String payChannel) {
        this.payChannel = payChannel;
    }

    public String getPayEqui() {
        return payEqui;
    }

    public void setPayEqui(String payEqui) {
        this.payEqui = payEqui;
    }

    public String getPayCompleteTime() {
        return payCompleteTime;
    }

    public void setPayCompleteTime(String payCompleteTime) {
        this.payCompleteTime = payCompleteTime;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getPayState() {
        return payState;
    }

    public void setPayState(String payState) {
        this.payState = payState;
    }
}
