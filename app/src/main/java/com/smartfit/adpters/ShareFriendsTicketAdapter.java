package com.smartfit.adpters;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.OtherCustomeMainActivity;
import com.smartfit.beans.ShareFriendsInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.Options;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author dengyancheng
 *         分享卷 ---》给好友
 */
public class ShareFriendsTicketAdapter extends BaseAdapter {
    private List<ShareFriendsInfo> data;

    private Context context;

    public ShareFriendsTicketAdapter(Context context, List<ShareFriendsInfo> datas) {
        this.data = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_share_friends_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        final ShareFriendsInfo item = data.get(position);

        viewHolder.chSelect.setChecked(item.isCheck());
        if (!TextUtils.isEmpty(item.getNickName())) {
            viewHolder.tvCoach.setText(item.getNickName());
        }
        ImageLoader.getInstance().displayImage(item.getUserPicUrl(), viewHolder.ivIcon, Options.getListOptions());
        if (!TextUtils.isEmpty(item.getSex())) {
            if (item.getSex().equals(Constants.SEX_WOMEN)) {
                viewHolder.tvCoach.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.mipmap.icon_woman), null);
            } else {
                viewHolder.tvCoach.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.mipmap.icon_man), null);
            }
        }

        if (TextUtils.isEmpty(item.getSignature())) {
            viewHolder.tvTime.setVisibility(View.GONE);
        } else {
            viewHolder.tvTime.setText(item.getSignature());
            viewHolder.tvTime.setVisibility(View.VISIBLE);
        }

        viewHolder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, item.getUid());
                ((BaseActivity) context).openActivity(OtherCustomeMainActivity.class, bundle);
            }
        });
        return convertView;
    }

    public void setData(List<ShareFriendsInfo> datas) {
        this.data = datas;
        notifyDataSetChanged();
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_share_friends_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        RoundedImageView ivIcon;
        @Bind(R.id.tv_coach)
        TextView tvCoach;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.ll_context_ui)
        LinearLayout llContextUi;
        @Bind(R.id.ch_select)
        CheckBox chSelect;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
