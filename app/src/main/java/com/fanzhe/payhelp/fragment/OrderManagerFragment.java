package com.fanzhe.payhelp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_order_manager, null);

        unbinder = ButterKnife.bind(this, view);

        mContext = getActivity();

        initView();

        return view;
    }
    private void initView() {
        ViewGroup.LayoutParams layoutParams = mTap.getLayoutParams();
        layoutParams.width = UtilsHelper.getScreenWidth(mContext) / 3;
        mTap.setLayoutParams(layoutParams);

        mData = new ArrayList<>();
        mAdapter = new OrderAdapter(mData, mContext);
        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mRvContent.setAdapter(mAdapter);

        startAnim(0);
        search("0");
    }


    int lastPosition;

    @OnClick({R.id.id_tab_1, R.id.id_tab_2, R.id.id_tab_3, R.id.id_tv_startTime, R.id.id_tv_endTime,R.id.id_search})
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
        lastPosition = index;
    }

    int tag2Num = 0;
    int tag3Num = 0;

    private void search(String order_status){
        mData.removeAll(mData);
        RequestParams params = new RequestParams(UrlAddress.ORDER_LIST);
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("order_sn", mEtName.getText().toString());
        params.addBodyParameter("order_status", order_status);
        params.addBodyParameter("start_time", mStartTime.getText().toString());
        params.addBodyParameter("end_time", mEndTime.getText().toString());
        NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                ToastUtils.showToast(mContext, "获取订单信息失败,请检查网络");
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onsuccessful(JSONObject jsonObject) {
                if (UtilsHelper.parseResult(jsonObject)) {
                    if (order_status.equals("0")) {
                        tag2Num = 0;
                        tag3Num = 0;
                    }
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Order order = new Order(jsonArray.optJSONObject(i));
                        mData.add(order);
                        if (order.getOrder_status().equals("10")) {
                            tag2Num ++;
                        }else{
                            tag3Num ++;
                        }
                    }
                    if (order_status.equals("0")) {
                        ((TextView)view.findViewById(R.id.id_tab_1)).setText("全部("+jsonArray.length()+")");
                        ((TextView)view.findViewById(R.id.id_tab_2)).setText("未支付("+tag2Num+")");
                        ((TextView)view.findViewById(R.id.id_tab_3)).setText("已支付("+tag3Num+")");
                    }
                    mAdapter.notifyDataSetChanged();
                }else{
                    ToastUtils.showToast(mContext, "获取订单信息失败," + jsonObject.optString("msg"));
                }
            }
        });
    }


}
