package com.smartfit.adpters;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.UserCoachPublishClassOneActivity;
import com.smartfit.activities.UserCoachPublishClassTwoActivity;
import com.smartfit.activities.UserCustomClassTwoActivity;
import com.smartfit.beans.ClassFication;
import com.smartfit.commons.Constants;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：dengyancheng on 16/4/24 17:17
 * 邮箱：yanchengdeng@gmail.com
 */
public class CustomClassGridViewItem extends BaseAdapter {
    private Context context;
    private List<ClassFication> classFications;
    private boolean isFromCoach = true;//是否是教练发起的课程

    public CustomClassGridViewItem(Context context, List<ClassFication> classFicationList, boolean isFromCoach) {
        this.context = context;
        this.classFications = classFicationList;
        this.isFromCoach = isFromCoach;
    }

    @Override
    public int getCount() {
        return classFications.size();
    }

    @Override
    public Object getItem(int position) {
        return classFications.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_custom_gridview, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (!TextUtils.isEmpty(classFications.get(position).getClassificationName()))
            viewHolder.tvName.setText(classFications.get(position).getClassificationName());

        viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, classFications.get(position).getId());
                if (isFromCoach)
                    ((BaseActivity) context).openActivity(UserCoachPublishClassTwoActivity.class, bundle);
                else
                    ((BaseActivity) context).openActivity(UserCustomClassTwoActivity.class, bundle);
            }
        });
        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_custom_gridview.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
