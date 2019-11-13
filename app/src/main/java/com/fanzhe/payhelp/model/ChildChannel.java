package com.fanzhe.payhelp.model;

public class ChildChannel {
    private String CodeBusName;
    private String payImgUrl;
    private String openState;

    public ChildChannel(String codeBusName, String payImgUrl, String openState) {
        CodeBusName = codeBusName;
        this.payImgUrl = payImgUrl;
        this.openState = openState;
    }

    public String getCodeBusName() {
        return CodeBusName;
    }

    public void setCodeBusName(String codeBusName) {
        CodeBusName = codeBusName;
    }

    public String getPayImgUrl() {
        return payImgUrl;
    }

    public void setPayImgUrl(String payImgUrl) {
        this.payImgUrl = payImgUrl;
    }

    public String getOpenState() {
        return openState;
    }

    public void setOpenState(String openState) {
        this.openState = openState;
    }
}
