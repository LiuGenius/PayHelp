package com.fanzhe.payhelp.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.NotificationManagerCompat;


import com.fanzhe.payhelp.model.PayInfo;
import com.fanzhe.payhelp.servers.HelperNotificationListenerService;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NoticeUtils {
    /**
     * 判断是否拥有通知栏权限
     * @param context 上下文
     * @return 是否拥有权限
     */
    public static boolean isNotificationListenerEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        if (packageNames.contains(context.getPackageName())) {
            return true;
        }
        return false;
    }

    /**
     * 跳转到设置界面设置权限
     * @param context 上下文
     */
    public static void startGetNotification(Activity context,int reqCode){
        try {
            Intent intent;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1) {
                intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
            } else {
                intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            }
            context.startActivityForResult(intent,reqCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断服务是否正在运行
     * @param context 上下文
     * @param className 包名+服务名
     * @return 是否在运行
     */
    public static boolean isWorked(Context context,String className) {
        ActivityManager myManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(1000);

        for (int i = 0; i < runningService.size(); i++) {
//            L.i("" + runningService.get(i).service.getClassName());
            if (runningService.get(i).service.getClassName().equals(className)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 启动服务组件
     * @param context
     */
    public static void startServer(Context context){
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(
                new ComponentName(
                        context,
                        HelperNotificationListenerService.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    /**
     * 判断是否是收款信息
     */
    public static PayInfo parseIsPayInfo(String pkg, String title, String content){
        //判断包名是否是支付宝或微信
        PayInfo payInfo = new PayInfo();
        if (pkg.equals("com.tencent.mm")) {
            payInfo.payType = "wechat";
        }else if(pkg.equals("com.eg.android.AlipayGphone")){
            payInfo.payType = "alipay";
        }else{
            return null;
        }

        //判断content中是否有金额
        String price = parseMoney(content);
        if (TextUtils.isEmpty(price)) {
            return null;
        }else{
            payInfo.payPrice = price;
        }
        //
        payInfo.payTime = new Date().toString();
        return payInfo;
    }

    /**
     * 解析内容字符串，提取金额
     * @param content 需要解析的内容
     * @return 金额
     */
    public static String parseMoney(String content) {
        Pattern pattern = Pattern.compile("收款(([1-9]\\d*)|0)(\\.(\\d){0,2})?元");
        Matcher matcher = pattern.matcher(content);
        if (matcher.find()) {
            String tmp = matcher.group();
            Pattern patternnum = Pattern.compile("(([1-9]\\d*)|0)(\\.(\\d){0,2})?");
            Matcher matchernum = patternnum.matcher(tmp);
            if (matchernum.find())
                return matchernum.group();
        }
        return null;
    }
}
