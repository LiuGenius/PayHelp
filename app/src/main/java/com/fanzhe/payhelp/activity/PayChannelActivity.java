package com.fanzhe.payhelp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.widget.EditText;
import android.widget.Spinner;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.adapter.ChannelAdapter;
import com.fanzhe.payhelp.model.Channel;
import com.fanzhe.payhelp.utils.UtilsHelper;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayChannelActivity extends AppCompatActivity {
    @BindView(R.id.id_et_name)
    EditText mName;
    @BindView(R.id.id_rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.id_sp_state)
    Spinner mState;

    Context mContext;

    ArrayList<Channel> mData;

    ChannelAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_pay_channel);

        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);

        ButterKnife.bind(this);

        mContext = this;

        initView();
    }

    private void initView() {
        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mData = new ArrayList<>();
        mAdapter = new ChannelAdapter(mData, mContext);
        mRvContent.setAdapter(mAdapter);


        initData();
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            mData.add(new Channel(i + "", "支付宝", 2 % (i + 1) == 0));
        }
        mAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.id_back)
    public void clickBack(){
        finish();
    }
}
