package com.fanzhe.payhelp.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static final boolean isDebug = true;
    public static void showToast(Context context,String msg){
        if (isDebug)
            Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
