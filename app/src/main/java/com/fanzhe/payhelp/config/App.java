package com.fanzhe.payhelp.config;

import android.app.Application;
import android.graphics.Typeface;

import com.fanzhe.payhelp.utils.L;

import org.xutils.BuildConfig;
import org.xutils.x;

import java.lang.reflect.Field;

public class App extends Application {
    static App app;

    public static App getInstance(){
        if (app == null) {
            app = new App();
        }
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        L.d("onCreate");

//        L.d("当前用户类别： " + USER_TYPE);
        try {
            //设置全局字体
            Typeface regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/WeiRuanYaHei.ttf");
            Field staticField = Typeface.class.getDeclaredField("SERIF");
            staticField.setAccessible(true);
            //替换系统字体样式
            staticField.set(null, regular);

            //xUtils框架初始化
            x.Ext.init(this);
            x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

            //初始化bugly
//            CrashReport.initCrashReport(context, "8cc880c2cd", true);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //
    private int USER_TYPE = -1;//1:平台  2：码商   3：商户

    public int getUSER_TYPE() {
        return USER_TYPE;
    }

    public void setUSER_TYPE(int USER_TYPE) {
        this.USER_TYPE = USER_TYPE;
    }
}
