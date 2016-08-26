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
import com.smartfit.beans.MyCreditRecord;
import com.smartfit.utils.DateUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/3/12.
 * 我的信用记录适配器
 */
public class MyCreditRecordAdapter extends BaseAdapter {
    private Context context;
    private List<MyCreditRecord> datas;
    private boolean isHandleShow = true;
    private EventBus eventBus;

    public MyCreditRecordAdapter(Context context, List<MyCreditRecord> datas) {
        this.context = context;
        this.datas = datas;
        eventBus = EventBus.getDefault();

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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_credit_record_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final MyCreditRecord item = datas.get(position);

        /**
         * type
         * 1签到
         2旷课
         3系统
         4申诉退回
         */

        if (!TextUtils.isEmpty(item.getScore())) {
            if (Integer.parseInt(item.getScore()) < 0) {
                viewHolder.tvCreditScore.setTextColor(context.getResources().getColor(R.color.common_header_bg));
            } else {
                viewHolder.tvCreditScore.setTextColor(context.getResources().getColor(R.color.blue_light));
            }
            viewHolder.tvCreditScore.setText(item.getScore());
        }

        if (!TextUtils.isEmpty(item.getReason())) {
            viewHolder.tvCreditReason.setText(item.getReason());
        }

        if (!TextUtils.isEmpty(item.getStartTime()) && !TextUtils.isEmpty(item.getEndTime())) {
            viewHolder.tvClassTime.setText(DateUtils.getData(item.getStartTime(), "yyyy-MM-dd HH:mm") + (DateUtils.getData(item.getEndTime(), "HH:mm")));
        }

        if (!TextUtils.isEmpty(item.getCourseName())) {
            viewHolder.tvClassName.setText(item.getCourseName());
        }

        if (!TextUtils.isEmpty(item.getSignTime())) {
            viewHolder.tvSignTime.setText(DateUtils.getData(item.getSignTime(), "yyyy-MM-dd HH:mm"));
        }

        return convertView;
    }


    public void setData(List<MyCreditRecord> datas) {
        this.datas = datas;
    }


    static class ViewHolder {
        @Bind(R.id.iv_record_icon)
        ImageView ivRecordIcon;
        @Bind(R.id.tv_class_name)
        TextView tvClassName;
        @Bind(R.id.tv_class_time)
        TextView tvClassTime;
        @Bind(R.id.tv_sign_time)
        TextView tvSignTime;
        @Bind(R.id.tv_credit_reason)
        TextView tvCreditReason;
        @Bind(R.id.tv_credit_score)
        TextView tvCreditScore;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
