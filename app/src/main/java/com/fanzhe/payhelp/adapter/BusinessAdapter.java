package com.fanzhe.payhelp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.activity.AddBusinessActivity;
import com.fanzhe.payhelp.activity.RateActivity;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.model.CodeBusiness;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BusinessAdapter extends RecyclerView.Adapter<BusinessAdapter.Holder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 。
     * 数据集合
     */
    private ArrayList<CodeBusiness> data;

    public BusinessAdapter(ArrayList<CodeBusiness> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_channel, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        //将数据设置到item上
        CodeBusiness business = data.get(position);
        holder.name.setText(business.getUser_name());
        holder.state.setChecked(business.getStatus().equals("1"));
        holder.edit.setText("编辑");
        holder.edit.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, AddBusinessActivity.class);
            intent.putExtra("tag", "addBusiness");
            intent.putExtra("editData", business);
            mContext.startActivity(intent);
        });
        holder.del.setOnClickListener(v -> {
            Dialog dialog = new Dialog(mContext, R.style.AlertDialogStyle);
            dialog.setContentView(R.layout.layout_alert_logout_view);
            dialog.setCancelable(false);
            dialog.show();
            Window window = dialog.getWindow();
            TextView tips = window.findViewById(R.id.id_tips);
            TextView title = window.findViewById(R.id.id_title);
            tips.setText("确认删除用户?");
            title.setText("删除用户");
            window.findViewById(R.id.id_submit).setOnClickListener(a -> {
                dialog.dismiss();
                RequestParams params = new RequestParams(UrlAddress.ORG_DEL_USER);
                params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
                params.addBodyParameter("uid", business.getId());
                NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
                    @Override
                    public void onfailure(String errorMsg) {
                        ToastUtils.showToast(mContext,"删除用户失败，请检查您的网络");
                    }

                    @Override
                    public void onsuccessful(JSONObject jsonObject) {
                        if (UtilsHelper.parseResult(jsonObject)) {
                            ToastUtils.showToast(mContext, "删除成功");
                            data.remove(position);
                            notifyDataSetChanged();
                        }else{
                            ToastUtils.showToast(mContext,"删除用户失败，" + jsonObject.optString("msg"));
                        }
                    }
                });
            });
            window.findViewById(R.id.id_cancel).setVisibility(View.VISIBLE);
            window.findViewById(R.id.id_cancel).setOnClickListener(view1 -> {
                dialog.dismiss();
            });
        });
        holder.rate.setVisibility(View.VISIBLE);
        holder.rate.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, RateActivity.class);
            intent.putExtra("uid",business.getId());
            intent.putExtra("name",business.getUser_name());
            mContext.startActivity(intent);
        });
        holder.state.setOnClickListener(view -> {
            RequestParams params = new RequestParams(UrlAddress.USER_SWITCH_STATUS);
            params.addBodyParameter("auth_key",App.getInstance().getUSER_DATA().getAuth_key());
            params.addBodyParameter("status",business.getStatus().equals("1") ? "0":"1");//1:启用 2禁用
            params.addBodyParameter("uid",business.getId());
            NetworkLoader.sendPost(mContext, params, new NetworkLoader.networkCallBack() {
                @Override
                public void onfailure(String errorMsg) {
                    ToastUtils.showToast(mContext,"修改用户状态失败,请检查网络");
                }

                @Override
                public void onsuccessful(JSONObject jsonObject) {
                    if (UtilsHelper.parseResult(jsonObject)) {
                        ToastUtils.showToast(mContext,"修改成功");
                    }else{
                        ToastUtils.showToast(mContext,"修改用户状态失败," + jsonObject.optString("msg"));
                    }
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_item_tv_name)
        TextView name;
        @BindView(R.id.id_item_sw_state)
        Switch state;
        @BindView(R.id.id_item_child)
        TextView edit;
        @BindView(R.id.id_item_del)
        TextView del;
        @BindView(R.id.id_item_rate)
        TextView rate;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
