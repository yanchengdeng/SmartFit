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

import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.FindSubstitueActivity;
import com.smartfit.activities.MembersListActivity;
import com.smartfit.beans.MyAddClass;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/3/12.
 * 教练进行教学的课程
 */
public class CoachClassGoingStatusAdapter extends BaseAdapter {
    private Context context;
    private List<MyAddClass> datas;
    private boolean isHandleShow = true;

    public CoachClassGoingStatusAdapter(Context context, List<MyAddClass> datas, boolean isHandleShow) {
        this.context = context;
        this.datas = datas;
        this.isHandleShow = isHandleShow;

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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_coach_going_class_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        MyAddClass item = datas.get(position);
        viewHolder.tvTime.setText(DateUtils.getData(item.getStartTime()) + "~" + DateUtils.getDataTime(item.getEndTime()));
        if (!TextUtils.isEmpty(item.getCourseName())) {
            viewHolder.tvClassName.setText(item.getCourseName());
        }

        if (!TextUtils.isEmpty(item.getCourseType())) {
            if (item.getCourseType().equals("0")) {
                viewHolder.tvClassType.setText("团操课");
            } else if (item.getCourseType().equals("1")) {
                viewHolder.tvClassType.setText("小班课");
            } else if (item.getCourseType().equals("2")) {
                viewHolder.tvClassType.setText("私教课");
            } else {
                viewHolder.tvClassType.setText("有氧器械");
            }
        }

        viewHolder.tvFitnessCenter.setText("暂无");
        if (!TextUtils.isEmpty(item.getSignCount())) {
            viewHolder.tvHaveRegiseter.setText(item.getSignCount()+"人已报名");
        }

        if (!TextUtils.isEmpty(item.getMaxPersonCount()) && !TextUtils.isEmpty(item.getSignCount())) {
            viewHolder.tvEmptyRegister.setText((Integer.parseInt(item.getMaxPersonCount())-Integer.parseInt(item.getSignCount()))+"");
        }
        //找人代课
        viewHolder.tvSubstitue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING,"12");
                ((BaseActivity) context).openActivity(FindSubstitueActivity.class,bundle);
            }
        });
        //取消课程
        viewHolder.tvCancleClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //成员
        viewHolder.tvMemberList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) context).openActivity(MembersListActivity.class);
            }
        });


        return convertView;
    }

    public void setData(List<MyAddClass> datas) {
        this.datas = datas;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_coach_going_class_view.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_operate)
        TextView tvOperate;
        @Bind(R.id.tv_class_name)
        TextView tvClassName;
        @Bind(R.id.tv_class_type)
        TextView tvClassType;
        @Bind(R.id.tv_fitness_center)
        TextView tvFitnessCenter;
        @Bind(R.id.tv_have_regiseter)
        TextView tvHaveRegiseter;
        @Bind(R.id.tv_empty_register)
        TextView tvEmptyRegister;
        @Bind(R.id.tv_empty_register_tips)
        TextView tvEmptyRegisterTips;
        @Bind(R.id.tv_money)
        TextView tvMoney;
        @Bind(R.id.tv_substitue)
        TextView tvSubstitue;
        @Bind(R.id.tv_cancle_class)
        TextView tvCancleClass;
        @Bind(R.id.tv_member_list)
        TextView tvMemberList;
        @Bind(R.id.ll_handle_funciton)
        LinearLayout llHandleFunciton;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
