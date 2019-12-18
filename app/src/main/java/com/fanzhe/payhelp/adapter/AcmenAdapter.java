package com.fanzhe.payhelp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
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

public class AcmenAdapter extends RecyclerView.Adapter<AcmenAdapter.Holder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 。
     * 数据集合
     */
    private ArrayList<CodeBusiness> data;

    String tag;

    public AcmenAdapter(ArrayList<CodeBusiness> data, Context context,String tag) {
        this.data = data;
        this.mContext = context;
        this.tag = tag;
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
        holder.edit.setVisibility(View.GONE);
        holder.agriculture.setVisibility(View.GONE);
        holder.rate.setVisibility(View.GONE);
        holder.del.setVisibility(View.GONE);
        holder.state.setClickable(false);
        if (tag.equals("1")) {
            holder.del.setVisibility(View.VISIBLE);
            holder.del.setText("解除关联");
            holder.del.setOnClickListener(view -> {
                RequestParams params = new RequestParams(UrlAddress.UNBIND_ACMEN);
                params.addBodyParameter("id",business.getId());
                params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
                NetworkLoader.sendPost(mContext, params, new NetworkLoader.networkCallBack() {
                    @Override
                    public void onfailure(String errorMsg) {
                        ToastUtils.showToast(mContext,"解除失败,请检查您的网络");
                    }

                    @Override
                    public void onsuccessful(JSONObject jsonObject) {
                        if (UtilsHelper.parseResult(jsonObject)) {
                            ToastUtils.showToast(mContext,"解除成功,请手动刷新查看");
                        }else{
                            ToastUtils.showToast(mContext,"解除失败," + jsonObject.optString("msg"));
                        }
                    }
                });
            });
        }
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
        @BindView(R.id.id_agriculture)
        TextView agriculture;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
