package com.smartfit.adpters;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.activities.CoachInfoActivity;
import com.smartfit.activities.MainBusinessActivity;
import com.smartfit.beans.PrivateEducationClass;
import com.smartfit.commons.Constants;
import com.smartfit.utils.Options;

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

         final PrivateEducationClass item = datas.get(position);
        if (!TextUtils.isEmpty(item.getNickName())) {
            viewHolder.tvCoach.setText("教练 "+item.getNickName());
        }

        if (!TextUtils.isEmpty(item.getSex())) {
            if (item.getSex().equals("0")){
                viewHolder.tvCoach.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable(R.mipmap.icon_woman),null);
            }else{
                viewHolder.tvCoach.setCompoundDrawablesWithIntrinsicBounds(null,null,context.getResources().getDrawable(R.mipmap.icon_man),null);
            }
        }
        viewHolder.chSelect.setChecked(item.isCheck());
        if(isDissmis){
            viewHolder.chSelect.setVisibility(View.VISIBLE);
        }else {
            viewHolder.chSelect.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(item.getPrice())) {
            viewHolder.chSelect.setText(item.getPrice()+"元/小时");
        }

       viewHolder.tvTime.setText("暂无时间 /有空");

        viewHolder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING,item.getUid());
                        ((MainBusinessActivity) context).openActivity(CoachInfoActivity.class,bundle);
            }
        });
        if (!TextUtils.isEmpty(item.getStars())) {
            viewHolder.ratingBar.setRating(Float.parseFloat(item.getStars()));
            viewHolder.tvNum.setText(item.getStars());
        }
        ImageLoader.getInstance().displayImage(item.getUserPicUrl(),viewHolder.ivIcon, Options.getListOptions());
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
