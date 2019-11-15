package com.fanzhe.payhelp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.activity.AddBusinessActivity;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.model.CodeBusiness;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
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
            RequestParams params = new RequestParams(UrlAddress.ORG_DEL_USER);
            params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
            params.addBodyParameter("uid", business.getId());
            NetworkLoader.sendPost(params, new NetworkLoader.networkCallBack() {
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
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
