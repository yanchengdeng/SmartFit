package com.smartfit.adpters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.beans.ClassInfo;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.Options;

import java.util.Calendar;
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
    private boolean isGroup;

    public GroupExpericeItemAdapter(Context context
            , List<ClassInfo> datas) {
        this.context = context;
        this.datas = datas;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 25);

    }

    public GroupExpericeItemAdapter(Context context, List<ClassInfo> datas, boolean isGroup) {
        this.context = context;
        this.datas = datas;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 25);
        this.isGroup = isGroup;
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

        if (TextUtils.isEmpty(item.getPartCount())) {
            item.setPartCount("0");
        }
        if (!TextUtils.isEmpty(item.getPersonCount()) && !TextUtils.isEmpty(item.getPersonCount())) {
            viewHolder.tvJoin.setText(item.getPartCount() + "/" + item.getPersonCount() + "人");
        }

        if (!TextUtils.isEmpty(item.getBeginTime()) && !TextUtils.isEmpty(item.getEndTime())) {
            viewHolder.tvTime.setText(DateUtils.getDataTime(item.getBeginTime()) + "-" + DateUtils.getDataTime(item.getEndTime()));
        }


        if (!TextUtils.isEmpty(item.getPrice())) {
            viewHolder.tvPrice.setText(item.getPrice() + "元");
        }

        if (!TextUtils.isEmpty(item.getCourseType())) {
            if (item.getCourseType().equals("1")) {
                viewHolder.tvPriceInfo.setVisibility(View.GONE);
            } else {
                viewHolder.tvPriceInfo.setVisibility(View.VISIBLE);
            }
        }

        if (!TextUtils.isEmpty(item.getCourseStatus())) {
            if (item.getCourseStatus().equals("0")) {
                //未开始
                viewHolder.rlAllUi.setBackgroundColor(context.getResources().getColor(R.color.white));
                viewHolder.llPriceUi.setVisibility(View.VISIBLE);
                viewHolder.tvClassStatus.setVisibility(View.GONE);
                viewHolder.tvClassStatus.setText("进行中");
                viewHolder.tvClassStatus.setTextColor(context.getResources().getColor(R.color.blue_light));
            } else if (item.getCourseStatus().equals("1")) {
                //正在进行
                viewHolder.rlAllUi.setBackgroundColor(context.getResources().getColor(R.color.white));
                viewHolder.llPriceUi.setVisibility(View.GONE);
                viewHolder.tvClassStatus.setVisibility(View.VISIBLE);
                viewHolder.tvClassStatus.setText("进行中");
                viewHolder.tvClassStatus.setTextColor(context.getResources().getColor(R.color.blue_light));

            } else if (item.getCourseStatus().equals("2")) {
                //已结束
                viewHolder.rlAllUi.setBackgroundColor(context.getResources().getColor(R.color.gray_light));
                viewHolder.llPriceUi.setVisibility(View.GONE);
                viewHolder.tvClassStatus.setVisibility(View.VISIBLE);
                viewHolder.tvClassStatus.setText("已结束");
                viewHolder.tvClassStatus.setTextColor(context.getResources().getColor(R.color.text_color_gray));
            }
        }

        ImageLoader.getInstance().displayImage(item.getClassUrl(), viewHolder.ivIcon, Options.getListOptions());


        //当前时间  》  可预约时间   可以下单
        if (isGroup) {
            if (System.currentTimeMillis() > Long.parseLong(item.getOpenAppointmentTime()) * 1000) {
                viewHolder.tvPrice.setText(item.getPrice() + "元");
                viewHolder.tvPriceInfo.setText("会员免费");
                viewHolder.tvPrice.setTextColor(context.getResources().getColor(R.color.common_header_bg));
                viewHolder.tvPriceInfo.setTextColor(context.getResources().getColor(R.color.common_header_bg));
            } else {
                viewHolder.tvPrice.setText(DateUtils.getDayOFWeek(context, DateUtils.getInteger(DateUtils.getDate(item.getOpenAppointmentTime()), Calendar.DAY_OF_WEEK)) + DateUtils.getData(item.getOpenAppointmentTime(), " HH:mm"));
                viewHolder.tvPriceInfo.setText("开放预约");
                viewHolder.tvPrice.setTextColor(context.getResources().getColor(R.color.text_color_gray));
                viewHolder.tvPriceInfo.setTextColor(context.getResources().getColor(R.color.text_color_gray));
            }
        }

        viewHolder.ratingBar.setLayoutParams(params);
        return convertView;
    }

    public void setData(List<ClassInfo> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        @Bind(R.id.iv_icon)
        RoundedImageView ivIcon;
        @Bind(R.id.rl_icon_ui)
        RelativeLayout rlIconUi;
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
        @Bind(R.id.ll_context_ui)
        RelativeLayout llContextUi;
        @Bind(R.id.tv_price)
        TextView tvPrice;
        @Bind(R.id.tv_price_info)
        TextView tvPriceInfo;
        @Bind(R.id.ll_price_ui)
        LinearLayout llPriceUi;
        @Bind(R.id.tv_class_status)
        TextView tvClassStatus;
        @Bind(R.id.rl_all_ui)
        RelativeLayout rlAllUi;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
