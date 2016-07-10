package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.beans.PrivateCalssType;
import com.smartfit.utils.Options;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/4.
 * 私教 --> 私教课程类别---》 数据列表
 */
public class PrivateEducationClassTypeAdapter extends BaseAdapter {
    private Context context;
    private List<PrivateCalssType> datas;
    LinearLayout.LayoutParams params;


    public PrivateEducationClassTypeAdapter(Context context
            , List<PrivateCalssType> datas) {
        this.context = context;
        this.datas = datas;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 25);
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_private_education_type_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        final PrivateCalssType item = datas.get(position);

        if (!TextUtils.isEmpty(item.getClassificationName())) {
            viewHolder.tvType.setText(item.getClassificationName());
        }

        if (!TextUtils.isEmpty(item.getCoachNum())) {
            viewHolder.tvNum.setText(String.format("%s位教练可选",item.getCoachNum()));
        }

        ImageLoader.getInstance().displayImage(item.getClassificationImg(), viewHolder.ivIcon, Options.getListOptions());
        return convertView;
    }


    public void setData(List<PrivateCalssType> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_private_education_type_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        RoundedImageView ivIcon;
        @Bind(R.id.rl_icon_ui)
        RelativeLayout rlIconUi;
        @Bind(R.id.tv_type)
        TextView tvType;
        @Bind(R.id.tv_num)
        TextView tvNum;
        @Bind(R.id.ll_context_ui)
        LinearLayout llContextUi;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
