package com.fanzhe.payhelp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.fragment.IndexFragment;
import com.fanzhe.payhelp.utils.UtilsHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Context mContext;

    ArrayList<Fragment> mFragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_main);

        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);

        ButterKnife.bind(this);

        mContext = this;

        initView();
    }

    private void initView() {
        mFragmentManager = getSupportFragmentManager();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new IndexFragment());
        mFragmentList.add(new Fragment());
        mFragmentList.add(new Fragment());
        mFragmentList.add(new Fragment());


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

    @BindViews({R.id.id_ll_index,R.id.id_ll_om,R.id.id_ll_usm,R.id.id_ll_me})
    List<LinearLayout> mLls;

    @OnClick({R.id.id_ll_index,R.id.id_ll_om,R.id.id_ll_usm,R.id.id_ll_me})
    public void clickTab(LinearLayout v){
        for (LinearLayout mLl : mLls) {
            TextView tv = (TextView) mLl.getChildAt(1);
            ImageView iv = (ImageView) mLl.getChildAt(0);
            tv.setTextColor(Color.parseColor("#101010"));
            // TODO: 2019/11/11 imageview替换
        }
        TextView tv = (TextView) v.getChildAt(1);
        ImageView iv = (ImageView) v.getChildAt(0);

        tv.setTextColor(Color.BLUE);

        switch (v.getId()){
            case R.id.id_ll_index:
                ShowFragment(0);
                break;
            case R.id.id_ll_om:
                ShowFragment(1);
                break;
            case R.id.id_ll_usm:
                ShowFragment(2);
                break;
            case R.id.id_ll_me:
                ShowFragment(3);
                break;
        }
    }
}
