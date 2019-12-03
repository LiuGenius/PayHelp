package com.fanzhe.payhelp.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.fanzhe.payhelp.R;
import com.fanzhe.payhelp.activity.AddBusinessActivity;
import com.fanzhe.payhelp.activity.CodePayChannelActivity;
import com.fanzhe.payhelp.activity.RateActivity;
import com.fanzhe.payhelp.config.App;
import com.fanzhe.payhelp.config.UrlAddress;
import com.fanzhe.payhelp.model.CodeBusiness;
import com.fanzhe.payhelp.utils.NetworkLoader;
import com.fanzhe.payhelp.utils.ToastUtils;
import com.fanzhe.payhelp.utils.UtilsHelper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MaNongAdapter extends RecyclerView.Adapter<MaNongAdapter.Holder> {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 。
     * 数据集合
     */
    private ArrayList<CodeBusiness> data;

    public MaNongAdapter(ArrayList<CodeBusiness> data, Context context) {
        this.data = data;
        this.mContext = context;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.layout_item_ma_nong, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        //将数据设置到item上
        CodeBusiness codeBusiness = data.get(position);
        holder.name.setText(codeBusiness.getUser_name());
        holder.yck.setText(codeBusiness.getTotal_balance() + "");
        holder.dj.setText((codeBusiness.getTotal_balance() - codeBusiness.getBalance()) + "");
        holder.sy.setText(codeBusiness.getBalance() + "");
        holder.status.setChecked(codeBusiness.getStatus().equals("1"));

        holder.edit.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, AddBusinessActivity.class);
            intent.putExtra("tag", "3");
            intent.putExtra("editData", codeBusiness);
            mContext.startActivity(intent);
        });
        holder.channel.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, CodePayChannelActivity.class);
            intent.putExtra("uid", codeBusiness.getId());
            mContext.startActivity(intent);
        });
        if (App.getInstance().getUSER_DATA().getRole_id().equals("1")) {
            holder.rk.setVisibility(View.VISIBLE);
        }
        holder.rk.setOnClickListener(v -> {
            Dialog dialog = new Dialog(mContext, R.style.AlertDialogStyle);
            dialog.setContentView(R.layout.layout_rk_view);
            dialog.show();
            Window window = dialog.getWindow();
            EditText editText = window.findViewById(R.id.id_edittext);
            ListView listView = window.findViewById(R.id.id_lv_deposit);
            editText.setText("");
            TextView submit = window.findViewById(R.id.id_submit);
            submit.setOnClickListener(view -> {
                String content = editText.getText().toString();
                RequestParams params = new RequestParams(UrlAddress.ORG_PRE_DEPOSIT);
                params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
                params.addBodyParameter("balance",content);
                params.addBodyParameter("uid",codeBusiness.getId());
                NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
                    @Override
                    public void onfailure(String errorMsg) {
                        dialog.dismiss();
                        ToastUtils.showToast(mContext, "入款失败,请检查网络");
                    }
                    @Override
                    public void onsuccessful(JSONObject jsonObject) {
                        dialog.dismiss();
                        if (UtilsHelper.parseResult(jsonObject)) {
                            ToastUtils.showToast(mContext, "入款成功");
                            holder.yck.setText((Integer.parseInt(content) + codeBusiness.getTotal_balance()) + "");
                            holder.sy.setText(((Integer.parseInt(content) + codeBusiness.getTotal_balance())));
                        }else{
                            ToastUtils.showToast(mContext, "入款失败," + jsonObject.optString("msg"));
                        }
                    }
                });
            });

            RequestParams params = new RequestParams(UrlAddress.ORH_IMPORT_DEPOSIT);
            params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
            params.addBodyParameter("uid", codeBusiness.getId());
            NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
                @Override
                public void onfailure(String errorMsg) {
                    ToastUtils.showToast(mContext,"读取入款明细失败，请检查您的网络");
                }

                @Override
                public void onsuccessful(JSONObject jsonObject) {
                    if (UtilsHelper.parseResult(jsonObject)) {
                        JSONArray jsonArray = jsonObject.optJSONArray("data");
                        ArrayList<String> listData = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.optJSONObject(i);
                            String notice = obj.optString("note");
                            String time = UtilsHelper.parseDateLong(obj.optString("create_time") + "000","yyyy/MM/dd HH:mm:ss");
                            listData.add(time + "    " + notice);
                        }
                        listView.setAdapter(new BaseAdapter() {
                            @Override
                            public int getCount() {
                                return listData.size();
                            }

                            @Override
                            public Object getItem(int position) {
                                return listData.get(position);
                            }

                            @Override
                            public long getItemId(int position) {
                                return position;
                            }

                            @Override
                            public View getView(int position, View convertView, ViewGroup parent) {
                                TextView tv = new TextView(mContext);
                                tv.setGravity(Gravity.CENTER);
                                tv.setPadding(10, 10, 10, 10);
                                tv.setTextColor(Color.parseColor("#101010"));
                                tv.setTextSize(14);
                                tv.setText(listData.get(position));
                                return tv;
                            }
                        });
                    }else{
                        ToastUtils.showToast(mContext,"读取入款明细失败，" + jsonObject.optString("msg"));
                    }
                }
            });
        });
        holder.del.setOnClickListener(v -> {
            Dialog dialog = new Dialog(mContext, R.style.AlertDialogStyle);
            dialog.setContentView(R.layout.layout_alert_logout_view);
            dialog.setCancelable(false);
            dialog.show();
            Window window = dialog.getWindow();
            TextView tips = window.findViewById(R.id.id_tips);
            TextView title = window.findViewById(R.id.id_title);
            TextView submit = window.findViewById(R.id.id_submit);
            tips.setText("确认删除用户?");
            title.setText("删除用户");
            submit.setOnClickListener(a -> {
                dialog.dismiss();
                RequestParams params = new RequestParams(UrlAddress.ORG_DEL_USER);
                params.addBodyParameter("auth_key", App.getInstance().getUSER_DATA().getAuth_key());
                params.addBodyParameter("uid", codeBusiness.getId());
                NetworkLoader.sendPost(mContext,params, new NetworkLoader.networkCallBack() {
                    @Override
                    public void onfailure(String errorMsg) {
                        ToastUtils.showToast(mContext,"删除用户失败，请检查您的网络");
                    }

                    @Override
                    public void onsuccessful(JSONObject jsonObject) {
                        if (UtilsHelper.parseResult(jsonObject)) {
                            ToastUtils.showToast(mContext, "删除成功");
                            data.remove(position);
                            notifyDataSetChanged();
                        }else{
                            ToastUtils.showToast(mContext,"删除用户失败，" + jsonObject.optString("msg"));
                        }
                    }
                });
            });
            window.findViewById(R.id.id_cancel).setVisibility(View.VISIBLE);
            window.findViewById(R.id.id_cancel).setOnClickListener(view1 -> {
                dialog.dismiss();
            });
        });
        if (App.getInstance().getUSER_DATA().getRole_id().equals("4")) {
            holder.rate.setVisibility(View.GONE);
        }
        holder.rate.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, RateActivity.class);
            intent.putExtra("uid",codeBusiness.getId());
            intent.putExtra("name",codeBusiness.getUser_name());
            mContext.startActivity(intent);
        });
        holder.status.setOnClickListener(view -> {
            RequestParams params = new RequestParams(UrlAddress.USER_SWITCH_STATUS);
            params.addBodyParameter("auth_key",App.getInstance().getUSER_DATA().getAuth_key());
            params.addBodyParameter("status",codeBusiness.getStatus().equals("1") ? "0":"1");//1:启用 2禁用
            params.addBodyParameter("uid",codeBusiness.getId());
            NetworkLoader.sendPost(mContext, params, new NetworkLoader.networkCallBack() {
                @Override
                public void onfailure(String errorMsg) {
                    ToastUtils.showToast(mContext,"修改用户状态失败,请检查网络");
                }

                @Override
                public void onsuccessful(JSONObject jsonObject) {
                    if (UtilsHelper.parseResult(jsonObject)) {
                        ToastUtils.showToast(mContext,"修改成功");
                    }else{
                        ToastUtils.showToast(mContext,"修改用户状态失败," + jsonObject.optString("msg"));
                    }
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.id_item_sw_state)
        Switch status;
        @BindView(R.id.id_item_tv_name)
        TextView name;
        @BindView(R.id.id_item_yck)
        TextView yck;
        @BindView(R.id.id_item_dj)
        TextView dj;
        @BindView(R.id.id_item_sy)
        TextView sy;

        @BindView(R.id.id_item_edit)
        TextView edit;
        @BindView(R.id.id_item_channel)
        TextView channel;
        @BindView(R.id.id_item_rk)
        TextView rk;
        @BindView(R.id.id_item_del)
        TextView del;
        @BindView(R.id.id_item_rate)
        TextView rate;
        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
