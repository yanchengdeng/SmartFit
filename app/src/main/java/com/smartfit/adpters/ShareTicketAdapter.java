package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.beans.TicketInfo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： 邓言诚 创建于： 2016/7/13 19:25.
 */
public class ShareTicketAdapter extends BaseAdapter {
    Context context;
    ArrayList<TicketInfo> ticketInfos;

    public ShareTicketAdapter(Context context, ArrayList<TicketInfo> ticketInfos) {
        this.context = context;
        this.ticketInfos = ticketInfos;

    }

    @Override
    public int getCount() {
        return ticketInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return ticketInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_share_ticket_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (!TextUtils.isEmpty(ticketInfos.get(position).getEventTitle())) {
            viewHolder.tvName.setText(ticketInfos.get(position).getEventTitle());
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
