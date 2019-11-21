package com.fanzhe.payhelp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.adapter.OrderAdapter;
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
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class OrderManagerFragment extends Fragment {
    View view;

    Unbinder unbinder;

    Context mContext;

    @BindView(R.id.id_v_tap)
    View mTap;

    @BindView(R.id.id_et_name)
    EditText mEtName;

    @BindView(R.id.id_rv_content)
    RecyclerView mRvContent;

    @BindView(R.id.id_tv_startTime)
    TextView mStartTime;
    @BindView(R.id.id_tv_endTime)
    TextView mEndTime;


    ArrayList<Order> mData;

    OrderAdapter mAdapter;

    @BindViews({R.id.id_tab_1, R.id.id_tab_2, R.id.id_tab_3, R.id.id_tab_4})
    List<TextView> mTabs;



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_order_manager, null);

        unbinder = ButterKnife.bind(this, view);

        mContext = getActivity();

        initView();

        return view;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initView() {
        ViewGroup.LayoutParams layoutParams = mTap.getLayoutParams();
        layoutParams.width = UtilsHelper.getScreenWidth(mContext) / 4;
        mTap.setLayoutParams(layoutParams);

        mData = new ArrayList<>();
        mAdapter = new OrderAdapter(mData, mContext);
        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mRvContent.setAdapter(mAdapter);

        startAnim(0);
        search("0");

        view.findViewById(R.id.id_back).setVisibility(View.GONE);

        mRvContent.setOnScrollChangeListener((view, i, i1, i2, i3) -> {
            if(!ViewCompat.canScrollVertically(view,1)){
                if (isHaveMoreData) {
                    mPage ++;
                    search(status);
                }else{
                    if (mData.size() >= 30) {
                        ToastUtils.showToast(mContext,"没有更多订单");
                    }
                }
            }
        });

        mStartTime.setText(UtilsHelper.parseDateLong(new Date().getTime() + "","yyyy-MM-01"));
        mEndTime.setText(UtilsHelper.parseDateLong(new Date().getTime() + "","yyyy-MM-dd"));
    }


    int lastPosition;

    @OnClick({R.id.id_tab_1, R.id.id_tab_2, R.id.id_tab_3, R.id.id_tab_4,R.id.id_tv_startTime, R.id.id_tv_endTime,R.id.id_search})
    public void clickTab(TextView view) {
        switch (view.getId()) {
            case R.id.id_tab_1:
            case R.id.id_search:
                startAnim(0);
                search("0");
                break;
            case R.id.id_tab_2:
                startAnim(1);
                search("10");
                break;
            case R.id.id_tab_3:
                startAnim(2);
                search("20");
                break;
            case R.id.id_tab_4:
                startAnim(3);
                search("30");
                break;
            case R.id.id_tv_startTime:
            case R.id.id_tv_endTime:
                UtilsHelper.showDatePicker(mContext, result -> {
                    view.setText(result);
                });
                break;
        }
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


    private int mPage = 1;
    private boolean isHaveMoreData = true;
    private String status;

    private void search(String order_status){
        status = order_status;
        if (mPage == 1) {
            mData.removeAll(mData);
        }
        RequestParams params = new RequestParams(UrlAddress.ORDER_LIST);
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("order_sn", mEtName.getText().toString());
        params.addBodyParameter("order_status", order_status);
        params.addBodyParameter("start_time", mStartTime.getText().toString());
        params.addBodyParameter("end_time", mEndTime.getText().toString());
        params.addBodyParameter("page", mPage + "");
        params.addBodyParameter("page_size", "30");
        NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                ToastUtils.showToast(mContext, "获取订单信息失败,请检查网络");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onsuccessful(JSONObject jsonObject) {
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
                    ToastUtils.showToast(mContext, "获取订单信息失败," + jsonObject.optString("msg"));
                }
            }
        });
    }


}
