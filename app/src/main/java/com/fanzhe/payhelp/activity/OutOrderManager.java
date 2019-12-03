package com.fanzhe.payhelp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.adapter.OutOrderAdapter;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.model.Order;
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

public class OutOrderManager extends AppCompatActivity {
    Context mContext;

    int mPage = 1;
    boolean isHaveMoreData = true;

    @BindView(R.id.id_rv_content)
    RecyclerView mRvContent;

    ArrayList<Order> mData;

    OutOrderAdapter mAdapter;

    @BindView(R.id.id_swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_out_order_manager);
        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);

        ButterKnife.bind(this);
        mContext = this;

        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        mData = new ArrayList<>();
        mAdapter = new OutOrderAdapter(mData, mContext);
        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mRvContent.setAdapter(mAdapter);

        mRvContent.setOnScrollChangeListener((view, i, i1, i2, i3) -> {
            if(!ViewCompat.canScrollVertically(view,1)){
                if (isHaveMoreData && mData.size() > 30) {
                    mPage ++;
                    search();
                }else{
                    ToastUtils.showToast(mContext,"没有更多订单");
                }
            }
        });

        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            search();
        });

        search();
    }




    @OnClick(R.id.id_back)
    public void clickBack(){
        finish();
    }


    private void search(){
        if (mPage == 1) {
            mData.removeAll(mData);
            mAdapter.notifyDataSetChanged();
        }
        RequestParams params = new RequestParams(UrlAddress.OUT_ORDER_LIST);
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("page", mPage + "");
        params.addBodyParameter("page_size", "30");
        NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                ToastUtils.showToast(mContext, "获取掉单列表失败,请检查网络");
                mSwipeRefreshLayout.setRefreshing(false);
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onsuccessful(JSONObject jsonObject) {
                mSwipeRefreshLayout.setRefreshing(false);
                if (UtilsHelper.parseResult(jsonObject)) {
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Order order = new Order(jsonArray.optJSONObject(i));
                        mData.add(order);
                    }
                    if (jsonArray.length() < 30) {
                        isHaveMoreData = false;
                    }
                    mAdapter.notifyDataSetChanged();
                }else{
                    ToastUtils.showToast(mContext, "获取掉单列表失败," + jsonObject.optString("msg"));
                }
            }
        });
    }


}
