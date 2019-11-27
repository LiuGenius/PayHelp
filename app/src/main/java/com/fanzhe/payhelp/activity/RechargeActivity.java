package com.fanzhe.payhelp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class RechargeActivity extends AppCompatActivity {
    @BindView(R.id.id_rg_money)
    RadioGroup mRgMoney;
    @BindView(R.id.id_rg_pay_type)
    RadioGroup mRgPayType;

    String money = "0.01";
    String payType = "wechat";

    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_recharge);

        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);

        ButterKnife.bind(this);

        mContext = this;
        mRgMoney.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rb = findViewById(radioGroup.getCheckedRadioButtonId());
            money = rb.getText().toString();
        });
        mRgPayType.setOnCheckedChangeListener((radioGroup, i) -> {
            RadioButton rb = findViewById(radioGroup.getCheckedRadioButtonId());
            payType = rb.getText().toString().equals("微信") ? "wechat" : "alipay";
        });
    }


    @OnClick({R.id.id_submit, R.id.id_back})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.id_back:
                finish();
                break;
            case R.id.id_submit:
                RequestParams params = new RequestParams(UrlAddress.MA_NONG_RECHARGE);
                params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
                params.addBodyParameter("pay_type", payType);
                params.addBodyParameter("money", money);
                NetworkLoader.sendPost(mContext, params, new NetworkLoader.networkCallBack() {
                    @Override
                    public void onfailure(String errorMsg) {
                        ToastUtils.showToast(mContext,"充值失败,请检查网络");
                    }

                    @Override
                    public void onsuccessful(JSONObject jsonObject) {
                        if (UtilsHelper.parseResult(jsonObject)) {
                            String pay_url = jsonObject.optJSONObject("data").optString("pay_url");
                            Uri uri = Uri.parse(pay_url);
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }else{
                            ToastUtils.showToast(mContext,"充值失败," + jsonObject.optString("msg"));
                        }
                    }
                });
                break;
        }
    }
}
