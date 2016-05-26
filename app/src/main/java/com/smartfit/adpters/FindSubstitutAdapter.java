package com.smartfit.adpters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.CoachInfoActivity;
import com.smartfit.beans.SustituteCoach;
import com.smartfit.commons.Constants;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/3/22.
 */
public class FindSubstitutAdapter extends BaseAdapter {

    private List<SustituteCoach> datas;

    private Context context;
    private LinearLayout.LayoutParams params;

    private String courseid;

    public FindSubstitutAdapter(Context context, List<SustituteCoach> datas, String courseid) {
        this.datas = datas;
        this.context = context;
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 24);
        this.courseid = courseid;
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
        final SustituteCoach item = datas.get(position);
        viewHolder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) context).openActivity(CoachInfoActivity.class);
            }
        });

        if (!TextUtils.isEmpty(item.getNickName())) {
            viewHolder.tvName.setText(item.getNickName());
        }

        if (!TextUtils.isEmpty(item.getSex())) {
            if (item.getSex().equals(Constants.SEX_WOMEN)) {
                viewHolder.tvName.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.mipmap.icon_woman), null);
            } else {
                viewHolder.tvName.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.mipmap.icon_man), null);
            }
        }

        if (!TextUtils.isEmpty(item.getStars())) {
            viewHolder.ratingBar.setRating(Float.parseFloat(item.getStars()));
            viewHolder.tvCoachScore.setText(item.getStars());
        }

        viewHolder.btnFindSubstitute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSustituteRequest(item);
            }
        });
        return convertView;
    }

    /**
     * 发出代课邀请
     *
     * @param item
     */
    private void sendSustituteRequest(SustituteCoach item) {
        Map<String, String> maps = new HashMap<>();
        maps.put("courseId", courseid);
        maps.put("coachUid", item.getUid());
        PostRequest request = new PostRequest(Constants.COACH_SUBSTITUTECOACH, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                ((BaseActivity) context).mSVProgressHUD.showSuccessWithStatus(context.getString(R.string.reqeust_have_send), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) context).mSVProgressHUD.showInfoWithStatus(context.getString(R.string.reqeust_have_send), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(context);
        ((BaseActivity) context).mQueue.add(request);


    }

    public void setData(List<SustituteCoach> datas) {
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
        ImageView ivIcon;
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
