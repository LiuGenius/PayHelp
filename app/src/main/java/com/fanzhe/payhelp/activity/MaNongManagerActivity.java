package com.fanzhe.payhelp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.adapter.MaNongAdapter;
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

public class MaNongManagerActivity extends AppCompatActivity {
    @BindView(R.id.id_et_name)
    EditText mEtName;
    @BindView(R.id.id_rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.id_sp_state)
    Spinner mState;

    Context mContext;

    ArrayList<CodeBusiness> mData;

    MaNongAdapter mAdapter;

    int status = -1;

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

        mTitle.setText("码农管理");

        mAdd.setVisibility(View.VISIBLE);

        initView();
    }

    private void initView() {
        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mData = new ArrayList<>();
        mAdapter = new MaNongAdapter(mData, this);
        mRvContent.setAdapter(mAdapter);

        mState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                status = position - 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            search();
        });
        search();
    }

    @OnClick({R.id.id_back,R.id.id_search,R.id.id_add})
    public void clickView(View view){
        switch (view.getId()) {
            case R.id.id_back:
                finish();
                break;
            case R.id.id_search:
                search();
                break;
            case R.id.id_add:
                Intent intent = new Intent(mContext, AddBusinessActivity.class);
                intent.putExtra("tag", "3");
                startActivity(intent);
                break;
        }
    }

    private void search(){
        mData.removeAll(mData);
        RequestParams params = new RequestParams(UrlAddress.USER_LIST);
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("status",status + "");
        params.addBodyParameter("user_name", mEtName.getText().toString());
        params.addBodyParameter("type", "6");
        NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                mSwipeRefreshLayout.setRefreshing(false);
                ToastUtils.showToast(mContext, "获取通道列表失败，请检查网络");
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
                    ToastUtils.showToast(mContext, "获取通道列表失败，" + jsonObject.optString("msg"));
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
