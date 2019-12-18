package com.fanzhe.payhelp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.model.SettlementInfo;
import com.fanzhe.payhelp.utils.UtilsHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
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
        holder.key1.setText(UtilsHelper.parseDateLong(settlementInfo.getCreate_time(),"yyyy/MM/dd HH:mm:ss"));
        holder.key2.setText(settlementInfo.getPay_amount() + " 元");
        holder.key3.setText(settlementInfo.getMerchants() + " 元");
        holder.key4.setText(settlementInfo.getCode() + " 元");
        holder.key5.setText(settlementInfo.getOrg() + " 元");
        holder.key6.setText(settlementInfo.getStatus());



        switch (App.getInstance().getUSER_DATA().getRole_id()) {
            case 1 + "":
                holder.mMaps.get(5).setVisibility(View.VISIBLE);
                holder.key7.setText(settlementInfo.getCodeMn() + "元");
                break;
            case 2 + "":
                holder.mMaps.get(4).setVisibility(View.GONE);
                ((TextView)holder.mMaps.get(3).getChildAt(0)).setText("平台扣除费用:");
                holder.key3.setText(settlementInfo.getMerchants() + " 元");
                holder.key4.setText(settlementInfo.getPlatform() + " 元");
                break;
            case 3 + "":
                holder.mMaps.get(4).setVisibility(View.GONE);
                holder.mMaps.get(3).setVisibility(View.GONE);
                ((TextView)holder.mMaps.get(2).getChildAt(0)).setText("码农收入:");
                holder.key3.setText(settlementInfo.getCodeMn() + " 元");
                break;
            case 4 + "":
                holder.mMaps.get(4).setVisibility(View.GONE);
                ((TextView)holder.mMaps.get(2).getChildAt(0)).setText("码商收入:");
                ((TextView)holder.mMaps.get(3).getChildAt(0)).setText("码农收入:");
                holder.key3.setText(settlementInfo.getCode() + " 元");
                holder.key4.setText(settlementInfo.getCodeMn() + " 元");
                break;
        }


        if (settlementInfo.getStatus().equals("已结算")) {
            holder.key6.setTextColor(Color.parseColor("#539FDC"));
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
        @BindView(R.id.id_item_tv_key7)
        TextView key7;

        @BindViews({R.id.id_map_1,R.id.id_map_2,R.id.id_map_3,R.id.id_map_4,R.id.id_map_5,R.id.id_map_6})
        List<LinearLayout> mMaps;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
