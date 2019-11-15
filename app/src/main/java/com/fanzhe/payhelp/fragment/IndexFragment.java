package com.fanzhe.payhelp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.activity.FinancialMagActivity;
import com.fanzhe.payhelp.activity.OrderManager;
import com.fanzhe.payhelp.activity.PayChannelActivity;
import com.fanzhe.payhelp.activity.SettlementActivity;
import com.fanzhe.payhelp.activity.UserManagerActivity;
import com.fanzhe.payhelp.config.App;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class IndexFragment extends Fragment {
    View view;

    Unbinder unbinder;

    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_index, null);

        unbinder = ButterKnife.bind(this, view);

        context = getActivity();

        switch (App.getInstance().getUSER_DATA().getRole_id()) {
            case "1":

                break;
            case "2":
                view.findViewById(R.id.id_ll_mygl).setVisibility(View.VISIBLE);
                view.findViewById(R.id.id_ll_yhgl).setVisibility(View.GONE);
                break;
            case "3":
                view.findViewById(R.id.id_ll_yhgl).setVisibility(View.GONE);
                break;
        }
        return view;
    }

    @OnClick({R.id.id_ll_tdgl, R.id.id_ll_cwgl, R.id.id_ll_jsgl, R.id.id_ll_yhgl, R.id.id_ll_ddgl})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.id_ll_tdgl:
                startActivity(new Intent(context, PayChannelActivity.class));
                break;
            case R.id.id_ll_cwgl:
                startActivity(new Intent(context, FinancialMagActivity.class));
                break;
            case R.id.id_ll_jsgl:
                startActivity(new Intent(context, SettlementActivity.class));
                break;
            case R.id.id_ll_yhgl:
                startActivity(new Intent(context, UserManagerActivity.class));
                break;
            case R.id.id_ll_ddgl:
                startActivity(new Intent(context, OrderManager.class));
                break;
        }
    }
}
