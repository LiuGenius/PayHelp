package com.fanzhe.payhelp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.model.Channel;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class Org_CodeChannelAdapter extends RecyclerView.Adapter<Org_CodeChannelAdapter.Holder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 。
     * 数据集合
     */
    private ArrayList<Channel> data;

    public Org_CodeChannelAdapter(ArrayList<Channel> data, Context context) {
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
        holder.state.setVisibility(View.GONE);
        holder.del.setVisibility(View.GONE);
        holder.child.setVisibility(View.GONE);
        holder.tvState.setText("状态:" + (channel.getStatus().equals("1") ? "开启" : "关闭"));
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
