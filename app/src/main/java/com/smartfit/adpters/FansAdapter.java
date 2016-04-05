package com.smartfit.adpters;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.OtherCustomeMainActivity;
import com.smartfit.beans.AttentionBean;
import com.smartfit.commons.Constants;
import com.smartfit.utils.Options;
import com.smartfit.views.SelectableRoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/14.
 */
public class FansAdapter extends BaseAdapter {
    private List<AttentionBean> data;

    private Context context;

    public FansAdapter(Context context, List<AttentionBean> datas) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_fans_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }

        final AttentionBean item = data.get(position);
        if (!TextUtils.isEmpty(item.getNickName())) {
            viewHolder.tvCoach.setText(item.getNickName());
        }
        ImageLoader.getInstance().displayImage(item.getUserPicUrl(), viewHolder.ivIcon, Options.getListOptions());
        if (!TextUtils.isEmpty(item.getSex())) {
            if (item.getSex().equals("0")) {
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

    public void setData(List<AttentionBean> datas) {
        this.data = datas;
        notifyDataSetChanged();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_fans_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        SelectableRoundedImageView ivIcon;
        @Bind(R.id.tv_coach)
        TextView tvCoach;
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.ll_context_ui)
        LinearLayout llContextUi;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
