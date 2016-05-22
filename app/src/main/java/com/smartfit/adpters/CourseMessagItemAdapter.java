package com.smartfit.adpters;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.activities.ArerobicDetailActivity;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.GroupClassDetailActivity;
import com.smartfit.activities.PrivateClassByMessageActivity;
import com.smartfit.beans.MesageInfo;
import com.smartfit.commons.Constants;
import com.smartfit.commons.MessageType;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.SelectableRoundedImageView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/4/21.
 */
public class CourseMessagItemAdapter extends BaseAdapter {
    Context context;
    List<MesageInfo> messageLists;

    private static final int ITEM_SIMPLE = 0;
    private static final int ITEM_WITH_BUTTON = 1;//带按钮的布局   消息类型：4、8、9

    public CourseMessagItemAdapter(Context context, List<MesageInfo> messageLists) {
        this.context = context;
        this.messageLists = messageLists;
    }

    @Override
    public int getItemViewType(int position) {
        //课程是  2 4  8  9  类型
        if (messageLists.get(position).getType().equals(MessageType.MESSAGE_TYPE_COURSE_INVITE) || messageLists.get(position).getType().equals(MessageType.MESSAGE_TYPE_COURSE_REQUEST) || messageLists.get(position).getType().equals(MessageType.MESSAGE_TYPE_COURSE_SUBSTITUTE_TO_COACH) || messageLists.get(position).getType().equals(MessageType.MESSAGE_TYPE_COURSE_SUBSTITUTE_TO_USER)) {
            return ITEM_WITH_BUTTON;
        } else {
            return ITEM_SIMPLE;
        }
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (getItemViewType(position) == ITEM_WITH_BUTTON) {
            ViewHolderWithButton viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_message_course_item, null);
                viewHolder = new ViewHolderWithButton(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolderWithButton) convertView.getTag();
            }

            final MesageInfo item = messageLists.get(position);
            ImageLoader.getInstance().displayImage(item.getMessageContent().getSourseUserPicUrl(), viewHolder.ivIcon, Options.getListOptions());
            if (!TextUtils.isEmpty(item.getMessageContent().getSourseUserName())) {
                viewHolder.tvName.setText(item.getMessageContent().getSourseUserName());
            }

