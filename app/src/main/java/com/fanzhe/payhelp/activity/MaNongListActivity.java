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
import com.fanzhe.payhelp.view.RecyclerViewClickListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MaNongListActivity extends AppCompatActivity {
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

        mTitle.setText("单击码农建立对应关系");
        findViewById(R.id.id_search_view).setVisibility(View.GONE);
        findViewById(R.id.id_ll_state_view).setVisibility(View.GONE);
        mAdd.setVisibility(View.GONE);

        initView();
    }

    private void initView() {
        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mData = new ArrayList<>();
        mAdapter = new AcmenAdapter(mData, this,"none");
        mRvContent.setAdapter(mAdapter);

        mRvContent.addOnItemTouchListener(new RecyclerViewClickListener(mContext, mRvContent, (view, position) -> {
            RequestParams params = new RequestParams(UrlAddress.ORG_BIND_ACMEN);
            params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
            params.addBodyParameter("mch_id",getIntent().getStringExtra("mch_id"));
            params.addBodyParameter("acmen_id",mData.get(position).getId());
            NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
                @Override
                public void onfailure(String errorMsg) {
                    ToastUtils.showToast(mContext, "操作失败，请检查网络");
                }

                @Override
                public void onsuccessful(JSONObject jsonObject) {
                    if (UtilsHelper.parseResult(jsonObject)) {
                        ToastUtils.showToast(mContext, "操作成功");
                        setResult(RESULT_OK);
                        finish();
                    }else{
                        ToastUtils.showToast(mContext, "操作失败，" + jsonObject.optString("msg"));
                    }
                }
            });
        }));

        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            search();
        });

        search();
    }

    @OnClick({R.id.id_back,R.id.id_add})
    public void clickView(View view){
        switch (view.getId()) {
            case R.id.id_back:
                finish();
                break;
        }
    }

    private void search(){
        mData.removeAll(mData);
        mAdapter.notifyDataSetChanged();
        RequestParams params = new RequestParams(UrlAddress.USER_LIST);
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("type","3");
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
                        CodeBusiness codeBusiness = new CodeBusiness(dataArray.optJSONObject(i));
                        if (codeBusiness.getSpecial().equals("1")) {
                            mData.add(codeBusiness);
                        }
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
