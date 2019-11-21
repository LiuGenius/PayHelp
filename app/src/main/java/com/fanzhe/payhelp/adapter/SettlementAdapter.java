package com.fanzhe.payhelp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.model.Settlement;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettlementAdapter extends RecyclerView.Adapter<SettlementAdapter.Holder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 。
     * 数据集合
     */
    private ArrayList<Settlement> data;

    public SettlementAdapter(ArrayList<Settlement> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_settlement, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        //将数据设置到item上
        Settlement settlement = data.get(position);
        holder.name.setText(settlement.getUser_name() + " , 获得: " + settlement.getTotal() + " 元");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_item_tv_name)
        TextView name;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
