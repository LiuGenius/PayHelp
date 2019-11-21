package com.fanzhe.payhelp.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fanzhe.payhelp.iface.OnOver;
import com.fanzhe.payhelp.iface.OnParseQrCodeImgToString;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtilsHelper {
    public static void setText(LinearLayout linearLayout,String keyStr,String valueStr){
        ((TextView)linearLayout.getChildAt(0)).setText(keyStr);
        ((TextView)linearLayout.getChildAt(1)).setText(valueStr);
    }
    /**
     * 返回当前程序版本号
     */
    public static String getAppVersionCode(Context context) {
        String versioncode = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            // versionName = pi.versionName;
            versioncode = pi.versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode;
    }

    /**
     * 获取屏幕的宽
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 解析二维码图片文件
     * @param FilePath
     * @param onParseQrCodeImgToString
     */
    public static void parseFile(String FilePath, OnParseQrCodeImgToString onParseQrCodeImgToString){
        CodeUtils.analyzeBitmap(FilePath, new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                onParseQrCodeImgToString.onResult(result);
            }

            @Override
            public void onAnalyzeFailed() {
                onParseQrCodeImgToString.onError();
            }
        });
    }

    /** 显示日期选择器
     * @param context
     * @param onOver
     */
    public static void showDatePicker(Context context, OnOver onOver){
        new DatePickerDialog(context, (view1, year, monthOfYear, dayOfMonth) -> {
            int m = monthOfYear + 1;
            if (IsLaterToday(year,m,dayOfMonth)) {
                ToastUtils.showToast(context,"不能晚于当前时间");
                return;
            }
            onOver.onResult(year + "-" + (m) + "-" + dayOfMonth);
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)).show();
    }

    @TargetApi(19)
    public static void transparencyBar(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    public static int getStateBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }


    public static void setStatusBarTextColor(Activity activity, boolean dark) {
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    /**
     * 判断手机号是否符合规范
     *
     * @param phoneNo 输入的手机号
     * @return
     */
    public static boolean isPhoneNumber(String phoneNo) {
        if (phoneNo.length() == 11) {
            return true;
        }
        return false;
    }

    /*
     *dp转px
     */
    public static int dpTopx(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /*
     *px转dp
     */
    public static int pxTodp(Context context, float px) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * 隐藏键盘的方法
     */
    public static void hideKeyboard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // 隐藏软键盘
        imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
    }

    public static void ShowKeyboard(Activity context) {
        ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInputFromInputMethod(context.getWindow().getDecorView().getWindowToken(), 2);
    }


    public static boolean IsLaterToday(int year, int monthOfYear, int dayOfMonth) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        try {
            date = simpleDateFormat.parse(year + "/" + monthOfYear + "/" + dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime() > new Date().getTime();
    }



    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 识别接口请求状态
     * @param jsonObject
     * @return
     */
    public static boolean parseResult(JSONObject jsonObject){
        if (jsonObject.optString("code").equals("0")) {
            return true;
        }else{
            return false;
        }
    }

    /**
     * 保存登录信息
     * @param context
     * @param userName
     * @param password
     */
    public static void saveLoginInfo(Context context,String userName,String password){
        SharedPreferences.Editor loginInfo = context.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE).edit();
        loginInfo.putString("userName", userName);
        loginInfo.putString("password", password);
        loginInfo.apply();
    }

    /**
     * 获取登录信息
     * @param context
     * @param key
     * @return
     */
    public static String getLoginInfo(Context context,String key){
        return context.getSharedPreferences("LoginInfo", Context.MODE_PRIVATE).getString(key, "");
    }

    /**
     * 判断输入是否合法
     * @param editText
     * @param tip
     * @param context
     * @return
     */
    public static boolean parseEditTextContent(EditText editText,String tip,Context context){
        if (TextUtils.isEmpty(editText.getText().toString())) {
            editText.setError(tip);
            ToastUtils.showToast(context, tip);
            return true;
        }
        return false;
    }

    public static String parseDateLong(String longdate,String reg){
        if (longdate.equals("null000") || TextUtils.isEmpty(longdate)) {
            return "";
        }
        Date date = new Date(Long.parseLong(longdate));
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TextUtils.isEmpty(reg) ? "yyyy/MM/dd" : reg);
        return simpleDateFormat.format(date);
    }


    /**
     * 解析二维码图片
     *
     * @param srcBitmap
     * @return
     */
    public static com.google.zxing.Result decodeQR(Bitmap srcBitmap) {
        com.google.zxing.Result result = null;
        if (srcBitmap != null) {
            int width = srcBitmap.getWidth();
            int height = srcBitmap.getHeight();
            int[] pixels = new int[width * height];
            srcBitmap.getPixels(pixels, 0, width, 0, 0, width, height);
            // 新建一个RGBLuminanceSource对象
            RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);
            // 将图片转换成二进制图片
            BinaryBitmap binaryBitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
            QRCodeReader reader = new QRCodeReader();// 初始化解析对象
            try {
                result = reader.decode(binaryBitmap, CodeHints.getDefaultDecodeHints());// 开始解析
            } catch (NotFoundException e) {
                e.printStackTrace();
            } catch (ChecksumException e) {
                e.printStackTrace();
            } catch (FormatException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 生成二维码
     * @param context
     * @param textContent
     * @return
     */
    public static Bitmap creatQrCodeImage(Context context, String textContent) {
        return CodeUtils.createImage(textContent, 300, 300, null);
    }

    /**
     * 保存bitmap为file
     * @param bitmap
     * @return
     */
    public static File saveBitmapToFile(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        File file = new File(Environment.getExternalStorageDirectory() + "/temp.jpg");
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            InputStream is = new ByteArrayInputStream(baos.toByteArray());
            int x = 0;
            byte[] b = new byte[1024 * 100];
            while ((x = is.read(b)) != -1) {
                fos.write(b, 0, x);
            }
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * @param log 数组长度
     * @param top 随机数上限
     * @return 生成随机数数组，内容为[-top,top]
     */
    public static int[] Getrandomarray(int log, int top) {
        int[] result = new int[log];
        for (int i = 0; i < log; i++) {
            int random = (int) (top * (2 * Math.random() - 1));
            result[i] = random;
        }
        return result;
    }
}