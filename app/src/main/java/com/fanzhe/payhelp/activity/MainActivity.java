package com.fanzhe.payhelp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.fragment.IndexFragment;
import com.fanzhe.payhelp.fragment.MyCententFragment;
import com.fanzhe.payhelp.fragment.OrderManagerFragment;
import com.fanzhe.payhelp.fragment.UserManagerFragment;
import com.fanzhe.payhelp.servers.HelperNotificationListenerService;
import com.fanzhe.payhelp.utils.L;
import com.fanzhe.payhelp.utils.NoticeUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;
import com.fanzhe.payhelp.utils.WsClientTool;
import com.yanzhenjie.permission.AndPermission;

import org.json.JSONException;
import org.json.JSONObject;

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

        if (App.getInstance().getUSER_DATA().getRole_id().equals("3")) {
            WsClientTool.getInstance().connect("ws://47.98.182.50:9511");
            L.d("开始判断通知栏读取权限");
            //判断是否有通知栏读取权限
            if (NoticeUtils.isNotificationListenerEnabled(this)) {
                L.d("已经拥有通知栏权限，开启服务");
                //有权限启动服务
                Intent intent = new Intent(this, HelperNotificationListenerService.class);
                startService(intent);
            } else {
                L.d("没有权限获取权限并且开启回调");
                //没有权限获取权限并且开启回调
                NoticeUtils.startGetNotification(this, 1);
            }

            //循环判断服务是否运行中
            L.d("开启循环不断读取服务是否在运行");
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    while (true) {
                        try {
                            Thread.sleep(1000 * 3);
                            handler.sendEmptyMessage(2);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }

        AndPermission.with(this)
                .permission(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
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
        mFragmentList.add(new UserManagerFragment());
        mFragmentList.add(new OrderManagerFragment());
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

    @BindViews({R.id.id_ll_index, R.id.id_ll_om, R.id.id_ll_usm, R.id.id_ll_me})
    List<LinearLayout> mLls;

    @OnClick({R.id.id_ll_index, R.id.id_ll_om, R.id.id_ll_usm, R.id.id_ll_me})
    public void clickTab(LinearLayout v) {
        for (LinearLayout mLl : mLls) {
            TextView tv = (TextView) mLl.getChildAt(1);
            ImageView iv = (ImageView) mLl.getChildAt(0);
            tv.setTextColor(Color.parseColor("#101010"));
        }
        TextView tv = (TextView) v.getChildAt(1);
        ImageView iv = (ImageView) v.getChildAt(0);

        tv.setTextColor(Color.BLUE);

        switch (v.getId()) {
            case R.id.id_ll_index:
                WsClientTool.getInstance().sendText("" + System.currentTimeMillis());
                ShowFragment(0);
                break;
            case R.id.id_ll_om:
                ShowFragment(2);
                break;
            case R.id.id_ll_usm:
                ShowFragment(1);
                break;
            case R.id.id_ll_me:
                ShowFragment(3);
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", "activeInfo");
                //判断服务是否在运行
                if (NoticeUtils.isWorked(MainActivity.this, getPackageName() + ".servers.HelperNotificationListenerService")) {
                    jsonObject.put("msg", "online");
                } else {
                    jsonObject.put("msg", "offline");
                }
                WsClientTool.getInstance().sendText(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


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
