package com.smartfit.adpters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
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
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.MessageEvent.CancleClass;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.beans.MyAddClass;
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
 * 课程状态
 */
public class MyClassOrderStatusAdapter extends BaseAdapter {
    private Context context;
    private List<MyAddClass> datas;
    private boolean isHandleShow = true;
    private EventBus eventBus;

    public MyClassOrderStatusAdapter(Context context, List<MyAddClass> datas, boolean isHandleShow) {
        this.context = context;
        this.datas = datas;
        this.isHandleShow = isHandleShow;
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_my_classes_status_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final MyAddClass item = datas.get(position);

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
        //有氧课  无教练  可去掉联系教练功能
        if (item.getCourseType().equals("3")) {
            viewHolder.tvContactCoach.setVisibility(View.GONE);
        } else {
            viewHolder.tvContactCoach.setVisibility(View.VISIBLE);
        }

        /**
         * （1我报名但未付款，
         * 2已经付款教练未接单，
         * 3已经付款教练接单（即正常），
         * 4课程已经结束
         * 5我退出该课程，
         * 6该课程被取消了
         * 7课程已结束未评论
         * 8已评论）
         * 9 正在排队
         * 10 已排到对
         * 11 迟到
         * 12 旷课
         */
        if (!TextUtils.isEmpty(item.getStatus())) {
            if (item.getStatus().equals("1")) {
                viewHolder.tvStatus.setText("报名但未付款");
                viewHolder.llHandleFunciton.setVisibility(View.VISIBLE);
            } else if (item.getStatus().equals("2")) {
                viewHolder.tvStatus.setText("等待接单");
                viewHolder.llHandleFunciton.setVisibility(View.VISIBLE);
            } else if (item.getStatus().equals("3")) {
                viewHolder.tvStatus.setText("已支付");
                viewHolder.llHandleFunciton.setVisibility(View.VISIBLE);
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
                viewHolder.llHandleFunciton.setVisibility(View.GONE);
            } else if (item.getStatus().equals("10")) {
                viewHolder.tvStatus.setText("已排到队");
                viewHolder.llHandleFunciton.setVisibility(View.GONE);
            } else if (item.getStatus().equals("11")) {
                viewHolder.tvStatus.setText("已迟到");
                viewHolder.llHandleFunciton.setVisibility(View.GONE);
            } else if (item.getStatus().equals("12")) {
                viewHolder.tvStatus.setText("已旷课");
                viewHolder.llHandleFunciton.setVisibility(View.GONE);
            }
        }


        viewHolder.tvContactCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialogStyleTwo(item.getCoachPhone());
            }
        });


        viewHolder.tvCancleClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(item.getStartTime())) {
                    if (Long.parseLong(item.getStartTime()) - (System.currentTimeMillis() / 1000) < 60 * 60 * 2) {
                        NormalDialogStyleTwoDel(item.getId(), "课前2小时内取消课程将全额扣除课时费哦！确定要取消课程？");
                    } else {
                        NormalDialogStyleTwoDel(item.getId(), "确认取消该课程吗？");
                    }
                }


            }
        });


        return convertView;
    }

    private void cancle(String id) {
        Map<String, String> maps = new HashMap<>();
        maps.put("courseId", id);
        PostRequest request = new PostRequest(Constants.USER_CANCELCOURSELIST, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                ((BaseActivity) context).mSVProgressHUD.showSuccessWithStatus("已取消", SVProgressHUD.SVProgressHUDMaskType.Clear);
                eventBus.post(new CancleClass());
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

    public void setData(List<MyAddClass> datas) {
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


    private void NormalDialogStyleTwo(final String phone) {
        if (TextUtils.isEmpty(phone)) {
            ((BaseActivity) context).mSVProgressHUD.showInfoWithStatus("空号", SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }
        final NormalDialog dialog = new NormalDialog(context);
        dialog.content("确定要拨打教练电话吗？")//
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
                        try {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
                                    phone));
                            ((BaseActivity) context).startActivity(intent);
                        } catch (Exception E) {
                            ((BaseActivity) context).mSVProgressHUD.showInfoWithStatus("您的设备没有打电话功能哦~", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        }
                    }
                });

    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_my_classes_status_view.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_status)
        TextView tvStatus;
        @Bind(R.id.iv_icon)
        ImageView ivIcon;
        @Bind(R.id.tv_name)
        TextView tvName;
        @Bind(R.id.tv_class_name)
        TextView tvClassName;
        @Bind(R.id.tv_class_info)
        TextView tvClassInfo;
        @Bind(R.id.tv_money)
        TextView tvMoney;
        @Bind(R.id.tv_contact_coach)
        TextView tvContactCoach;
        @Bind(R.id.tv_cancle_class)
        TextView tvCancleClass;
        @Bind(R.id.ll_handle_funciton)
        LinearLayout llHandleFunciton;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
