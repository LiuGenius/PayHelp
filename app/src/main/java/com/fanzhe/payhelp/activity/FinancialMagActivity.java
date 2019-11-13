package com.fanzhe.payhelp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.utils.UtilsHelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FinancialMagActivity extends AppCompatActivity {
    @BindView(R.id.id_et_name)
    EditText mEtName;
    @BindView(R.id.id_rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.id_tv_title)
    TextView mTitle;

    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_pay_channel);

        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        findViewById(R.id.id_ll_state_view).setVisibility(View.GONE);
        findViewById(R.id.id_ll_t_view).setVisibility(View.VISIBLE);
        findViewById(R.id.id_add_channel).setVisibility(View.GONE);


        mTitle.setText("财务管理");
    }

    @OnClick(R.id.id_back)
    public void clickBack(){
        finish();
    }
}
