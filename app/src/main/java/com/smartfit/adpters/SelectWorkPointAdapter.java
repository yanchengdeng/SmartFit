package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.beans.WorkPointAddress;
import com.smartfit.utils.Options;
import com.smartfit.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yanchengdeng on 2016/3/23.
 * 选择工作地点
 */
public class SelectWorkPointAdapter extends BaseAdapter {

    Context context;
    List<WorkPointAddress> datas;

    public SelectWorkPointAdapter(Context context, List<WorkPointAddress> datas) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_select_work_point_address_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        WorkPointAddress item = datas.get(position);
        viewHolder.chSelect.setChecked(item.isCheck());
        if (!TextUtils.isEmpty(item.getVenueName())) {
            viewHolder.tvAddress.setText(item.getVenueName());
        }
        viewHolder.tvDistance.setText(Util.getDistance(item.getLat(), item.getLongit()));
        ImageLoader.getInstance().displayImage(item.getVenueUrl(), viewHolder.ivIcon, Options.getListOptions());
        return convertView;
    }

    public void setData(List<WorkPointAddress> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_select_work_point_address_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.rl_icon_ui)
        RelativeLayout rlIconUi;
        @Bind(R.id.tv_address)
        TextView tvAddress;
        @Bind(R.id.tv_distance)
        TextView tvDistance;
        @Bind(R.id.ll_context_ui)
        LinearLayout llContextUi;
        @Bind(R.id.ch_select)
        CheckBox chSelect;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
