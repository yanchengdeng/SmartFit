package com.smartfit.activities;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.google.gson.JsonObject;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.smartfit.MessageEvent.UpdateAreoClassDetail;
import com.smartfit.R;
import com.smartfit.beans.AreaDetailInfo;
import com.smartfit.beans.CashTickeInfo;
import com.smartfit.beans.ClassInfoDetail;
import com.smartfit.beans.CourseNotition;
import com.smartfit.beans.LingyunListInfo;
import com.smartfit.beans.LinyuCourseInfo;
import com.smartfit.beans.LinyuRecord;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.DeviceUtil;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.utils.Util;
import com.smartfit.views.ShareBottomDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 有氧器械   详情页
 */
public class AerobicAppratusDetailActivity extends BaseActivity {


    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.roll_view_pager)
    RollPagerView rollViewPager;
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
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_room)
    TextView tvRoom;
    @Bind(R.id.tv_distance)
    TextView tvDistance;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.btn_order)
    Button btnOrder;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.iv_send_red)
    ImageView ivSendRed;
    @Bind(R.id.ll_price)
    LinearLayout llPrice;
    @Bind(R.id.tv_waiting_accept)
    TextView tvWaitingAccept;
    @Bind(R.id.tv_warning_tips)
    TextView tvWarningTips;
    @Bind(R.id.tv_rank_num)
    TextView tvRankNum;
    @Bind(R.id.tv_rank_tips)
    TextView tvRankTips;
    @Bind(R.id.btn_add_rank)
    TextView btnAddRank;
    @Bind(R.id.rl_rank)
    RelativeLayout rlRank;
    private String classroomid;
    private String startTime, endTime, openTime;


    private EventBus eventBus;
    private String cashEventId;
    private String courseId;
    private String cashEventName;

    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aerobic_appratus_detail);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        ButterKnife.bind(this);
        initView();
        addLisener();

    }

    @Subscribe
    public void onEvent(Object event) {
        if (event instanceof UpdateAreoClassDetail) {
            btnOrder.setVisibility(View.GONE);
           /* llScanBar.setVisibility(View.VISIBLE);
            llViewScanCode.setVisibility(View.GONE);
            tvScanCodeInfo.setVisibility(View.VISIBLE);
            tvScanCodeInfo.setText(String.format("课程二维码在开课前两个小时才会生效，您可以将如下链接保存：%1$s/sys/upload/qrCodeImg?courseId=%2$s&uid=%3$s", new Object[]{Constants.Net.URL, detail.getId(), SharedPreferencesUtils.getInstance().getString(Constants.UID, "")}));
            tvSaveToPhone.setText(getString(R.string.copy_link));*/
            if (!TextUtils.isEmpty(((UpdateAreoClassDetail) event).getId())) {
                courseId = ((UpdateAreoClassDetail) event).getId();
                getClassInfo(((UpdateAreoClassDetail) event).getId());
                showCashTicketButton(((UpdateAreoClassDetail) event).getId());
            }
        }

    /* Do something */
    }

    /**
     * 获取现金券id
     * 0:团操课
     * <p/>
     * 1:小班课
     * <p/>
     * 2:私教课
     * <p/>
     * 3:器械课
     * <p/>
     * 4:月卡
     *
     * @param id
     */


    private void showCashTicketButton(String id) {
        Map<String, String> data = new HashMap<>();
        data.put("orgType", "3");
        data.put("orgId", id);
        PostRequest request = new PostRequest(Constants.EVENT_GETAVAILABLECASHEVENT, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                CashTickeInfo cashTickeInfo = JsonUtils.objectFromJson(response, CashTickeInfo.class);
                if (cashTickeInfo != null && !TextUtils.isEmpty(cashTickeInfo.getId())) {
                    cashEventId = cashTickeInfo.getId();
                    cashEventName = cashTickeInfo.getCashEventName();
                    ivSendRed.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(AerobicAppratusDetailActivity.this);
        mQueue.add(request);
    }

    /**
     * 获取现金券内容
     *
     * @param shareId
     */
    private void getShareContent(String shareId) {
        Map<String, String> data = new HashMap<>();
        data.put("shareId", shareId);
        data.put("mobileNo", SharedPreferencesUtils.getInstance().getString(Constants.ACCOUNT, "0"));
        PostRequest request = new PostRequest(Constants.EVENT_GETAVAILABLECASHEVENT, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(AerobicAppratusDetailActivity.this);
        mQueue.add(request);
    }


    private void getClassInfo(String courseId) {

        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> data = new HashMap<>();
        data.put("courseId", courseId);
        data.put("courseType", "3");
        data.put("startTime", startTime);
        data.put("endTime", endTime);
        PostRequest request = new PostRequest(Constants.SEARCH_CLASS_DETAIL, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                ClassInfoDetail detail = JsonUtils.objectFromJson(response.toString(), ClassInfoDetail.class);
                fillOrderSuccess(detail);
                mSVProgressHUD.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(AerobicAppratusDetailActivity.this);
        mQueue.add(request);

    }


    AreaDetailInfo detail;

    private void fillData(AreaDetailInfo detail) {
        this.detail = detail;
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
        if (!TextUtils.isEmpty(detail.getState())) {
            if (Integer.parseInt(detail.getState()) >= 4) {
                tvSaveToPhone.setVisibility(View.GONE);
            }
            if (DateUtils.isQeWorked(detail.getStartTime())) {
                llViewScanCode.setVisibility(View.VISIBLE);
                tvScanCodeInfo.setVisibility(View.GONE);
                tvSaveToPhone.setText(getString(R.string.save_to_phone));
            } else {
                llViewScanCode.setVisibility(View.GONE);
                tvScanCodeInfo.setVisibility(View.VISIBLE);
                tvScanCodeInfo.setText(String.format("课程二维码在开课前两个小时才会生效，您可以将如下链接保存：%1$s/sys/upload/qrCodeImg?courseId=%2$s&uid=%3$s", new Object[]{Constants.Net.URL, detail.getId(), SharedPreferencesUtils.getInstance().getString(Constants.UID, "")}));
                tvSaveToPhone.setText(getString(R.string.copy_link));
            }
        }

        if (!TextUtils.isEmpty(detail.getQrcodeUrl())) {
            llScanBar.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(detail.getQrcodeUrl(), ivScanBar, Options.getListOptions());
            codeBar = detail.getQrcodeUrl();
            countDownTimer.start();
        }


        if (!TextUtils.isEmpty(detail.getVenue().getVenueName())) {
            tvAddress.setText(detail.getVenue().getVenueName());
        }
        if (!TextUtils.isEmpty(detail.getClassroomName())) {
            tvRoom.setText(detail.getClassroomName());
        }

        tvDistance.setText("距离 " + Util.getDistance(detail.getVenue().getLatitude(), detail.getVenue().getLongitude()));

        if (!TextUtils.isEmpty(detail.getVenue().getLastModifyTime())) {
            tvTime.setText(DateUtils.getData(startTime) + "~" + DateUtils.getDataTime(endTime));
        }

        if (!TextUtils.isEmpty(detail.getClassroomPrice())) {
            tvPrice.setText(detail.getClassroomPrice());
        } else {
            detail.getVenue().setOrderPriceSum("免费");
            tvPrice.setText("免费");
        }
        if (null != detail.getClassroomPics() && detail.getClassroomPics().length > 0) {
            rollViewPager.setVisibility(View.VISIBLE);
        } else {
            detail.setClassroomPics(new String[]{""});
            rollViewPager.setVisibility(View.VISIBLE);
        }

        if (System.currentTimeMillis() > Long.parseLong(openTime) * 1000) {
            btnOrder.setVisibility(View.VISIBLE);
            tvWaitingAccept.setVisibility(View.INVISIBLE);
        } else {
            btnOrder.setVisibility(View.GONE);
            tvWaitingAccept.setVisibility(View.VISIBLE);
            tvWaitingAccept.setText(DateUtils.getDayOFWeek(AerobicAppratusDetailActivity.this, DateUtils.getInteger(DateUtils.getDate(openTime), Calendar.DAY_OF_WEEK)) + DateUtils.getData(openTime, " HH:mm") + "开放预约");
        }

        rollViewPager.setPlayDelay(3000);
        rollViewPager.setAnimationDurtion(500);
        rollViewPager.setAdapter(new TestNomalAdapter(detail.getClassroomPics(), detail.getClassroomName()));
        rollViewPager.setHintView(new ColorPointHintView(this, getResources().getColor(R.color.common_header_bg), Color.WHITE));


        // 显示 排队
        if (!TextUtils.isEmpty(detail.getIsFull())) {
            if (detail.getIsFull().equals("1")) {
                //团操课 排队机制
                //首先是未开始的课程都可以去排队  购买
                /**
                 * 0:未开始1:正在进行2:已结束3:已排队 4:已排到
                 */
                if (detail.getCourseStatus().equals("0")) {
                    btnOrder.setVisibility(View.GONE);
                    rlRank.setVisibility(View.VISIBLE);
                    tvRankNum.setText(String.format("预约已满,当前排队人数：%s", detail.getBookTotal()));
                    btnAddRank.setVisibility(View.VISIBLE);
                } else if (detail.getCourseStatus().equals("3")) {
                    btnOrder.setVisibility(View.GONE);
                    rlRank.setVisibility(View.VISIBLE);
                    tvRankNum.setText(String.format("我的排队序号：%s", detail.getBookNumber()));
                    btnAddRank.setVisibility(View.GONE);
                } else if (detail.getCourseStatus().equals("4")) {
                    rlRank.setVisibility(View.GONE);
                } else if (detail.getCourseStatus().equals("1")) {
                    btnOrder.setVisibility(View.GONE);
                    rlRank.setVisibility(View.GONE);
                    tvWaitingAccept.setVisibility(View.VISIBLE);
                    tvWaitingAccept.setText("课程进行中...");
                } else if (detail.getCourseStatus().equals("2")) {
                    btnOrder.setVisibility(View.GONE);
                    rlRank.setVisibility(View.GONE);
                    tvWaitingAccept.setVisibility(View.GONE);
                }
            }
        }


        scrollView.fullScroll(ScrollView.FOCUS_UP);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.setVisibility(View.VISIBLE);
            }
        }, 500);
    }


    private void initView() {
        tvTittle.setText(getString(R.string.aerobic_apparatus));
        tvWarningTips.setText(getString(R.string.areob_class_cancle_class_tips));
        classroomid = getIntent().getStringExtra(Constants.COURSE_ID);
        startTime = getIntent().getStringExtra("start");
        endTime = getIntent().getStringExtra("end");
        openTime = getIntent().getStringExtra("open_time");
        rollViewPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (DeviceUtil.getWidth(this) * 0.75)));
        getClassInfo();
        countDownTimer = new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                LogUtil.w("dyc","倒计时结束");
                ivScanBar.setImageResource(R.mipmap.error_scan);

            }
        };

    }

    private void getClassInfo() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> data = new HashMap<>();
        data.put("classroomId", classroomid);
        data.put("startTime", startTime);
        data.put("endTime", endTime);
        PostRequest request = new PostRequest(Constants.CLASSROOM_GETCLASSROOM, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                AreaDetailInfo detail = JsonUtils.objectFromJson(response.toString(), AreaDetailInfo.class);
                if (detail != null) {
                    fillData(detail);
                }
                mSVProgressHUD.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(AerobicAppratusDetailActivity.this);
        mQueue.add(request);
    }


    String codeBar;
    ClassInfoDetail classInfoDetail;

    private void fillOrderSuccess(ClassInfoDetail detail) {
        this.classInfoDetail = detail;


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
        if (!TextUtils.isEmpty(detail.getOrderStatus())) {
            if (Integer.parseInt(detail.getOrderStatus()) >= 4) {
                tvSaveToPhone.setVisibility(View.GONE);
            }
            if (DateUtils.isQeWorked(detail.getStartTime())) {
                llViewScanCode.setVisibility(View.VISIBLE);
                tvScanCodeInfo.setVisibility(View.GONE);
                tvSaveToPhone.setText(getString(R.string.save_to_phone));
            } else {
                llViewScanCode.setVisibility(View.GONE);
                tvScanCodeInfo.setVisibility(View.VISIBLE);
                tvScanCodeInfo.setText(String.format("课程二维码在开课前两个小时才会生效，您可以将如下链接保存：%1$s/sys/upload/qrCodeImg?courseId=%2$s&uid=%3$s", new Object[]{Constants.Net.URL, courseId, SharedPreferencesUtils.getInstance().getString(Constants.UID, "")}));
                tvSaveToPhone.setText(getString(R.string.copy_link));
            }
        }

        if (!TextUtils.isEmpty(detail.getQrcodeUrl())) {
            llScanBar.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(detail.getQrcodeUrl(), ivScanBar, Options.getListOptions());
            codeBar = detail.getQrcodeUrl();
        }


        if (!TextUtils.isEmpty(detail.getVenueName())) {
            tvAddress.setText(detail.getVenueName());
        }
        if (!TextUtils.isEmpty(detail.getClassroomName())) {
            tvRoom.setText(detail.getClassroomName());
        }

        tvDistance.setText("距离 " + Util.getDistance(detail.getLat(), detail.getLongit()));

        tvTime.setText(DateUtils.getData(startTime) + "~" + DateUtils.getDataTime(endTime));

        if (!TextUtils.isEmpty(detail.getPrice())) {
            tvPrice.setText(detail.getPrice());
        } else {
            detail.setPrice("免费");
            tvPrice.setText("免费");
        }

        if (null != detail.getCoursePics() && detail.getCoursePics().length > 0) {
            rollViewPager.setVisibility(View.VISIBLE);
        } else {
            detail.setCoursePics(new String[]{""});
            rollViewPager.setVisibility(View.VISIBLE);
        }
        rollViewPager.setPlayDelay(3000);
        rollViewPager.setAnimationDurtion(500);
        rollViewPager.setAdapter(new TestNomalAdapter(detail.getCoursePics(), detail.getClassroomName()));
        rollViewPager.setHintView(new ColorPointHintView(this, getResources().getColor(R.color.common_header_bg), Color.WHITE));

        // 显示 排队
        if (!TextUtils.isEmpty(detail.getIsFull())) {
            if (detail.getIsFull().equals("1")) {
                //团操课 排队机制
                //首先是未开始的课程都可以去排队  购买
                /**
                 * 0:未开始1:正在进行2:已结束3:已排队 4:已排到
                 */
                if (detail.getCourseStatus().equals("0")) {
                    rlRank.setVisibility(View.VISIBLE);
                    tvRankNum.setText(String.format("预约已满,当前排队人数：%s", detail.getBookTotal()));
                    btnAddRank.setVisibility(View.VISIBLE);
                } else if (detail.getCourseStatus().equals("3")) {
                    rlRank.setVisibility(View.VISIBLE);
                    tvRankNum.setText(String.format("预约已满,当前排队人数：%s", detail.getBookTotal()));
                    btnAddRank.setVisibility(View.GONE);
                } else if (detail.getCourseStatus().equals("4")) {
                    rlRank.setVisibility(View.GONE);
                } else if (detail.getCourseStatus().equals("1")) {
                    btnOrder.setVisibility(View.GONE);
                    rlRank.setVisibility(View.GONE);
                    tvWaitingAccept.setVisibility(View.VISIBLE);
                    tvWaitingAccept.setText("课程进行中...");
                } else if (detail.getCourseStatus().equals("2")) {
                    btnOrder.setVisibility(View.GONE);
                    rlRank.setVisibility(View.GONE);
                    tvWaitingAccept.setVisibility(View.GONE);
                }
            }
        }
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.setVisibility(View.VISIBLE);
            }
        }, 500);
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getShowerRecord();

            }
        });

        //保存手机
        tvSaveToPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvSaveToPhone.getText().equals(getString(R.string.copy_link))) {
                    Util.copyToClob(tvScanCodeInfo.getText().toString(), AerobicAppratusDetailActivity.this);
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
                            Util.saveImageToPhoto(AerobicAppratusDetailActivity.this, loadedImage);
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


        ivSendRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCashDialog();
            }
        });
        ivScanBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewScanCode();
            }
        });

        //加入排队
        btnAddRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRandLink();
            }




        });
    }

    private void addRandLink() {
        if (detail != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.PAGE_INDEX, 4);
            bundle.putString(Constants.COURSE_ID, detail.getId());
            bundle.putString(Constants.COURSE_MONEY, detail.getClassroomPrice());
            bundle.putString("start_time", startTime);
            bundle.putString("end_time", endTime);
            bundle.putString("classroom", detail.getId());
            bundle.putString(Constants.COURSE_TYPE, "3");
            bundle.putBoolean("add_rank", true);
            bundle.putString(Constants.COURSE_ORDER_CODE, detail.getOrderCode());
            openActivity(ConfirmOrderCourseActivity.class, bundle);
        }
    }

    /**
     * 获取新的二维码
     */
    private void getNewScanCode() {
        Map<String, String> map = new HashMap<>();
        map.put("courseId", courseId);
        PostRequest request = new PostRequest(Constants.CLASSIF_GETQRCODE, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc", response.get("data").getAsString());
                if (!TextUtils.isEmpty(response.toString())) {
                    ImageLoader.getInstance().displayImage(response.get("data").getAsString(), ivScanBar);
                    countDownTimer.cancel();
                    countDownTimer.start();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.w("dyc", error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(AerobicAppratusDetailActivity.this);
        mQueue.add(request);

    }

    private void showCashDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_cash_ticket_content);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        TextView tvTittle = (TextView) dialog.getWindow().findViewById(R.id.tv_tittle);
        dialog.getWindow().findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.getWindow().findViewById(R.id.commit_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                showShareWxDialog();
             /*   Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, cashEventId);
                bundle.putString("course_id", courseId);
                bundle.putString(Constants.TICKET_SHARE_TYPE, "2");
                ArrayList<TicketInfo> ticketInfos = new ArrayList<TicketInfo>();
                TicketInfo ticketInfo = new TicketInfo();
                ticketInfo.setEventTitle(cashEventName);
                ticketInfos.add(ticketInfo);
                bundle.putParcelableArrayList(Constants.PASS_OBJECT, ticketInfos);
                openActivity(WXEntryActivity.class, bundle);*/

            }
        });
    }

    private void showShareWxDialog() {
        ShareBottomDialog dialog = new ShareBottomDialog(AerobicAppratusDetailActivity.this, scrollView, cashEventId, "3", courseId);
        dialog.showAnim(new BounceTopEnter())//
                .show();
    }

    /**
     * 获取淋浴欠费接口
     */
    private void getShowerRecord() {
        PostRequest request = new PostRequest(Constants.TBEVENTRECORD_GETSHOWERRECORD, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc==", response.toString());
                LingyunListInfo lingyunListInfo = JsonUtils.objectFromJson(response, LingyunListInfo.class);
                if (lingyunListInfo != null && lingyunListInfo.getListData() != null && lingyunListInfo.getListData().size() > 0) {
                    createLinyuOrder(lingyunListInfo.getListData());
                } else {
                    if (detail != null) {
                        getCourseNotition(detail);
                    } else {
                        mSVProgressHUD.showInfoWithStatus("课程请求获取失败", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                    }
                }

                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mSVProgressHUD.showErrorWithStatus(error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(AerobicAppratusDetailActivity.this);
        mQueue.add(request);

    }

    /**
     * 获取预约提醒
     *
     * @param detail
     */
    private void getCourseNotition(final AreaDetailInfo detail) {
        Map<String, String> map = new HashMap<>();
        map.put("beginTime", detail.getStartTime());
        map.put("courseType", "3");
        PostRequest request = new PostRequest(Constants.COURSE_GETNOTIFICATION, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                CourseNotition courseNotition = JsonUtils.objectFromJson(response.toString(), CourseNotition.class);
                if (courseNotition != null) {
                    //  课程提醒
                    // 0:忽略不弹窗1:预约协议2:限制消息3 禁止消息
                    if (courseNotition.getType().equals("0")) {
                        goBuyCourseUI();
                    } else {
                        showCourseNotiton(courseNotition);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.w("dyc", error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(AerobicAppratusDetailActivity.this);
        mQueue.add(request);
    }


    private void goBuyCourseUI() {
        if (detail != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(Constants.PAGE_INDEX, 4);
            bundle.putString(Constants.COURSE_ID, detail.getId());
            bundle.putString(Constants.COURSE_MONEY, detail.getClassroomPrice());
            bundle.putString("start_time", startTime);
            bundle.putString("end_time", endTime);
            bundle.putString("classroom", detail.getId());
            bundle.putString(Constants.COURSE_TYPE, "3");
            bundle.putString(Constants.COURSE_ORDER_CODE, detail.getOrderCode());
            openActivity(ConfirmOrderCourseActivity.class, bundle);
        }

    }

    /**
     * 弹出课程提醒对话框
     * 1:预约协议2:限制消息 3:禁止消息
     *
     * @param courseNotition
     */
    private void showCourseNotiton(final CourseNotition courseNotition) {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_course_notition);

        TextView tvTittle = (TextView) dialog.getWindow().findViewById(R.id.tv_tittle);

        TextView tvRightButton = (TextView) dialog.getWindow().findViewById(R.id.commit_action);
        TextView tvLeftButton = (TextView) dialog.getWindow().findViewById(R.id.cancel_action);
        final CheckBox checkBox = (CheckBox) dialog.getWindow().findViewById(R.id.ck_remeber);
        if (courseNotition.getType().equals("1")) {
            tvTittle.setText("器械区预约协议");
            tvRightButton.setText("同意协议，马山预约");
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(false);
        } else if (courseNotition.getType().equals("2")) {
            tvTittle.setText("预约确认");
            tvRightButton.setText("确定预约");
            checkBox.setVisibility(View.GONE);
            checkBox.setChecked(true);
        } else if (courseNotition.getType().equals("3")) {
            tvTittle.setText("限制提示");
            tvLeftButton.setText("知道了");
            tvRightButton.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
            checkBox.setChecked(true);
        }


        TextView tvContent = (TextView) dialog.getWindow().findViewById(R.id.tv_content);
        if (!TextUtils.isEmpty(courseNotition.getContent())) {
            tvContent.setText(courseNotition.getContent());
        }


        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });
        dialog.getWindow().findViewById(R.id.commit_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkBox.isChecked() && checkBox.getVisibility() == View.VISIBLE) {
                    dialog.dismiss();
                    saveHaveReaderProtocol();
                } else if (checkBox.isChecked() && checkBox.getVisibility() == View.GONE) {
                    if (courseNotition.getType().equals("2")) {
                        goBuyCourseUI();
                        dialog.dismiss();
                    } else if (courseNotition.getType().equals("3")) {
                        dialog.dismiss();
                    }
                } else {
                    Toast.makeText(AerobicAppratusDetailActivity.this, getString(R.string.cancel_course_tips), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * 设置已读协议
     */
    private void saveHaveReaderProtocol() {
        Map<String, String> map = new HashMap<>();
        map.put("courseType", "3");
        PostRequest request = new PostRequest(Constants.USER_SAVENOPROTOCOL, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                goBuyCourseUI();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.w("dyc", error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(AerobicAppratusDetailActivity.this);
        mQueue.add(request);
    }

    /**
     * 生成淋浴订单
     *
     * @param listData
     */
    private void createLinyuOrder(final List<LinyuRecord> listData) {
        StringBuilder sbID = new StringBuilder();
        for (LinyuRecord item : listData) {
            sbID.append(item.getRecordId()).append("|");
        }
        Map<String, String> map = new HashMap<>();
        map.put("recordIdStr", sbID.toString());
        PostRequest request = new PostRequest(Constants.ORDER_ORDERSHOWER, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc==", response.toString());
                LinyuCourseInfo linyuCourseInfo = JsonUtils.objectFromJson(response, LinyuCourseInfo.class);
                if (linyuCourseInfo != null) {
                    Util.showLinyuRechagerDialog(AerobicAppratusDetailActivity.this, linyuCourseInfo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.w("dyc", error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(AerobicAppratusDetailActivity.this);
        mQueue.add(request);

    }


    private class TestNomalAdapter extends StaticPagerAdapter {

        private String[] imgs;


        private String courceName;
        private String[] infos = {
                "燃烧吧，脂肪君"

        };

        public TestNomalAdapter(String[] bigUrl, String courceName) {
            imgs = bigUrl;
            this.courceName = courceName;

        }


        @Override
        public View getView(ViewGroup container, int position) {
            View relativeLayout = LayoutInflater.from(getBaseContext()).inflate(R.layout.ad_common_view, null);
            ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.iv_cover_bg);
            TextView textView = (TextView) relativeLayout.findViewById(R.id.tv_tittle);
            ImageLoader.getInstance().displayImage(imgs[position], imageView, Options.getListOptions());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//            textView.setText(infos[0] + "(" + courceName + ")");
            textView.setText("");
            return relativeLayout;
        }


        @Override
        public int getCount() {
            return imgs.length;
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer!=null){
            countDownTimer.cancel();
        }
    }
}
