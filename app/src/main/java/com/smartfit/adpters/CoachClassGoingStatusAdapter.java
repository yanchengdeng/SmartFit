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
import com.smartfit.MessageEvent.UpdateCoachClass;
import com.smartfit.R;
import com.smartfit.activities.ArerobicDetailActivity;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.FindSubstitueActivity;
import com.smartfit.activities.GroupClassDetailActivity;
import com.smartfit.activities.MembersListActivity;
import com.smartfit.activities.PrivateClassByMessageActivity;
import com.smartfit.beans.MyAddClass;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/3/12.
 * 教练进行教学的课程
 */
public class CoachClassGoingStatusAdapter extends BaseAdapter {
    private Context context;
    private List<MyAddClass> datas;
    private EventBus eventBus;

    public CoachClassGoingStatusAdapter(Context context, List<MyAddClass> datas, boolean isHandleShow) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_coach_going_class_view, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        final MyAddClass item = datas.get(position);
        viewHolder.tvTime.setText(DateUtils.getData(item.getStartTime()) + "~" + DateUtils.getDataTime(item.getEndTime()));
        if (!TextUtils.isEmpty(item.getCourseName())) {
            viewHolder.tvClassName.setText(item.getCourseName());
        }

        if (!TextUtils.isEmpty(item.getCourseType())) {
            if (item.getCourseType().equals("0")) {
                viewHolder.tvClassType.setText("团操课");
            } else if (item.getCourseType().equals("1")) {
                viewHolder.tvClassType.setText("小班课");
            } else if (item.getCourseType().equals("2")) {
                viewHolder.tvClassType.setText("私教课");
            } else {
                viewHolder.tvClassType.setText("有氧器械");
            }
        }

        if (!TextUtils.isEmpty(item.getCourseDetail())) {
            viewHolder.tvFitnessCenter.setText(item.getCourseDetail());
        }

        if (!TextUtils.isEmpty(item.getSignCount())) {
            viewHolder.tvHaveRegiseter.setText(item.getSignCount() + "人已报名");
        }

        if (!TextUtils.isEmpty(item.getMaxPersonCount()) && !TextUtils.isEmpty(item.getSignCount())) {
            viewHolder.tvEmptyRegister.setText((Integer.parseInt(item.getMaxPersonCount()) - Integer.parseInt(item.getSignCount())) + "");
        }

        if (TextUtils.isEmpty(item.getStartUserName())) {
            viewHolder.tvOperate.setText("暂无");
        } else {
            viewHolder.tvOperate.setText(item.getStartUserName());
        }

        /**
         * （1我报名但未付款，
         * 2已经付款教练未接单，
         * 3已经付款教练接单（即正常），
         * 4课程已经结束
         * 5我退出该课程，
         * 6该课程被取消了
         * ，7课程已结束未评论8已评论）
         */
        if (!TextUtils.isEmpty(item.getStatus())) {
            if (item.getStatus().equals("1")) {
                viewHolder.tvOperate.setText("报名但未付款");
            } else if (item.getStatus().equals("2")) {
                viewHolder.tvOperate.setText("等待接单");
            } else if (item.getStatus().equals("3")) {
                viewHolder.tvOperate.setText("进行中");
            } else if (item.getStatus().equals("4")) {
                viewHolder.tvOperate.setText("已结束");
            } else if (item.getStatus().equals("5")) {
                viewHolder.tvOperate.setText("已退出该课程");
            } else if (item.getStatus().equals("6")) {
                viewHolder.tvOperate.setText("课程已取消");
            } else if (item.getStatus().equals("7")) {
                viewHolder.tvOperate.setText("课程已结束");
            } else if (item.getStatus().equals("8")) {
                viewHolder.tvOperate.setText("已评价");
            }
        }


        //找人代课
        viewHolder.tvSubstitue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PASS_OBJECT, item);
                ((BaseActivity) context).openActivity(FindSubstitueActivity.class, bundle);
            }
        });
        //取消课程
        viewHolder.tvCancleClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialogStyleTwoDel(item.getId());
            }
        });

        //成员
        viewHolder.tvMemberList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, item.getId());
                ((BaseActivity) context).openActivity(MembersListActivity.class, bundle);
            }
        });

        if (!TextUtils.isEmpty(item.getCoursePrice())) {
            viewHolder.tvMoney.setText("￥" + item.getCoursePrice());
        }

        viewHolder.llCoachGoingUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openClass(item);
            }
        });


        return convertView;
    }


    private void openClass(MyAddClass item) {
        if (!TextUtils.isEmpty(item.getCourseType())) {
            //0  团操 1  小班   2  私教  3  有氧
            if (item.getCourseType().equals("0")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, item.getId());
                bundle.putString(Constants.COURSE_TYPE, "0");
                bundle.putString("start", item.getStartTime());
                bundle.putString("end", item.getEndTime());
                ((BaseActivity) context).openActivity(GroupClassDetailActivity.class, bundle);

            } else if (item.getCourseType().equals("1")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, item.getId());
                bundle.putString(Constants.COURSE_TYPE, "1");
                bundle.putString("start", item.getStartTime());
                bundle.putString("end", item.getEndTime());
                ((BaseActivity) context).openActivity(GroupClassDetailActivity.class, bundle);

            } else if (item.getCourseType().equals("2")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, item.getId());
                ((BaseActivity) context).openActivity(PrivateClassByMessageActivity.class, bundle);

            } else if (item.getCourseType().equals("3")) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, item.getId());
                bundle.putString("start", item.getStartTime());
                bundle.putString("end", item.getEndTime());
                ((BaseActivity) context).openActivity(ArerobicDetailActivity.class, bundle);
            }
        }
    }

    private void NormalDialogStyleTwoDel(final String id) {

        final NormalDialog dialog = new NormalDialog(context);
        dialog.content("确认取消该课程吗？")//
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


    private void cancle(String id) {
        Map<String, String> maps = new HashMap<>();
        maps.put("courseId", id);
        PostRequest request = new PostRequest(Constants.USER_CANCELCOURSELIST, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                ((BaseActivity) context).mSVProgressHUD.showSuccessWithStatus("已取消", SVProgressHUD.SVProgressHUDMaskType.Clear);
                eventBus.post(new UpdateCoachClass());
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

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'adapter_coach_going_class_view.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    static class ViewHolder {
        @Bind(R.id.tv_time)
        TextView tvTime;
        @Bind(R.id.tv_operate)
        TextView tvOperate;
        @Bind(R.id.tv_class_name)
        TextView tvClassName;
        @Bind(R.id.tv_class_type)
        TextView tvClassType;
        @Bind(R.id.tv_fitness_center)
        TextView tvFitnessCenter;
        @Bind(R.id.tv_have_regiseter)
        TextView tvHaveRegiseter;
        @Bind(R.id.tv_empty_register)
        TextView tvEmptyRegister;
        @Bind(R.id.tv_empty_register_tips)
        TextView tvEmptyRegisterTips;
        @Bind(R.id.tv_money)
        TextView tvMoney;
        @Bind(R.id.tv_substitue)
        TextView tvSubstitue;
        @Bind(R.id.tv_cancle_class)
        TextView tvCancleClass;
        @Bind(R.id.tv_member_list)
        TextView tvMemberList;
        @Bind(R.id.ll_handle_funciton)
        LinearLayout llHandleFunciton;
        @Bind(R.id.ll_coach_going_class_ui)
        LinearLayout llCoachGoingUI;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
