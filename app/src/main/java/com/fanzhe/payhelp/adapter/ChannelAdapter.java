package com.fanzhe.payhelp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.activity.ChildChannelActivity;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.model.Channel;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.Holder> {
    /**
     * 上下文
     */
    private Activity mContext;
    /**
     * 。
     * 数据集合
     */
    private ArrayList<Channel> data;

    public ChannelAdapter(ArrayList<Channel> data, Activity context) {
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
        holder.state.setOnClickListener(view -> {
            RequestParams params = new RequestParams(UrlAddress.CHANNEL_SWITCH_STATE);
            if (!App.getInstance().getUSER_DATA().getRole_id().equals("1")) {
                params.setUri(UrlAddress.CODE_CHANNEL_SWITCH_STATE);
                params.addBodyParameter("channel_id", channel.getId());
            }
            params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
            params.addBodyParameter("status",channel.getStatus().equals("1") ? "0" : "1");
            params.addBodyParameter("id", channel.getId());
            NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
                @Override
                public void onfailure(String errorMsg) {
                    ToastUtils.showToast(mContext, "切换通道状态失败，请检查网络");
                }

                @Override
                public void onsuccessful(JSONObject jsonObject) {
                    if (UtilsHelper.parseResult(jsonObject)) {
                        ToastUtils.showToast(mContext, jsonObject.optString("msg"));
                    }else{
                        ToastUtils.showToast(mContext, "切换通道状态失败，" + jsonObject.optString("msg"));
                    }
                }
            });
        });

        holder.del.setVisibility(View.GONE);
        holder.del.setOnClickListener(v -> {
        });
        if(App.getInstance().getUSER_DATA().getRole_id().equals("3")){
            holder.child.setVisibility(View.VISIBLE);
            holder.child.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, ChildChannelActivity.class);
                intent.putExtra("instance_id",channel.getInstance_id());
                intent.putExtra("id",channel.getId());
                intent.putExtra("channelName",channel.getChannel_name());
                mContext.startActivityForResult(intent,1);
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
