package com.fanzhe.payhelp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.fragment.IndexFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IndexDataShowAdapter extends RecyclerView.Adapter<IndexDataShowAdapter.Holder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 。
     * 数据集合
     */
    private ArrayList<IndexFragment.dataView> data;

    public IndexDataShowAdapter(ArrayList<IndexFragment.dataView> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_index_data, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        //将数据设置到item上
        IndexFragment.dataView dataView = data.get(position);
        holder.key.setText(dataView.getKey());
        holder.value.setText(dataView.getValue());

        if (position == data.size() - 1 || position == data.size() - 2) {
            holder.d1.setVisibility(View.GONE);
        }
        if (position % 2 == 1) {
            holder.d2.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_key)
        TextView key;
        @BindView(R.id.id_value)
        TextView value;
        @BindView(R.id.id_dever1)
        View d1;
        @BindView(R.id.id_dever2)
        View d2;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
