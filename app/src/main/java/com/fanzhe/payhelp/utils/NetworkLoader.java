package com.fanzhe.payhelp.utils;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;

import com.fanzhe.payhelp.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * Created by fanzhe on 2019/4/10.
 * 网络请求工具类
 */
public class NetworkLoader {
    public static final String TAG = "fanzhezh3";

    //get请求和post请求回调方法
    public interface networkCallBack {
        void onfailure(String errorMsg);

        void onsuccessful(JSONObject jsonObject);
    }




    /**
     * 发送一个一部get请求
     */
    public static void sendGet(Context context,RequestParams params, final networkCallBack networkCallBack) {
        x.http().get(params, new myCommonCallback(context,params.getUri(),networkCallBack,params));
    }
    /**
     * 发起一个post请求
     */
    public static void sendPost(Context context,RequestParams params, final networkCallBack networkCallBack) {

        x.http().post(params, new myCommonCallback(context,params.getUri(),networkCallBack,params));
    }

    static class myCommonCallback implements Callback.CommonCallback<String> {
        String url;
        networkCallBack networkCallBack;
        RequestParams params;
        Context context;

        public myCommonCallback(Context context, String url, NetworkLoader.networkCallBack networkCallBack, RequestParams params) {
            this.context = context;
            this.url = url;
            this.networkCallBack = networkCallBack;
            this.params = params;
        }


        @Override
        public void onSuccess(String result) {
            L.d("\t\n\n");
            L.d("onSuccess  ========================>>>> "+url+" <<<<========================\t\n");
            L.d(result);
            try {
                if (new JSONObject(result).optString("msg").equals("用户不存在")) {
                    Dialog dialog = new Dialog(context, R.style.AlertDialogStyle);
                    dialog.setContentView(R.layout.layout_alert_logout_view);
                    dialog.setCancelable(false);
                    dialog.show();
                    Window window = dialog.getWindow();
                    window.findViewById(R.id.id_submit).setOnClickListener(view -> {
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    });
                }else{
                    networkCallBack.onsuccessful(new JSONObject(result));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        public void onError(Throwable ex, boolean isOnCallback) {
            L.d("\t\n\n");
            L.d("========================>>>> "+url+" <<<<========================");
            Log.d(TAG, " Message: " + ex.getMessage());
            ex.printStackTrace();
            networkCallBack.onfailure(ex.getMessage());
        }

        @Override
        public void onCancelled(CancelledException cex) {

        }

        @Override
        public void onFinished() {
            printRequestParams(params);
        }
    }

    /**
     * 打印请求参数
     */
    private static void printRequestParams(RequestParams params){
        String log = "";
        for (int i = 0; i < params.getBodyParams().size(); i++) {
            log += params.getBodyParams().get(i).key + " : " + params.getBodyParams().get(i).value + " ,";
        }
        for (int i = 0; i < params.getQueryStringParams().size(); i++) {
            log += params.getQueryStringParams().get(i).key + " : " + params.getQueryStringParams().get(i).value + ", ";
        }
        if (!TextUtils.isEmpty(params.getBodyContent())) {
            log += "BodyContent : " + params.getBodyContent();
        }
//        for (int i = 0; i < params.getHeaders().size(); i++) {
//            if (params.getHeaders().get(i).value != null) {
//                log += "\nHeaders ===>" +  params.getHeaders().get(i).key + "  :  " + params.getHeaders().get(i).value;
//            }
//        }
        L.d("请求数据：" + log);

        L.d("================================================");
    }




}
