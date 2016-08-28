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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.JsonObject;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.ConfirmOrderCourseActivity;
import com.smartfit.beans.MyRankClass;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/3/12.
 * 我的排入课程列表适配器
 */
public class MyRankClassStatusAdapter extends BaseAdapter {
    private Context context;
    private List<MyRankClass> datas;
    private EventBus eventBus;

    public MyRankClassStatusAdapter(Context context, List<MyRankClass> datas) {
        this.context = context;
        this.datas = datas;
        eventBus = EventBus.getDefault();

    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_rank_classes_status_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final MyRankClass item = datas.get(position);

        if (!TextUtils.isEmpty(item.getCourseName())) {
            viewHolder.tvClassName.setText(item.getCourseName());
        }
        if (!TextUtils.isEmpty(item.getStartTime()) && !TextUtils.isEmpty(item.getEndTime())) {
            viewHolder.tvTime.setText(DateUtils.getData(item.getStartTime()) + "~" + DateUtils.getDataTime(item.getEndTime()));
        }

        ImageLoader.getInstance().displayImage(item.getStartUserPicUrl(), viewHolder.ivIcon, Options.getHeaderOptions());
        if (!TextUtils.isEmpty(item.getStartUserName())) {
            viewHolder.tvName.setText(item.getStartUserName());
        }

        if (!TextUtils.isEmpty(item.getCourseName())) {
            viewHolder.tvClassName.setText(item.getCourseName());
        }
        if (!TextUtils.isEmpty(item.getCourseDetail())) {
            viewHolder.tvClassInfo.setText(item.getCourseDetail());
        }

        if (!TextUtils.isEmpty(item.getCoursePrice())) {
            viewHolder.tvMoney.setText(item.getCoursePrice());
        }


        /**
         * （1我报名但未付款，
         * 2已经付款教练未接单，
         * 3已经付款教练接单（即正常），
         * 4课程已经结束
         * 5我退出该课程，
         * 6该课程被取消了
         * ，7课程已结束未评论8已评论）
         * 9正在排队
         * 10 已经排到
         */
        if (!TextUtils.isEmpty(item.getStatus())) {
            if (item.getStatus().equals("1")) {
                viewHolder.tvStatus.setText("报名但未付款");
                viewHolder.llHandleFunciton.setVisibility(View.VISIBLE);
                viewHolder.tvPayMoney.setVisibility(View.VISIBLE);
                viewHolder.tvCancleRank.setVisibility(View.GONE);
            } else if (item.getStatus().equals("2")) {
                viewHolder.tvStatus.setText("等待接单");
                viewHolder.llHandleFunciton.setVisibility(View.VISIBLE);
                viewHolder.tvPayMoney.setVisibility(View.GONE);
                viewHolder.tvCancleRank.setVisibility(View.VISIBLE);
            } else if (item.getStatus().equals("3")) {
                viewHolder.tvStatus.setText("进行中");
                viewHolder.llHandleFunciton.setVisibility(View.GONE);
            } else if (item.getStatus().equals("4")) {
                viewHolder.tvStatus.setText("已结束");
                viewHolder.llHandleFunciton.setVisibility(View.GONE);

            } else if (item.getStatus().equals("5")) {
                viewHolder.tvStatus.setText("已退出该课程");
                viewHolder.llHandleFunciton.setVisibility(View.GONE);
            } else if (item.getStatus().equals("6")) {
                viewHolder.tvStatus.setText("课程已取消");
                viewHolder.llHandleFunciton.setVisibility(View.GONE);
            } else if (item.getStatus().equals("7")) {
                viewHolder.tvStatus.setText("课程已结束");
                viewHolder.llHandleFunciton.setVisibility(View.GONE);
            } else if (item.getStatus().equals("8")) {
                viewHolder.tvStatus.setText("已评价");
                viewHolder.llHandleFunciton.setVisibility(View.GONE);
            } else if (item.getStatus().equals("9")) {
                viewHolder.tvStatus.setText("正在排队");
                viewHolder.llHandleFunciton.setVisibility(View.VISIBLE);
                viewHolder.tvPayMoney.setVisibility(View.GONE);
                viewHolder.tvCancleRank.setVisibility(View.VISIBLE);
            } else if (item.getStatus().equals("10")) {
                viewHolder.tvStatus.setText("已经排到");
                viewHolder.llHandleFunciton.setVisibility(View.VISIBLE);
                viewHolder.tvPayMoney.setVisibility(View.GONE);
                viewHolder.tvCancleRank.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.tvRankNum.setText(String.format("当前排队序号：%s", item.getBookNumber()));


        viewHolder.tvPayMoney.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getCourseType().equals("0")){
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.PAGE_INDEX, 1);//  1   2  小班课 和团操课  一样处理
                    bundle.putString(Constants.COURSE_ORDER_CODE, "");
                    bundle.putString(Constants.COURSE_ID, item.getCourseClassId());
                    bundle.putString(Constants.COURSE_MONEY, item.getCoursePrice());
                    bundle.putString(Constants.COURSE_TYPE, item.getCourseType());
                    ((BaseActivity)context).openActivity(ConfirmOrderCourseActivity.class, bundle);
                }
            }
        });


        viewHolder.tvPayMoney.setVisibility(View.GONE);


        viewHolder.tvCancleRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialogStyleTwoDel(item.getId(), "确定要取消排队吗？");
            }
        });


        return convertView;
    }

    private void cancle(String id) {
        Map<String, String> maps = new HashMap<>();
        maps.put("courseId", id);
        PostRequest request = new PostRequest(Constants.USER_CANCELBOOK, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                ((BaseActivity) context).mSVProgressHUD.showSuccessWithStatus("已取消排队", SVProgressHUD.SVProgressHUDMaskType.Clear);
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

    public void setData(List<MyRankClass> datas) {
        this.datas = datas;
    }

    private void NormalDialogStyleTwoDel(final String id, String tips) {

        final NormalDialog dialog = new NormalDialog(context);
        dialog.content(tips)//
                .style(NormalDialog.STYLE_TWO)//
                .titleTextSize(23)//
                //.showAnim(mBasIn)//
                //.dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        cancle(id);
                    }
                });

    }

    static class ViewHolder {
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_status)
        TextView tvStatus;
        @Bind(R.id.iv_icon)
        RoundedImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_class_name)
        TextView tvClassName;
        @Bind(R.id.tv_class_info)
        TextView tvClassInfo;
        @Bind(R.id.tv_rank_num)
        TextView tvRankNum;
        @Bind(R.id.tv_money)
        TextView tvMoney;
        @Bind(R.id.tv_pay_money)
        TextView tvPayMoney;
        @Bind(R.id.tv_cancle_rank)
        TextView tvCancleRank;
        @Bind(R.id.ll_handle_funciton)
        LinearLayout llHandleFunciton;
        @Bind(R.id.ll_cours_ui)
        LinearLayout llCoursUi;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
