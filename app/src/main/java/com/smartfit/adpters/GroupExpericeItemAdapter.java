package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.beans.ClassInfo;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.Options;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/4.
 * 团体操  数据列表
 */
public class GroupExpericeItemAdapter extends BaseAdapter {
    private Context context;
    private List<ClassInfo> datas;
    LinearLayout.LayoutParams params;

    public GroupExpericeItemAdapter(Context context
            , List<ClassInfo> datas) {
        this.context = context;
        this.datas = datas;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 25);

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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_group_experice_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ClassInfo item = datas.get(position);
        if (!TextUtils.isEmpty(item.getStars())) {
            viewHolder.ratingBar.setRating(Float.parseFloat(item.getStars()));
        }

        if (!TextUtils.isEmpty(item.getCoachRealName())) {
            viewHolder.tvCouch.setText("教练  " + item.getCoachRealName());
        }

        if (!TextUtils.isEmpty(item.getCourseName())) {
            viewHolder.tvTittle.setText(item.getCourseName());
        }

        if (TextUtils.isEmpty(item.getPartCount())){
            item.setPartCount("0");
        }
        if (!TextUtils.isEmpty(item.getPersonCount()) && !TextUtils.isEmpty(item.getPersonCount())) {
            viewHolder.tvJoin.setText(item.getPartCount() + "/" + item.getPersonCount() + "人");
        }

        if (!TextUtils.isEmpty(item.getBeginTime()) && !TextUtils.isEmpty(item.getEndTime())) {
            viewHolder.tvTime.setText(DateUtils.getDataTime(item.getBeginTime())+ "-" + DateUtils.getDataTime(item.getEndTime()));
        }


        if (!TextUtils.isEmpty(item.getPrice())) {
            viewHolder.tvPrice.setText(item.getPrice() + "元");
        }

        ImageLoader.getInstance().displayImage(item.getClassUrl(), viewHolder.ivIcon, Options.getListOptions());

        viewHolder.ratingBar.setLayoutParams(params);
        return convertView;
    }

    public void setData(List<ClassInfo> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_group_experice_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_tittle)
        TextView tvTittle;
        @Bind(R.id.tv_couch)
        TextView tvCouch;
        @Bind(R.id.ratingBar)
        RatingBar ratingBar;
        @Bind(R.id.tv_join)
        TextView tvJoin;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_price)
        TextView tvPrice;
        @Bind(R.id.tv_price_info)
        TextView tvPriceInfo;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
