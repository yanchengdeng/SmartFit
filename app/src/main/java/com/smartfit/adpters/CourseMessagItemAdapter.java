package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.beans.MesageInfo;
import com.smartfit.commons.MessageType;
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

    private static final int ITEM_SIMPLE = 1;
    private static final int ITEM_WITH_BUTTON = 2;//带按钮的布局   消息类型：4、8、9

    public CourseMessagItemAdapter(Context context, List<MesageInfo> messageLists) {
        this.context = context;
        this.messageLists = messageLists;
    }

    @Override
    public int getItemViewType(int position) {
        if (messageLists.get(position).equals(MessageType.MESSAGE_TYPE_COURSE_REQUEST) || messageLists.get(position).equals(MessageType.MESSAGE_TYPE_COURSE_SUBSTITUTE_TO_COACH) || messageLists.get(position).equals(MessageType.MESSAGE_TYPE_COURSE_SUBSTITUTE_TO_USER))
            return ITEM_WITH_BUTTON;
        else
            return ITEM_SIMPLE;
    }


    @Override
    public int getViewTypeCount() {
        return 2;
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

        if (getItemViewType(position) == ITEM_WITH_BUTTON) {
            ViewHolderWithButton viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_message_course_item, null);
                viewHolder = new ViewHolderWithButton(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolderWithButton) convertView.getTag();
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

        } else {
            ViewHolder viewHolderInfo;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_message_course_item__info, null);
                viewHolderInfo = new ViewHolder(convertView);
                convertView.setTag(viewHolderInfo);
            } else {
                viewHolderInfo = (ViewHolder) convertView.getTag();
            }
            MesageInfo item = messageLists.get(position);
            ImageLoader.getInstance().displayImage(item.getMessageContent().getSourseUserPicUrl(), viewHolderInfo.ivIcon, Options.getListOptions());
            if (!TextUtils.isEmpty(item.getMessageContent().getSourseUserName())) {
                viewHolderInfo.tvName.setText(item.getMessageContent().getSourseUserName());
            }
            if (!TextUtils.isEmpty(item.getTime())) {
                viewHolderInfo.tvDate.setText(DateUtils.getDataTimeMonth(item.getTime()));
            }

            if (!TextUtils.isEmpty(item.getMessageContent().getCourseName())){
                viewHolderInfo.tvTittle.setText(String.format(context.getString(R.string.course_aggree_info),new Object[]{item.getMessageContent().getCoachName()}));
            }

        }


        return convertView;
    }

    public void setData(List<MesageInfo> messageLists) {
        this.messageLists = messageLists;
        notifyDataSetChanged();
    }


    static class ViewHolderWithButton {
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
        @Bind(R.id.btn_aggren)
        Button btnAggren;
        @Bind(R.id.btn_refuse)
        Button btnRefuse;

        ViewHolderWithButton(View view) {
            ButterKnife.bind(this, view);
        }
    }

    static class ViewHolder {
        @Bind(R.id.iv_icon)
        SelectableRoundedImageView ivIcon;
        @Bind(R.id.tv_date)
        TextView tvDate;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_tittle)
        TextView tvTittle;
        @Bind(R.id.tv_class_detail)
        TextView tvClassDetail;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
