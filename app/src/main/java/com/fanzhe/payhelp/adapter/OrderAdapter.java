package com.fanzhe.payhelp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.model.Order;
import com.fanzhe.payhelp.utils.UtilsHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.Holder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 。
     * 数据集合
     */
    private ArrayList<Order> data;

    public OrderAdapter(ArrayList<Order> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder((LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_item_order, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        //将数据设置到item上
        Order order = data.get(position);
        holder.orderNum.setText(order.getOrder_sn());
        holder.outOrderNum.setText(order.getOut_trade_no());
        holder.createTime.setText(UtilsHelper.parseDateLong(order.getCreate_time() + "000","yyyy/MM/dd HH:mm:ss"));
        holder.codeName.setText(order.getOrder_name());
        holder.channelName.setText(order.getChannel_name());
        holder.equiName.setText(order.getDevice_info());
        holder.completeTime.setText(UtilsHelper.parseDateLong(order.getComplete_time() + "000","yyyy/MM/dd HH:mm:ss"));
        switch (order.getOrder_status()) {
            case "10":
                holder.status.setText("支付中");
                holder.status.setTextColor(Color.parseColor("#56ACF1"));
                break;
            case "20":
                holder.status.setText("已支付");
                holder.status.setTextColor(Color.parseColor("#999999"));
                break;
            case "30":
                holder.status.setText("支付失败");
                holder.status.setTextColor(Color.parseColor("#FD0000"));
                break;
        }
        holder.orderPrice.setText("¥ " + order.getOrder_amount());
        holder.payPrice.setText("¥ " + order.getPay_amount());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_item_order_num)
        TextView orderNum;
        @BindView(R.id.id_item_order_status)
        TextView status;
        @BindView(R.id.id_item_order_out_num)
        TextView outOrderNum;
        @BindView(R.id.id_item_order_create_time)
        TextView createTime;
        @BindView(R.id.id_item_order_code_name)
        TextView codeName;
        @BindView(R.id.id_item_order_channel_name)
        TextView channelName;
        @BindView(R.id.id_item_order_equi_name)
        TextView equiName;
        @BindView(R.id.id_item_order_complete_time)
        TextView completeTime;
        @BindView(R.id.id_item_order_price)
        TextView orderPrice;
        @BindView(R.id.id_item_pay_price)
        TextView payPrice;

        public Holder(LinearLayout itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
