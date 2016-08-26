package com.smartfit.adpters;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.MessageEvent.AllegeClassOver;
import com.smartfit.MessageEvent.CancleClass;
import com.smartfit.R;
import com.smartfit.activities.AllegeAbsentClassActivity;
import com.smartfit.activities.BaseActivity;
import com.smartfit.beans.MyAbsentClass;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.Options;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/3/12.
 * 我的旷课
 */
public class MyAbsentClassAdapter extends BaseAdapter {
    private Context context;
    private List<MyAbsentClass> datas;
    private boolean isHandleShow = true;
    private EventBus eventBus;

    public MyAbsentClassAdapter(Context context, List<MyAbsentClass> datas, boolean isHandleShow) {
        this.context = context;
        this.datas = datas;
        this.isHandleShow = isHandleShow;
        eventBus = EventBus.getDefault();
        eventBus.register(this);

    }

    @Subscribe
    public void onEvent(AllegeClassOver event) {/* Do something */
        for (MyAbsentClass item : datas) {
            if (event.getClassId().equals(item.getId())) {
                item.setStatus("1");
            }
        }

        setData(datas);
    }

    @Override
    public int getCount() {
        return datas.size();
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_absent_classes_view
                    , null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final MyAbsentClass item = datas.get(position);

        if (!TextUtils.isEmpty(item.getCourseName())) {
            viewHolder.tvClassName.setText(item.getCourseName());
        }
        if (!TextUtils.isEmpty(item.getStartTime()) && !TextUtils.isEmpty(item.getEndTime())) {
            viewHolder.tvTime.setText(DateUtils.getData(item.getStartTime()) + "~" + DateUtils.getDataTime(item.getEndTime()));
        }

        ImageLoader.getInstance().displayImage(item.getStartUserPicUrl(), viewHolder.ivIcon, Options.getHeaderOptions());
        if (!TextUtils.isEmpty(item.getStartUserName())) {
            viewHolder.tvName.setText(item.getStartUserName());
        }

        if (!TextUtils.isEmpty(item.getCourseName())) {
            viewHolder.tvClassName.setText(item.getCourseName());
        }
        if (!TextUtils.isEmpty(item.getCourseDetail())) {
            viewHolder.tvClassInfo.setText(item.getCourseDetail());
        }

        if (!TextUtils.isEmpty(item.getCoursePrice())) {
            viewHolder.tvMoney.setText(item.getCoursePrice());
        }

        /**
         *
         0: 未申述

         1: 已申述

         2:申述通过

         3:申述不通过
         */
        if (!TextUtils.isEmpty(item.getStatus())) {
            if (item.getStatus().equals("0")) {
                viewHolder.tvStatus.setText("旷课");
                viewHolder.llHandleFunciton.setVisibility(View.VISIBLE);
            } else if (item.getStatus().equals("1")) {
                viewHolder.tvStatus.setText("已申述");
                viewHolder.llHandleFunciton.setVisibility(View.GONE);
            } else if (item.getStatus().equals("2")) {
                viewHolder.tvStatus.setText("申述中");
                viewHolder.llHandleFunciton.setVisibility(View.GONE);
            } else if (item.getStatus().equals("3")) {
                viewHolder.tvStatus.setText("申述不通过");
                viewHolder.llHandleFunciton.setVisibility(View.GONE);
            }
        }

        if (!TextUtils.isEmpty(item.getScore())) {
            String time = item.getPunishTime();
            if (TextUtils.isEmpty(time)) {
                time = "0";
            } else {
                time = String.valueOf(Long.parseLong(time) / 60 / 60);
            }
            viewHolder.tvCredit.setText(String.format("信用值：%s  限制：%sH", new Object[]{item.getScore(), time}));
        }

        viewHolder.tvAbsentStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.PASS_OBJECT, item);
                ((BaseActivity) context).openActivity(AllegeAbsentClassActivity.class, bundle);
            }
        });

        return convertView;
    }


    public void setData(List<MyAbsentClass> datas) {
        this.datas = datas;
    }


    static class ViewHolder {
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_status)
        TextView tvStatus;
        @Bind(R.id.iv_icon)
        RoundedImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_class_name)
        TextView tvClassName;
        @Bind(R.id.tv_class_info)
        TextView tvClassInfo;
        @Bind(R.id.tv_credit)
        TextView tvCredit;
        @Bind(R.id.tv_money)
        TextView tvMoney;
        @Bind(R.id.tv_absent_status)
        TextView tvAbsentStatus;
        @Bind(R.id.ll_handle_funciton)
        LinearLayout llHandleFunciton;
        @Bind(R.id.ll_cours_ui)
        LinearLayout llCoursUi;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
