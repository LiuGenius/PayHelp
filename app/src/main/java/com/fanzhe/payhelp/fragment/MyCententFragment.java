package com.fanzhe.payhelp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.activity.LoginActivity;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.utils.WsClientTool;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyCententFragment extends Fragment {
    View view;

    Unbinder unbinder;

    Context mContext;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_content, null);

        unbinder = ButterKnife.bind(this, view);

        mContext = getActivity();

        initView();

        return view;
    }
    private void initView() {

    }


    @OnClick({R.id.id_ll_change_login_pwd,R.id.id_ll_change_pay_pwd,R.id.id_ll_logout})
    public void onclickView(View view){
        switch (view.getId()) {
            case R.id.id_ll_change_login_pwd:

                break;
            case R.id.id_ll_change_pay_pwd:

                break;
            case R.id.id_ll_logout:
                App.getInstance().setUSER_DATA(null);
                WsClientTool.getInstance().disconnect();
                startActivity(new Intent(mContext, LoginActivity.class));
                getActivity().finish();
                break;
        }
    }
}
