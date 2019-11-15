package com.fanzhe.payhelp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.adapter.ChannelAdapter;
import com.fanzhe.payhelp.adapter.Org_CodeChannelAdapter;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.model.Channel;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CodePayChannelActivity extends AppCompatActivity {
    @BindView(R.id.id_et_name)
    EditText mName;
    @BindView(R.id.id_rv_content)
    RecyclerView mRvContent;
    @BindView(R.id.id_sp_state)
    Spinner mState;

    Context mContext;

    ArrayList<Channel> mData;

    Org_CodeChannelAdapter mAdapter;

    int status = -1;


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

        initView();
    }

    private void initView() {
        mRvContent.setLayoutManager(new LinearLayoutManager(mContext));
        mData = new ArrayList<>();
        mAdapter = new Org_CodeChannelAdapter(mData, mContext);
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

        search();
    }

    @OnClick({R.id.id_back,R.id.id_search,R.id.id_add_channel})
    public void clickView(View view){
        switch (view.getId()) {
            case R.id.id_back:
                finish();
                break;
            case R.id.id_search:
                search();
                break;
            case R.id.id_add_channel:

                break;
        }
    }

    private void search(){
        mData.removeAll(mData);
        RequestParams params = new RequestParams(UrlAddress.ORG_CODE_CHANNEL_LIST);
        params.addBodyParameter("uid", getIntent().getStringExtra("uid"));
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("status",status + "");
        params.addBodyParameter("channel_name", mName.getText().toString());
        NetworkLoader.sendPost(params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                ToastUtils.showToast(mContext, "获取通道列表失败，请检查网络");
            }

            @Override
            public void onsuccessful(JSONObject jsonObject) {
                if (UtilsHelper.parseResult(jsonObject)) {
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        mData.add(new Channel(dataArray.optJSONObject(i)));
                    }
                    mAdapter.notifyDataSetChanged();
                }else{
                    ToastUtils.showToast(mContext, "获取通道列表失败，" + jsonObject.optString("msg"));
                }
            }
        });
    }
}
