package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.activities.TeachClassListActivity;
import com.smartfit.beans.ClassFication;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： 邓言诚 创建于： 2016/5/9 16:45.
 */
public class TeachClassAdapter extends BaseAdapter {
    private Context context;
    private List<ClassFication> classFicationList;

    public TeachClassAdapter(Context context, List<ClassFication> classFicationList) {
        this.context = context;
        this.classFicationList = classFicationList;
    }

    @Override
    public int getCount() {
        return classFicationList.size();
    }

    @Override
    public Object getItem(int position) {
        return classFicationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_teach_class_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ClassFication item = classFicationList.get(position);
        if (!TextUtils.isEmpty(item.getClassificationName())) {
            viewHolder.tvClass.setText(item.getClassificationName());
        }
        viewHolder.chSelect.setChecked(item.isCheck());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_class)
        TextView tvClass;
        @Bind(R.id.ch_select)
        CheckBox chSelect;
        @Bind(R.id.ll_friends_ui)
        RelativeLayout llFriendsUi;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
