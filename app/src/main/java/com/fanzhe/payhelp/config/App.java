package com.fanzhe.payhelp.config;

import android.app.Application;
import android.graphics.Typeface;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.json.JSONObject;
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

            ZXingLibrary.initDisplayOpinion(this);

            //初始化bugly
//            CrashReport.initCrashReport(context, "8cc880c2cd", true);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private USER USER_DATA;

    public USER getUSER_DATA() {
        return USER_DATA;
    }

    public void setUSER_DATA(USER USER_DATA) {
        this.USER_DATA = USER_DATA;
    }

    public static class USER{
        private String user_name;//用户名
        private String auth_key;//授权码
        private String uid;//uid
        private String role_id;//role_id  1平台 2商户  3码商
        private long login_time;//登录时间
        private long last_login_time;//上次登录时间

        public USER(JSONObject userData) {
            this.user_name = userData.optString("user_name");
            this.auth_key = userData.optString("auth_key");
            this.uid = userData.optString("uid");
            this.role_id = userData.optString("role_id");
            this.login_time = Long.parseLong(userData.optString("login_time"));
            this.last_login_time =  Long.parseLong(userData.optString("last_login_time"));
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getAuth_key() {
            return auth_key;
        }

        public void setAuth_key(String auth_key) {
            this.auth_key = auth_key;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getRole_id() {
            return role_id;
        }

        public void setRole_id(String role_id) {
            this.role_id = role_id;
        }

        public long getLogin_time() {
            return login_time;
        }

        public void setLogin_time(long login_time) {
            this.login_time = login_time;
        }

        public long getLast_login_time() {
            return last_login_time;
        }

        public void setLast_login_time(long last_login_time) {
            this.last_login_time = last_login_time;
        }
    }
}
