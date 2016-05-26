package com.smartfit.adpters;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.hyphenate.util.DensityUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.DynamicDetailActivity;
import com.smartfit.beans.DynamicInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.DeviceUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/3/13.
 */
public class DynamicAdapter extends BaseAdapter {

    private List<DynamicInfo> datas;
    private Context context;

    private LinearLayout.LayoutParams params;

    private int selectPostion = 0 ;


    public DynamicAdapter(Context context, List<DynamicInfo> datas) {
        this.context = context;
        this.datas = datas;
        params = new LinearLayout.LayoutParams(DeviceUtil.getWidth(context) / 2, DeviceUtil.getWidth(context) / 2);
        params.gravity = Gravity.LEFT;
        params.leftMargin = DensityUtil.dip2px(context,16);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_dynamic_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final DynamicInfo item = datas.get(position);
        if (!TextUtils.isEmpty(item.getNickName())) {
            viewHolder.tvName.setText(item.getNickName());
        }

        viewHolder.ivPhoto.setLayoutParams(params);
        if (TextUtils.isEmpty(item.getImgUrl())) {
            viewHolder.ivPhoto.setVisibility(View.GONE);
        } else {
            ImageLoader.getInstance().displayImage(item.getImgUrl(), viewHolder.ivPhoto, Options.getListOptions());
            viewHolder.ivPhoto.setVisibility(View.VISIBLE);
        }
        ImageLoader.getInstance().displayImage(item.getUserPicUrl(), viewHolder.ivIcon, Options.getListOptions());

        if (!TextUtils.isEmpty(item.getContent())) {
            viewHolder.tvDynamicTittle.setText(item.getContent());
        }
        if (!TextUtils.isEmpty(item.getCommentCount())) {
            viewHolder.tvMessage.setText(item.getCommentCount());
        }

        if (!TextUtils.isEmpty(item.getGood())) {
            viewHolder.tvPraise.setText(item.getGood());
        }

        if (!TextUtils.isEmpty(item.getAddTime())){
            viewHolder.tvDynamicDate.setText(DateUtils.getData(item.getAddTime()));
        }

        if (TextUtils.isEmpty(item.getHadGood())) {
            if (item.getHadGood().equals("1")){
                //TODO  点赞图标
//                viewHolder.tvPraise
            }else{

            }
        }
     /*   viewHolder.tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, item.getTopicId());
                ((BaseActivity) context).openActivity(ClassMoreCommentsActivity.class);
            }
        });*/

        viewHolder.tvPraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportGood(item.getId(), position);
            }
        });

        viewHolder.tvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putString(Constants.PASS_STRING, item.getTopicId());
                selectPostion = position;
//                ((BaseActivity) context).openActivity(DynamicMakeDiscussActivity.class, bundle);

                Bundle bundle =  new Bundle();
                bundle.putParcelable(Constants.PASS_OBJECT,datas.get(position));
                ((BaseActivity)context).openActivity(DynamicDetailActivity.class, bundle);
            }
        });

        viewHolder.llDynamicUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle =  new Bundle();
                bundle.putParcelable(Constants.PASS_OBJECT,datas.get(position));
                ((BaseActivity)context).openActivity(DynamicDetailActivity.class, bundle);
            }
        });

        return convertView;
    }

    public  int getSelectPostion(){
        return selectPostion;
    }
    private void supportGood(String id, final int position) {
        Map<String, String> maps = new HashMap<>();
        maps.put("dynamicId", id);
        PostRequest request = new PostRequest(Constants.DYNAMIC_GOOD, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                datas.get(position).setGood(String.valueOf(Integer.parseInt(datas.get(position).getGood()) + 1));
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

    public void setData(List<DynamicInfo> datas) {
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
        ImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_dynamic_tittle)
        TextView tvDynamicTittle;
        @Bind(R.id.iv_photo)
        ImageView ivPhoto;
        @Bind(R.id.tv_share_friends)
        TextView tvShareFriends;
        @Bind(R.id.tv_message)
        TextView tvMessage;
        @Bind(R.id.tv_praise)
        TextView tvPraise;
        @Bind(R.id.ll_dynamic_ui)
        LinearLayout llDynamicUi;
        @Bind(R.id.tv_dynamic_date)
        TextView tvDynamicDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
