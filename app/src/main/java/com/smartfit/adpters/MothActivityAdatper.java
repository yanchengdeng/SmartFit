package com.smartfit.adpters;/**
 * 作者：dengyancheng on 16/7/9 23:32
 * 邮箱：yanchengdeng@gmail.com
 */

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.ConfimPayNoramlActivity;
import com.smartfit.beans.NewMouthServerEvent;
import com.smartfit.commons.Constants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：dengyancheng on 16/7/9 23:32
 * 邮箱：yanchengdeng@gmail.com
 * <p/>
 * 月卡活动
 */
public class MothActivityAdatper extends BaseAdapter {
    private Context context;
    private ArrayList<NewMouthServerEvent> newMouthServerEvents;

    public MothActivityAdatper(Context context, ArrayList<NewMouthServerEvent> newMouthServerEvents) {
        this.context = context;
        this.newMouthServerEvents = newMouthServerEvents;
    }

    @Override
    public int getCount() {
        return newMouthServerEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return newMouthServerEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_month_activity_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final NewMouthServerEvent item = newMouthServerEvents.get(position);
        if (item.getPics() != null && item.getPics().length > 0) {
            ImageLoader.getInstance().displayImage(item.getPics()[0], viewHolder.ivIcon);
        }else{
            viewHolder.ivIcon.setImageResource(R.mipmap.bg_pic);
        }

        viewHolder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.PAGE_INDEX, 10);
                bundle.putParcelable(Constants.PASS_OBJECT, item);
                ((BaseActivity) context).openActivity(ConfimPayNoramlActivity.class, bundle);
            }
        });
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_month_activity_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        RoundedImageView ivIcon;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
