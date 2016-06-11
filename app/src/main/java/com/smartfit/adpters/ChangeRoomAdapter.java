package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.beans.IdleClassInfo;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/19.
 */
public class ChangeRoomAdapter extends BaseAdapter {

    private ArrayList<IdleClassInfo> classroomList;

    private Context context;

    public ChangeRoomAdapter(Context context, ArrayList<IdleClassInfo> classroomList) {
        this.context = context;
        this.classroomList = classroomList;
    }


    @Override
    public int getCount() {
        return classroomList.size();
    }

    @Override
    public Object getItem(int position) {
        return classroomList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_select_rooms_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        IdleClassInfo item = classroomList.get(position);
        if (!TextUtils.isEmpty(item.getClassroomName())) {
            viewHolder.tvName.setText(item.getClassroomName());
        }

        if (!TextUtils.isEmpty(item.getClassroomPrice())) {
            viewHolder.tvPrice.setText(item.getClassroomPrice() + "/小时");
        }
        viewHolder.tvPrice.setVisibility(View.INVISIBLE);
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_price)
        TextView tvPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
