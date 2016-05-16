package com.smartfit.activities;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.google.gson.JsonObject;
import com.hyphenate.easeui.EaseConstant;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.R;
import com.smartfit.beans.UserInfo;
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DeviceUtil;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.views.SelectableRoundedImageView;

import java.util.HashMap;
import java.util.Map;

/**
 * 其他个人主页信息
 * 非教练主页
 */
public class OtherCustomeMainActivity extends BaseActivity {

    private PullToZoomScrollViewEx scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_info);
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.bar_regiter_bg);//通知栏所需颜色
        loadViewForCode();
        scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        initView();
        addLisener();
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
        getCoachInfo();
    }



    RatingBar ratingBar;
    View tvCoachMoreInfo;
    ImageView ivBack, ivHeader;
    TextView tvTeachCaptial, tvCoachCaptial, tvAttentionNum;
    TextView tvVIP, tvName, tvMotto, tvAttention, tvFans, tvCore, tvaddFriends;
    TextView tvCoachBrief, tvReadMoreBrief, tvRating, tvTeachedClass, tvHeight, tvWeight, tvCoachInfo, tvHisClasses;
    LinearLayout llDynamic,llCoachUI;


    private void initView() {
        tvCoachMoreInfo = scrollView.getPullRootView().findViewById(R.id.tv_read_more_info);
        ratingBar = (RatingBar) scrollView.getPullRootView().findViewById(R.id.ratingBar);
        ratingBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 24));
        tvCore = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_core);
        ///===============
        tvAttentionNum = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_attentioon_num);
        ivBack = (ImageView) scrollView.getPullRootView().findViewById(R.id.iv_back);
        ivHeader = (ImageView) scrollView.getPullRootView().findViewById(R.id.iv_header);
        tvTeachCaptial = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_teach_capacity);
        tvCoachCaptial = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_coach_capacity);
        tvVIP = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_vip);
        tvName = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_name);
        tvMotto = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_motto);
        tvAttention = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_attention);
        tvaddFriends = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_add_friends);
        tvFans = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_fans);
        tvCoachBrief = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_coach_brief);
        tvReadMoreBrief = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_read_more_brief);
        tvRating = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_core);
        tvTeachedClass = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_teached_classes);
        tvHeight = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_height);
        tvWeight = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_weight);
        tvCoachInfo = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_coach_info);
        tvHisClasses = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_his_classes);
        llDynamic = (LinearLayout) scrollView.getPullRootView().findViewById(R.id.ll_dynamic_ui);
        llCoachUI = (LinearLayout) scrollView.getPullRootView().findViewById(R.id.ll_coach_brief_ui);
        llCoachUI.setVisibility(View.GONE);

    }




    String uid;

    private void getCoachInfo() {
        scrollView.getPullRootView().findViewById(R.id.tv_teach_capacity).setVisibility(View.GONE);
        scrollView.getPullRootView().findViewById(R.id.tv_coach_capacity).setVisibility(View.GONE);

        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        uid = getIntent().getExtras().getString(Constants.PASS_STRING);
        Map<String, String> maps = new HashMap<>();
        maps.put("uid", uid);
        PostRequest request = new PostRequest(Constants.MAIN_PAGE_INFO, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                UserInfo userInfo = JsonUtils.objectFromJson(response, UserInfo.class);
                if (null != userInfo) {
                    fillData(userInfo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(OtherCustomeMainActivity.this);
        mQueue.add(request);

    }

    UserInfo userInfo;
    TextView tvDoAttention, tvAddFriends, tvHisClass;//操作关注用户 /添加健身伙伴

    private void fillData(UserInfo userInfo) {
        this.userInfo = userInfo;
        SelectableRoundedImageView ivHeader = (SelectableRoundedImageView) scrollView.getPullRootView().findViewById(R.id.iv_header);
        ImageLoader.getInstance().displayImage(userInfo.getUserPicUrl(), ivHeader, Options.getHeaderOptions());
        TextView tvNickname = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_name);
        if (!TextUtils.isEmpty(userInfo.getNickName())) {
            tvNickname.setText(userInfo.getNickName());
        }
        TextView tvVip = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_vip);
        if (!TextUtils.isEmpty(userInfo.getIsVip()) && userInfo.getIsVip().equals("1")) {
            tvVip.setVisibility(View.VISIBLE);
        } else {
            tvVip.setVisibility(View.INVISIBLE);
        }

        TextView tvMotto = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_motto);
        if (!TextUtils.isEmpty(userInfo.getSignature())) {
            tvMotto.setText(userInfo.getSignature());
        }

        if (!TextUtils.isEmpty(userInfo.getFocusCount())) {
            TextView tvFoucs = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_attentioon_num);
            tvFoucs.setText("关注  " + userInfo.getFocusCount());
        }

        if (!TextUtils.isEmpty(userInfo.getFansCount())) {
            TextView tvfans = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_fans);
            tvfans.setText("粉丝  " + userInfo.getFansCount());
        }

        if (!TextUtils.isEmpty(userInfo.getIsFoused())) {
            if (userInfo.getIsFoused().equals("0")) {
                tvDoAttention.setText("关注");
            } else {
                tvDoAttention.setText("已关注");
            }
        }

        if (!TextUtils.isEmpty(userInfo.getIsFriend())) {
            if (userInfo.getIsFriend().equals("0")) {
                tvAddFriends.setText("加为健身好友");
            } else {
                tvAddFriends.setText("已添加好友");
            }
        }

        if (!TextUtils.isEmpty(userInfo.getSex())) {
            if (userInfo.getSex().equals(Constants.SEX_WOMEN)) {
                tvNickname.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_woman), null);
            } else {
                tvNickname.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_man), null);
            }
        }

        if (!TextUtils.isEmpty(userInfo.getCurClassCount())) {
            tvHisClass.setText(" 正在进行" + userInfo.getCurClassCount() + "个课程");
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DeviceUtil.dp2px(OtherCustomeMainActivity.this, 60), DeviceUtil.dp2px(OtherCustomeMainActivity.this, 60));
        params.topMargin = 16;
        params.leftMargin = 16;
        params.bottomMargin = 16;
        LinearLayout linearLayout = (LinearLayout) scrollView.getPullRootView().findViewById(R.id.ll_pictures);
        if (userInfo.getCoachDynamicPics() != null && userInfo.getCoachDynamicPics().length > 0) {

            for (String item : userInfo.getCoachDynamicPics()) {
                ImageView imageView = new ImageView(OtherCustomeMainActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(params);
                ImageLoader.getInstance().displayImage(item, imageView, Options.getListOptions());
                if (linearLayout.getChildCount() < 3) {
                    linearLayout.addView(imageView);
                }
            }
        } else {
            ImageView imageView = new ImageView(OtherCustomeMainActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.mipmap.icon_pic);
            linearLayout.addView(imageView);
        }

        if (!TextUtils.isEmpty(userInfo.getCoachId())){
            llCoachUI.setVisibility(View.VISIBLE);
            tvCoachCaptial.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(userInfo.getResumeContent())) {
            tvCoachBrief.setText(userInfo.getResumeContent());
        }

        if (!TextUtils.isEmpty(userInfo.getStars())) {
            ratingBar.setRating(Float.parseFloat(userInfo.getStars()));
            tvCore.setText(userInfo.getStars());
        }

        if (!TextUtils.isEmpty(userInfo.getCourseCount())) {
            tvTeachedClass.setText(String.format("已授课%s节", new Object[]{userInfo.getCourseCount()}));
        }
        if (!TextUtils.isEmpty(userInfo.getHight())) {
            tvHeight.setText(String.format("身高：%sCM", new Object[]{userInfo.getHight()}));
        }
        if (!TextUtils.isEmpty(userInfo.getWeight())) {
            tvWeight.setText(String.format("体重：%sKG", new Object[]{userInfo.getWeight()}));
        }

        if (!TextUtils.isEmpty(userInfo.getCertificates())) {
            tvCoachInfo.setText(String.format("持有证书：%s", new Object[]{userInfo.getCertificates()}));
        }

        if (!TextUtils.isEmpty(userInfo.getCurClassCount())) {
            tvHisClasses.setText(String.format("正在进行%s个课程", new Object[]{userInfo.getCurClassCount()}));
        }
        if (!TextUtils.isEmpty(userInfo.getIsFoused())) {
            if (userInfo.getIsFoused().equals("0")) {
                tvAttention.setText("关注");
            } else {
                tvAttention.setText("已关注");
            }
        }

        if (!TextUtils.isEmpty(userInfo.getIsFriend())) {
            if (userInfo.getIsFriend().equals("0")) {
                tvaddFriends.setText("加为健身好友");
            } else {
                tvaddFriends.setText("已添加好友");
            }
        }
    }

    private void addLisener() {

        scrollView.getPullRootView().findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvDoAttention = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_attention);
        tvAddFriends = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_add_friends);
        tvDoAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfo != null) {
                    doAttention(uid);
                } else {
                    mSVProgressHUD.showInfoWithStatus(getString(R.string.try_later), SVProgressHUD.SVProgressHUDMaskType.Clear);
                }
            }
        });


        tvAddFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfo != null) {
                    doAddFriends(uid);
                } else {
                    mSVProgressHUD.showInfoWithStatus(getString(R.string.try_later), SVProgressHUD.SVProgressHUDMaskType.Clear);
                }
            }
        });

        scrollView.getPullRootView().findViewById(R.id.tv_send_message).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfo != null) {

                    Bundle bundle = new Bundle();
                    bundle.putString(EaseConstant.EXTRA_USER_ID, "user_" + userInfo.getUid());
                    bundle.putString("name", userInfo.getNickName());
                    bundle.putString("icon", userInfo.getUserPicUrl());
                    String userInfo = SharedPreferencesUtils.getInstance().getString(Constants.USER_INFO, "");
                    UserInfoDetail userInfoDetail;
                    if (!TextUtils.isEmpty(userInfo)) {
                        userInfoDetail = JsonUtils.objectFromJson(userInfo, UserInfoDetail.class);
                        bundle.putString("user_icon", userInfoDetail.getUserPicUrl());
                    } else {
                        bundle.putString("user_icon", "");
                    }
                    openActivity(ChatActivity.class, bundle);
                }

            }
        });

        scrollView.getPullRootView().findViewById(R.id.rl_dynamic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null !=userInfo) {
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.PASS_STRING,userInfo.getUid());
                    openActivity(CustomeDynamicActivity.class,bundle);
                }
            }
        });

        tvCoachMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == userInfo) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, userInfo.getUid());
                openActivity(CoachAuthentitionActivity.class, bundle);
            }
        });


    }

    /***
     * 添加好友
     *
     * @param uid
     */
    private void doAddFriends(String uid) {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        data.put("friendId", uid);
        PostRequest request = new PostRequest(Constants.USER_ADDFRIEND, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc", response.toString());
                mSVProgressHUD.showSuccessWithStatus("已向对方发送申请", SVProgressHUD.SVProgressHUDMaskType.Clear);
                mSVProgressHUD.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(getString(R.string.try_later));
            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(OtherCustomeMainActivity.this);
        mQueue.add(request);
    }

    /******
     * 关注 用户操作
     *
     * @param uid
     */
    private void doAttention(String uid) {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        data.put("focusId", uid);
        PostRequest request = new PostRequest(Constants.USER_ADDFOCUS, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc", response.toString());
                mSVProgressHUD.showSuccessWithStatus(getString(R.string.focus_success), SVProgressHUD.SVProgressHUDMaskType.Clear);
                tvDoAttention.setText("已关注");
                mSVProgressHUD.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage());
            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(OtherCustomeMainActivity.this);
        mQueue.add(request);

    }

    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        View headView = LayoutInflater.from(this).inflate(R.layout.coach_header_ui, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.coach_hade_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.coach_content_ui, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
        scrollView.setParallax(true);
        tvHisClass = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_his_classes);


    }


}
