package com.fanzhe.payhelp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.model.CodeBusiness;

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

    public AcmenAdapter(ArrayList<CodeBusiness> data, Context context) {
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
        holder.edit.setVisibility(View.GONE);
        holder.agriculture.setVisibility(View.GONE);
        holder.rate.setVisibility(View.GONE);
        holder.del.setVisibility(View.GONE);
        holder.state.setClickable(false);
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
