package com.smartfit.adpters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.views.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/3/13.
 */
public class DynamicAdapter extends BaseAdapter {

    private List<String> datas;
    private Context context;

    List<String> photo;


    public DynamicAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
        photo = new ArrayList<>();
        photo.add("");
        photo.add("");

    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_dynamic_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }

    public void setData(List<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_dynamic_view.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        SelectableRoundedImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_dynamic_tittle)
        TextView tvDynamicTittle;
        @Bind(R.id.gv_photos)
        ImageView gvPhotos;
        @Bind(R.id.tv_share_friends)
        TextView tvShareFriends;
        @Bind(R.id.tv_message)
        TextView tvMessage;
        @Bind(R.id.tv_praise)
        TextView tvPraise;
        @Bind(R.id.ll_dynamic_ui)
        LinearLayout llDynamicUi;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
