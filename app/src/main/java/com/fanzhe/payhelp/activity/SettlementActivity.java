package com.fanzhe.payhelp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.adapter.SettlementAdapter;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.model.Settlement;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettlementActivity extends AppCompatActivity {
    Context mContext;

    @BindView(R.id.id_tv_startTime)
    TextView mStartTime;
    @BindView(R.id.id_tv_endTime)
    TextView mEndTime;
    @BindView(R.id.id_sp_state)
    Spinner mSpState;

    @BindView(R.id.id_total)
    TextView mTotal;

    int state = -1;

    @BindView(R.id.id_rv_content)
    RecyclerView mRvContent;
    SettlementAdapter mAdapter;
    ArrayList<Settlement> mData;



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

        initView();
    }

    private void initView() {
        mSpState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                state = i - 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mData = new ArrayList<>();
        mAdapter = new SettlementAdapter(mData,mContext);
        mRvContent.setAdapter(mAdapter);

        mStartTime.setText(UtilsHelper.parseDateLong(new Date().getTime() + "",""));
        mEndTime.setText(UtilsHelper.parseDateLong(new Date().getTime() + "",""));

        search();
    }


    @OnClick({R.id.id_back, R.id.id_tv_startTime, R.id.id_tv_endTime, R.id.id_search, R.id.id_settlement, R.id.id_info})
    public void clickTab(View view) {
        switch (view.getId()) {
            case R.id.id_info:
                Intent intent = new Intent(mContext, SettlementInfoActivity.class);
                intent.putExtra("start_time",mStartTime.getText().toString());
                intent.putExtra("end_time",mEndTime.getText().toString());
                intent.putExtra("status",state == -1 ? "" : state + "");
                startActivity(intent);
                break;
            case R.id.id_settlement:
                settlement();
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
                search();
                break;
        }
    }

    private void search() {
        mData.removeAll(mData);
        RequestParams params = new RequestParams(UrlAddress.SETTLEMENT_LIST);
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("start_time", mStartTime.getText().toString());
        params.addBodyParameter("end_time", mEndTime.getText().toString());
        params.addBodyParameter("status", state == -1 ? "" : state + "");
        NetworkLoader.sendPost(mContext, params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                ToastUtils.showToast(mContext, "获取结算列表失败,请检查您的网络");
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onsuccessful(JSONObject jsonObject) {
                if (UtilsHelper.parseResult(jsonObject)) {
                    JSONObject object = jsonObject.optJSONObject("data");
                    String order_total = object.optString("order_total");
                    mTotal.setText("平台获得: " + order_total + " 元");
                    JSONArray jsonArray = object.optJSONArray("list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mData.add(new Settlement(jsonArray.optJSONObject(i)));
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showToast(mContext, "获取结算列表失败," + jsonObject.optString("msg"));
                }
            }
        });
    }

    private void settlement(){
        RequestParams params = new RequestParams(UrlAddress.SETTLEMENT);
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("start_time", mStartTime.getText().toString());
        params.addBodyParameter("end_time", mEndTime.getText().toString());
        params.addBodyParameter("status", state == -1 ? "" : state + "");
        NetworkLoader.sendPost(mContext, params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                ToastUtils.showToast(mContext, "结算失败,请检查您的网络");
            }
            @SuppressLint("SetTextI18n")
            @Override
            public void onsuccessful(JSONObject jsonObject) {
                if (UtilsHelper.parseResult(jsonObject)) {
                    ToastUtils.showToast(mContext,"结算成功");
                    finish();
                } else {
                    ToastUtils.showToast(mContext, "结算失败," + jsonObject.optString("msg"));
                }
            }
        });
    }
}
