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
import com.smartfit.beans.WorkDay;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yanchengdeng on 2016/4/12.
 * 选择工作日期
 */
public class SelectWorkDateAdapter extends BaseAdapter {

    private List<WorkDay> workDayList;

    private Context context;

    public SelectWorkDateAdapter(Context context, List<WorkDay> workDayList) {
        this.context = context;
        this.workDayList = workDayList;
    }

    @Override
    public int getCount() {
        return workDayList.size();
    }

    @Override
    public Object getItem(int position) {
        return workDayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_select_week_days_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        WorkDay workDay = workDayList.get(position);

        viewHolder.tvName.setText(workDay.getName());
        viewHolder.chSelect.setChecked(workDay.isChecked());
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_select_week_days_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.ch_select)
        CheckBox chSelect;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
