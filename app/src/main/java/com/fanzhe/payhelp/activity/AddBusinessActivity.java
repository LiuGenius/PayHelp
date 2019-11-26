package com.fanzhe.payhelp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.model.CodeBusiness;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBusinessActivity extends AppCompatActivity {

    Context mContext;

    @BindView(R.id.id_tv_title)
    TextView mTitle;

    @BindView(R.id.id_et_user_name)
    EditText mEtUserName;//用户名
    @BindView(R.id.id_et_login_password1)
    EditText mEtLoginPwd;//登录密码
    @BindView(R.id.id_et_pay_password1)
    EditText mEtPayPwd;//支付密码
    @BindView(R.id.id_et_true_name)
    EditText mEtTrueName;//真实姓名
    @BindView(R.id.id_et_id_card)
    EditText mEtIdCard;//身份证号
    @BindView(R.id.id_et_mobile)
    EditText mEtMobile;//手机号码
    @BindView(R.id.id_sw_state)
    Switch mSwState;//状态
    @BindView(R.id.id_sp_sex)
    Spinner mSpSex;

    String sex;

    @BindView(R.id.id_ll_special)
    LinearLayout mLlSpecial;
    @BindView(R.id.id_sw_special)
    Switch mSpecial;

    CodeBusiness mEditData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//remove title bar  即隐藏标题栏
        getSupportActionBar().hide();// 隐藏ActionBar
        setContentView(R.layout.activity_add_business);

        UtilsHelper.transparencyBar(this);
        UtilsHelper.setStatusBarTextColor(this, true);

        ButterKnife.bind(this);

        mContext = this;

        initView();
    }

    private void initView() {
        mEditData = getIntent().getParcelableExtra("editData");


        switch (getIntent().getStringExtra("tag")) {
            case "2":
                mTitle.setText("添加/编辑商户");
                mLlSpecial.setVisibility(View.VISIBLE);
                break;
            case "3":
                mTitle.setText("添加/编辑码农");
                mLlSpecial.setVisibility(View.VISIBLE);
                break;
            case "4":
                mTitle.setText("添加/编辑码商");
                break;
        }

        if (mEditData != null) {
            mEtUserName.setText(mEditData.getUser_name());
            mEtTrueName.setText(mEditData.getTrue_name());
            mEtIdCard.setText(mEditData.getId_card());
            mEtMobile.setText(mEditData.getMobile());
            mSwState.setChecked(mEditData.getStatus().equals("1"));

            mEtLoginPwd.setVisibility(View.GONE);
            mEtPayPwd.setVisibility(View.GONE);
        }

        mSpSex.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sex = (position == 0) ? "男" : "女";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @OnClick({R.id.id_back, R.id.id_save})
    public void clickView(View view) {
        switch (view.getId()) {
            case R.id.id_back:
                finish();
                break;
            case R.id.id_save:
                save();
                break;
        }
    }

    private void save() {
        String userName = mEtUserName.getText().toString();
        String loginPwd = mEtLoginPwd.getText().toString();
        String payPwd = mEtPayPwd.getText().toString();
        String trueName = mEtTrueName.getText().toString();
        String idCard = mEtIdCard.getText().toString();
        String mobile = mEtMobile.getText().toString();


        if (mEditData != null) {
            if (UtilsHelper.parseEditTextContent(mEtUserName, "用户名不能为空", mContext) ||
                    UtilsHelper.parseEditTextContent(mEtTrueName, "姓名不能为空", mContext) ||
                    UtilsHelper.parseEditTextContent(mEtMobile, "手机号码不能为空", mContext) ||
                    UtilsHelper.parseEditTextContent(mEtIdCard, "证件号码号码不能为空", mContext)) {
                return;
            }
        } else {
            if (UtilsHelper.parseEditTextContent(mEtUserName, "用户名不能为空", mContext) ||
                    UtilsHelper.parseEditTextContent(mEtLoginPwd, "登录密码不能为空", mContext) ||
                    UtilsHelper.parseEditTextContent(mEtPayPwd, "支付密码不能为空", mContext) ||
                    UtilsHelper.parseEditTextContent(mEtTrueName, "姓名不能为空", mContext) ||
                    UtilsHelper.parseEditTextContent(mEtMobile, "手机号码不能为空", mContext) ||
//                    UtilsHelper.parseEditTextContent(mEtRate,"费率不能为空",mContext) ||
                    UtilsHelper.parseEditTextContent(mEtIdCard, "证件号码号码不能为空", mContext)) {
                return;
            }
        }

        RequestParams params = new RequestParams(UrlAddress.USER_EDIT_ADD);
        if (mEditData != null) {
            params.addBodyParameter("id", mEditData.getId());
        } else {
            params.addBodyParameter("password", loginPwd);
            params.addBodyParameter("paypass", payPwd);
        }
//        params.addBodyParameter("pre_deposit",mEtYck.getText().toString());
        params.addBodyParameter("user_name", userName);
        params.addBodyParameter("mobile", mobile);
        params.addBodyParameter("true_name", trueName);
        params.addBodyParameter("status", mSwState.isChecked() ? "1" : "0");
        params.addBodyParameter("sex", sex);
        params.addBodyParameter("id_card", idCard);
        params.addBodyParameter("type", getIntent().getStringExtra("tag"));
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        params.addBodyParameter("is_special", mSpecial.isChecked() ? "1" : "0");
        NetworkLoader.sendPost(mContext, params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                ToastUtils.showToast(mContext, "添加用户失败，请检查您的网络");
            }

            @Override
            public void onsuccessful(JSONObject jsonObject) {
                if (UtilsHelper.parseResult(jsonObject)) {
                    ToastUtils.showToast(mContext, "保存成功,请刷新查看");
                    finish();
                } else {
                    ToastUtils.showToast(mContext, "添加用户失败，" + jsonObject.optString("msg"));
                }
            }
        });

    }
}
