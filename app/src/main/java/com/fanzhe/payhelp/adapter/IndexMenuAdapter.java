package com.fanzhe.payhelp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.activity.OrderManager;
import com.fanzhe.payhelp.activity.PayChannelActivity;
import com.fanzhe.payhelp.activity.SettlementActivity;
import com.fanzhe.payhelp.activity.UserManagerActivity;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.fragment.IndexFragment;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IndexMenuAdapter extends RecyclerView.Adapter<IndexMenuAdapter.Holder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 。
     * 数据集合
     */
    private ArrayList<IndexFragment.dataMenu> data;

    public IndexMenuAdapter(ArrayList<IndexFragment.dataMenu> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_index_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        //将数据设置到item上
        IndexFragment.dataMenu dataMenu = data.get(position);
        holder.name.setText(dataMenu.getMenuName());
        holder.icon.setImageResource(dataMenu.getMenuRes());
        holder.itemView.setOnClickListener(view -> {
            switch (dataMenu.getMenuName()) {
                case "通道管理":
                    mContext.startActivity(new Intent(mContext, PayChannelActivity.class));
                    break;
                case "结算管理":
                    mContext.startActivity(new Intent(mContext, SettlementActivity.class));
                    break;
                case "用户管理":
                    mContext.startActivity(new Intent(mContext, UserManagerActivity.class));
                    break;
                case "订单管理":
                    mContext.startActivity(new Intent(mContext, OrderManager.class));
                    break;
                case "密钥管理":
                    Dialog dialog = new Dialog(mContext, R.style.AlertDialogStyle);
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
                        NetworkLoader.sendPost(mContext, params, new NetworkLoader.networkCallBack() {
                            @Override
                            public void onfailure(String errorMsg) {
                                ToastUtils.showToast(mContext, "查看密钥失败,请检查您的网络");
                            }

                            @Override
                            public void onsuccessful(JSONObject jsonObject) {
                                if (UtilsHelper.parseResult(jsonObject)) {
                                    String key = jsonObject.optJSONObject("data").optString("secret_key");
                                    et_key.setText(key);
                                } else {
                                    ToastUtils.showToast(mContext, "查看密钥失败," + jsonObject.optString("msg"));
                                }
                            }
                        });
                    });
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_name)
        TextView name;
        @BindView(R.id.id_icon)
        ImageView icon;


        View itemView;
        public Holder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
