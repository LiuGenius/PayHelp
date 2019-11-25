package com.fanzhe.payhelp.model;

import org.json.JSONObject;

import java.text.DecimalFormat;

public class Settlement {
    private String total;
    private String user_name;

    public Settlement(JSONObject jsonObject) {
        this.total = new DecimalFormat("#.##").format(Double.parseDouble(jsonObject.optString("total")));
        this.user_name = jsonObject.optString("user_name");
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
