package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.beans.AccountRecord;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/21.
 */
public class WalletAdapter extends BaseAdapter {

    private List<AccountRecord> datas;

    private Context context;

    public WalletAdapter(List<AccountRecord> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }


    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_wallet_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AccountRecord item = datas.get(position);
        if (!TextUtils.isEmpty(item.getPrice())) {
            if (item.getPrice().startsWith("-")) {
                viewHolder.tvMoney.setTextColor(context.getResources().getColor(R.color.common_header_bg));
            } else {
                viewHolder.tvMoney.setTextColor(context.getResources().getColor(R.color.text_color_blue));
            }
            viewHolder.tvMoney.setText(item.getPrice());
        }
        if (!TextUtils.isEmpty(item.getRecordName())) {
            viewHolder.tvName.setText(item.getRecordName());
        }
        if (!TextUtils.isEmpty(item.getRecordTime())) {
            viewHolder.tvDate.setText(item.getRecordTime());
        }
        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.ll_red_point)
        LinearLayout llRedPoint;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_money)
        TextView tvMoney;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
