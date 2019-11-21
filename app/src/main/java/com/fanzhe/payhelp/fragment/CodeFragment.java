package com.fanzhe.payhelp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.activity.AddBusinessActivity;
import com.fanzhe.payhelp.adapter.CodeBusinessAdapter;
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
import butterknife.Unbinder;

public class CodeFragment extends Fragment {
    View view;

    Unbinder unbinder;

    Context context;

    @BindView(R.id.id_et_name)
    EditText mEtName;

    @BindView(R.id.id_rv_content)
    RecyclerView mRvContent;

    @BindView(R.id.id_sp_state)
    Spinner mState;

    CodeBusinessAdapter mAdapter;

    ArrayList<CodeBusiness> mData;

    int status = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_business, null);

        unbinder = ButterKnife.bind(this, view);

        context = getActivity();

        initView();

        return view;
    }

    private void initView() {
        mRvContent.setLayoutManager(new LinearLayoutManager(context));
        mData = new ArrayList<>();
        mAdapter = new CodeBusinessAdapter(mData, context);
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


    @OnClick({R.id.id_add,R.id.id_search})
    public void clickView(TextView textView){
        switch (textView.getId()) {
            case R.id.id_add:
                Intent intent = new Intent(context, AddBusinessActivity.class);
                intent.putExtra("tag", "addCode");
                startActivity(intent);
                break;
            case R.id.id_search:
                search();
                break;
        }
    }

    private void search(){
        mData.removeAll(mData);
        RequestParams params = new RequestParams(UrlAddress.USER_LIST);
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("status",status + "");
        params.addBodyParameter("user_name", mEtName.getText().toString());
        params.addBodyParameter("type", "3");
        NetworkLoader.sendPost(context,params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                ToastUtils.showToast(context, "获取码商列表失败，请检查网络");
            }

            @Override
            public void onsuccessful(JSONObject jsonObject) {
                if (UtilsHelper.parseResult(jsonObject)) {
                    JSONArray dataArray = jsonObject.optJSONArray("data");
                    for (int i = 0; i < dataArray.length(); i++) {
                        mData.add(new CodeBusiness(dataArray.optJSONObject(i)));
                    }
                    mAdapter.notifyDataSetChanged();
                }else{
                    ToastUtils.showToast(context, "获取码商列表失败，" + jsonObject.optString("msg"));
                }
            }
        });
    }

}
