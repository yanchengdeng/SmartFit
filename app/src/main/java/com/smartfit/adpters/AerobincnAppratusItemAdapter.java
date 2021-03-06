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
import com.smartfit.beans.AreoInfo;
import com.smartfit.utils.DateUtils;

import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/4.
 * 有氧机械  数据列表
 */
public class AerobincnAppratusItemAdapter extends BaseAdapter {
    private Context context;
    private List<AreoInfo> datas;

    public AerobincnAppratusItemAdapter(Context context
            , List<AreoInfo> datas) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_aerobiincn_appratus_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        AreoInfo item = datas.get(position);
        if (!TextUtils.isEmpty(item.getClassroomName())) {
            viewHolder.tvTittle.setText(item.getClassroomName());
        }


        if (!TextUtils.isEmpty(item.getPartCount())) {
            if (item.getClassroomPersonCount().equals(item.getPartCount())) {
                viewHolder.tvJoin.setText("该时段已有" + item.getPartCount() + "人预约,预约已满");
            } else {
                viewHolder.tvJoin.setText("该时段已有" + item.getPartCount() + "人预约");
            }
        }

        if (!TextUtils.isEmpty(item.getClassroomPrice())) {
            viewHolder.tvPrice.setText(item.getClassroomPrice() + "元/次");
        }

        if (System.currentTimeMillis() > Long.parseLong(item.getOpenAppointmentTime()) * 1000) {

            viewHolder.tvPrice.setText(item.getClassroomPrice() + "元/次");
            viewHolder.tvPriceInfo.setText("会员免费");
            viewHolder.tvPrice.setTextColor(context.getResources().getColor(R.color.find_private_coach));
            viewHolder.tvPriceInfo.setTextColor(context.getResources().getColor(R.color.common_header_bg));
        } else {
            viewHolder.tvPrice.setText(DateUtils.getDayOFWeek(context, DateUtils.getInteger(DateUtils.getDate(item.getOpenAppointmentTime()), Calendar.DAY_OF_WEEK)) + DateUtils.getData(item.getOpenAppointmentTime(), " HH:mm"));
            viewHolder.tvPriceInfo.setText("开放预约");
            viewHolder.tvPrice.setTextColor(context.getResources().getColor(R.color.text_color_gray));
            viewHolder.tvPriceInfo.setTextColor(context.getResources().getColor(R.color.text_color_gray));
        }

        return convertView;
    }

    public void setData(List<AreoInfo> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_aerobiincn_appratus_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_tittle)
        TextView tvTittle;
        @Bind(R.id.tv_info)
        TextView tvInfo;
        @Bind(R.id.tv_join)
        TextView tvJoin;
        @Bind(R.id.tv_price)
        TextView tvPrice;
        @Bind(R.id.tv_price_info)
        TextView tvPriceInfo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
