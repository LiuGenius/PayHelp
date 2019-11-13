package com.fanzhe.payhelp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.model.Channel;


import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.ButterKnife;

public class ChannelAdapter extends RecyclerView.Adapter<ChannelAdapter.Holder>{
        /**
         * 上下文
         */
        private Context mContext;
        /**。
         * 数据集合
         */
        private ArrayList<Channel> data;

        public ChannelAdapter(ArrayList<Channel> data, Context context) {
            this.data = data;
            this.mContext = context;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_channel, parent,false));
        }

        @Override
        public void onBindViewHolder(Holder Holder, final int position) {
            //将数据设置到item上
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class Holder extends RecyclerView.ViewHolder {
            public Holder(View itemView) {
                super(itemView);
                ButterKnife.bind(this,itemView);
            }
        }
}
