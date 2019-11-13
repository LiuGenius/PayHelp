package com.fanzhe.payhelp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettlementActivity extends AppCompatActivity {
    Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_settlement);

        ButterKnife.bind(this);
        mContext = this;

        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);
    }


    @OnClick({R.id.id_back,R.id.id_tv_startTime, R.id.id_tv_endTime,R.id.id_search,R.id.id_settlement,R.id.id_info})
    public void clickTab(View view) {
        switch (view.getId()) {
            case R.id.id_info:
                startActivity(new Intent(mContext,SettlementInfoActivity.class));
                break;
            case R.id.id_settlement:
                ToastUtils.showToast(mContext, "去结算");
                break;
            case R.id.id_back:
                finish();
                break;
            case R.id.id_tv_startTime:
            case R.id.id_tv_endTime:
                UtilsHelper.showDatePicker(mContext, result -> {
                    ((TextView) view).setText(result);
                });
                break;
            case R.id.id_search:
                ToastUtils.showToast(mContext, "search");
                break;
        }
    }
}
