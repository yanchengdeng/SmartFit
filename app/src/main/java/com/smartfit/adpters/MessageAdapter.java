package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.beans.MesageInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/18.
 */
public class MessageAdapter extends BaseAdapter {

    private Context context;
    private List<MesageInfo> messageLists;

    public MessageAdapter(Context context, List<MesageInfo> messageLists) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewhold ;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_message_item, null);
            viewhold = new ViewHolder(convertView);
            convertView.setTag(viewhold);

        }else{
            viewhold = (ViewHolder) convertView.getTag();
        }

        /***
         * 1-系统消息；2-课程邀请消息；3-预约成功消息；4-课程请求消息；5-好友邀请消息；6-订单成功消息
         */

        MesageInfo item = messageLists.get(position);
        if (!TextUtils.isEmpty(item.getType())) {
            if (item.getType().equals("1")) {
                viewhold.tvName.setText("系统消息");
            }else if(item.getType().equals("2")){
                viewhold.tvName.setText("课程邀请消息");
            }else if(item.getType().equals("3")){
                viewhold.tvName.setText("预约成功消息");
            }else if(item.getType().equals("4")){
                viewhold.tvName.setText("课程请求消息");
            }else if(item.getType().equals("5")){
                viewhold.tvName.setText("好友邀请消息");
            }else if(item.getType().equals("6")){
                viewhold.tvName.setText("订单成功消息");
            }
        }


        if (!TextUtils.isEmpty(item.getMessageContent().getTitle())) {
            viewhold.tvContent.setText(item.getMessageContent().getTitle());
        }



        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_content)
        TextView tvContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
