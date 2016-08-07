package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.beans.ListMessageAllInfoItem;
import com.smartfit.commons.MessageType;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.Options;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/4/20.
 */
public class ListMessageAllInfoAdaper extends BaseAdapter {
    private Context context;
    private List<ListMessageAllInfoItem> listMessageAllInfoItems;

    public ListMessageAllInfoAdaper(Context context, List<ListMessageAllInfoItem> listMessageAllInfoItems) {
        this.context = context;
        this.listMessageAllInfoItems = listMessageAllInfoItems;

    }

    @Override
    public int getCount() {
        return listMessageAllInfoItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listMessageAllInfoItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_message_all_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ListMessageAllInfoItem item = listMessageAllInfoItems.get(position);
        if (!item.getSysMessage().getType().equals("-1")) {
            if (!TextUtils.isEmpty(item.getUnReadSysCount()) && Integer.parseInt(item.getUnReadSysCount()) > 0) {
                viewHolder.tvMsgNum.setText(item.getUnReadSysCount());
                viewHolder.tvMsgNum.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvMsgNum.setVisibility(View.GONE);
            }
/**
 * -1 环信消息
 *
 * 1-系统消息；2-课程邀请消息；3-预约成功消息；4-课程请求消息；5-好友邀请消息；6-订单成功消息
 */
            if (!TextUtils.isEmpty(item.getSysMessage().getType())) {
                if (item.getSysMessage().getType().equals("1")) {
                    viewHolder.tvName.setText("系统消息");
                } else if (item.getSysMessage().getType().equals("2")) {
                    viewHolder.tvName.setText("课程邀请消息");
                } else if (item.getSysMessage().getType().equals("3")) {
                    viewHolder.tvName.setText("预约成功消息");
                } else if (item.getSysMessage().getType().equals("4")) {
                    viewHolder.tvName.setText("课程请求消息");
                } else if (item.getSysMessage().getType().equals("5")) {
                    viewHolder.tvName.setText("好友邀请消息");
                } else if (item.getSysMessage().getType().equals("6")) {
                    viewHolder.tvName.setText("订单成功消息");
                } else if (item.getSysMessage().getType().equals("7")) {
                    viewHolder.tvName.setText("代课邀请消息");
                } else if (item.getSysMessage().getType().equals("21")) {
                    //领取券
                    viewHolder.tvName.setText("系统消息");
                } else if (item.getSysMessage().getType().equals("23")) {
                    //领取券
                    viewHolder.tvName.setText("系统消息");
                } else if (item.getSysMessage().getType().equals("24")) {
                   //器械课完结后通知
                    viewHolder.tvName.setText("系统消息");
                }
            }

            if (!TextUtils.isEmpty(item.getSysMessage().getTitle())) {
                viewHolder.tvContent.setText(item.getSysMessage().getTitle());
            }

            if (item.getSysMessage().getType().equals("7")) {
                viewHolder.tvContent.setText(item.getSysMessage().getMessageContent().getSourseUserName() + "向您发起代课邀请");
            } else if (item.getSysMessage().getType().equals("4")) {
                viewHolder.tvContent.setText(item.getSysMessage().getMessageContent().getSourseUserName() + "请求您作为教练");
            } else if (item.getSysMessage().getType().equals("2")) {
                viewHolder.tvContent.setText(item.getSysMessage().getMessageContent().getSourseUserName() + "邀请您加入课程");
            } else if (item.getSysMessage().getType().equals("5")) {
                viewHolder.tvContent.setText(item.getSysMessage().getMessageContent().getSourseUserName() + "决定请求您添加为健身好友");
            } else if (item.getSysMessage().getType().equals(MessageType.TICKE_GIFT_GIVE)) {
                if (item.getSysMessage().getMessageContent() != null) {

                    if (!TextUtils.isEmpty(item.getSysMessage().getMessageContent().getContent())) {
                        viewHolder.tvContent.setText(item.getSysMessage().getMessageContent().getDetail());
                    }
                }
            } else if (item.getSysMessage().getType().equals(MessageType.TICKET_BACK_MESSAGE)) {
                if (item.getSysMessage().getMessageContent() != null) {
                    if (!TextUtils.isEmpty(item.getSysMessage().getMessageContent().getContent())) {
                        viewHolder.tvContent.setText(item.getSysMessage().getMessageContent().getDetail());
                    }
                }
                if (!TextUtils.isEmpty(item.getSysMessage().getTime())) {
                    viewHolder.tvDate.setText(DateUtils.getDataTimeMonth(item.getSysMessage().getTime()));
                }
            } else if (item.getSysMessage().getType().equals(MessageType.AEREABICON_CLASS_OVER)) {
                if (item.getSysMessage().getMessageContent() != null) {
                    if (!TextUtils.isEmpty(item.getSysMessage().getMessageContent().getContent())) {
                        viewHolder.tvContent.setText(item.getSysMessage().getMessageContent().getDetail());
                    }
                }
                if (!TextUtils.isEmpty(item.getSysMessage().getTime())) {
                    viewHolder.tvDate.setText(DateUtils.getDataTimeMonth(item.getSysMessage().getTime()));
                }
            }
        } else {

            if (!TextUtils.isEmpty(item.getUnReadSysCount()) && Integer.parseInt(item.getUnReadSysCount()) > 0) {
                viewHolder.tvMsgNum.setText(item.getUnReadSysCount());
                viewHolder.tvMsgNum.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvMsgNum.setVisibility(View.GONE);
            }

            if (!TextUtils.isEmpty(item.getSysMessage().getContent())) {
                viewHolder.tvContent.setText(item.getSysMessage().getContent());
            }

            if (!TextUtils.isEmpty(item.getSysMessage().getTitle())) {
                viewHolder.tvName.setText(item.getSysMessage().getTitle());
            }

            if (!TextUtils.isEmpty(item.getSysMessage().getTime())) {
                viewHolder.tvDate.setText(item.getSysMessage().getTime());
            }

            if (!TextUtils.isEmpty(item.getSysMessage().getTime())) {
                viewHolder.tvDate.setText(DateUtils.getDataTimeMonth(item.getSysMessage().getTime()));
            }

            ImageLoader.getInstance().displayImage(item.getSysMessage().getUrl(), viewHolder.ivIcon, Options.getListOptions());
        }

        return convertView;
    }

    public void setData(List<ListMessageAllInfoItem> listMessageAllInfoItems) {
        this.listMessageAllInfoItems = listMessageAllInfoItems;
        notifyDataSetChanged();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_message_all_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_msg_num)
        TextView tvMsgNum;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.tv_date)
        TextView tvDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
