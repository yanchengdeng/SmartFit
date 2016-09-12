package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.beans.ClassCommend;
import com.smartfit.utils.Options;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 教练评价列表  item
 * Created by Administrator on 2016/3/8.
 */
public class CoachAppraiseAllAdapter extends BaseAdapter {
    private Context context;
    private List<ClassCommend> datas;
    RelativeLayout.LayoutParams params;

    public CoachAppraiseAllAdapter(Context context, List<ClassCommend> datas) {
        this.context = context;
        this.datas = datas;
        params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 24);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_VERTICAL);
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
        ViewHolder viewhold;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_coach_appraise_all_item, null);
            viewhold = new ViewHolder(convertView);
            convertView.setTag(viewhold);
        } else {
            viewhold = (ViewHolder) convertView.getTag();
        }

        viewhold.ratingBar.setLayoutParams(params);
        ClassCommend item = datas.get(position);
        if (!TextUtils.isEmpty(item.getCommentContent())) {
            viewhold.tvJoin.setText(item.getCommentContent());
        }
        viewhold.tvJoin.setSingleLine();
        if (!TextUtils.isEmpty(item.getCommentStar())) {
            viewhold.ratingBar.setRating(Float.parseFloat(item.getCommentStar()));
        }

        ImageLoader.getInstance().displayImage(item.getUserPicUrl(), viewhold.ivHeader, Options.getHeaderOptions());
        return convertView;
    }

    public void setData(List<ClassCommend> data) {
        this.datas = data;
        notifyDataSetChanged();
    }


    static class ViewHolder {
        @Bind(R.id.iv_header)
        CircleImageView ivHeader;
        @Bind(R.id.tv_join)
        TextView tvJoin;
        @Bind(R.id.ratingBar)
        RatingBar ratingBar;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}