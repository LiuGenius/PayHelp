package com.fanzhe.payhelp.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.activity.OrderManager;
import com.fanzhe.payhelp.activity.PayChannelActivity;
import com.fanzhe.payhelp.activity.SettlementActivity;
import com.fanzhe.payhelp.activity.UserManagerActivity;
import com.fanzhe.payhelp.adapter.IndexDataShowAdapter;
import com.fanzhe.payhelp.adapter.IndexMenuAdapter;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.servers.HelperNotificationListenerService;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.NoticeUtils;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;
import com.fanzhe.payhelp.utils.WsClientTool;
import com.fanzhe.payhelp.view.RadarView;
import com.fanzhe.payhelp.view.RecyclerViewClickListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class IndexFragment extends Fragment {
    View view;

    Unbinder unbinder;

    Context context;

    @BindView(R.id.id_swiperefreshlayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

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

    @BindView(R.id.id_radarView)
    RelativeLayout mRadarView;
    @BindView(R.id.id_radar)
    RadarView mRadar;
    @BindView(R.id.id_serverState)
    TextView mServerState;
    @BindView(R.id.id_listeningState)
    TextView mListeningState;
    @BindView(R.id.id_time)
    TextView mTime;
    @BindView(R.id.id_stop)
    TextView mStop;


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
                mMenuData.add(new dataMenu("接单状态",R.mipmap.icon_get_order));
                //开启监听服务
                Intent intent = new Intent(context, HelperNotificationListenerService.class);
                context.startService(intent);
                //连接ws
                WsClientTool.getInstance().connect("ws://47.98.182.50:9511");
                //循环判断服务是否运行中
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        while (true) {
                            try {
                                Thread.sleep(1000);
                                handler.sendEmptyMessage(2);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
                ToastUtils.showToast(context,"已经在后台开始接单");
                break;
        }

        mMenuAdapter = new IndexMenuAdapter(mMenuData,context);
        mRvMenuContent.setLayoutManager(new GridLayoutManager(context,4));
        mRvMenuContent.setAdapter(mMenuAdapter);
        mRvMenuContent.addOnItemTouchListener(new RecyclerViewClickListener(context, mRvMenuContent, (view, position) -> {
            switch (mMenuData.get(position).getMenuName()) {
                case "接单状态":
                    startServer();
                    break;
                case "通道管理":
                    startActivity(new Intent(context, PayChannelActivity.class));
                    break;
                case "结算管理":
                    startActivity(new Intent(context, SettlementActivity.class));
                    break;
                case "用户管理":
                    startActivity(new Intent(context, UserManagerActivity.class));
                    break;
                case "订单管理":
                    startActivity(new Intent(context, OrderManager.class));
                    break;
                case "密钥管理":
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
                        params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
                        params.addBodyParameter("paypass", content);
                        NetworkLoader.sendPost(context, params, new NetworkLoader.networkCallBack() {
                            @Override
                            public void onfailure(String errorMsg) {
                                ToastUtils.showToast(context, "查看密钥失败,请检查您的网络");
                            }

                            @Override
                            public void onsuccessful(JSONObject jsonObject) {
                                if (UtilsHelper.parseResult(jsonObject)) {
                                    String key = jsonObject.optJSONObject("data").optString("secret_key");
                                    et_key.setText(key);
                                } else {
                                    ToastUtils.showToast(context, "查看密钥失败," + jsonObject.optString("msg"));
                                }
                            }
                        });
                    });
                    break;
            }
        }));

        mRadarView.setOnClickListener(view -> {

        });

        mSwipeRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        mSwipeRefreshLayout.setOnRefreshListener(() -> getData());
    }

    private void getData() {
        mDataViewData.removeAll(mDataViewData);
        mDataViewAdapter.notifyDataSetChanged();
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
                    mSwipeRefreshLayout.setRefreshing(false);
                } else {
                    ToastUtils.showToast(context, "获取统计数据失败," + jsonObject.optString("msg"));
                }
            }
        });
    }

    @OnClick(R.id.id_stop)
    public void stopServer(){
        mRadarView.setVisibility(View.GONE);
//        //停止监听服务
//        Intent intent = new Intent(context, HelperNotificationListenerService.class);
//        context.stopService(intent);
//        //断开ws
//        WsClientTool.getInstance().disconnect();
        //停止雷达图
        ToastUtils.showToast(context,"服务正在后台运行,请不要关闭当前页面");
        mRadar.stop();
    }

    private void startServer(){
        mRadarView.setVisibility(View.VISIBLE);
        //开启雷达图
        mRadar.setDirection(RadarView.CLOCK_WISE);
        mRadar.start();
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("type", "activeInfo");
                //判断监听服务是否在运行
                if (NoticeUtils.isWorked(context, context.getPackageName() + ".servers.HelperNotificationListenerService")) {
                    jsonObject.put("msg", "online");
                    mListeningState.setText("当前监听服务在线");
                } else {
                    jsonObject.put("msg", "offline");
                    mListeningState.setText("当前监听服务离线");
                }
                //判断ws连接
                if (WsClientTool.getInstance().isConnected()) {
                    mServerState.setText("当前服务器连接正常");
                }else{
                    mServerState.setText("当前服务器连接异常");
                }
                mTime.setText(UtilsHelper.parseDateLong(String.valueOf(new Date().getTime()), "yyyy/MM/dd HH:mm:ss"));
                WsClientTool.getInstance().sendText(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
