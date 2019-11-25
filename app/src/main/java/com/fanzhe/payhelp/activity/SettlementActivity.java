package com.fanzhe.payhelp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettlementActivity extends AppCompatActivity {
    Context mContext;

    @BindView(R.id.id_tv_startTime)
    TextView mStartTime;
    @BindView(R.id.id_tv_endTime)
    TextView mEndTime;
    @BindView(R.id.id_total)
    TextView mTotal;

    @BindView(R.id.id_rv_content)
    RecyclerView mRvContent;
    SettlementAdapter mAdapter;
    ArrayList<Settlement> mData;

    @BindView(R.id.id_v_tap)
    View mTap;
    @BindViews({R.id.id_tab_1, R.id.id_tab_2, R.id.id_tab_3})
    List<TextView> mTabs;
    int lastPosition;
    String state = "";


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
        ViewGroup.LayoutParams layoutParams = mTap.getLayoutParams();
        layoutParams.width = UtilsHelper.getScreenWidth(mContext) / 3;
        mTap.setLayoutParams(layoutParams);

        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mData = new ArrayList<>();
        mAdapter = new SettlementAdapter(mData,mContext);
        mRvContent.setAdapter(mAdapter);

        mStartTime.setText(UtilsHelper.parseDateLong(new Date().getTime() + "","yyyy-MM-01"));
        mEndTime.setText(UtilsHelper.parseDateLong(new Date().getTime() + "","yyyy-MM-dd"));

        startAnim(0);
        search("");

        if (App.getInstance().getUSER_DATA().getRole_id().equals("1")) {
            findViewById(R.id.id_settlement).setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.id_tab_1, R.id.id_tab_2, R.id.id_tab_3,R.id.id_back, R.id.id_tv_startTime, R.id.id_tv_endTime, R.id.id_search, R.id.id_settlement, R.id.id_info})
    public void clickTab(View view) {
        switch (view.getId()) {
            case R.id.id_tab_1:
            case R.id.id_search:
                startAnim(0);
                search("");
                break;
            case R.id.id_tab_2:
                startAnim(1);
                search("1");
                break;
            case R.id.id_tab_3:
                startAnim(2);
                search("0");
                break;
            case R.id.id_info:
                Intent intent = new Intent(mContext, SettlementInfoActivity.class);
                intent.putExtra("start_time",mStartTime.getText().toString());
                intent.putExtra("end_time",mEndTime.getText().toString());
                intent.putExtra("status","");
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
        }
    }

    private void search(String state) {
        this.state = state;
        mData.removeAll(mData);
        mAdapter.notifyDataSetChanged();
        RequestParams params = new RequestParams(UrlAddress.SETTLEMENT_LIST);
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("start_time", mStartTime.getText().toString());
        params.addBodyParameter("end_time", mEndTime.getText().toString());
        params.addBodyParameter("status", state);
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
                    mTotal.setText("平台获得: " + new DecimalFormat("#.##").format(Double.parseDouble(order_total)) + " 元");
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

    private void startAnim(int index) {
        int width = mTap.getLayoutParams().width;
        Animation translateAnimation = new TranslateAnimation(lastPosition * width,
                index * width, 0, 0);
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        mTap.startAnimation(translateAnimation);

        for (int i = 0; i < mTabs.size(); i++) {
            mTabs.get(i).setTextColor(Color.parseColor("#A0A0A0"));
        }
        mTabs.get(index).setTextColor(Color.parseColor("#46A9F4"));
        lastPosition = index;
    }

    private void settlement(){
        RequestParams params = new RequestParams(UrlAddress.SETTLEMENT);
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("start_time", mStartTime.getText().toString());
        params.addBodyParameter("end_time", mEndTime.getText().toString());
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
