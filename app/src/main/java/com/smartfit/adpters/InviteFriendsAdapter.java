package com.smartfit.adpters;/**
 * 作者：dengyancheng on 16/5/8 11:02
 * 邮箱：yanchengdeng@gmail.com
 */

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.beans.AttentionBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者：dengyancheng on 16/5/8 11:02
 * 邮箱：yanchengdeng@gmail.com
 */
public class InviteFriendsAdapter extends BaseAdapter {

    private List<AttentionBean> datas;
    private Context contxt;

    public InviteFriendsAdapter(Context context, List<AttentionBean> beans) {
        this.datas = beans;
        this.contxt = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(contxt).inflate(R.layout.adapter_invite_friends_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AttentionBean item = datas.get(position);
        viewHolder.chSelect.setChecked(item.isCheck());
        if (!TextUtils.isEmpty(item.getNickName())) {
            viewHolder.tvCoach.setText(item.getNickName());
        }

        return convertView;
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_invite_friends_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.ch_select)
        CheckBox chSelect;
        @Bind(R.id.tv_coach)
        TextView tvCoach;
        @Bind(R.id.ll_friends_ui)
        LinearLayout llFriendsUi;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
