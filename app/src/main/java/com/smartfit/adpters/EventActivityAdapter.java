package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.beans.EventActivity;
import com.smartfit.utils.Options;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： 邓言诚 创建于： 2016/5/18 16:52.
 */
public class EventActivityAdapter extends BaseAdapter {
    Context context;
    List<EventActivity> eventActivityList;

    public EventActivityAdapter(Context context, List<EventActivity> eventActivityList) {
        this.context = context;
        this.eventActivityList = eventActivityList;

    }

    @Override
    public int getCount() {
        return eventActivityList.size();
    }

    @Override
    public Object getItem(int position) {
        return eventActivityList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_event_activity_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        EventActivity item = eventActivityList.get(position);
        if (!TextUtils.isEmpty(item.getEventDetial())) {
            viewHolder.tvTittle.setText(item.getEventDetial());
        }

        if (!TextUtils.isEmpty(item.getEventTitle())) {
            viewHolder.tvName.setText(item.getEventTitle());
        }

        if (!TextUtils.isEmpty(item.getEventPrice())) {
            viewHolder.tvPrice.setText(item.getEventPrice() + "元");
        }

        if (null != item.getPics() && item.getPics().length > 0) {
            ImageLoader.getInstance().displayImage(item.getPics()[0], viewHolder.ivIcon, Options.getListOptions());
        } else {
            ImageLoader.getInstance().displayImage("", viewHolder.ivIcon, Options.getListOptions());
        }
        return convertView;
    }

    public void setData(List<EventActivity> datas) {
        this.eventActivityList = datas;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_tittle)
        TextView tvTittle;
        @Bind(R.id.ll_context_ui)
        LinearLayout llContextUi;
        @Bind(R.id.tv_price)
        TextView tvPrice;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
