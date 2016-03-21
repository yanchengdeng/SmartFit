package com.smartfit.adpters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartfit.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/3/12.
 * 教练进行教学的课程
 */
public class CoachClassGoingStatusAdapter extends BaseAdapter {
    private Context context;
    private List<String> datas;
    private boolean isHandleShow = true;

    public CoachClassGoingStatusAdapter(Context context, List<String> datas, boolean isHandleShow) {
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

        return convertView;
    }

    public void setData(List<String> datas) {
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
