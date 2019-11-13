package com.fanzhe.payhelp.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.fragment.BusinessFragment;
import com.fanzhe.payhelp.fragment.CodeFragment;
import com.fanzhe.payhelp.fragment.IndexFragment;
import com.fanzhe.payhelp.utils.UtilsHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserManagerActivity extends AppCompatActivity {

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Context mContext;

    ArrayList<Fragment> mFragmentList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_user_manager);

        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);

        ButterKnife.bind(this);

        mContext = this;

        initView();

    }

    private void initView() {
        mFragmentManager = getSupportFragmentManager();

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

    @OnClick(R.id.id_back)
    public void clickBack(){
        finish();
    }
}
