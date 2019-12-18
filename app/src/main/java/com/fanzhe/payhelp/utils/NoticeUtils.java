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
     * com.newland.satrpos.starposmanager   星管家
     *
     * com.chinatelecom.bestpay.yimatong   钱到啦
     */
    public static PayInfo parseIsPayInfo(String pkg, String title, String content){
        PayInfo payInfo = new PayInfo();
        switch (pkg){
            case "com.tencent.mm"://微信支付
                payInfo.payType = "wechat";
                break;
            case "com.wosai.cashbar"://收钱吧
                payInfo.payType = "cashbar";
                break;
            case "com.eg.android.AlipayGphone"://支付宝支付
                payInfo.payType = "alipay";
                break;
            case "com.newland.satrpos.starposmanager"://星管家
                payInfo.payType = "starPay";
                break;
            case "com.chinatelecom.bestpay.yimatong"://钱到啦 暂时无法使用
                payInfo.payType = "moneyPay";
                break;
            case "com.unionpay"://云闪付 暂时无法使用
                payInfo.payType = "unionpay";
                break;
            case "com.lakala.shanghutong":
                payInfo.payType = "lakalaPay";//拉卡拉
                break;
            case "com.xiaomi.xmsf"://小米通道收到消息
                if(title.contains("拉卡拉收款成功")){
                    payInfo.payType = "lakalaPay";
                }else if(title.contains("支付宝收款")){
                    payInfo.payType = "alipay";
                } else if(title.contains("微信收款")){
                    payInfo.payType = "wechat";
                }else{
                    return null;
                }
                break;
                default:
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
        if (content.contains("收款") && content.contains("元")) {
            String money = content.substring(content.indexOf("收款") + 2,content.indexOf("元"));
            return money;
        }
        return null;
    }
}
