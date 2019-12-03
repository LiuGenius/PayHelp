package com.fanzhe.payhelp.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.fragment.BusinessFragment;
import com.fanzhe.payhelp.fragment.CodeFragment;
import com.fanzhe.payhelp.fragment.MaNongFragment;
import com.fanzhe.payhelp.utils.UtilsHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserManagerActivity extends AppCompatActivity {


    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Context mContext;

    ArrayList<Fragment> mFragmentList;

    int lastPosition;
    @BindView(R.id.id_v_tap)
    View mTap;
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
        ViewGroup.LayoutParams layoutParams = mTap.getLayoutParams();
        layoutParams.width = UtilsHelper.getScreenWidth(mContext) / 3;
        mTap.setLayoutParams(layoutParams);

        mFragmentManager = getSupportFragmentManager();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new BusinessFragment());
        mFragmentList.add(new CodeFragment());
        mFragmentList.add(new MaNongFragment());


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

    @BindViews({R.id.id_business,R.id.id_code,R.id.id_manong})
    List<TextView> mTvs;

    @OnClick({R.id.id_business,R.id.id_code,R.id.id_manong})
    public void clickTab(TextView tv){
        switch (tv.getId()){
            case R.id.id_business:
                startAnim(0);
                ShowFragment(0);
                break;
            case R.id.id_code:
                startAnim(1);
                ShowFragment(1);
                break;
            case R.id.id_manong:
                startAnim(2);
                ShowFragment(2);
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
        for (TextView mtv : mTvs) {
            mtv.setTextColor(Color.parseColor("#A0A0A0"));
        }
        mTvs.get(index).setTextColor(Color.parseColor("#46A9F4"));
        lastPosition = index;
    }

    @OnClick(R.id.id_back)
    public void clickBack(){
        finish();
    }
}
