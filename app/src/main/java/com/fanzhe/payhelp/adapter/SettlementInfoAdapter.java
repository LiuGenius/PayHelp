package com.fanzhe.payhelp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.model.SettlementInfo;
import com.fanzhe.payhelp.utils.UtilsHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettlementInfoAdapter extends RecyclerView.Adapter<SettlementInfoAdapter.Holder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 。
     * 数据集合
     */
    private ArrayList<SettlementInfo> data;

    public SettlementInfoAdapter(ArrayList<SettlementInfo> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_settlement_info, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        //将数据设置到item上
        SettlementInfo settlementInfo = data.get(position);
        holder.key1.setText("支付时间:" + UtilsHelper.parseDateLong(settlementInfo.getCreate_time(),"yyyy/MM/dd HH:mm:ss"));
        holder.key2.setText("支付金额:" + settlementInfo.getPay_amount() + "元");
        holder.key3.setText("商户获得金额:" + settlementInfo.getMerchants() + "元");
        holder.key4.setText("码商获得金额:" + settlementInfo.getCode() + "元");
        holder.key5.setText("平台获得金额:" + settlementInfo.getOrg() + "元");
        holder.key6.setText(settlementInfo.getStatus());
        if (settlementInfo.getStatus().equals("已结算")) {
            holder.key6.setTextColor(Color.BLUE);
        }else{
            holder.key6.setTextColor(Color.RED);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_item_tv_key1)
        TextView key1;
        @BindView(R.id.id_item_tv_key2)
        TextView key2;
        @BindView(R.id.id_item_tv_key3)
        TextView key3;
        @BindView(R.id.id_item_tv_key4)
        TextView key4;
        @BindView(R.id.id_item_tv_key5)
        TextView key5;
        @BindView(R.id.id_item_tv_key6)
        TextView key6;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
