package com.smartfit.adpters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.beans.SelectedSort;
import com.smartfit.utils.Util;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/2.
 * 排序选择  日期  教练得分等
 */
public class ChooseOrderAdapter extends BaseAdapter {
    private Context context;

    private List<SelectedSort> sortList;
    public ChooseOrderAdapter(Context context) {
        this.context = context;
        sortList = Util.getSortList(context);
    }

    @Override
    public int getCount() {
        return sortList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_choose_shop_address_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvAddress.setText(sortList.get(position).getName());
        viewHolder.tvDistance.setVisibility(View.GONE);
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_choose_shop_address_view.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.tv_address)
        TextView tvAddress;
        @Bind(R.id.tv_distance)
        TextView tvDistance;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
