package com.fanzhe.payhelp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.activity.ChildChannelActivity;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.model.Channel;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;


import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.Holder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 。
     * 数据集合
     */
    private ArrayList<Channel> data;

    public ChannelAdapter(ArrayList<Channel> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_channel, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        //将数据设置到item上
        Channel channel = data.get(position);
        holder.name.setText(channel.getChannel_name());
        holder.state.setChecked(channel.getStatus().equals("1"));
        holder.state.setOnClickListener(v -> {
            RequestParams params = new RequestParams(UrlAddress.CHANNEL_SWITCH_STATE);
            if (App.getInstance().getUSER_DATA().getRole_id().equals("3")) {
                params.setUri(UrlAddress.CODE_CHANNEL_SWITCH_STATE);
                params.addBodyParameter("channel_id", channel.getId());
            }
            params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
            params.addBodyParameter("status",holder.state.isChecked() ? "1" : "0");
            params.addBodyParameter("id", channel.getId());
            NetworkLoader.sendPost(params, new NetworkLoader.networkCallBack() {
                @Override
                public void onfailure(String errorMsg) {
                    ToastUtils.showToast(mContext, "切换通道状态失败，请检查网络");
                    holder.state.setChecked(!holder.state.isChecked());
                }

                @Override
                public void onsuccessful(JSONObject jsonObject) {
                    if (UtilsHelper.parseResult(jsonObject)) {
                        ToastUtils.showToast(mContext, jsonObject.optString("msg"));
                    }else{
                        ToastUtils.showToast(mContext, "切换通道状态失败，" + jsonObject.optString("msg"));
                        holder.state.setChecked(!holder.state.isChecked());
                    }
                }
            });
        });

        holder.del.setVisibility(View.GONE);
        holder.del.setOnClickListener(v -> {
            // TODO: 2019/11/14 删除通道
        });
        if(App.getInstance().getUSER_DATA().getRole_id().equals("3")){
            holder.child.setVisibility(View.VISIBLE);
            holder.child.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, ChildChannelActivity.class);
                intent.putExtra("channelId",channel.getId());
                intent.putExtra("channelName",channel.getChannel_name());
                mContext.startActivity(intent);
            });
        }else {
            holder.child.setVisibility(View.GONE);
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
        TextView child;
        @BindView(R.id.id_item_del)
        TextView del;
        @BindView(R.id.id_item_tv_state)
        TextView tvState;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
