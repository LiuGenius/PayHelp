package com.fanzhe.payhelp.model;

public class CodeBus {
    private String name;
    private String totalFee;
    private String freezeFee;
    private String cFee;
    private String state;

    public CodeBus(String name, String totalFee, String freezeFee, String cFee, String state) {
        this.name = name;
        this.totalFee = totalFee;
        this.freezeFee = freezeFee;
        this.cFee = cFee;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(String totalFee) {
        this.totalFee = totalFee;
    }

    public String getFreezeFee() {
        return freezeFee;
    }

    public void setFreezeFee(String freezeFee) {
        this.freezeFee = freezeFee;
    }

    public String getcFee() {
        return cFee;
    }

    public void setcFee(String cFee) {
        this.cFee = cFee;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
