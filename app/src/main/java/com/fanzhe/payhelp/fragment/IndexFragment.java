package com.fanzhe.payhelp.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.activity.OrderManager;
import com.fanzhe.payhelp.activity.PayChannelActivity;
import com.fanzhe.payhelp.activity.SettlementActivity;
import com.fanzhe.payhelp.activity.UserManagerActivity;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class IndexFragment extends Fragment {
    View view;

    Unbinder unbinder;

    Context context;

    @BindView(R.id.id_ll_mygl)
    LinearLayout mMygl;
    @BindView(R.id.id_ll_yhgl)
    LinearLayout mYhgl;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_index, null);

        unbinder = ButterKnife.bind(this, view);

        context = getActivity();

        switch (App.getInstance().getUSER_DATA().getRole_id()) {
            case "1":
//                view.findViewById(R.id.id_ll_mygl).setVisibility(View.GONE);
                mMygl.getChildAt(0).setVisibility(View.GONE);
                mMygl.getChildAt(1).setVisibility(View.GONE);
                mMygl.setClickable(false);
                break;
            case "2":
//                view.findViewById(R.id.id_ll_mygl).setVisibility(View.VISIBLE);
//                view.findViewById(R.id.id_ll_yhgl).setVisibility(View.GONE);
                mYhgl.getChildAt(0).setVisibility(View.GONE);
                mYhgl.getChildAt(1).setVisibility(View.GONE);
                mYhgl.setClickable(false);
                break;
            case "3":
                mYhgl.getChildAt(0).setVisibility(View.GONE);
                mYhgl.getChildAt(1).setVisibility(View.GONE);
                mYhgl.setClickable(false);
                mMygl.getChildAt(0).setVisibility(View.GONE);
                mMygl.getChildAt(1).setVisibility(View.GONE);
                mMygl.setClickable(false);
//                view.findViewById(R.id.id_ll_yhgl).setVisibility(View.GONE);
//                view.findViewById(R.id.id_ll_mygl).setVisibility(View.GONE);
                break;
        }
        return view;
    }

    @OnClick({R.id.id_ll_tdgl, R.id.id_ll_cwgl, R.id.id_ll_jsgl, R.id.id_ll_yhgl, R.id.id_ll_ddgl,R.id.id_ll_mygl})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.id_ll_tdgl:
                startActivity(new Intent(context, PayChannelActivity.class));
                break;
            case R.id.id_ll_cwgl:
//                startActivity(new Intent(context, FinancialMagActivity.class));
                break;
            case R.id.id_ll_jsgl:
                startActivity(new Intent(context, SettlementActivity.class));
                break;
            case R.id.id_ll_yhgl:
                startActivity(new Intent(context, UserManagerActivity.class));
                break;
            case R.id.id_ll_ddgl:
                startActivity(new Intent(context, OrderManager.class));
                break;
            case R.id.id_ll_mygl:
//                startActivity(new Intent(context, KeyManagerActivity.class));
                Dialog dialog = new Dialog(context, R.style.AlertDialogStyle);
                dialog.setContentView(R.layout.layout_key_view);
                dialog.show();
                Window window = dialog.getWindow();
                EditText editText = window.findViewById(R.id.id_edittext);
                EditText et_key = window.findViewById(R.id.id_tv_key);
                editText.setText("");
                TextView submit = window.findViewById(R.id.id_submit);
                submit.setOnClickListener(v -> {
                    String content = editText.getText().toString();
                    RequestParams params = new RequestParams(UrlAddress.LOOK_PS_KET);
                    params.addBodyParameter("auth_key",App.getInstance().getUSER_DATA().getAuth_key());
                    params.addBodyParameter("paypass",content);
                    NetworkLoader.sendPost(context,params, new NetworkLoader.networkCallBack() {
                        @Override
                        public void onfailure(String errorMsg) {
                            ToastUtils.showToast(context,"查看密钥失败,请检查您的网络");
                        }

                        @Override
                        public void onsuccessful(JSONObject jsonObject) {
                            if (UtilsHelper.parseResult(jsonObject)) {
                                String key = jsonObject.optJSONObject("data").optString("secret_key");
                                et_key.setText(key);
                            }else{
                                ToastUtils.showToast(context,"查看密钥失败," + jsonObject.optString("msg"));
                            }
                        }
                    });
                });
                break;
        }
    }
}
