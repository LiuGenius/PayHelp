package com.fanzhe.payhelp.servers;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.os.Build;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import androidx.annotation.RequiresApi;

import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.model.PayInfo;
import com.fanzhe.payhelp.utils.L;
import com.fanzhe.payhelp.utils.NoticeUtils;
import com.fanzhe.payhelp.utils.WsClientTool;

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("OverrideAbstract")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class HelperNotificationListenerService extends NotificationListenerService {
    //来通知时的调用
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification notification = sbn.getNotification();
        if (notification == null) {
            return;
        }
        Bundle extras = notification.extras;
        if (extras != null) {
            //包名
            String pkg = sbn.getPackageName();
            // 获取通知标题
            String title = extras.getString(Notification.EXTRA_TITLE, "");
            // 获取通知内容
            String content = extras.getString(Notification.EXTRA_TEXT, "");
            L.d(String.format("收到通知，包名：%s，标题：%s，内容：%s", pkg, title, content));

            //处理
            processOnReceive(pkg, title, content);
        }
        super.onNotificationPosted(sbn);
    }

    /**
     * 消息来时处理
     *
     * @param pkg
     * @param title
     * @param content
     */
    private void processOnReceive(String pkg, String title, String content) {
        PayInfo payInfo = NoticeUtils.parseIsPayInfo(pkg, title, content);
        if (null == payInfo) {
//            MainActivity.addLogs(String.format("收到通知，包名：%s，标题：%s，内容：%s", pkg, title, content));
        }else{
//            MainActivity.addLogs(String.format("到账提醒，支付方式：%s，支付金额：%s，支付时间：%s", payInfo.payType, payInfo.payPrice, payInfo.payTime));
            L.d("processOnReceive: " + content);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type","payInfo");
                jsonObject.put("title",payInfo.payType);//支付类型  wechat/alipay
                jsonObject.put("amount",payInfo.payPrice);//支付金额
                jsonObject.put("payTime",payInfo.payTime);//支付时间
                jsonObject.put("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
                WsClientTool.getInstance().sendText(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




    @Override
    public void onListenerConnected() {
        //当连接成时调用，一般在开启监听后会回调一次该方法
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type","activeInfo");
            jsonObject.put("msg","online");
            WsClientTool.getInstance().sendText(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onListenerConnected();
    }

    @Override
    public void onListenerDisconnected() {
//        MainActivity.addLogs("服务链接被断开,正在重启");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type","activeInfo");
            jsonObject.put("msg","offline");
            WsClientTool.getInstance().sendText(jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onListenerDisconnected();
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        //当移除一条消息的时候回调，sbn是被移除的消息
        super.onNotificationRemoved(sbn);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.d("监听服务 ==== onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.d("监听服务 ==== onDestroy");
    }
}
