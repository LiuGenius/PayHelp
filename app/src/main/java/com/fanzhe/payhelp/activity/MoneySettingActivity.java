package com.fanzhe.payhelp.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.fragment.IndexFragment;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoneySettingActivity extends AppCompatActivity {


    Context mContext;

    @BindView(R.id.id_ll_recharge_view)
    LinearLayout mLlRechargeView;

    @BindView(R.id.id_mixPrice)
    EditText mMixPrice;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_setting_money);

        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);

        ButterKnife.bind(this);

        mContext = this;
        /**
         * 获取充值配置信息
         */
        RequestParams params = new RequestParams(UrlAddress.CHARGE_CONF);
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        NetworkLoader.sendPost(mContext, params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                ToastUtils.showToast(mContext, "获取充值配置失败,请检查网络");
            }

            @Override
            public void onsuccessful(JSONObject jsonObject) {
                if (UtilsHelper.parseResult(jsonObject)) {
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < mLlRechargeView.getChildCount(); i++) {
                        String s = jsonArray.optString(i);
                        ((EditText)mLlRechargeView.getChildAt(i)).setText(s);
                    }
                } else {
                    ToastUtils.showToast(mContext, "获取充值配置失败," + jsonObject.optString("msg"));
                }
            }
        });

        mMixPrice.setText(IndexFragment.mMoney + "");
    }


    @OnClick({R.id.id_submit, R.id.id_back})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.id_back:
                finish();
                break;
            case R.id.id_submit:
                JSONArray recharge_money_list = new JSONArray();
                try {
                    recharge_money_list.put(getChildValueAtIndex(0));
                    recharge_money_list.put(getChildValueAtIndex(1));
                    recharge_money_list.put(getChildValueAtIndex(2));
                    recharge_money_list.put(getChildValueAtIndex(3));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String min_money = mMixPrice.getText().toString();
                RequestParams params = new RequestParams(UrlAddress.EDIT_MONEY_SETTING);
                params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
                params.addBodyParameter("recharge_money_list", recharge_money_list.toString());
                params.addBodyParameter("min_money", min_money);
                NetworkLoader.sendPost(mContext, params, new NetworkLoader.networkCallBack() {
                    @Override
                    public void onfailure(String errorMsg) {
                        ToastUtils.showToast(mContext, "保存设置失败,请检查您的网络");
                    }
                    @Override
                    public void onsuccessful(JSONObject jsonObject) {
                        if (UtilsHelper.parseResult(jsonObject)) {
                            ToastUtils.showToast(mContext, "修改成功");
                            IndexFragment.mMoney = Double.parseDouble(mMixPrice.getText().toString());
                        } else {
                            ToastUtils.showToast(mContext, "保存设置失败," + jsonObject.optString("msg"));
                        }
                    }
                });
                break;
        }
    }

    private double getChildValueAtIndex(int index){
        String s = ((EditText) mLlRechargeView.getChildAt(index)).getText().toString();
        if (TextUtils.isEmpty(s)) {
            return 0;
        }
        return Double.parseDouble(s);
    }
}
