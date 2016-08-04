package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.beans.EventUserful;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： 邓言诚 创建于： 2016/5/20 16:06.
 */
public class UserEventAdapter extends BaseAdapter {
    private Context context;
    private List<EventUserful> eventUserfuls;

    public UserEventAdapter(Context context, List<EventUserful> eventUserfuls) {
        this.context = context;
        this.eventUserfuls = eventUserfuls;

    }

    @Override
    public int getCount() {
        return eventUserfuls.size();
    }

    @Override
    public Object getItem(int position) {
        return eventUserfuls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_event_userful_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        EventUserful item = eventUserfuls.get(position);

        if (!TextUtils.isEmpty(item.getEventTitle())) {
            viewHolder.tvUsefulType.setText(item.getEventTitle());
        }

        if (!TextUtils.isEmpty(item.getEventDetial())) {
            viewHolder.tvUsefulTittle.setText(item.getEventDetial());
        }

        viewHolder.chSelect.setChecked(item.isCheked());


        return convertView;
    }


    static class ViewHolder {
        @Bind(R.id.iv_useful)
        ImageView ivUseful;
        @Bind(R.id.tv_useful_type)
        TextView tvUsefulType;
        @Bind(R.id.tv_useful_tittle)
        TextView tvUsefulTittle;
        @Bind(R.id.ch_select)
        CheckBox chSelect;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
