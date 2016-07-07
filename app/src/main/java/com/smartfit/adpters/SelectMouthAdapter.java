package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.beans.SelectMouth;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 作者： 邓言诚 创建于： 2016/7/5 14:06.
 */
public class SelectMouthAdapter extends BaseAdapter {


    private List<SelectMouth> selectMouths;

    private Context context;

    public SelectMouthAdapter(Context context, List<SelectMouth> selectMouthList) {
        this.selectMouths = selectMouthList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return selectMouths.size();
    }

    @Override
    public Object getItem(int position) {
        return selectMouths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_selectd_mouth_num, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        SelectMouth selectMouth = selectMouths.get(position);
        viewHolder.tvNun.setText(String.format("%d  个月", selectMouth.getNum()));
        if (selectMouth.isSelect()) {
            viewHolder.ivSelect.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivSelect.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public void setData(List<SelectMouth> selectMouthList) {
        this.selectMouths = selectMouthList;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        @Bind(R.id.tv_nun)
        TextView tvNun;
        @Bind(R.id.iv_select)
        ImageView ivSelect;
        @Bind(R.id.rl_select_mouth_ui)
        RelativeLayout rlSelectMouthUi;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
