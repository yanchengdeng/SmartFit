package com.smartfit.adpters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.beans.CustomeDate;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.DeviceUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/22.
 * 选择日期
 */
public class SelectDateAdapter extends BaseAdapter {
    private List<CustomeDate> customeDateList;
    private Context context;
    private LinearLayout.LayoutParams params;
    private int currentPop = 0;

    public SelectDateAdapter(List<CustomeDate> customeDateList, Context context) {
        this.customeDateList = customeDateList;
        this.context = context;
        params = new LinearLayout.LayoutParams(DeviceUtil.getWidth(context) / 7, DeviceUtil.dp2px(context, 60));
    }

    @Override
    public int getCount() {
        return customeDateList.size();
    }

    @Override
    public Object getItem(int position) {
        return customeDateList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_select_date, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CustomeDate item = customeDateList.get(position);
        viewHolder.tvDate.setText(item.getDate());
        viewHolder.tvWeek.setText(DateUtils.getDayOFWeek(context, item.getWeekday()));
        viewHolder.llWeek.setLayoutParams(params);

        if (currentPop == position) {
            viewHolder.tvDate.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.tvWeek.setTextColor(context.getResources().getColor(R.color.white));
            viewHolder.llWeek.setBackgroundColor(context.getResources().getColor(R.color.transparent_half));
        }

        return convertView;
    }

    public void setCurrentPositon(int postion) {
        this.currentPop = postion;
        notifyDataSetChanged();

    }


    static class ViewHolder {
        @Bind(R.id.tv_week)
        TextView tvWeek;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.ll_week)
        LinearLayout llWeek;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
