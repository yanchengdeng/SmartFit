package com.smartfit.adpters;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.activities.MyWorkPointActivity;
import com.smartfit.activities.SelectWorkPointActivity;
import com.smartfit.activities.WorkPlaceEditActivity;
import com.smartfit.beans.WorkPoint;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by yanchengdneg on 2016/3/23.
 * 新的工作地点
 */
public class WorkPointAdapter extends BaseAdapter {

    private List<WorkPoint> datas;
    private Context context;

    Handler handler;

    public WorkPointAdapter(Context context, List<WorkPoint> datas, Handler handler) {
        this.datas = datas;
        this.context = context;
        this.handler = handler;

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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_work_point_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final WorkPoint item = datas.get(position);
        if (TextUtils.isEmpty(item.getId())) {
            viewHolder.tvWorkName.setText(item.getTittle());
            viewHolder.tvWorkPoint.setText(item.getVenueName());
            viewHolder.tvWorkTime.setText(item.getStartTime());
        } else {
            viewHolder.tvWorkName.setText(item.getTittle());
            viewHolder.tvWorkPoint.setText(item.getVenueName());
            viewHolder.tvWorkTime.setText(DateUtils.getTimeFromZeroToNow(item.getStartTime()) + " - " + DateUtils.getTimeFromZeroToNow(item.getEndTime()));
        }

        viewHolder.ivDelet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = handler.obtainMessage();
                msg.what = position;
                handler.sendMessage(msg);
            }
        });




        viewHolder.llContextUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(Constants.PASS_OBJECT,item);
                        ((MyWorkPointActivity) context).openActivity(WorkPlaceEditActivity.class,bundle);
            }
        });

        return convertView;
    }

    public void setData(List<WorkPoint> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_work_point_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.tv_work_name)
        TextView tvWorkName;
        @Bind(R.id.tv_work_time_tittle)
        TextView tvWorkTimeTittle;
        @Bind(R.id.tv_work_time)
        TextView tvWorkTime;
        @Bind(R.id.tv_work_point_tittle)
        TextView tvWorkPointTittle;
        @Bind(R.id.tv_work_point)
        TextView tvWorkPoint;
        @Bind(R.id.ll_context_ui)
        RelativeLayout llContextUi;
        @Bind(R.id.iv_delet)
        ImageView ivDelet;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
