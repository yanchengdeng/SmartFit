package com.smartfit.adpters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.CoachInfoActivity;
import com.smartfit.views.SelectableRoundedImageView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/22.
 */
public class FindSubstitutAdapter extends BaseAdapter {

    private List<String> datas;

    private Context context;
    private LinearLayout.LayoutParams params;

    public FindSubstitutAdapter(Context context, List<String> datas) {
        this.datas = datas;
        this.context = context;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 24);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_find_substitut_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.ratingBar.setLayoutParams(params);

        viewHolder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity)context).openActivity(CoachInfoActivity.class);
            }
        });
        return convertView;
    }

    public void setData(List<String> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_find_substitut_item.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.iv_icon)
        SelectableRoundedImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.ratingBar)
        RatingBar ratingBar;
        @Bind(R.id.tv_coach_score)
        TextView tvCoachScore;
        @Bind(R.id.ll_context_ui)
        LinearLayout llContextUi;
        @Bind(R.id.btn_find_substitute)
        Button btnFindSubstitute;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
