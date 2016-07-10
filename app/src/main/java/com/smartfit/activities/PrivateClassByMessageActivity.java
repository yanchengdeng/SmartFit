package com.smartfit.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.smartfit.R;
import com.smartfit.adpters.PrivateEducationOrderAdapter;
import com.smartfit.beans.ClassInfoDetail;
import com.smartfit.beans.CoachInfo;
import com.smartfit.beans.PrivateClassOrderInfo;
import com.smartfit.beans.PrivateEducationClass;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.utils.Util;
import com.smartfit.views.MyListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 从课程消息传过来的私教详情
 */
public class PrivateClassByMessageActivity extends BaseActivity {


    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.listView)
    MyListView listView;
    @Bind(R.id.iv_space_icon)
    ImageView ivSpaceIcon;
    @Bind(R.id.tv_space_name)
    TextView tvSpaceName;
    @Bind(R.id.tv_room_name)
    TextView tvRoomName;
    @Bind(R.id.tv_space_info)
    TextView tvSpaceInfo;
    @Bind(R.id.tv_room_time)
    TextView tvRoomTime;
    @Bind(R.id.tv_class_time)
    TextView tvClassTime;
    @Bind(R.id.tv_class_price)
    TextView tvClassPrice;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.tv_waiting_accept)
    TextView tvWaitingAccept;
    @Bind(R.id.btn_order)
    Button btnOrder;
    @Bind(R.id.no_data)
    TextView noData;
    @Bind(R.id.tv_class_name)
    TextView tvClassName;
    @Bind(R.id.iv_scan_bar)
    ImageView ivScanBar;
    @Bind(R.id.ll_view_scan_code)
    LinearLayout llViewScanCode;
    @Bind(R.id.tv_scan_code_info)
    TextView tvScanCodeInfo;
    @Bind(R.id.tv_save_to_phone)
    TextView tvSaveToPhone;
    @Bind(R.id.tv_share_friends)
    TextView tvShareFriends;
    @Bind(R.id.ll_scan_bar)
    LinearLayout llScanBar;
    private String couseId;
    private PrivateEducationOrderAdapter adapter;
    private ArrayList<PrivateEducationClass> privateEducationClasses;
    private String startTime, endTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_class_by_message);
        ButterKnife.bind(this);
        couseId = getIntent().getStringExtra(Constants.PASS_STRING);
        initView();
        getData();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.private_education));
        btnOrder.setVisibility(View.GONE);
    }

    private void getData() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> data = new HashMap<>();
        data.put("courseId", couseId);
        data.put("courseType", "2");
        PostRequest request = new PostRequest(Constants.SEARCH_CLASS_DETAIL, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                ClassInfoDetail detail = JsonUtils.objectFromJson(response.toString(), ClassInfoDetail.class);
                fillData(detail);
                mSVProgressHUD.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                scrollView.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(PrivateClassByMessageActivity.this);
        mQueue.add(request);
    }

    private void addLisener(){
        //保存手机
        tvSaveToPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvSaveToPhone.getText().equals(getString(R.string.copy_link))) {
                    Util.copyToClob(tvScanCodeInfo.getText().toString(), PrivateClassByMessageActivity.this);
                    mSVProgressHUD.showSuccessWithStatus("复制成功", SVProgressHUD.SVProgressHUDMaskType.Clear);
                } else if (!TextUtils.isEmpty(codeBar)) {
                    mSVProgressHUD.showWithStatus(getString(R.string.save_ing), SVProgressHUD.SVProgressHUDMaskType.Clear);
                    ImageLoader.getInstance().loadImage(codeBar, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            mSVProgressHUD.dismiss();
                            mSVProgressHUD.showInfoWithStatus("保存失败", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            mSVProgressHUD.dismiss();
                            Util.saveImageToPhoto(PrivateClassByMessageActivity.this, loadedImage);
                            mSVProgressHUD.showSuccessWithStatus("保存成功", SVProgressHUD.SVProgressHUDMaskType.Clear);

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            mSVProgressHUD.dismiss();
                            mSVProgressHUD.showInfoWithStatus("保存失败", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        }
                    });
                }
            }
        });
    }

    /**
     * 订单状态（
     * 1我报名但未付款，
     * 2已经付款教练未接单，
     * 3已经付款教练接单（即正常），
     * 4课程已经结束
     * 5我退出该课程，
     * 6该课程被取消了，
     * 7课程已结束未评论
     * 8已评论）
     */

    ClassInfoDetail detail;

    private String codeBar;

    private void fillData(ClassInfoDetail detail) {
        this.detail = detail;
        privateEducationClasses = new ArrayList<>();
        if (detail.getCoachList() != null && detail.getCoachList().size() > 0) {
            for (CoachInfo item : detail.getCoachList()) {
                PrivateEducationClass privateEducationClass = new PrivateEducationClass();
                privateEducationClass.setNickName(item.getNickName());
                privateEducationClass.setPrice(item.getPrice());
                privateEducationClass.setSex(item.getSex());
                privateEducationClass.setStars(item.getStars());
                privateEducationClass.setUserPicUrl(item.getUserPicUrl());
                privateEducationClass.setShowPrice(detail.getPrice());
                privateEducationClasses.add(privateEducationClass);
            }
        }
        adapter = new PrivateEducationOrderAdapter(this, privateEducationClasses);
        listView.setAdapter(adapter);
        listView.setBackgroundColor(getResources().getColor(R.color.gray_light));
        startTime = detail.getStartTime();
        endTime = detail.getEndTime();
        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
            tvClassTime.setText(DateUtils.getData(startTime) + " ~ " + DateUtils.getDataTime(endTime));
        }
        ImageLoader.getInstance().displayImage(detail.getVenueUrl(), ivSpaceIcon, Options.getListOptions());
        if (!TextUtils.isEmpty(detail.getLat()) && !TextUtils.isEmpty(detail.getLongit())) {
            tvSpaceInfo.setText(Util.getDistance(detail.getLat(), detail.getLongit()));
        }
        if (!TextUtils.isEmpty(detail.getVenueName())) {
            tvSpaceName.setText(detail.getVenueName());
        }
        initRoom(0, detail);

        if (!TextUtils.isEmpty(detail.getQrcodeUrl())) {
            llScanBar.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(detail.getQrcodeUrl(), ivScanBar, Options.getListOptions());
            codeBar = detail.getQrcodeUrl();
        }
        if (!TextUtils.isEmpty(detail.getOrderStatus())) {

            if (DateUtils.isQeWorked(detail.getStartTime())) {
                llViewScanCode.setVisibility(View.VISIBLE);
                tvScanCodeInfo.setVisibility(View.GONE);
                tvSaveToPhone.setText(getString(R.string.save_to_phone));
            } else {
                llViewScanCode.setVisibility(View.GONE);
                tvScanCodeInfo.setVisibility(View.VISIBLE);
                tvScanCodeInfo.setText(String.format("课程二维码在开课前一个小时才会生效，您可以将如下链接保存：%1$s/sys/upload/qrCodeImg?courseId=%2$s&uid=%3$s", new Object[]{Constants.Net.URL, detail.getCourseId(), SharedPreferencesUtils.getInstance().getString(Constants.UID, "")}));
                tvSaveToPhone.setText(getString(R.string.copy_link));
            }

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.setVisibility(View.VISIBLE);
                noData.setVisibility(View.GONE);
            }
        }, 500);
    }


    private void initRoom(int positon, ClassInfoDetail detail) {


        if (!TextUtils.isEmpty(detail.getClassroomName())) {
            tvRoomName.setText(detail.getClassroomName());
        }

        if (TextUtils.isEmpty(detail.getPrice())) {
            tvRoomTime.setText("免费");
        } else {
            tvRoomTime.setText(detail.getPrice() + "/小时");
        }

        tvRoomTime.setVisibility(View.GONE);
        countPrice(positon, detail);
    }

    /**
     * 计算价格
     *
     * @param positon
     * @param detail
     */
    private void countPrice(int positon, ClassInfoDetail detail) {
        float monney = 0;

        for (PrivateEducationClass item : privateEducationClasses) {
            if (monney < Float.parseFloat(item.getPrice())) {
                monney = Float.parseFloat(item.getPrice());
            }
        }


        if (!TextUtils.isEmpty(detail.getPrice())) {
            monney += Float.parseFloat(detail.getPrice());
        }
        tvClassPrice.setText(String.format("%.2f", monney * Float.parseFloat(DateUtils.getHour(startTime, endTime))));
    }


    @OnClick({R.id.tv_room_time, R.id.btn_order, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_room_time:
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(Constants.PASS_IDLE_CLASS_INFO, idleClassListInfo);
//                openActivity(SelectVenueRoomsActivity.class, bundle);
                break;
            case R.id.btn_order:
                if (detail != null)
//                    orderPrivateClass();
                    break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /**
     * 预约私教课
     */
    private void orderPrivateClass() {
        StringBuilder stringBuilder = new StringBuilder();
        for (PrivateEducationClass item : privateEducationClasses) {
            stringBuilder.append(item.getCoachId()).append("|");
        }


        HashMap<String, String> maps = new HashMap<>();
        maps.put("coachIdList", stringBuilder.toString());
        maps.put("startTime", String.valueOf(DateUtils.getTheDateTimeMillions(startTime)));
        maps.put("endTime", String.valueOf(DateUtils.getTheDateTimeMillions(endTime)));
        maps.put("latitude", detail.getLat());
        maps.put("longitude", detail.getLongit());
        maps.put("classroomId", detail.getClassroomId());


        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        PostRequest request = new PostRequest(Constants.COACH_BESPOKECOACH, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                PrivateClassOrderInfo privateClassOrderInfo = JsonUtils.objectFromJson(response.toString(), PrivateClassOrderInfo.class);
                if (privateClassOrderInfo != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.PAGE_INDEX, 3);
                    bundle.putString(Constants.COURSE_ID, privateClassOrderInfo.getCourseId());
                    bundle.putString(Constants.COURSE_MONEY, tvClassPrice.getText().toString());
                    bundle.putString(Constants.COURSE_TYPE, "2");
                    openActivity(ConfirmOrderCourseActivity.class, bundle);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(PrivateClassByMessageActivity.this);
        mQueue.add(request);


    }


}
