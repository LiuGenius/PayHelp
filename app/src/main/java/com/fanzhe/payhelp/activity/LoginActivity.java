package com.fanzhe.payhelp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends Activity {

    @BindView(R.id.id_et_userName)
    EditText mUserName;

    @BindView(R.id.id_et_password)
    EditText mPassword;

    @BindView(R.id.id_cb_rmbAc)
    CheckBox mRmbAc;

    Context mContext;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
//        getSupportActionBar().hide();// 隐藏ActionBar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_login);

        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);

        ButterKnife.bind(this);

        mContext = this;

//

        mUserName.setText(UtilsHelper.getLoginInfo(mContext, "userName"));
        mPassword.setText(UtilsHelper.getLoginInfo(mContext, "password"));



    }

    @OnClick({R.id.id_login,R.id.id_viewPwd})
    public void onclickView(View view){
        switch (view.getId()){
            case R.id.id_login:
                login();
                break;
            case R.id.id_viewPwd:
                if (mPassword.getInputType() == 129) {
                    mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }else{
                    mPassword.setInputType(129);
                }
                break;
        }
    }

    private void login(){
        String userName = mUserName.getText().toString();
        String password = mPassword.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.showToast(mContext, "请输入用户名");
            mUserName.setError("请输入用户名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showToast(mContext, "请输入密码");
            mPassword.setError("请输入密码");
            return;
        }
        RequestParams params = new RequestParams(UrlAddress.USER_LOGIN);
        params.addBodyParameter("username", userName);
        params.addBodyParameter("password", password);
        NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                ToastUtils.showToast(mContext,"登录失败，请检查网络");
            }

            @Override
            public void onsuccessful(JSONObject jsonObject) {
                if (UtilsHelper.parseResult(jsonObject)) {
                    App.getInstance().setUSER_DATA(new App.USER(jsonObject.optJSONObject("data")));

                    startActivity(new Intent(mContext, MainActivity.class));
                    finish();
                    //保存登录信息
                    if (mRmbAc.isChecked()) {
                        UtilsHelper.saveLoginInfo(mContext, userName, password);
                    }else{
                        UtilsHelper.saveLoginInfo(mContext, "", "");
                    }
                }else{
                    ToastUtils.showToast(mContext,"登录失败，" + jsonObject.optString("msg"));
                }
            }
        });
    }
}
