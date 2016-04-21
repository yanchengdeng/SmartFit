package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.beans.MesageInfo;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.Options;
import com.smartfit.views.SelectableRoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/4/21.
 */
public class CourseMessagItemAdapter extends BaseAdapter {
    Context context;
    List<MesageInfo> messageLists;

    public CourseMessagItemAdapter(Context context, List<MesageInfo> messageLists) {
        this.context = context;
        this.messageLists = messageLists;

    }

    @Override
    public int getCount() {
        return messageLists.size();
    }

    @Override
    public Object getItem(int position) {
        return messageLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_message_course_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MesageInfo item = messageLists.get(position);
        ImageLoader.getInstance().displayImage(item.getMessageContent().getSourseUserPicUrl(), viewHolder.ivIcon, Options.getListOptions());
        if (!TextUtils.isEmpty(item.getMessageContent().getSourseUserName())) {
            viewHolder.tvName.setText(item.getMessageContent().getSourseUserName());
        }

        if (!TextUtils.isEmpty(item.getMessageContent().getCourseName())) {
            viewHolder.tvType.setText(item.getMessageContent().getCourseName());
        }

        if (!TextUtils.isEmpty(item.getMessageContent().getCourseName()) && !TextUtils.isEmpty(item.getMessageContent().getVenueName()) && !TextUtils.isEmpty(item.getMessageContent().getCoachName())) {
            viewHolder.tvClassInfo.setText(item.getMessageContent().getCourseName() + "-" + item.getMessageContent().getVenueName() + "-" + item.getMessageContent().getCoachName());
        }

        if (!TextUtils.isEmpty(item.getMessageContent().getStartTime()) && !TextUtils.isEmpty(item.getMessageContent().getEndTime())) {
            viewHolder.tvClassTime.setText(DateUtils.getData(item.getMessageContent().getStartTime()) + "~" + DateUtils.getDataTime(item.getMessageContent().getEndTime()));
        }

        if (!TextUtils.isEmpty(item.getMessageContent().getCoursePrice())) {
            viewHolder.tvClassPrice.setText("￥" + item.getMessageContent().getCoursePrice());
        }

        if (!TextUtils.isEmpty(item.getTime())) {
            viewHolder.tvDate.setText(DateUtils.getDataTimeMonth(item.getTime()));
        }
        return convertView;
    }

    public void setData(List<MesageInfo> messageLists) {
        this.messageLists = messageLists;
        notifyDataSetChanged();
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_message_course_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        SelectableRoundedImageView ivIcon;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_tittle)
        TextView tvTittle;
        @Bind(R.id.tv_type)
        TextView tvType;
        @Bind(R.id.tv_class_info)
        TextView tvClassInfo;
        @Bind(R.id.tv_class_time)
        TextView tvClassTime;
        @Bind(R.id.tv_class_price)
        TextView tvClassPrice;
        @Bind(R.id.tv_class_detail)
        TextView tvClassDetail;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
