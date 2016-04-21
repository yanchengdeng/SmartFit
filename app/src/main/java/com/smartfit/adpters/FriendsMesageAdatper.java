package com.smartfit.adpters;

import android.content.ComponentName;
import android.content.Context;
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
import com.smartfit.activities.BaseActivity;
import com.smartfit.beans.MesageInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.SelectableRoundedImageView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/21.
 */
public class FriendsMesageAdatper extends BaseAdapter {
    private Context context;
    List<MesageInfo> messageLists;


    public FriendsMesageAdatper(Context context, List<MesageInfo> messageLists) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_friends_message_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final MesageInfo item = messageLists.get(position);
        ImageLoader.getInstance().displayImage(item.getMessageContent().getSourseUserPicUrl(), viewHolder.ivIcon, Options.getListOptions());
        if (!TextUtils.isEmpty(item.getMessageContent().getSourseUserName())) {
            viewHolder.tvName.setText(item.getMessageContent().getSourseUserName());
        }

        viewHolder.btnIgnore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ignoreFriends(item.getId(), position);
            }
        });

        viewHolder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accepterFriends(item.getMessageContent().getSourseUid(),position);

            }
        });


        return convertView;
    }

    private void accepterFriends(String sourseUid, int position) {
        Map<String, String> map = new HashMap<>();
        map.put("friendId", sourseUid);
        PostRequest request = new PostRequest(Constants.USER_ADDFRIEND, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

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

    static class ViewHolder {
        @Bind(R.id.iv_icon)
        SelectableRoundedImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_content)
        TextView tvContent;
        @Bind(R.id.btn_ignore)
        Button btnIgnore;
        @Bind(R.id.btn_accept)
        Button btnAccept;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
