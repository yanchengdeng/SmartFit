package com.smartfit.adpters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.activities.CoachInfoActivity;
import com.smartfit.activities.MainBusinessActivity;
import com.smartfit.beans.PrivateEducationClass;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/4.
 * 私教  数据列表
 */
public class PrivateEducationAdapter extends BaseAdapter  {
    private Context context;
    private List<PrivateEducationClass> datas;
    LinearLayout.LayoutParams params;

    private boolean isDissmis = true;
    public PrivateEducationAdapter(Context context
            , List<PrivateEducationClass> datas) {
        this.context = context;
        this.datas = datas;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 24);
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
    public View getView( int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_private_education_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ratingBar.setLayoutParams(params);

         PrivateEducationClass item = datas.get(position);
        viewHolder.tvCoach.setText(item.getName());
        viewHolder.chSelect.setChecked(item.isCheck());
        if(isDissmis){
            viewHolder.chSelect.setVisibility(View.VISIBLE);
        }else {
            viewHolder.chSelect.setVisibility(View.GONE);
        }
        viewHolder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainBusinessActivity) context).openActivity(CoachInfoActivity.class);
            }
        });
        return convertView;
    }



    public void setData(List<PrivateEducationClass> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void setDismissCheck(boolean dissmis) {
        this.isDissmis = dissmis;
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_private_education_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    public static class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_coach)
        TextView tvCoach;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.ratingBar)
        RatingBar ratingBar;
        @Bind(R.id.tv_num)
        TextView tvNum;
        @Bind(R.id.ch_select)
        public CheckBox chSelect;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
