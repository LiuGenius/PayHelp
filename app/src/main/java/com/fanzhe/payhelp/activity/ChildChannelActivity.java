package com.fanzhe.payhelp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.utils.L;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChildChannelActivity extends AppCompatActivity {
    Context mContext;

    @BindView(R.id.id_tv_name)
    TextView mName;

    @BindView(R.id.id_iv_pay_code)
    ImageView mPayQrcode;

    @BindView(R.id.id_del)
    TextView mDel;

    String sub_id = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_child_pay_channel);

        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);
        ButterKnife.bind(this);

        mContext = this;

        initView();
    }

    private void initView() {
        mName.setText(getIntent().getStringExtra("channelName"));
        getData();
    }

    private void getData() {
        RequestParams params = new RequestParams(UrlAddress.CODE_CHILD_CHANNEL);
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("instance_id", getIntent().getStringExtra("instance_id"));
        NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                ToastUtils.showToast(mContext, "获取子通道信息失败，请检查网络");
            }

            @Override
            public void onsuccessful(JSONObject jsonObject) {
                if (UtilsHelper.parseResult(jsonObject)) {
                    JSONArray jsonArray = jsonObject.optJSONArray("data");
                    if (null == jsonArray || jsonArray.length() == 0) {

                    } else {
                        JSONObject dataJson = jsonArray.optJSONObject(0);
                        String pay_qrcode = dataJson.optString("pay_qrcode");
                        sub_id = dataJson.optString("id");
                        Glide.with(mContext)
                                .load(pay_qrcode)
                                .centerCrop()
                                .fitCenter()
                                .into(mPayQrcode);
                    }
                } else {
                    ToastUtils.showToast(mContext, "获取子通道信息失败，" + jsonObject.optString("msg"));
                }
            }
        });
    }

    @OnClick({R.id.id_back, R.id.id_iv_pay_code})
    public void clickView(View v) {
        switch (v.getId()) {
            case R.id.id_back:
                finish();
                break;
            case R.id.id_del:

                break;
            case R.id.id_iv_pay_code:
//                Album.startAlbum(this, 1001, 1/*,
//                        ContextCompat.getColor(this, R.color.colorPrimary),
//                        ContextCompat.getColor(this, R.color.colorPrimaryDark)*/);  // 指定状态栏的颜色。
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1001);
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == RESULT_OK) { // 判断是否成功。
                ProgressDialog dialog = new ProgressDialog(mContext);
                dialog.setTitle("处理中");
                dialog.setMessage("正在处理,请稍候...");
                dialog.setCancelable(false);
                dialog.show();
                try {
                    Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picPath = cursor.getString(columnIndex);  //获取照片路径
                    cursor.close();
                    // 拿到用户选择的图片路径List：
                    Result result = UtilsHelper.decodeQR(BitmapFactory.decodeFile(picPath));
                    //解析二维码图片
                    if (result != null) {
                        L.d("解析结果 : " + result.getText());
                        //上传文件并且保存
                        Bitmap bm = UtilsHelper.creatQrCodeImage(mContext, result.getText());
                        updataImgFile(UtilsHelper.saveBitmapToFile(bm));
                        mPayQrcode.setImageBitmap(bm);
                    }else{
                        ToastUtils.showToast(mContext,"没有检测到支付二维码,请重新选择图片");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            } else if (resultCode == RESULT_CANCELED) { // 用户取消选择。
                // 根据需要提示用户取消了选择。
            }
        }
    }

    private void updataImgFile(File file) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setCancelable(false);
        dialog.setTitle("正在上传图片");
        dialog.setMessage("正在保存信息");
        dialog.show();

        RequestParams params = new RequestParams(UrlAddress.UPDATE_FILE);
        params.addBodyParameter("file",file);
        params.setMultipart(true);
        NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                dialog.dismiss();
                ToastUtils.showToast(mContext,"文件上传失败,请检查网络");
            }

            @Override
            public void onsuccessful(JSONObject jsonObject) {
                dialog.dismiss();
                if (UtilsHelper.parseResult(jsonObject)) {
                    String imgUrl = jsonObject.optJSONObject("data").optString("url");
                    saveChildChannel(imgUrl);
                }else{
                    ToastUtils.showToast(mContext,"文件上传失败，" + jsonObject.optString("msg"));
                }
            }
        });
    }

    private void saveChildChannel(String imgUrl){
        ProgressDialog dialog = new ProgressDialog(mContext);
        dialog.setCancelable(false);
        dialog.setTitle("正在修改子通道信息");
        dialog.setMessage("正在保存信息");
        dialog.show();

        RequestParams params = new RequestParams(UrlAddress.CODE_SAVE_CHILD_CHANNEL);
        params.addBodyParameter("auth_key",App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("channel_id",getIntent().getStringExtra("id"));
        params.addBodyParameter("pay_qrcode",imgUrl);
        params.addBodyParameter("status","");
        params.addBodyParameter("sub_id",sub_id);
        NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                dialog.dismiss();
                ToastUtils.showToast(mContext,"修改失败,请检查网络");
            }

            @Override
            public void onsuccessful(JSONObject jsonObject) {
                dialog.dismiss();
                if (UtilsHelper.parseResult(jsonObject)) {
                    setResult(RESULT_OK);
                    finish();
                }else{
                    ToastUtils.showToast(mContext,"修改失败，" + jsonObject.optString("msg"));
                }
            }
        });
    }
}
