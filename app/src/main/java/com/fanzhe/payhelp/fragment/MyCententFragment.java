package com.fanzhe.payhelp.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;
import com.fanzhe.payhelp.utils.WsClientTool;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MyCententFragment extends Fragment {
    View view;

    Unbinder unbinder;

    Context mContext;

    @BindView(R.id.id_role)
    TextView mRole;

    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_content, null);

        unbinder = ButterKnife.bind(this, view);

        mContext = getActivity();

        initView();

        switch (App.getInstance().getUSER_DATA().getRole_id()){
            case "1":
                mRole.setText("我的角色 : " + App.getInstance().getUSER_DATA().getUser_name() + "  (平台)");
                break;
            case "2":
                mRole.setText("我的角色 : " + App.getInstance().getUSER_DATA().getUser_name() + "  (商户)");
                break;
            case "3":
                mRole.setText("我的角色 : " + App.getInstance().getUSER_DATA().getUser_name() + "  (码农)");
                break;
            case "4":
                mRole.setText("我的角色 : " + App.getInstance().getUSER_DATA().getUser_name() + "  (码商)");
                break;
        }

        return view;
    }
    private void initView() {

    }


    @OnClick({R.id.id_ll_change_login_pwd,R.id.id_ll_change_pay_pwd,R.id.id_ll_logout,R.id.id_ll_check_version})
    public void onclickView(View view){
        switch (view.getId()) {
            case R.id.id_ll_change_login_pwd:
                changePwd("1");
                break;
            case R.id.id_ll_change_pay_pwd:
                changePwd("2");
                break;
            case R.id.id_ll_logout:
                Dialog dialog = new Dialog(mContext, R.style.AlertDialogStyle);
                dialog.setContentView(R.layout.layout_alert_logout_view);
                dialog.setCancelable(false);
                dialog.show();
                Window window = dialog.getWindow();
                TextView tips = window.findViewById(R.id.id_tips);
                tips.setText("确认退出登录?");
                window.findViewById(R.id.id_submit).setOnClickListener(v -> {
                    dialog.dismiss();
                    App.getInstance().setUSER_DATA(null);
                    WsClientTool.getInstance().disconnect();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                });
                window.findViewById(R.id.id_cancel).setVisibility(View.VISIBLE);
                window.findViewById(R.id.id_cancel).setOnClickListener(view1 -> {
                    dialog.dismiss();
                });
                break;
            case R.id.id_ll_check_version:
                new NetworkLoader().sendGet(mContext,new RequestParams(String.format(UrlAddress.CHECK_NEW_VERSION,UtilsHelper.getAppVersionCode(mContext))), new NetworkLoader.networkCallBack() {
                    @Override
                    public void onfailure(String errorMsg) {
                        ToastUtils.showToast(mContext, "当前已经是最新版本");
                    }
                    @Override
                    public void onsuccessful(JSONObject jsonObject) {
                        if (UtilsHelper.parseResult(jsonObject)) {
                            if (jsonObject.optJSONObject("data").optBoolean("update")) {
                                String url = jsonObject.optJSONObject("data").optString("downloadUrl");
                                UtilsHelper.downloadUpdateApk(getActivity(), url);
                            }else{
                                ToastUtils.showToast(mContext, "当前已经是最新版本");
                            }
                        }else{
                            ToastUtils.showToast(mContext, "当前已经是最新版本");
                        }
                    }
                });
                break;
        }
    }

    private void changePwd(String type){
        Dialog dialog = new Dialog(mContext, R.style.AlertDialogStyle);
        dialog.setContentView(R.layout.layout_change_pwd_view);
        dialog.show();
        Window window = dialog.getWindow();
        EditText et_pwd1 = window.findViewById(R.id.id_tv_pwd1);
        EditText et_pwd2 = window.findViewById(R.id.id_tv_pwd2);
        EditText et_pwd3 = window.findViewById(R.id.id_tv_pwd3);
        et_pwd1.setText("");
        et_pwd2.setText("");
        et_pwd3.setText("");
        window.findViewById(R.id.id_submit).setOnClickListener(v -> {
            if (!et_pwd2.getText().toString().equals(et_pwd3.getText().toString())) {
                et_pwd2.setError("两次密码输入不一致");
                return;
            }
            RequestParams params = new RequestParams(UrlAddress.EDIT_PASSWORD);
            params.addBodyParameter("auth_key",App.getInstance().getUSER_DATA().getAuth_key());
            params.addBodyParameter("old_pass",et_pwd1.getText().toString());
            params.addBodyParameter("new_pass",et_pwd2.getText().toString());
            params.addBodyParameter("type",type);
            NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
                @Override
                public void onfailure(String errorMsg) {
                    ToastUtils.showToast(mContext,"密码修改失败,请检查您的网络");
                }

                @Override
                public void onsuccessful(JSONObject jsonObject) {
                    if (UtilsHelper.parseResult(jsonObject)) {
                        ToastUtils.showToast(mContext,"修改成功");
                        dialog.dismiss();
                    }else{
                        ToastUtils.showToast(mContext,"密码修改失败," + jsonObject.optString("msg"));
                    }
                }
            });
        });
    }
}
