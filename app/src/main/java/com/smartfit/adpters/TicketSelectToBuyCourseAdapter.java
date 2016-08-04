package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.beans.EventUserful;
import com.smartfit.utils.DateUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/3/12.
 * <p>
 * 用券 去  抵扣 磕碜费用
 */
public class TicketSelectToBuyCourseAdapter extends BaseAdapter {


    private ArrayList<EventUserful> datas;
    private Context context;

    public TicketSelectToBuyCourseAdapter(Context context, ArrayList<EventUserful> datas) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_ticket_gift_share_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        EventUserful item = datas.get(position);

        if (!TextUtils.isEmpty(item.getEventTitle())) {
            viewHolder.tvClassName.setText(item.getEventTitle());
        }

        if (!TextUtils.isEmpty(item.getEventDetial())) {
            viewHolder.tvClassInfo.setText(item.getEventDetial());
        }

        if (!TextUtils.isEmpty(item.getEventEndTime())) {
            viewHolder.tvClassOutdate.setText(String.format("过期时间:%s", DateUtils.getData(item.getEventEndTime())));
        }

        viewHolder.chSelect.setChecked(item.isCheked());

        if (!TextUtils.isEmpty(item.getEventTitle())) {
            if (item.getEventType().equals("21")) {
                //0现金券1满减券2折扣券
                if (item.getCashEventType().equals("0")) {
                    viewHolder.tvType.setText("￥" + item.getTicketPrice());
                } else if (item.getCashEventType().equals("1")) {
                    viewHolder.tvType.setText("￥" + item.getTicketPrice());
                } else if (item.getCashEventType().equals("2")) {
                    viewHolder.tvType.setText(item.getTicketPrice() + "折");
                }
            } else {
                viewHolder.tvType.setText(item.getEventTitle());
            }
        }

        return convertView;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_ticket_gift_share_view.xml'
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
        @Bind(R.id.ch_select)
        CheckBox chSelect;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
