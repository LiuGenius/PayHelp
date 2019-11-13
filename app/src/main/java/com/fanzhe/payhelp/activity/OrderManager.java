package com.fanzhe.payhelp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.iface.OnOver;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderManager extends AppCompatActivity {
    Context mContext;

    @BindView(R.id.id_v_tap)
    View mTap;

    @BindView(R.id.id_et_name)
    EditText mEtName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_order_manager);
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
    }


    int lastPosition;

    @OnClick({R.id.id_tab_1, R.id.id_tab_2, R.id.id_tab_3, R.id.id_tv_startTime, R.id.id_tv_endTime,R.id.id_search})
    public void clickTab(TextView view) {
        switch (view.getId()) {
            case R.id.id_tab_1:
                startAnim(0);
                break;
            case R.id.id_tab_2:
                startAnim(1);
                break;
            case R.id.id_tab_3:
                startAnim(2);
                break;
            case R.id.id_tv_startTime:
            case R.id.id_tv_endTime:
                UtilsHelper.showDatePicker(mContext, result -> {
                    view.setText(result);
                });
                break;
            case R.id.id_search:
                ToastUtils.showToast(mContext, "search: " + mEtName.getText().toString());
                break;
        }
    }

    @OnClick(R.id.id_back)
    public void clickBack(){
        finish();
    }

    private void startAnim(int index) {
        int width = mTap.getLayoutParams().width;
        Animation translateAnimation = new TranslateAnimation(lastPosition * width,
                index * width, 0, 0);
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        mTap.startAnimation(translateAnimation);
        lastPosition = index;
    }


}