            if (item.getType().equals(MessageType.MESSAGE_TYPE_APPOStringMENT_SUCCESS)) {
                if (!TextUtils.isEmpty(item.getMessageContent().getCourseName())) {
                    viewHolder.tvTittle.setText(String.format(context.getString(R.string.course_aggree_info), new Object[]{item.getMessageContent().getCourseName()}));
                }
            } else if (item.getType().equals(MessageType.MESSAGE_TYPE_COURSE_REFUSE)) {
                if (!TextUtils.isEmpty(item.getMessageContent().getCourseName()) && !TextUtils.isEmpty(item.getMessageContent().getInvitedUserName())) {
                    viewHolder.tvTittle.setText(String.format(context.getString(R.string.refuse_your_course), new Object[]{item.getMessageContent().getInvitedUserName(), item.getMessageContent().getCourseName()}));
                }

            } else if (item.getType().equals(MessageType.MESSAGE_TYPE_COURSE_SUBSITUTE_ACCEPT)) {
                if (!TextUtils.isEmpty(item.getMessageContent().getCourseName()) && !TextUtils.isEmpty(item.getMessageContent().getInvitedUserName())) {
                    viewHolder.tvTittle.setText(String.format(context.getString(R.string.accept_your_course), new Object[]{item.getMessageContent().getInvitedUserName(), item.getMessageContent().getCourseName()}));
                }
            }else if(item.getType().equals(MessageType.MESSAGE_TYPE_COURSE_INVITE)){
                if (!TextUtils.isEmpty(item.getMessageContent().getCourseName()) && !TextUtils.isEmpty(item.getMessageContent().getInvitedUserName())) {
                    viewHolder.tvTittle.setText("邀请您加入课程");
                }
            } else {
                if (!TextUtils.isEmpty(item.getMessageContent().getCourseName())) {
                    viewHolder.tvTittle.setText(String.format(context.getString(R.string.course_aggree_info), new Object[]{item.getMessageContent().getCourseName()}));
                }
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

            //2  隐藏按钮
            if (item.getType().equals(MessageType.MESSAGE_TYPE_COURSE_INVITE)) {
                viewHolder.btnAggren.setVisibility(View.GONE);
                viewHolder.btnRefuse.setVisibility(View.GONE);
            } else {
                viewHolder.btnAggren.setVisibility(View.VISIBLE);
                viewHolder.btnRefuse.setVisibility(View.VISIBLE);
            }


            //  9  隐藏价格
            if (item.getType().equals(MessageType.MESSAGE_TYPE_COURSE_SUBSTITUTE_TO_USER)) {
                viewHolder.tvClassPrice.setVisibility(View.GONE);
            } else {
                viewHolder.tvClassPrice.setVisibility(View.VISIBLE);
            }

            viewHolder.btnAggren.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aggree(item.getId(),position,item.getMessageContent().getCourseId(), 1);
                }
            });

            viewHolder.btnRefuse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aggree(item.getId(),position,item.getMessageContent().getCourseId(), 2);
                }
            });

            viewHolder.tvClassDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openClass(item);
                }
            });
        } else {
            ViewHolder viewHolderInfo;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.adapter_message_course_item__info, null);
                viewHolderInfo = new ViewHolder(convertView);
                convertView.setTag(viewHolderInfo);
            } else {
                viewHolderInfo = (ViewHolder) convertView.getTag();
            }
            final MesageInfo item = messageLists.get(position);
            ImageLoader.getInstance().displayImage(item.getMessageContent().getSourseUserPicUrl(), viewHolderInfo.ivIcon, Options.getListOptions());
            if (!TextUtils.isEmpty(item.getMessageContent().getSourseUserName())) {
                viewHolderInfo.tvName.setText(item.getMessageContent().getSourseUserName());
            }
            if (!TextUtils.isEmpty(item.getTime())) {
                viewHolderInfo.tvDate.setText(DateUtils.getDataTimeMonth(item.getTime()));
            }

            if (item.getType().equals(MessageType.MESSAGE_TYPE_APPOStringMENT_SUCCESS)) {
                if (!TextUtils.isEmpty(item.getMessageContent().getCourseName())) {
                    viewHolderInfo.tvTittle.setText(String.format(context.getString(R.string.course_aggree_info), new Object[]{item.getMessageContent().getCourseName()}));
                }
            } else if (item.getType().equals(MessageType.MESSAGE_TYPE_COURSE_REFUSE)) {
                if (!TextUtils.isEmpty(item.getMessageContent().getCourseName()) && !TextUtils.isEmpty(item.getMessageContent().getInvitedUserName())) {
                    viewHolderInfo.tvTittle.setText(String.format(context.getString(R.string.refuse_your_course), new Object[]{item.getMessageContent().getInvitedUserName(), item.getMessageContent().getCourseName()}));
                }

            } else if (item.getType().equals(MessageType.MESSAGE_TYPE_COURSE_SUBSITUTE_ACCEPT)) {
                if (!TextUtils.isEmpty(item.getMessageContent().getCourseName()) && !TextUtils.isEmpty(item.getMessageContent().getInvitedUserName())) {
                    viewHolderInfo.tvTittle.setText(String.format(context.getString(R.string.accept_your_course), new Object[]{item.getMessageContent().getInvitedUserName(), item.getMessageContent().getCourseName()}));
                }
            } else {
                if (!TextUtils.isEmpty(item.getMessageContent().getCourseName())) {
                    viewHolderInfo.tvTittle.setText(String.format(context.getString(R.string.course_aggree_info), new Object[]{item.getMessageContent().getCourseName()}));
                }
            }

            viewHolderInfo.tvClassDetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openClass(item);
                }
            });
        }


        return convertView;
    }

    /**
     * 跳转叨叨详情页
     *
     * @param item
     */
    private void openClass(MesageInfo item) {
        if (!TextUtils.isEmpty(item.getMessageContent().getCourseType())) {
            //0  团操 1  小班   2  私教  3  有氧
            if (item.getMessageContent().getCourseType().equals("0")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, item.getMessageContent().getCourseId());
                bundle.putString(Constants.COURSE_TYPE, "0");
                ((BaseActivity) context).openActivity(GroupClassDetailActivity.class, bundle);

            } else if (item.getMessageContent().getCourseType().equals("1")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, item.getMessageContent().getCourseId());
                bundle.putString(Constants.COURSE_TYPE, "1");
                ((BaseActivity) context).openActivity(GroupClassDetailActivity.class, bundle);

            } else if (item.getMessageContent().getCourseType().equals("2")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING,item.getMessageContent().getCourseId());
                        ((BaseActivity) context).openActivity(PrivateClassByMessageActivity.class,bundle);

            } else if (item.getMessageContent().getCourseType().equals("3")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING,item.getMessageContent().getCourseId());
                ((BaseActivity) context).openActivity(ArerobicDetailActivity.class,bundle);
            }
        }

    }

    /**
     * 同意代课
     *
     * @param courseId
     * @param flag     1  同意  2  拒绝
     */
    private void aggree(final String messageId, final int posiotn,String courseId, final int flag) {
        Map<String, String> maps = new HashMap<>();
        maps.put("courseId", courseId);
        String host;
        if (flag == 1) {
            host = Constants.COACH_RECEIVESUBSTITUTECOACH;
        } else {
            host = Constants.COACH_REJECTSUBSTITUTECOACH;
        }

        PostRequest request = new PostRequest(host, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if (flag == 1) {
                    ((BaseActivity) context).mSVProgressHUD.showInfoWithStatus("已同意", SVProgressHUD.SVProgressHUDMaskType.Clear);
                } else {
                    ((BaseActivity) context).mSVProgressHUD.showInfoWithStatus("已拒绝", SVProgressHUD.SVProgressHUDMaskType.Clear);
                }

                ignoreFriends(messageId,posiotn);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) context).mSVProgressHUD.showInfoWithStatus(context.getString(R.string.do_later), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(context);
        ((BaseActivity) context).mQueue.add(request);
    }


    private void ignoreFriends(String id, final int psoiton) {

        Map<String, String> map = new HashMap<>();
        map.put("id", id);
        PostRequest request = new PostRequest(Constants.MESSAGE_DEL, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                messageLists.remove(psoiton);
                notifyDataSetChanged();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) context).mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(context);
        ((BaseActivity) context).mQueue.add(request);
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
