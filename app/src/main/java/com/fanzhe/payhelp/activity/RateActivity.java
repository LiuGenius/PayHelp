package com.fanzhe.payhelp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.adapter.RateAdapter;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.model.Rate;
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

public class RateActivity extends AppCompatActivity {
    @BindView(R.id.id_rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.id_tv_name)
    TextView mName;

    RateAdapter mAdapter;

    public static ArrayList<Rate> mData;

    Context mContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_rate);

        ButterKnife.bind(this);
        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);

        mContext = this;

        initView();
    }

    private void initView() {
        mName.setText(getIntent().getStringExtra("name"));

        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mData = new ArrayList<>();
        mAdapter = new RateAdapter(mData,mContext);
        mRvContent.setAdapter(mAdapter);

        getData();
    }

    private void getData() {
        RequestParams params = new RequestParams(UrlAddress.RATE_LIST);
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("uid",getIntent().getStringExtra("uid"));
        NetworkLoader.sendPost(mContext, params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                ToastUtils.showToast(mContext,"获取费率列表失败,请检查网络");
            }
            @Override
            public void onsuccessful(JSONObject jsonObject) {
                if (UtilsHelper.parseResult(jsonObject)) {
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        mData.add(new Rate(jsonArray.optJSONObject(i)));
                    }
                    mAdapter.notifyDataSetChanged();
                }else{
                    ToastUtils.showToast(mContext,"获取费率列表失败," + jsonObject.optString("msg"));
                }
            }
        });
    }

    @OnClick({R.id.id_back,R.id.id_save})
    public void clickView(View view){
        switch (view.getId()) {
            case R.id.id_back:
                finish();
                break;
            case R.id.id_save:
                for (int i = 0; i < mData.size(); i++) {
                    Rate rate = mData.get(i);
                    RequestParams params = new RequestParams(UrlAddress.EDIT_RATE);
                    params.addBodyParameter("auth_key",App.getInstance().getUSER_DATA().getAuth_key());
                    params.addBodyParameter("conf_id",rate.getConf_id());
                    params.addBodyParameter("channel_id",rate.getId());
                    params.addBodyParameter("uid",getIntent().getStringExtra("uid"));
                    params.addBodyParameter("rate",rate.getRate());
                    NetworkLoader.sendPost(mContext, params, new NetworkLoader.networkCallBack() {
                        @Override
                        public void onfailure(String errorMsg) {
                            ToastUtils.showToast(mContext,"修改费率失败,请检查网络");
                        }

                        @Override
                        public void onsuccessful(JSONObject jsonObject) {
                            if (UtilsHelper.parseResult(jsonObject)) {

                            }else{
                                ToastUtils.showToast(mContext,"修改费率失败," + jsonObject.optString("msg"));
                            }
                        }
                    });
                }
                finish();
                break;
        }

    }
}
