package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.beans.MesageInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
*系统消息适配器
*@author yanchengdeng
*create at 2016/5/3 9:38
*
*/
public class SystemMesageAdatper extends BaseAdapter {
    private Context context;
    List<MesageInfo> messageLists;


    public SystemMesageAdatper(Context context, List<MesageInfo> messageLists) {
        this.context = context;
        this.messageLists = messageLists;
    }

    @Override
    public int getCount() {
        return messageLists.size();
    }

    @Override
    public Object getItem(int position) {
        return messageLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_system_message_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final MesageInfo item = messageLists.get(position);
        if (!TextUtils.isEmpty(item.getMessageContent().getContent())) {
            viewHolder.tvContent.setText(item.getMessageContent().getContent());
        }




        return convertView;
    }


    public void setData(List<MesageInfo> messageLists) {
        this.messageLists = messageLists;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_content)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
