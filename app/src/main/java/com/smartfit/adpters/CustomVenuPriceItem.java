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
import com.smartfit.beans.CustomClassVenueItem;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： Administrator 创建于： 2016/4/25 10:18.
 * 邮箱：yanchengdeng@gmail.com
 */
public class CustomVenuPriceItem extends BaseAdapter {
    private Context context;
    private List<CustomClassVenueItem> classroomList;

    public CustomVenuPriceItem(Context context, List<CustomClassVenueItem> classroomList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_custom_venue_price_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CustomClassVenueItem item = classroomList.get(position);
        if (!TextUtils.isEmpty(item.getClassroomName())) {
            viewHolder.tvName.setText(item.getClassroomName());
        }
        if (!TextUtils.isEmpty(item.getClassroomPrice())) {
            viewHolder.chSelectPrice.setText(item.getClassroomPrice() + "元/小时");
        }

        viewHolder.chSelectPrice.setChecked(item.isCheck());

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.ch_select_price)
        CheckBox chSelectPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
