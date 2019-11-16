package com.fanzhe.payhelp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.model.Channel;
import com.fanzhe.payhelp.model.Order;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
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
        holder.getTextView(1).setText("外部订单号:    " + order.getOut_trade_no());
        holder.getTextView(2).setText("订单生成时间:    " + UtilsHelper.parseDateLong(order.getCreate_time() + "000","yyyy/MM/dd HH:mm:ss"));
        holder.getTextView(3).setText("支付码商名:    " + order.getOrder_name());
        holder.getTextView(4).setText("支付通道:    " + order.getChannel_name());
        holder.getTextView(5).setText("支付设备号:    " + order.getDevice_info());
        holder.getTextView(6).setText("支付完成时间:    " + UtilsHelper.parseDateLong(order.getComplete_time() + "000","yyyy/MM/dd HH:mm:ss"));
        ((TextView)((LinearLayout)holder.itemView.getChildAt(0)).getChildAt(0)).setText("订单号:    " + order.getOrder_sn());
        switch (order.getOrder_status()) {
            case "10":
                ((TextView)((LinearLayout)holder.itemView.getChildAt(0)).getChildAt(1)).setText("支付中");
                ((TextView)((LinearLayout)holder.itemView.getChildAt(0)).getChildAt(1)).setTextColor(Color.parseColor("#FF9800"));
                break;
            case "20":
                ((TextView)((LinearLayout)holder.itemView.getChildAt(0)).getChildAt(1)).setText("已支付");
                ((TextView)((LinearLayout)holder.itemView.getChildAt(0)).getChildAt(1)).setTextColor(Color.parseColor("#0000ff"));
                break;
        }
        ((TextView)((LinearLayout)holder.itemView.getChildAt(7)).getChildAt(0)).setText("订单金额:    " + order.getOrder_amount());
        ((TextView)((LinearLayout)holder.itemView.getChildAt(7)).getChildAt(1)).setText("支付金额:    " + order.getPay_amount());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        LinearLayout itemView;
        public Holder(LinearLayout itemView) {
            super(itemView);
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public TextView getTextView(int positon){
            return (TextView) itemView.getChildAt(positon);
        }
    }
}
