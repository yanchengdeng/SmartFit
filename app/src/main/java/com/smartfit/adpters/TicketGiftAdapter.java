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
import com.smartfit.utils.DateUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/3/12.
 */
public class TicketGiftAdapter extends BaseAdapter {


    private List<TicketInfo> datas;
    private Context context;

    public TicketGiftAdapter(Context context, List<TicketInfo> datas) {
        this.context = context;
        this.datas = datas;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_ticket_gift_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TicketInfo item = datas.get(position);

        if (!TextUtils.isEmpty(item.getEventTitle())) {
            viewHolder.tvClassName.setText(item.getEventTitle());
        }

        if (!TextUtils.isEmpty(item.getEventDetial())) {
            viewHolder.tvClassInfo.setText(item.getEventDetial());
        }

        if (!TextUtils.isEmpty(item.getEventEndTime())) {
            viewHolder.tvClassOutdate.setText(String.format("过期时间:%s", DateUtils.getData(item.getEventEndTime())));
        }

        if (!TextUtils.isEmpty(item.getEventType())) {
            if (item.getEventType().equals("3")){
                viewHolder.tvType.setText("月卡");
            }else if(item.getEventType().equals("2")){
                viewHolder.tvType.setText("买赠");
            }else if(item.getEventType().equals("1")){
                viewHolder.tvType.setText("活动捆绑");
            }
        }

        if (!TextUtils.isEmpty(item.getStatus())) {
            if (item.getStatus().equals("0")){
                viewHolder.tvStatus.setVisibility(View.INVISIBLE);
            }else if(item.getStatus().equals("1")){
                viewHolder.tvStatus.setVisibility(View.VISIBLE);
                viewHolder.tvStatus.setText("已使用");

            }else if(item.getStatus().equals("2")){
                viewHolder.tvStatus.setVisibility(View.VISIBLE);
                viewHolder.tvStatus.setText("使用中");

            }else if(item.getStatus().equals("3")){
                viewHolder.tvStatus.setVisibility(View.VISIBLE);
                viewHolder.tvStatus.setText("已分享未领取");

            }else if(item.getStatus().equals("4")){
                viewHolder.tvStatus.setVisibility(View.VISIBLE);
                viewHolder.tvStatus.setText("已分享已领取");

            }
        }
        return convertView;
    }

    public void setData(List<TicketInfo> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_ticket_gift_view.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.tv_type)
        TextView tvType;
        @Bind(R.id.tv_class_name)
        TextView tvClassName;
        @Bind(R.id.tv_class_info)
        TextView tvClassInfo;
        @Bind(R.id.tv_class_outdate)
        TextView tvClassOutdate;
        @Bind(R.id.tv_status)
        TextView tvStatus;


        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
