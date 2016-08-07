package com.smartfit.activities;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.smartfit.MessageEvent.UpdatePrivateClassDetail;
import com.smartfit.R;
import com.smartfit.adpters.PrivateEducationOrderAdapter;
import com.smartfit.beans.CashTickeInfo;
import com.smartfit.beans.ClassInfoDetail;
import com.smartfit.beans.CoachInfo;
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
import com.smartfit.views.ShareBottomDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    @Bind(R.id.iv_send_red)
    ImageView ivSendRed;
    @Bind(R.id.ratingBar_my_class)
    RatingBar ratingBarMyClass;
    @Bind(R.id.tv_my_class_score)
    TextView tvMyClassScore;
    @Bind(R.id.ll_myclass_score)
    LinearLayout llMyclassScore;
    @Bind(R.id.ratingBar_for_coach)
    com.hedgehog.ratingbar.RatingBar ratingBarForCoach;
    @Bind(R.id.et_coach_apprise)
    EditText etCoachApprise;
    @Bind(R.id.btn_commit_comments)
    Button btnCommitComments;
    @Bind(R.id.ll_mack_score)
    LinearLayout llMackScore;
    private String couseId;
    private PrivateEducationOrderAdapter adapter;
    private ArrayList<PrivateEducationClass> privateEducationClasses;
    private String startTime, endTime;
    private String cashEventId, cashEventName;

    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_class_by_message);
        ButterKnife.bind(this);
        couseId = getIntent().getStringExtra(Constants.PASS_STRING);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        initView();
        getData();
        addLisener();
    }

    @Subscribe
    public void onEvent(Object event) {/* Do something */

        if (event instanceof UpdatePrivateClassDetail) {
            btnOrder.setVisibility(View.INVISIBLE);
            tvWaitingAccept.setVisibility(View.VISIBLE);
//            getCourseCode();
//            showCashTicketDialog();
        }
    }

    float starts = 5.0f;

    private void initView() {
        tvTittle.setText(getString(R.string.private_education));
        btnOrder.setVisibility(View.GONE);
        ratingBarForCoach.setStar(starts);
        ratingBarMyClass.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 24));

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
     */
    private void showCashTicketDialog() {
        Map<String, String> data = new HashMap<>();
        data.put("orgType", "2");
        data.put("orgId", couseId);
        PostRequest request = new PostRequest(Constants.EVENT_GETAVAILABLECASHEVENT, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                CashTickeInfo cashTickeInfo = JsonUtils.objectFromJson(response, CashTickeInfo.class);
                if (cashTickeInfo != null && !TextUtils.isEmpty(cashTickeInfo.getId())) {
                    cashEventId = cashTickeInfo.getId();
                    cashEventName = cashTickeInfo.getCashEventName();
                    ivSendRed.setVisibility(View.VISIBLE);
                    showCashDialog();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(PrivateClassByMessageActivity.this);
        mQueue.add(request);
    }

    private void showCashTicketButton() {
        Map<String, String> data = new HashMap<>();
        data.put("orgType", "2");
        data.put("orgId", couseId);
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
        request.headers = NetUtil.getRequestBody(PrivateClassByMessageActivity.this);
        mQueue.add(request);
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

    private void addLisener() {
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
        ivSendRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCashDialog();
            }
        });

        btnCommitComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submintScore(starts, etCoachApprise.getEditableText().toString());
            }
        });

        ratingBarForCoach.setOnRatingChangeListener(new com.hedgehog.ratingbar.RatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChange(float v) {
                ratingBarForCoach.setStar(v);
                starts = v;
            }
        });

    }

    private void submintScore(float rating, String comments) {
        if (detail == null)
            return;
        mSVProgressHUD.showWithStatus(getString(R.string.submit_ing), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> data = new HashMap<>();
        if (detail.getCommentList() != null && detail.getCommentList().size() > 0) {
            data.put("commentId", detail.getCommentList().get(0).getCommentId());
        }

        data.put("topicId", detail.getTopicId());
        data.put("commentStar", String.valueOf(rating));
        data.put("commentContent", comments);
        PostRequest request = new PostRequest(Constants.COMMENT_SAVE, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus("已评分", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                getData();
                showCashTicketDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showInfoWithStatus(getString(R.string.try_later), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(PrivateClassByMessageActivity.this);
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

              /*  Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, cashEventId);
                bundle.putString("course_id", couseId);
                bundle.putString(Constants.TICKET_SHARE_TYPE, "2");
                ArrayList<TicketInfo> ticketInfos = new ArrayList<TicketInfo>();
                TicketInfo ticketInfo = new TicketInfo();
                ticketInfo.setEventTitle(cashEventName);
                ticketInfos.add(ticketInfo);
                bundle.putParcelableArrayList(Constants.PASS_OBJECT, ticketInfos);
                openActivity(WXEntryActivity.class, bundle);*/

                showShareWxDialog();

            }
        });
    }

    private void showShareWxDialog() {
        ShareBottomDialog dialog = new ShareBottomDialog(PrivateClassByMessageActivity.this, scrollView, cashEventId, "2", couseId);
        dialog.showAnim(new BounceTopEnter())//
                .show();
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
                tvScanCodeInfo.setText(String.format("课程二维码在开课前两个小时才会生效，您可以将如下链接保存：%1$s/sys/upload/qrCodeImg?courseId=%2$s&uid=%3$s", new Object[]{Constants.Net.URL, detail.getCourseId(), SharedPreferencesUtils.getInstance().getString(Constants.UID, "")}));
                tvSaveToPhone.setText(getString(R.string.copy_link));
            }
        }

        if (detail.getOrderStatus().equals("8")) {
            //已评论
            llMyclassScore.setVisibility(View.VISIBLE);
            llMackScore.setVisibility(View.GONE);
            if (!TextUtils.isEmpty(detail.getCommentStar())) {
                ratingBarMyClass.setRating(Float.parseFloat(detail.getCommentStar()));
            }
            if (!TextUtils.isEmpty(detail.getCommentContent())) {
                tvMyClassScore.setText(detail.getCommentContent());
            }
            showCashTicketButton();
        } else if (detail.getOrderStatus().equals("7")) {
            llMyclassScore.setVisibility(View.GONE);
            llMackScore.setVisibility(View.VISIBLE);
        }

        if (!TextUtils.isEmpty(detail.getCourseStatus())) {
            if (detail.getCourseStatus().equals("0")) {
                btnOrder.setVisibility(View.VISIBLE);
                tvWaitingAccept.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(detail.getIsParted())) {
                    if (detail.getIsParted().equals("0")) {
                        btnOrder.setVisibility(View.VISIBLE);
                    } else {
                        btnOrder.setVisibility(View.GONE);
                    }
                }
            } else if (detail.getCourseStatus().equals("1")) {

                tvWaitingAccept.setVisibility(View.VISIBLE);
                tvWaitingAccept.setText("课程进行中...");
                if (!TextUtils.isEmpty(detail.getIsParted())) {
                    if (detail.getIsParted().equals("0")) {
                        btnOrder.setVisibility(View.GONE);
                    } else {
                        btnOrder.setVisibility(View.GONE);
                    }
                }
            } else if (detail.getCourseStatus().equals("2")) {
                tvWaitingAccept.setVisibility(View.GONE);
                btnOrder.setVisibility(View.GONE);

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


//        if (!TextUtils.isEmpty(detail.getPrice())) {
        monney = Float.parseFloat(detail.getPrice());
//        }
        tvClassPrice.setText(String.format("%.2f", monney * (DateUtils.getHourNum(startTime, endTime))));
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
                    orderPrivateClass();
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
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.PAGE_INDEX, getIntent().getIntExtra(Constants.PAGE_INDEX, 3));//  1   2  小班课 和团操课  一样处理
        bundle.putString(Constants.COURSE_ORDER_CODE, detail.getOrderCode());
        bundle.putString(Constants.COURSE_ID, detail.getCourseId());
        bundle.putString(Constants.COURSE_MONEY, detail.getPrice());
        bundle.putString(Constants.COURSE_TYPE, "2");
        openActivity(ConfirmOrderCourseActivity.class, bundle);


    }


}
