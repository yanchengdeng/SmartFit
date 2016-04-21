package com.smartfit.activities;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.R;
import com.smartfit.beans.UserInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
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
    }

    String uid;

    private void initView() {
        scrollView.getPullRootView().findViewById(R.id.tv_teach_capacity).setVisibility(View.GONE);
        scrollView.getPullRootView().findViewById(R.id.tv_coach_capacity).setVisibility(View.GONE);

        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        uid = getIntent().getExtras().getString(Constants.PASS_STRING);
        Map<String, String> maps = new HashMap<>();
        maps.put("uid", uid);
        maps.put("isCoach", "0");
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
                mSVProgressHUD.showErrorWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(OtherCustomeMainActivity.this);
        mQueue.add(request);

    }

    UserInfo userInfo;
    TextView tvDoAttention, tvAddFriends;//操作关注用户 /添加健身伙伴

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
            TextView tvFoucs = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_attentioon);
            tvFoucs.setText("关注  " + userInfo.getFocusCount());
        }

        if (!TextUtils.isEmpty(userInfo.getFansCount())) {
            TextView tvfans = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_fans);
            tvfans.setText("粉丝  " + userInfo.getFansCount());
        }

        if (!TextUtils.isEmpty(userInfo.getIsFoused())) {
            if (userInfo.getIsFoused().equals("0")) {
                tvAddFriends.setText("关注");
            } else {
                tvAddFriends.setText("已关注");
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
            if (userInfo.getSex().equals("0")) {
                tvNickname.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.icon_woman),null);
            }else{
                tvNickname.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.icon_man),null);
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
                tvAddFriends.setText("已关注");
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

    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        View headView = LayoutInflater.from(this).inflate(R.layout.coach_header_ui, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.coach_hade_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.un_coach_content_ui, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
        scrollView.setParallax(true);


    }


}
