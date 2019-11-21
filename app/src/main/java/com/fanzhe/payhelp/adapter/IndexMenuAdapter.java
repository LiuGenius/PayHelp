package com.fanzhe.payhelp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.fragment.IndexFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IndexMenuAdapter extends RecyclerView.Adapter<IndexMenuAdapter.Holder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 。
     * 数据集合
     */
    private ArrayList<IndexFragment.dataMenu> data;

    public IndexMenuAdapter(ArrayList<IndexFragment.dataMenu> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_index_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        //将数据设置到item上
        IndexFragment.dataMenu dataMenu = data.get(position);
        holder.name.setText(dataMenu.getMenuName());
        holder.icon.setImageResource(dataMenu.getMenuRes());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_name)
        TextView name;
        @BindView(R.id.id_icon)
        ImageView icon;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
