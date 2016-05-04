package com.smartfit.adpters;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.beans.MyAddClass;
import com.smartfit.commons.Constants;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/3/12.
 * 课程状态  已结束
 */
public class MyClassOrderOverStatusAdapter extends BaseAdapter {
    private Context context;
    private List<MyAddClass> datas;
    private EventBus eventBus;

    public MyClassOrderOverStatusAdapter(Context context, List<MyAddClass> datas) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_my_classes_over_status_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final MyAddClass item = datas.get(position);

        if (!TextUtils.isEmpty(item.getCourseName())) {
            viewHolder.tvClassName.setText(item.getCourseName());
        }
//        viewHolder.tvTime.setText(DateUtils.getData(item.getStartTime() + "~" + DateUtils.getDataTime(item.getEndTime())));

        if (!TextUtils.isEmpty(item.getStartUserName())) {
            viewHolder.tvName.setText(item.getStartUserName());
        }

        if (!TextUtils.isEmpty(item.getCourseName())) {
            viewHolder.tvClassName.setText(item.getCourseName());
        }

        if (!TextUtils.isEmpty(item.getCoursePrice())) {
            viewHolder.tvMoney.setText(item.getCoursePrice());
        }

        if (!TextUtils.isEmpty(item.getSignCount())) {
            viewHolder.tvClassMember.setText(item.getSignCount() + "位学员");
        }

        viewHolder.btnReopen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING,item.getId());
//                ((BaseActivity) context).openActivity(ReopenClassActivity.class, bundle);
            }
        });


        return convertView;
    }


    public void setData(List<MyAddClass> datas) {
        this.datas = datas;
    }


    static class ViewHolder {
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_status)
        TextView tvStatus;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_class_name)
        TextView tvClassName;
        @Bind(R.id.tv_class_member)
        TextView tvClassMember;
        @Bind(R.id.tv_money)
        TextView tvMoney;
        @Bind(R.id.btn_reopen)
        Button btnReopen;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
