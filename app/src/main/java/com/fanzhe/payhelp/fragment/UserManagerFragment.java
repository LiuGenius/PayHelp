package com.fanzhe.payhelp.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.activity.AddBusinessActivity;
import com.fanzhe.payhelp.adapter.BusinessAdapter;
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
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class UserManagerFragment extends Fragment {
    View view;

    Unbinder unbinder;

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Context mContext;

    ArrayList<Fragment> mFragmentList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_manager, null);

        unbinder = ButterKnife.bind(this, view);

        mContext = getActivity();

        initView();

        return view;
    }

    private void initView() {
        mFragmentManager = getChildFragmentManager();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new BusinessFragment());
        mFragmentList.add(new CodeFragment());


        mFragmentTransaction = mFragmentManager.beginTransaction();
        for (Fragment fragment : mFragmentList) {
            mFragmentTransaction.add(R.id.id_fl_content, fragment);
        }
        mFragmentTransaction.commit();

        ShowFragment(0);
    }

    /**
     * 显示 fragment
     */
    public void ShowFragment(int position) {
        mFragmentTransaction = mFragmentManager.beginTransaction();
        for (Fragment fragment : mFragmentList) {
            mFragmentTransaction.hide(fragment);
        }
        mFragmentTransaction.show(mFragmentList.get(position)).commit();
    }

    @BindViews({R.id.id_business,R.id.id_code})
    List<TextView> mTvs;

    @OnClick({R.id.id_business,R.id.id_code})
    public void clickTab(TextView tv){
        for (TextView mtv : mTvs) {
            mtv.setTextColor(Color.parseColor("#101010"));
            mtv.setBackgroundColor(Color.parseColor("#E6E6E6"));
        }

        tv.setTextColor(Color.parseColor("#ffffff"));
        tv.setBackgroundColor(Color.parseColor("#0076FF"));

        switch (tv.getId()){
            case R.id.id_business:
                ShowFragment(0);
                break;
            case R.id.id_code:
                ShowFragment(1);
                break;
        }
    }


}
