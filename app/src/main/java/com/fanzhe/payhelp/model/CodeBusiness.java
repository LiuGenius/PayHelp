package com.fanzhe.payhelp.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class CodeBusiness implements Parcelable {
    private String id;
    private String user_name;
    private String status;
    private int total_balance;
    private int balance;
    private String sex;
    private String id_card;
    private String true_name;
    private String mobile;

    private String special;


    public CodeBusiness(JSONObject jsonObject) {
        this.id = jsonObject.optString("id");
        this.user_name = jsonObject.optString("user_name");
        this.status = jsonObject.optString("status");
        this.total_balance = jsonObject.optInt("total_balance");
        this.balance = jsonObject.optInt("balance");
        this.sex = jsonObject.optString("sex");
        this.id_card = jsonObject.optString("id_card");
        this.true_name = jsonObject.optString("true_name");
        this.mobile = jsonObject.optString("mobile");
        this.special = jsonObject.optString("is_special");
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    protected CodeBusiness(Parcel in) {
        id = in.readString();
        user_name = in.readString();
        status = in.readString();
        total_balance = in.readInt();
        balance = in.readInt();
        sex = in.readString();
        id_card = in.readString();
        true_name = in.readString();
        mobile = in.readString();
        special = in.readString();
    }

    public static final Creator<CodeBusiness> CREATOR = new Creator<CodeBusiness>() {
        @Override
        public CodeBusiness createFromParcel(Parcel in) {
            return new CodeBusiness(in);
        }

        @Override
        public CodeBusiness[] newArray(int size) {
            return new CodeBusiness[size];
        }
    };

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getTrue_name() {
        return true_name;
    }

    public void setTrue_name(String true_name) {
        this.true_name = true_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotal_balance() {
        return total_balance;
    }

    public void setTotal_balance(int total_balance) {
        this.total_balance = total_balance;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user_name);
        dest.writeString(status);
        dest.writeInt(total_balance);
        dest.writeInt(balance);
        dest.writeString(sex);
        dest.writeString(id_card);
        dest.writeString(true_name);
        dest.writeString(mobile);
        dest.writeString(special);
    }
}
