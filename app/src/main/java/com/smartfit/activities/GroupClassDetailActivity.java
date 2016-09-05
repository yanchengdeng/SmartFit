package com.smartfit.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.smartfit.MessageEvent.UpdateGroupClassDetail;
import com.smartfit.R;
import com.smartfit.adpters.DiscussItemAdapter;
import com.smartfit.beans.CashTickeInfo;
import com.smartfit.beans.ClassCommend;
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
import com.smartfit.views.MyListView;
import com.smartfit.views.ShareBottomDialog;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/***
 * 团体/小班操课程详情
 *
 * @author yanchengdeng
 */
public class GroupClassDetailActivity extends BaseActivity {

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
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.iv_coach_icon)
    ImageView ivCoachIcon;
    @Bind(R.id.tv_coach_name)
    TextView tvCoachName;
    @Bind(R.id.tv_coach_info)
    TextView tvCoachInfo;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.tv_coach_score)
    TextView tvCoachScore;
    @Bind(R.id.iv_space_icon)
    ImageView ivSpaceIcon;
    @Bind(R.id.tv_space_name)
    TextView tvSpaceName;
    @Bind(R.id.tv_space_info)
    TextView tvSpaceInfo;
    @Bind(R.id.iv_operate_person)
    ImageView ivOperatePerson;
    @Bind(R.id.ll_have_ordered_members)
    LinearLayout llHaveOrderedMembers;
    @Bind(R.id.tv_class_time)
    TextView tvClassTime;
    @Bind(R.id.tv_class_price)
    TextView tvClassPrice;
    @Bind(R.id.lisview_discuss)
    MyListView lisviewDiscuss;
    @Bind(R.id.tv_more)
    TextView tvMore;
    @Bind(R.id.btn_order)
    Button btnOrder;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.tv_operate_address)
    TextView tvOperateAddress;
    @Bind(R.id.tv_class_tittle)
    TextView tvClassTittle;
    @Bind(R.id.btn_commit_comments)
    Button btnCommitComments;
    @Bind(R.id.ll_mack_score)
    LinearLayout llMackScore;
    @Bind(R.id.tv_class_name)
    TextView tvClassName;
    @Bind(R.id.iv_scan_bar)
    ImageView ivScanBar;
    @Bind(R.id.ll_scan_bar)
    LinearLayout llScanBar;
    @Bind(R.id.tv_save_to_phone)
    TextView tvSaveToPhone;
    @Bind(R.id.tv_share_friends)
    TextView tvShareFriends;
    @Bind(R.id.tv_contact_coach)
    TextView tvContactCoach;
    @Bind(R.id.tv_invite_friends)
    TextView tvInviteFriends;
    @Bind(R.id.ll_order_success)
    LinearLayout llOrderSuccess;
    @Bind(R.id.ratingBar_my_class)
    RatingBar ratingBarMyClass;
    @Bind(R.id.tv_my_class_score)
    TextView tvMyClassScore;
    @Bind(R.id.ll_myclass_score)
    LinearLayout llMyclassScore;
    @Bind(R.id.tv_waiting_accept)
    TextView tvWaitingAccept;
    @Bind(R.id.et_coach_apprise)
    EditText etCoachApprise;
    @Bind(R.id.tv_apprise_list_tips)
    TextView tvAppriseListTips;
    @Bind(R.id.rl_coach_ui)
    RelativeLayout rlCoachUi;
    @Bind(R.id.ll_view_scan_code)
    LinearLayout llViewScanCode;//二维码  显示
    @Bind(R.id.tv_scan_code_info)
    TextView tvScanCodeInfo;// 链接显示
    @Bind(R.id.iv_send_red)
    ImageView ivSendRed;
    @Bind(R.id.ratingBar_for_coach)
    com.hedgehog.ratingbar.RatingBar ratingBarForCoach;
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
    @Bind(R.id.tv_scan_bar_tips)
    TextView tvScanBarTips;


    private DiscussItemAdapter adapter;

    ClassInfoDetail classInfoDetail;

    private String id;

    private String type = "0";

    private String startTime, endTime;

    private EventBus eventBus;

    private CountDownTimer countDownTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_class_detail);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        id = getIntent().getStringExtra(Constants.PASS_STRING);
        type = getIntent().getStringExtra(Constants.COURSE_TYPE);
        startTime = getIntent().getStringExtra("start");
        endTime = getIntent().getStringExtra("end");
        tvTittle.setText(getString(R.string.class_detail));
        if (type.equals("0")) {
            tvWarningTips.setText(getString(R.string.group_experien_cancle_class_tips));
        } else {
            tvWarningTips.setText(getString(R.string.small_class_cancle_class_tips));
        }
        ivFunction.setImageResource(R.mipmap.ic_more_share);
        //TODO  分享暂时隐藏
        ivFunction.setVisibility(View.INVISIBLE);
        ratingBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 24));
        ratingBarMyClass.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 24));
