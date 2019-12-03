package com.fanzhe.payhelp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.utils.UtilsHelper;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoneySettingActivity extends AppCompatActivity {


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

        // TODO: 2019-12-03 获取充值金额列表和接单预存款最低额度

    }


    @OnClick({R.id.id_submit, R.id.id_back})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.id_back:
                finish();
                break;
            case R.id.id_submit:
                // TODO: 2019-12-03 保存充值金额列表和接单预存款最低额度
                break;
        }
    }
}
