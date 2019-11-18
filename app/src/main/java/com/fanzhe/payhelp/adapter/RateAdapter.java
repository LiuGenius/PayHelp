package com.fanzhe.payhelp.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.activity.RateActivity;
import com.fanzhe.payhelp.model.Rate;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.Holder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 。
     * 数据集合
     */
    private ArrayList<Rate> data;

    public RateAdapter(ArrayList<Rate> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_rate, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        //将数据设置到item上
        Rate rate = data.get(position);
        holder.channel_name.setText(rate.getChannel_name());
        holder.et_rate.setText(rate.getRate());
        holder.et_rate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                RateActivity.mData.get(position).setRate(editable.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_item_channel_name)
        TextView channel_name;
        @BindView(R.id.id_item_et_rate)
        EditText et_rate;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
