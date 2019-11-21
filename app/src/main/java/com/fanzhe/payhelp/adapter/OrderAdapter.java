package com.fanzhe.payhelp.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.model.Order;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

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

        holder.off_single.setVisibility(View.GONE);
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
                holder.status.setText("支付超时");
                holder.status.setTextColor(Color.parseColor("#FD0000"));
                if (App.getInstance().getUSER_DATA().getRole_id().equals("3")) {
                    holder.off_single.setVisibility(View.VISIBLE);
                }
                break;
        }
        holder.orderPrice.setText("¥ " + order.getOrder_amount());
        holder.payPrice.setText("¥ " + order.getPay_amount());
        holder.off_single.setOnClickListener(view -> {
            Dialog dialog = new Dialog(mContext, R.style.AlertDialogStyle);
            dialog.setContentView(R.layout.layout_edit_order_view);
            dialog.show();
            Window window = dialog.getWindow();
            EditText editText = window.findViewById(R.id.id_edittext);
            editText.setText("");
            TextView submit = window.findViewById(R.id.id_submit);
            submit.setOnClickListener(b -> {
                RequestParams params = new RequestParams(UrlAddress.OFF_SINGLE);
                params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
                params.addBodyParameter("order_id", order.getOrder_id());
                params.addBodyParameter("amount", editText.getText().toString());
                NetworkLoader.sendPost(mContext, params, new NetworkLoader.networkCallBack() {
                    @Override
                    public void onfailure(String errorMsg) {
                        ToastUtils.showToast(mContext,"修改订单状态失败,请检查您的网络");
                    }

                    @Override
                    public void onsuccessful(JSONObject jsonObject) {
                        if (UtilsHelper.parseResult(jsonObject)) {
                            holder.off_single.setVisibility(View.GONE);
                            ToastUtils.showToast(mContext,"修改成功,请刷新查看");
                            dialog.dismiss();
                        }else{
                            ToastUtils.showToast(mContext,"修改订单状态失败," + jsonObject.optString("msg"));
                        }
                    }
                });
            });
        });

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

        @BindView(R.id.id_item_off_single)
        TextView off_single;
        public Holder(LinearLayout itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
