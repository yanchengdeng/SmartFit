package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.beans.MesageInfo;
import com.smartfit.commons.Constants;
import com.smartfit.commons.MessageType;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 系统消息适配器
 *
 * @author yanchengdeng
 *         create at 2016/5/3 9:38
 */
public class SystemMesageAdatper extends BaseAdapter {
    private Context context;
    List<MesageInfo> messageLists;


    public SystemMesageAdatper(Context context, List<MesageInfo> messageLists) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_system_message_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final MesageInfo item = messageLists.get(position);

        if (item.getType().equals(MessageType.MESSAGE_TYPE_SYTEM)) {
            if (!TextUtils.isEmpty(item.getMessageContent().getContent())) {
                viewHolder.tvContent.setText(item.getMessageContent().getContent());
            }
            if (!TextUtils.isEmpty(item.getTitle())) {
                viewHolder.tvName.setText(item.getTitle());
            }
            viewHolder.tvAccepte.setVisibility(View.GONE);
        } else if (item.getType().equals(MessageType.TICKE_GIFT_ACCEPTE)) {
            if (item.getMessageContent() != null) {
                if (!TextUtils.isEmpty(item.getMessageContent().getTitle())) {
                    viewHolder.tvName.setText(item.getMessageContent().getTitle());
                }
                if (!TextUtils.isEmpty(item.getMessageContent().getDetail())) {
                    viewHolder.tvContent.setText(item.getMessageContent().getDetail());
                }

            }
            viewHolder.tvAccepte.setVisibility(View.GONE);
        } else if (item.getType().equals(MessageType.TICKET_BACK_MESSAGE)) {
            if (item.getMessageContent() != null) {
                if (!TextUtils.isEmpty(item.getMessageContent().getTitle())) {
                    viewHolder.tvName.setText(item.getMessageContent().getTitle());
                }

                if (!TextUtils.isEmpty(item.getMessageContent().getDetail())) {
                    viewHolder.tvContent.setText(item.getMessageContent().getDetail());
                }
            }
            viewHolder.tvAccepte.setVisibility(View.GONE);
        } else if (item.getType().equals(MessageType.TICKE_GIFT_GIVE)) {
            if (item.getMessageContent() != null) {
                if (!TextUtils.isEmpty(item.getMessageContent().getSourseUserName())) {
                    viewHolder.tvName.setText(item.getMessageContent().getSourseUserName());
                }
                viewHolder.tvContent.setText("送你SMART FIT健身券啦");

            }
            ImageLoader.getInstance().displayImage(item.getMessageContent().getSourseUserPicUrl(), viewHolder.ivIcon, Options.getHeaderOptions());
            viewHolder.tvAccepte.setVisibility(View.VISIBLE);
        }else if(item.getType().equals(MessageType.AEREABICON_CLASS_OVER)){
            if (item.getMessageContent() != null) {
                if (!TextUtils.isEmpty(item.getMessageContent().getTitle())) {
                    viewHolder.tvName.setText(item.getMessageContent().getTitle());
                }
                if (!TextUtils.isEmpty(item.getMessageContent().getDetail())) {
                    viewHolder.tvContent.setText(item.getMessageContent().getDetail());
                }
            }
        }else if(item.getType().equals(MessageType.ABSENT_CLASS_MSG)){
            if (item.getMessageContent() != null) {
                if (!TextUtils.isEmpty(item.getMessageContent().getTitle())) {
                    viewHolder.tvName.setText(item.getMessageContent().getTitle());
                }
                if (!TextUtils.isEmpty(item.getMessageContent().getDetail())) {
                    viewHolder.tvContent.setText(item.getMessageContent().getDetail());
                }
            }
        }else if(item.getType().equals(MessageType.STEAGE_PASS)){
            if (item.getMessageContent() != null) {
                if (!TextUtils.isEmpty(item.getMessageContent().getTitle())) {
                    viewHolder.tvName.setText(item.getMessageContent().getTitle());
                }
                if (!TextUtils.isEmpty(item.getMessageContent().getDetail())) {
                    viewHolder.tvContent.setText(item.getMessageContent().getDetail());
                }
            }
        }else if(item.getType().equals(MessageType.SETAGE_PASS_NOT)){
            if (item.getMessageContent() != null) {
                if (!TextUtils.isEmpty(item.getMessageContent().getTitle())) {
                    viewHolder.tvName.setText(item.getMessageContent().getTitle());
                }
                if (!TextUtils.isEmpty(item.getMessageContent().getDetail())) {
                    viewHolder.tvContent.setText(item.getMessageContent().getDetail());
                }
            }
        }else if(item.getType().equals(MessageType.ONE_HOUR_TIPS_BEFORE_OPREN_CLASS)){
            if (item.getMessageContent() != null) {
                if (!TextUtils.isEmpty(item.getMessageContent().getTitle())) {
                    viewHolder.tvName.setText(item.getMessageContent().getTitle());
                }
                if (!TextUtils.isEmpty(item.getMessageContent().getDetail())) {
                    viewHolder.tvContent.setText(item.getMessageContent().getDetail());
                }
            }
        }else {
            if (item.getMessageContent() != null) {
                if (!TextUtils.isEmpty(item.getMessageContent().getTitle())) {
                    viewHolder.tvName.setText(item.getMessageContent().getTitle());
                }
                if (!TextUtils.isEmpty(item.getMessageContent().getDetail())) {
                    viewHolder.tvContent.setText(item.getMessageContent().getDetail());
                }
            }
        }

        viewHolder.tvAccepte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accepterTicket(item,position);
            }
        });

        return convertView;
    }

    private void accepterTicket(MesageInfo item, final int position) {
            Map<String, String> map = new HashMap<>();
            map.put("shareId", item.getMessageContent().getGoodsId());

            PostRequest request = new PostRequest(Constants.EVENT_ACCEPTSHAREDEVENTUSER, map, new Response.Listener<JsonObject>() {
                @Override
                public void onResponse(JsonObject response) {
                    messageLists.get(position).setType(MessageType.TICKE_GIFT_ACCEPTE);
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

    static class ViewHolder {
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.tv_accpet)
        TextView tvAccepte;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
