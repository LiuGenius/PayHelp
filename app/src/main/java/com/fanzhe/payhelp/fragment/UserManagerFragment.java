package com.fanzhe.payhelp.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.utils.UtilsHelper;

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

    int lastPosition;
    @BindView(R.id.id_v_tap)
    View mTap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_user_manager, null);

        unbinder = ButterKnife.bind(this, view);

        mContext = getActivity();

        initView();

        return view;
    }

    private void initView() {
        ViewGroup.LayoutParams layoutParams = mTap.getLayoutParams();
        layoutParams.width = UtilsHelper.getScreenWidth(mContext) / 2;
        mTap.setLayoutParams(layoutParams);

        view.findViewById(R.id.id_back).setVisibility(View.GONE);
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
        switch (tv.getId()){
            case R.id.id_business:
                startAnim(0);
                ShowFragment(0);
                break;
            case R.id.id_code:
                startAnim(1);
                ShowFragment(1);
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

}
