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
import com.smartfit.beans.ClassInfo;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/4.
 * 有氧机械  数据列表
 */
public class AerobincnAppratusItemAdapter extends BaseAdapter {
    private Context context;
    private List<ClassInfo> datas;

    public AerobincnAppratusItemAdapter(Context context
            , List<ClassInfo> datas) {
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

        ClassInfo item = datas.get(position);
        if (!TextUtils.isEmpty(item.getCourseName())) {
            viewHolder.tvTittle.setText(item.getCourseName());
        }


        if (!TextUtils.isEmpty(item.getPartCount())) {
            if (item.getClassroomPersonCount().equals(item.getPartCount())) {
                viewHolder.tvJoin.setText("该时段已有" + item.getPartCount() + "人预约,预约已满");
            } else {
                viewHolder.tvJoin.setText("该时段已有" + item.getPartCount() + "人预约");
            }
        }

        if (!TextUtils.isEmpty(item.getPrice())) {
            viewHolder.tvPrice.setText(item.getPrice() + "元/次");
        }

        return convertView;
    }

    public void setData(List<ClassInfo> datas) {
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
