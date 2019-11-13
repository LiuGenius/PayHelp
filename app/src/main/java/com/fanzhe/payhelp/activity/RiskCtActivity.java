package com.fanzhe.payhelp.activity;

import android.os.Bundle;
import android.view.Window;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.utils.UtilsHelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RiskCtActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_risk_ct);
        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.id_back)
    public void clickBack(){
        finish();
    }
}
