package com.fanzhe.payhelp.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.fragment.IndexFragment;
import com.fanzhe.payhelp.fragment.MyCententFragment;
import com.fanzhe.payhelp.servers.HelperNotificationListenerService;
import com.fanzhe.payhelp.utils.L;
import com.fanzhe.payhelp.utils.NoticeUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;
import com.fanzhe.payhelp.utils.WsClientTool;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;
    Context mContext;

    ArrayList<Fragment> mFragmentList;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_main);

        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);

        ButterKnife.bind(this);

        mContext = this;

        initView();

        if (App.getInstance().getUSER_DATA().getRole_id().equals("3")) {
            L.d("开始判断通知栏读取权限");
            NoticeUtils.startGetNotification(this, 1);
//            if (NoticeUtils.isNotificationListenerEnabled(this)) {
//                L.d("已经拥有通知栏权限，开启服务");
//                ToastUtils.showToast(mContext,"已经获取到权限,可以开始监听服务");
//            } else {
//                L.d("没有权限获取权限并且开启回调");
//                NoticeUtils.startGetNotification(this, 1);
//            }
        }

        AndPermission.with(this)
                .permission(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                )
                .start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        WsClientTool.getInstance().disconnect();

        //调用系统API结束进程
        android.os.Process.killProcess(android.os.Process.myPid());

        //结束整个虚拟机进程，注意如果在manifest里用android:process给app指定了不止一个进程，则只会结束当前进程
        System.exit(0);
    }

    private void initView() {
        mFragmentManager = getSupportFragmentManager();

        mFragmentList = new ArrayList<>();
        mFragmentList.add(new IndexFragment());
        mFragmentList.add(new MyCententFragment());


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

    @BindViews({R.id.id_ll_index,R.id.id_ll_me})
    List<LinearLayout> mLls;

    @OnClick({R.id.id_ll_index,/* R.id.id_ll_om, R.id.id_ll_usm,*/ R.id.id_ll_me})
    public void clickTab(LinearLayout v) {

        for (int i = 0; i < mLls.size(); i++) {
            TextView tv = (TextView) mLls.get(i).getChildAt(1);
            ImageView iv = (ImageView) mLls.get(i).getChildAt(0);
            tv.setTextColor(Color.parseColor("#969696"));
            if (i == 0) {
                iv.setImageResource(R.mipmap.icon_tab_index_nosel);
            }else{
                iv.setImageResource(R.mipmap.icon_tab_me_nosel);
            }
        }
        TextView tv = (TextView) v.getChildAt(1);
        ImageView iv = (ImageView) v.getChildAt(0);

        tv.setTextColor(Color.parseColor("#539FDC"));

        switch (v.getId()) {
            case R.id.id_ll_index:
                ShowFragment(0);
                iv.setImageResource(R.mipmap.icon_tab_index_sel);
                break;
            case R.id.id_ll_me:
                ShowFragment(1);
                iv.setImageResource(R.mipmap.icon_tab_me_sel);
                break;
        }


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (event.getAction()) {
            case KeyEvent.KEYCODE_HOME:
            case KeyEvent.KEYCODE_MOVE_HOME:
            case KeyEvent.KEYCODE_MENU:
            case KeyEvent.KEYCODE_BACK:

                return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                //重新判断权限
                if (!NoticeUtils.isNotificationListenerEnabled(this)) {
                    L.d("任然没有权限继续获取权限并且开启回调");
                    //没有权限获取权限并且开启回调
                    NoticeUtils.startGetNotification(this,1);
                }else{
                    L.d("已经获取到通知栏权限，开启服务");
                    //有权限启动服务
                    Intent intent = new Intent(this, HelperNotificationListenerService.class);
                    startService(intent);
                }
                break;
        }
    }
}