//        ratingBarForCoach.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 50));
        ratingBarForCoach.setStar(starts);
        rollViewPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (DeviceUtil.getWidth(this) * 0.75)));
        loadData();
        addLisener();
//        showCashTicketButton();
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                LogUtil.w("dyc", "倒计时结束");
                ivScanBar.setImageResource(R.mipmap.error_scan);

            }
        };

    }

    private String cashEventId;

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
        data.put("orgType", type);
        data.put("orgId", id);
        PostRequest request = new PostRequest(Constants.EVENT_GETAVAILABLECASHEVENT, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                CashTickeInfo cashTickeInfo = JsonUtils.objectFromJson(response, CashTickeInfo.class);
                if (cashTickeInfo != null && !TextUtils.isEmpty(cashTickeInfo.getId())) {
                    cashEventId = cashTickeInfo.getId();
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
        request.headers = NetUtil.getRequestBody(GroupClassDetailActivity.this);
        mQueue.add(request);
    }


    /**
     * 弹出红包  按钮
     */
    private void showCashTicketButton() {
        Map<String, String> data = new HashMap<>();
        data.put("orgType", type);
        data.put("orgId", id);
        PostRequest request = new PostRequest(Constants.EVENT_GETAVAILABLECASHEVENT, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                CashTickeInfo cashTickeInfo = JsonUtils.objectFromJson(response, CashTickeInfo.class);
                if (cashTickeInfo != null && !TextUtils.isEmpty(cashTickeInfo.getId())) {
                    cashEventId = cashTickeInfo.getId();
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
        request.headers = NetUtil.getRequestBody(GroupClassDetailActivity.this);
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
        PostRequest request = new PostRequest(Constants.EVENT_GETSHAREDMAINPAGE, data, new Response.Listener<JsonObject>() {
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
        request.headers = NetUtil.getRequestBody(GroupClassDetailActivity.this);
        mQueue.add(request);
    }


    @Subscribe
    public void onEvent(Object event) {
        if (event instanceof UpdateGroupClassDetail) {
            loadData();
//            showCashTicketButton();
        }
    }

    private void initView(ClassInfoDetail detail) {
        classInfoDetail = detail;
        if (!TextUtils.isEmpty(detail.getCourseName())) {
            tvClassTittle.setText(detail.getCourseName());
        }
        if (!TextUtils.isEmpty(detail.getCourseDetail())) {
            tvContent.setText(detail.getCourseDetail());
        }

        if (!TextUtils.isEmpty(detail.getCoachRealName())) {
            tvCoachName.setText(detail.getCoachRealName() + "教练");
        }

        if (!TextUtils.isEmpty(detail.getSignature())) {
            tvCoachInfo.setText(detail.getSignature());
        }

        if (!TextUtils.isEmpty(detail.getUserSex())) {
            if (detail.getUserSex().equals(Constants.SEX_WOMEN)) {
                tvCoachName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_woman), null);
            } else {
                tvCoachName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_man), null);
            }
        }
        if (!TextUtils.isEmpty(detail.getCourseDetail())) {
            tvContent.setText(detail.getCourseDetail());
        }


        if (!TextUtils.isEmpty(detail.getStars())) {
            ratingBar.setRating(Float.parseFloat(detail.getStars()));
            tvCoachScore.setText(detail.getStars());
        }

        ImageLoader.getInstance().displayImage(detail.getVenueUrl(), ivSpaceIcon, Options.getListOptions());
        if (!TextUtils.isEmpty(detail.getVenueName())) {
            tvSpaceName.setText(detail.getVenueName());
        }

        if (!TextUtils.isEmpty(detail.getServiceDetails())) {
            tvCoachInfo.setText(detail.getServiceDetails());
        }

        if (!TextUtils.isEmpty(detail.getPrice())) {
            tvClassPrice.setText(detail.getPrice());
        }

        if (!TextUtils.isEmpty(detail.getUserNickName())) {
            tvOperateAddress.setText(detail.getUserNickName());
        }

        tvSpaceInfo.setText("距离" + Util.getDistance(detail.getLat(), detail.getLongit()));
        ImageLoader.getInstance().displayImage(detail.getVenueUrl(), ivSpaceIcon, Options.getListOptions());
        ImageLoader.getInstance().displayImage(detail.getUserPicUrl(), ivCoachIcon, Options.getHeaderOptions());
        ImageLoader.getInstance().displayImage(detail.getUserHeadImg(), ivOperatePerson, Options.getHeaderOptions());
        tvOperateAddress.setText(TextUtils.isEmpty(detail.getUserNickName()) ? "暂无" : detail.getUserNickName());
        if (detail.getPersionList().size() == 0) {
            TextView textView = new TextView(GroupClassDetailActivity.this);
            textView.setText(getString(R.string.no_report_members));
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextColor(getResources().getColor(R.color.text_color_gray));
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DeviceUtil.dp2px(mContext, 40), DeviceUtil.dp2px(mContext, 40));
            params.gravity = Gravity.CENTER;
            params.leftMargin = 15;
            llHaveOrderedMembers.removeAllViews();
            for (int i = 0; i < detail.getPersionList().size(); i++) {
                ImageView imageView = new ImageView(GroupClassDetailActivity.this);
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageLoader.getInstance().displayImage(detail.getPersionList().get(i).getUserPicUrl(), imageView, Options.getHeaderOptionsCircle());
                llHaveOrderedMembers.addView(imageView);
            }
        }


        if (null != detail.getCoursePics() && detail.getCoursePics().length > 0) {
            rollViewPager.setVisibility(View.VISIBLE);
        } else {
            detail.setCoursePics(new String[]{""});
            rollViewPager.setVisibility(View.VISIBLE);
        }
        rollViewPager.setPlayDelay(3000);
        rollViewPager.setAnimationDurtion(500);
        rollViewPager.setAdapter(new TestNomalAdapter(detail.getCoursePics(), detail.getCourseName()));
        rollViewPager.setHintView(new ColorPointHintView(this, getResources().getColor(R.color.common_header_bg), Color.WHITE));
        ratingBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 24));

        if (detail.getCommentList().size() > 0) {
            adapter = new DiscussItemAdapter(detail.getCommentList(), this);
            lisviewDiscuss.setAdapter(adapter);

        }

        if (!TextUtils.isEmpty(detail.getStartTime()) && !TextUtils.isEmpty(detail.getEndTime())) {
            tvClassTime.setText(DateUtils.getData(detail.getStartTime()) + "~" + DateUtils.getDataTime(detail.getEndTime()));
        }


        if (detail.getCommentList().size() > 0) {
            ClassCommend commentInfo = detail.getCommentList().get(0);
            if (!TextUtils.isEmpty(commentInfo.getCommentStar())) {
                ratingBarMyClass.setRating(Float.parseFloat(commentInfo.getCommentStar()));
            }

            if (!TextUtils.isEmpty(commentInfo.getCommentContent())) {
                tvMyClassScore.setText(commentInfo.getCommentContent());
            }
            tvAppriseListTips.setVisibility(View.VISIBLE);
            lisviewDiscuss.setVisibility(View.VISIBLE);
            tvMore.setVisibility(View.VISIBLE);
        } else {
            tvAppriseListTips.setVisibility(View.GONE);
            lisviewDiscuss.setVisibility(View.GONE);
            tvMore.setVisibility(View.GONE);

        }


        if (!TextUtils.isEmpty(detail.getShared())) {
            if (detail.getShared().equals("0")) {
                tvShareFriends.setText("发送给朋友");
                tvShareFriends.setBackgroundColor(getResources().getColor(R.color.common_header_bg));
                tvShareFriends.setClickable(true);
            } else {
                tvShareFriends.setText("已发送");
                tvShareFriends.setBackgroundColor(getResources().getColor(R.color.line_gray));
            }
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


        if (TextUtils.isEmpty(detail.getOrderStatus())) {
            //去订购
            btnOrder.setVisibility(View.VISIBLE);
            tvWaitingAccept.setVisibility(View.GONE);
            tvClassTittle.setVisibility(View.VISIBLE);
            tvContent.setVisibility(View.VISIBLE);
            llMyclassScore.setVisibility(View.GONE);//我的课程得分
            llMackScore.setVisibility(View.GONE);//评分
            //TODO  暂时隐藏
            llScanBar.setVisibility(View.GONE);//发送朋友 二维码
            llOrderSuccess.setVisibility(View.GONE);//订购成功底部

            showRankView(detail);

        } else {
            if (detail.getOrderStatus().equals("2")) {
                //教练接单状态
                btnOrder.setVisibility(View.GONE);
                tvWaitingAccept.setVisibility(View.VISIBLE);
                tvClassTittle.setVisibility(View.VISIBLE);
                tvContent.setVisibility(View.VISIBLE);
                llMyclassScore.setVisibility(View.GONE);//我的课程得分
                llMackScore.setVisibility(View.GONE);//评分
                //TODO  暂时隐藏
                llScanBar.setVisibility(View.GONE);//发送朋友 二维码
                llOrderSuccess.setVisibility(View.GONE);//订购成功底部


            } else if (detail.getOrderStatus().equals("3")) {
                //订单详情页   预约成功
                btnOrder.setVisibility(View.GONE);
                tvWaitingAccept.setVisibility(View.GONE);
                tvClassTittle.setVisibility(View.GONE);
                tvContent.setVisibility(View.GONE);
                llMyclassScore.setVisibility(View.GONE);//我的课程得分
                llMackScore.setVisibility(View.GONE);//评分
                //TODO  暂时隐藏
                llScanBar.setVisibility(View.GONE);//发送朋友 二维码
                llOrderSuccess.setVisibility(View.VISIBLE);//订购成功底部


            } else if (detail.getOrderStatus().equals("4")) {
                //订单详情页   预约成功
                btnOrder.setVisibility(View.GONE);
                tvWaitingAccept.setVisibility(View.GONE);
                tvClassTittle.setVisibility(View.GONE);
                tvContent.setVisibility(View.GONE);
                llMyclassScore.setVisibility(View.GONE);//我的课程得分
                llMackScore.setVisibility(View.GONE);//评分
                //TODO  暂时隐藏
                llScanBar.setVisibility(View.GONE);//发送朋友 二维码
                llOrderSuccess.setVisibility(View.VISIBLE);//订购成功底部


            } else if (detail.getOrderStatus().equals("7")) {
                //订单详情页  已结束 未评论
                btnOrder.setVisibility(View.GONE);
                tvWaitingAccept.setVisibility(View.GONE);
                tvClassTittle.setVisibility(View.GONE);
                tvContent.setVisibility(View.GONE);
                //TODO  暂时隐藏
                llScanBar.setVisibility(View.GONE);//发送朋友 二维码
                llOrderSuccess.setVisibility(View.GONE);//订购成功底部

                if (!TextUtils.isEmpty(detail.getIsParted())) {
                    if (detail.getIsParted().equals("0")) {
                        //未报名
                        llMyclassScore.setVisibility(View.GONE);//我的课程得分
                        llMackScore.setVisibility(View.GONE);//评分
                    } else {
                        llMyclassScore.setVisibility(View.GONE);//我的课程得分
                        llMackScore.setVisibility(View.VISIBLE);//评分
                    }
                }


            } else if (detail.getOrderStatus().equals("8") || detail.getOrderStatus().equals("5") || detail.getOrderStatus().equals("6")) {
                //订单详情页  已结束 未评论
                btnOrder.setVisibility(View.GONE);
                tvWaitingAccept.setVisibility(View.GONE);
                tvClassTittle.setVisibility(View.GONE);
                tvContent.setVisibility(View.GONE);
                llMyclassScore.setVisibility(View.VISIBLE);//我的课程得分
                llMackScore.setVisibility(View.GONE);//评分
                //TODO  暂时隐藏
                llScanBar.setVisibility(View.GONE);//发送朋友 二维码
                llOrderSuccess.setVisibility(View.GONE);//订购成功底部
            } else {
                //去订购
//                btnOrder.setVisibility(View.VISIBLE);
                tvWaitingAccept.setVisibility(View.GONE);
                tvClassTittle.setVisibility(View.VISIBLE);
                tvContent.setVisibility(View.VISIBLE);
                llMyclassScore.setVisibility(View.GONE);//我的课程得分
                llMackScore.setVisibility(View.GONE);//评分
                //TODO  暂时隐藏
                llScanBar.setVisibility(View.GONE);//发送朋友 二维码
                llOrderSuccess.setVisibility(View.GONE);//订购成功底部

            }


            if (!TextUtils.isEmpty(detail.getQrcodeUrl())) {
                llScanBar.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(detail.getQrcodeUrl(), ivScanBar, Options.getListOptions());
                codeBar = detail.getQrcodeUrl();
                countDownTimer.start();
            }
            if (detail.getIsParted().equals("1")) {
                llScanBar.setVisibility(View.VISIBLE);
                llViewScanCode.setVisibility(View.VISIBLE);
                tvScanCodeInfo.setVisibility(View.VISIBLE);
                if (DateUtils.isQeWorked(detail.getStartTime())) {
                    if (!TextUtils.isEmpty(detail.getQrcodeUrl())) {
                        ImageLoader.getInstance().displayImage(detail.getQrcodeUrl(), ivScanBar, Options.getListOptions());
                        codeBar = detail.getQrcodeUrl();
                        countDownTimer.start();
                    } else {
                        ivScanBar.setImageResource(R.mipmap.code_pre_src);
                        tvScanBarTips.setText(getString(R.string.have_not_genery_code_image));
                    }
                } else {
                    ivScanBar.setImageResource(R.mipmap.code_pre_src);
                    tvScanBarTips.setText(getString(R.string.have_not_genery_code_image));
                }
            }


        }

        if (!TextUtils.isEmpty(detail.getCourseStatus())) {
            if (detail.getCourseStatus().equals("0")) {
                btnOrder.setVisibility(View.VISIBLE);
                tvWaitingAccept.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(detail.getIsParted())) {
                    if (detail.getIsParted().equals("0")) {
                        btnOrder.setVisibility(View.VISIBLE);
                        llOrderSuccess.setVisibility(View.GONE);
                    } else {
                        btnOrder.setVisibility(View.GONE);
                        llOrderSuccess.setVisibility(View.VISIBLE);
                    }
                }
            } else if (detail.getCourseStatus().equals("1")) {

                tvWaitingAccept.setVisibility(View.VISIBLE);
                tvWaitingAccept.setText("课程进行中...");
                if (!TextUtils.isEmpty(detail.getIsParted())) {
                    if (detail.getIsParted().equals("0")) {
                        btnOrder.setVisibility(View.GONE);
                        llOrderSuccess.setVisibility(View.GONE);
                    } else {
                        btnOrder.setVisibility(View.GONE);
                        llOrderSuccess.setVisibility(View.GONE);
                    }
                }
            } else if (detail.getCourseStatus().equals("2")) {
                tvWaitingAccept.setVisibility(View.GONE);
                btnOrder.setVisibility(View.GONE);
                if (detail.getIsParted().equals("0")) {
                    llOrderSuccess.setVisibility(View.GONE);
                } else {
                    llOrderSuccess.setVisibility(View.VISIBLE);
                }
            }

        }

        if (!TextUtils.isEmpty(detail.getOrderStatus())) {
            if (detail.getOrderStatus().equals("4") || detail.getOrderStatus().equals("5") || detail.getOrderStatus().equals("6") || detail.getOrderStatus().equals("7") || detail.getOrderStatus().equals("8")) {
                tvWaitingAccept.setVisibility(View.GONE);
                btnOrder.setVisibility(View.GONE);
                llOrderSuccess.setVisibility(View.GONE);
            }

            if (detail.getOrderStatus().equals("8")) {
                showCashTicketDialog();
            }
        }


        if (!TextUtils.isEmpty(detail.getOpenAppointmentTime())) {
            if (type.equals("0")) {
                if (System.currentTimeMillis() > Long.parseLong(detail.getOpenAppointmentTime()) * 1000) {

                } else {
                    btnOrder.setVisibility(View.GONE);
                    tvWaitingAccept.setVisibility(View.VISIBLE);
                    tvWaitingAccept.setText(DateUtils.getDayOFWeek(GroupClassDetailActivity.this, DateUtils.getInteger(DateUtils.getDate(detail.getOpenAppointmentTime()), Calendar.DAY_OF_WEEK)) + DateUtils.getData(detail.getOpenAppointmentTime(), " HH:mm") + "开放预约");
                }
            }
        }
        LogUtil.w("dyc", detail.getIsFull() + "....." + detail.getBookNumber());
        showRankView(detail);


        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    /**
     * 显示排队视图
     *
     * @param detail
     */
    private void showRankView(ClassInfoDetail detail) {
        // 显示 排队
        if (!TextUtils.isEmpty(detail.getIsFull())) {
            if (detail.getIsFull().equals("1")) {
                //团操课 排队机制
                if (type.equals("0")) {
                    //首先是未开始的课程都可以去排队  购买
                    /**
                     * 0:未开始1:正在进行2:已结束3:已排队 4:已排到
                     */
                    if (detail.getCourseStatus().equals("0")) {
                        btnOrder.setVisibility(View.GONE);
                        rlRank.setVisibility(View.VISIBLE);
                        tvRankNum.setText(String.format("预约已满,当前排队人数：%s", detail.getBookTotal()));
                        btnAddRank.setVisibility(View.VISIBLE);
                        llOrderSuccess.setVisibility(View.GONE);
                    } else if (detail.getCourseStatus().equals("3")) {
                        btnOrder.setVisibility(View.GONE);
                        rlRank.setVisibility(View.VISIBLE);
                        tvRankNum.setText(String.format("我的排队序号：%s", detail.getBookNumber()));
                        btnAddRank.setVisibility(View.GONE);
                        llOrderSuccess.setVisibility(View.GONE);
                    } else if (detail.getCourseStatus().equals("4")) {
                        rlRank.setVisibility(View.GONE);
//                        tvRankNum.setText(String.format("预约已满,当前排队人数：%s", detail.getBookTotal()));
//                        btnAddRank.setVisibility(View.GONE);
                        llOrderSuccess.setVisibility(View.VISIBLE);
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
        }
    }


    private void loadData() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> data = new HashMap<>();
        data.put("courseId", id);
        data.put("courseType", type);
        data.put("beginTime", startTime);
        data.put("endTime", endTime);
        PostRequest request = new PostRequest(Constants.SEARCH_CLASS_DETAIL, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                ClassInfoDetail detail = JsonUtils.objectFromJson(response.toString(), ClassInfoDetail.class);
                initView(detail);
                mSVProgressHUD.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();

            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(GroupClassDetailActivity.this);
        mQueue.add(request);
    }


    String codeBar;

    float starts = 5.0f;

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareBottomDialog dialog = new ShareBottomDialog(GroupClassDetailActivity.this, scrollView, cashEventId, type, id);
                dialog.showAnim(new BounceTopEnter())//
                        .show();
            }
        });


        ivSendRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCashDialog();
            }
        });


        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, classInfoDetail.getTopicId());
                openActivity(ClassMoreCommentsActivity.class, bundle);
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


        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLinyuRechagerInfo();

            }
        });

        rlCoachUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classInfoDetail != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.PASS_STRING, classInfoDetail.getUid());
                    bundle.putString("coach_id", classInfoDetail.getCoachId());
                    openActivity(CoachInfoActivity.class, bundle);
                }
            }
        });

        //联系教练
        tvContactCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classInfoDetail != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("icon", classInfoDetail.getUserPicUrl());
                    bundle.putString("name", classInfoDetail.getCoachRealName());
                    bundle.putString("phone", classInfoDetail.getMobileNo());
                    openActivity(ContactCoachActivity.class, bundle);
                }
            }
        });

        //邀请好友
        tvInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classInfoDetail != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.PASS_STRING, classInfoDetail.getCourseId());
                    openActivity(InviteFriendsActivity.class, bundle);
                } else {

                }
            }
        });

        //保存手机
        tvSaveToPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvSaveToPhone.getText().equals(getString(R.string.copy_link))) {
                    Util.copyToClob(tvScanCodeInfo.getText().toString(), GroupClassDetailActivity.this);
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
                            Util.saveImageToPhoto(GroupClassDetailActivity.this, loadedImage);
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

        tvRankTips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRandDialog();
            }
        });

        //加入排队
        btnAddRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRandLink();
            }


        });
        ivScanBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewScanCode();
            }
        });
    }

    /**
     * 获取新的二维码
     */
    private void getNewScanCode() {
        Map<String, String> map = new HashMap<>();
        map.put("courseId", classInfoDetail.getCourseId());
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
        request.headers = NetUtil.getRequestBody(GroupClassDetailActivity.this);
        mQueue.add(request);

    }


    /**
     * 加入排队
     */
    private void addRandLink() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.PAGE_INDEX, getIntent().getIntExtra(Constants.PAGE_INDEX, 1));//  1   2  小班课 和团操课  一样处理
        bundle.putString(Constants.COURSE_ORDER_CODE, classInfoDetail.getOrderCode());
        bundle.putString(Constants.COURSE_ID, classInfoDetail.getCourseId());
        bundle.putString(Constants.COURSE_MONEY, classInfoDetail.getPrice());
        bundle.putString(Constants.COURSE_TYPE, type);
        bundle.putBoolean("add_rank", true);
        openActivity(ConfirmOrderCourseActivity.class, bundle);


    }

    /**
     * 排队对话框
     */
    private void openRandDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_rank_tips);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
               /* Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, cashEventId);
                bundle.putString("course_id", classInfoDetail.getCourseId());
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
        ShareBottomDialog dialog = new ShareBottomDialog(GroupClassDetailActivity.this, scrollView, cashEventId, type, id);
        dialog.showAnim(new BounceTopEnter())//
                .show();
    }

    /**
     * 检查淋浴欠费情况
     */
    private void checkLinyuRechagerInfo() {
        PostRequest request = new PostRequest(Constants.TBEVENTRECORD_GETSHOWERRECORD, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc==", response.toString());
                LingyunListInfo lingyunListInfo = JsonUtils.objectFromJson(response, LingyunListInfo.class);
                if (lingyunListInfo != null && lingyunListInfo.getListData() != null && lingyunListInfo.getListData().size() > 0) {
                    createLinyuOrder(lingyunListInfo.getListData());
                } else {
                    if (classInfoDetail != null) {
                        getCourseNotition(classInfoDetail);
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
        request.headers = NetUtil.getRequestBody(GroupClassDetailActivity.this);
        mQueue.add(request);

    }

    private void getCourseNotition(ClassInfoDetail classInfoDetail) {
        Map<String, String> map = new HashMap<>();
        map.put("beginTime", classInfoDetail.getStartTime());
        map.put("courseType", type);
        PostRequest request = new PostRequest(Constants.COURSE_GETNOTIFICATION, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                CourseNotition courseNotition = JsonUtils.objectFromJson(response.toString(), CourseNotition.class);
                if (courseNotition != null) {
                    //  课程提醒
                    // 0:忽略不弹窗1:预约协议2:限制消息
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
        request.headers = NetUtil.getRequestBody(GroupClassDetailActivity.this);
        mQueue.add(request);
    }

    private void goBuyCourseUI() {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.PAGE_INDEX, getIntent().getIntExtra(Constants.PAGE_INDEX, 1));//  1   2  小班课 和团操课  一样处理
        bundle.putString(Constants.COURSE_ORDER_CODE, classInfoDetail.getOrderCode());
        bundle.putString(Constants.COURSE_ID, classInfoDetail.getCourseId());
        bundle.putString(Constants.COURSE_MONEY, classInfoDetail.getPrice());
        bundle.putString(Constants.COURSE_TYPE, type);
        openActivity(ConfirmOrderCourseActivity.class, bundle);
    }

    /**
     * 弹出课程提醒对话框
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
            if (type.equals("0")) {
                tvTittle.setText("团操预约协议");
            } else {
                tvTittle.setText("小班预约协议");
            }
            tvRightButton.setText("同意协议，马上预约");
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
                if ( checkBox.getVisibility() == View.GONE) {
                    if (courseNotition.getType().equals("2")){
                        goBuyCourseUI();
                        dialog.dismiss();
                    }else if (courseNotition.getType().equals("3")) {
                        dialog.dismiss();
                    }
                }else{
                    if (checkBox.isChecked()){
                        dialog.dismiss();
                        saveHaveReaderProtocol();
                    }else{
                        goBuyCourseUI();
                    }
                }
            }
        });

    }

    /**
     * 设置已读协议
     */
    private void saveHaveReaderProtocol() {
        Map<String, String> map = new HashMap<>();
        map.put("courseType", type);
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
        request.headers = NetUtil.getRequestBody(GroupClassDetailActivity.this);
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
                    Util.showLinyuRechagerDialog(GroupClassDetailActivity.this, linyuCourseInfo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.w("dyc", error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(GroupClassDetailActivity.this);
        mQueue.add(request);

    }

    private void submintScore(float rating, String comments) {
        if (classInfoDetail == null)
            return;
        mSVProgressHUD.showWithStatus(getString(R.string.submit_ing), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> data = new HashMap<>();
        if (classInfoDetail.getCommentList() != null && classInfoDetail.getCommentList().size() > 0) {
            data.put("commentId", classInfoDetail.getCommentList().get(0).getCommentId());
        }

        data.put("topicId", classInfoDetail.getTopicId());
        data.put("commentStar", String.valueOf(rating));
        data.put("commentContent", comments);
        PostRequest request = new PostRequest(Constants.COMMENT_SAVE, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus("已评分", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                loadData();
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
        request.headers = NetUtil.getRequestBody(GroupClassDetailActivity.this);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}
