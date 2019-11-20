package com.fanzhe.payhelp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.adapter.IndexDataShowAdapter;
import com.fanzhe.payhelp.adapter.IndexMenuAdapter;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class IndexFragment extends Fragment {
    View view;

    Unbinder unbinder;

    Context context;

    @BindView(R.id.id_rv_data_content)
    RecyclerView mRvDataContent;

    ArrayList<dataView> mDataViewData;

    IndexDataShowAdapter mDataViewAdapter;

    public class dataView{
        private String key;
        private String value;

        public dataView(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    @BindView(R.id.id_rv_menu_content)
    RecyclerView mRvMenuContent;

    ArrayList<dataMenu> mMenuData;

    IndexMenuAdapter mMenuAdapter;


    public class dataMenu{
        private String menuName;
        private int menuRes;

        public dataMenu(String menuName, int menuRes) {
            this.menuName = menuName;
            this.menuRes = menuRes;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }

        public int getMenuRes() {
            return menuRes;
        }

        public void setMenuRes(int menuRes) {
            this.menuRes = menuRes;
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_index, null);

        unbinder = ButterKnife.bind(this, view);

        context = getActivity();

        initView();


        getData();
        return view;
    }

    private void initView() {
        mDataViewData = new ArrayList<>();
        mDataViewAdapter = new IndexDataShowAdapter(mDataViewData,context);
        mRvDataContent.setLayoutManager(new GridLayoutManager(context,2));
        mRvDataContent.setAdapter(mDataViewAdapter);

        mMenuData = new ArrayList<>();
        mMenuData.add(new dataMenu("订单管理",R.mipmap.icon_ddgl));
        mMenuData.add(new dataMenu("通道管理",R.mipmap.icon_tdgl));
        mMenuData.add(new dataMenu("结算管理",R.mipmap.icon_jsgl));
        switch (App.getInstance().getUSER_DATA().getRole_id()) {
            case "1":
                mMenuData.add(new dataMenu("用户管理",R.mipmap.icon_yhgl));
                break;
            case "2":
                mMenuData.add(new dataMenu("密钥管理",R.mipmap.icon_mygl));
                break;
            case "3":
                break;
        }

        mMenuAdapter = new IndexMenuAdapter(mMenuData,context);
        mRvMenuContent.setLayoutManager(new GridLayoutManager(context,4));
        mRvMenuContent.setAdapter(mMenuAdapter);

    }

    private void getData() {
        RequestParams params = new RequestParams(UrlAddress.INDEX_DATA);
        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
        NetworkLoader.sendPost(context, params, new NetworkLoader.networkCallBack() {
            @Override
            public void onfailure(String errorMsg) {
                ToastUtils.showToast(context, "获取统计数据失败,请检查网络");
            }

            @Override
            public void onsuccessful(JSONObject jsonObject) {
                if (UtilsHelper.parseResult(jsonObject)) {
                    JSONObject object = jsonObject.optJSONObject("data");
                    switch (App.getInstance().getUSER_DATA().getRole_id()) {
                        case "1":
                            mDataViewData.add(new dataView("商户数",object.optString("mch_nums")));
                            mDataViewData.add(new dataView("码商数",object.optString("coder_nums")));
                            mDataViewData.add(new dataView("今日流水",object.optString("day_amount")));
                            mDataViewData.add(new dataView("今日收入",object.optString("day_income")));
                            break;
                        case "2":
                            mDataViewData.add(new dataView("今日总订单数",object.optString("order_total_num")));
                            mDataViewData.add(new dataView("今日订单完成数",object.optString("complete_num")));
                            mDataViewData.add(new dataView("今日流水",object.optString("day_amount")));
                            mDataViewData.add(new dataView("今日收入",object.optString("day_income")));
                            break;
                        case "3":
                            mDataViewData.add(new dataView("今日总订单数",object.optString("order_total_num")));
                            mDataViewData.add(new dataView("今日订单完成数",object.optString("complete_num")));
                            mDataViewData.add(new dataView("账户可用额度",object.optString("balance")));
                            mDataViewData.add(new dataView("账户冻结额度",object.optString("freeze_balance")));
                            mDataViewData.add(new dataView("今日流水",object.optString("day_amount")));
                            mDataViewData.add(new dataView("今日收入",object.optString("day_income")));
                            break;
                    }
                    mDataViewAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.showToast(context, "获取统计数据失败," + jsonObject.optString("msg"));
                }
            }
        });
    }
}
