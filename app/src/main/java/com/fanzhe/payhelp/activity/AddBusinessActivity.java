package com.fanzhe.payhelp.activity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.utils.UtilsHelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBusinessActivity extends AppCompatActivity {
    @BindView(R.id.id_et_yck)
    EditText mEtYck;//预存款
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_add_business);

        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);

        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        switch (getIntent().getStringExtra("tag")){
            case "addBusiness":
                mEtYck.setVisibility(View.GONE);
                break;
            case "addCode":

                break;
        }
    }

    @OnClick(R.id.id_back)
    public void clickBack(){
        finish();
    }
}
