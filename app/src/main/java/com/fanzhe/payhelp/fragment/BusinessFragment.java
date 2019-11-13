package com.fanzhe.payhelp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.activity.AddBusinessActivity;
import com.fanzhe.payhelp.activity.FinancialMagActivity;
import com.fanzhe.payhelp.activity.PayChannelActivity;
import com.fanzhe.payhelp.activity.SettlementActivity;
import com.fanzhe.payhelp.utils.ToastUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BusinessFragment extends Fragment {
    View view;

    Unbinder unbinder;

    Context context;

    @BindView(R.id.id_et_name)
    EditText mEtName;



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

    }

    @OnClick({R.id.id_add,R.id.id_search})
    public void clickView(TextView textView){
        switch (textView.getId()) {
            case R.id.id_add:
                Intent intent = new Intent(context, AddBusinessActivity.class);
                intent.putExtra("tag", "addBusiness");
                startActivity(intent);
                break;
            case R.id.id_search:
                ToastUtils.showToast(context, "search: " + mEtName.getText().toString());
                break;
        }
    }

}
