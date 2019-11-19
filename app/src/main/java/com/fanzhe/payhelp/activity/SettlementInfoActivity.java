package com.fanzhe.payhelp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.adapter.SettlementInfoAdapter;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.model.SettlementInfo;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettlementInfoActivity extends AppCompatActivity {
    @BindView(R.id.id_rv_content)
    RecyclerView mRvContent;

    Context mContext;

    ArrayList<SettlementInfo> mData;

    SettlementInfoAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_settlement_info);
        ButterKnife.bind(this);
        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);

        mContext = this;

        initView();
    }

    private void initView() {
        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mData = new ArrayList<>();
        mAdapter = new SettlementInfoAdapter(mData,mContext);
        mRvContent.setAdapter(mAdapter);

        getData();
    }

    private void getData() {
        RequestParams params = new RequestParams(UrlAddress.SETTLEMENT_INFO_LIST);
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("start_time",getIntent().getStringExtra("start_time"));
        params.addBodyParameter("end_time",getIntent().getStringExtra("end_time"));
        params.addBodyParameter("status",getIntent().getStringExtra("status"));
        NetworkLoader.sendPost(mContext, params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                ToastUtils.showToast(mContext,"获取结算明细失败,请检查您的网络");
            }

            @Override
            public void onsuccessful(JSONObject jsonObject) {
                if (UtilsHelper.parseResult(jsonObject)) {
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mData.add(new SettlementInfo(jsonArray.optJSONObject(i)));
                    }
                    mAdapter.notifyDataSetChanged();
                }else{
                    ToastUtils.showToast(mContext,"获取结算明细失败," + jsonObject.optString("msg"));
                }
            }
        });
    }

    @OnClick(R.id.id_back)
    public void clickBack(){
        finish();
    }
}
