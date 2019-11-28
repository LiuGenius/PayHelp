package com.fanzhe.payhelp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.adapter.AcmenAdapter;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.model.CodeBusiness;
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

public class MaNongInfoActivity extends AppCompatActivity {
    @BindView(R.id.id_rv_content)
    RecyclerView mRvContent;
    Context mContext;

    ArrayList<CodeBusiness> mData;

    AcmenAdapter mAdapter;

    @BindView(R.id.id_tv_title)
    TextView mTitle;

    @BindView(R.id.id_add)
    TextView mAdd;

    @BindView(R.id.id_swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

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

        switch (getIntent().getStringExtra("tag")){
            case "1":
                mTitle.setText("平台-商户码农");
                mAdd.setVisibility(View.VISIBLE);
                break;
            case "2":
                mTitle.setText("平台-码商码农");
                break;
        }

        findViewById(R.id.id_search_view).setVisibility(View.GONE);
        findViewById(R.id.id_ll_state_view).setVisibility(View.GONE);

        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            search();
        });

        initView();
    }

    private void initView() {
        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mData = new ArrayList<>();
        mAdapter = new AcmenAdapter(mData, this);
        mRvContent.setAdapter(mAdapter);

        search();
    }

    @OnClick({R.id.id_back,R.id.id_add})
    public void clickView(View view){
        switch (view.getId()) {
            case R.id.id_back:
                finish();
                break;
            case R.id.id_add:
                Intent intent = new Intent(mContext,MaNongListActivity.class);
                intent.putExtra("mch_id",getIntent().getStringExtra("mch_id"));
                startActivityForResult(intent,1);
                break;
        }
    }

    private void search(){
        mData.removeAll(mData);
        mAdapter.notifyDataSetChanged();
        RequestParams params = new RequestParams();
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        switch (getIntent().getStringExtra("tag")){
            case "1":
                params.setUri(UrlAddress.ORG_LOOK_MCH_MA_NONG);
                params.addBodyParameter("mch_id",getIntent().getStringExtra("mch_id"));
                break;
            case "2":
                params.setUri(UrlAddress.USER_LIST);
                params.addBodyParameter("coder_id",getIntent().getStringExtra("coder_id"));
                params.addBodyParameter("type","5");
                break;
        }
        NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                mSwipeRefreshLayout.setRefreshing(false);
                ToastUtils.showToast(mContext, "获取码农列表失败，请检查网络");
            }

            @Override
            public void onsuccessful(JSONObject jsonObject) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (UtilsHelper.parseResult(jsonObject)) {
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        mData.add(new CodeBusiness(dataArray.optJSONObject(i)));
                    }
                    mAdapter.notifyDataSetChanged();
                }else{
                    ToastUtils.showToast(mContext, "获取码农列表失败，" + jsonObject.optString("msg"));
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            search();
        }
    }
}
