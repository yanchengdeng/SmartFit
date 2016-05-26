package com.smartfit.activities;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.MessageEvent.LoginOut;
import com.smartfit.MessageEvent.LoginSuccess;
import com.smartfit.MessageEvent.UpdateCoachInfo;
import com.smartfit.R;
import com.smartfit.beans.UserInfo;
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.commons.Constants;
import com.smartfit.fragments.CustomAnimationDemoFragment;
import com.smartfit.utils.DeviceUtil;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人主页
 */
public class CustomeMainActivity extends BaseActivity {
    private PullToZoomScrollViewEx scrollView;

    private EventBus eventBus;

    private ImageView imageViewHeader;

    private TextView tvName, tvSigneture;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_main);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.bar_regiter_bg);//通知栏所需颜色
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CustomAnimationDemoFragment())
                    .commit();
        }
        loadViewForCode();
        scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        initView();
        addLisener();
        getCustomeInfo();
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
    }

    @Override
    public void onResume() {
        super.onResume();
        String userinfo = SharedPreferencesUtils.getInstance().getString(Constants.USER_INFO, "");
        if (!TextUtils.isEmpty(userinfo)) {
            UserInfoDetail userInfoDetail = JsonUtils.objectFromJson(userinfo, UserInfoDetail.class);
            if (userInfoDetail != null) {
                TextView tvBallence = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_my_pocket);
                if (!TextUtils.isEmpty(userInfoDetail.getBalance())) {
                    tvBallence.setText("余额" + userInfoDetail.getBalance() + "元");
                }
            }
        }
    }

    private void getCustomeInfo() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> maps = new HashMap<>();
        maps.put("uid", SharedPreferencesUtils.getInstance().getString(Constants.UID, ""));
        maps.put("isCoach", "0");
        PostRequest request = new PostRequest(Constants.MAIN_PAGE_INFO, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                UserInfo userInfo = JsonUtils.objectFromJson(response, UserInfo.class);
                if (null != userInfo) {
                    fillData(userInfo);
                }

                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(CustomeMainActivity.this);
        mQueue.add(request);
    }

    @Subscribe
    public void onEvent(Object event) {/* Do something */
        if (event instanceof UpdateCoachInfo) {
            if (!TextUtils.isEmpty(((UpdateCoachInfo) event).getUserPicUrl())) {
                ImageLoader.getInstance().displayImage(((UpdateCoachInfo) event).getUserPicUrl(), imageViewHeader, Options.getHeaderOptions());
            }

            if (!TextUtils.isEmpty(((UpdateCoachInfo) event).getNickName())) {
                tvName.setText(((UpdateCoachInfo) event).getNickName());
            }

            if (!TextUtils.isEmpty(((UpdateCoachInfo) event).getSignature())) {
                tvSigneture.setText(((UpdateCoachInfo) event).getSignature());
            }
        } else if (event instanceof LoginSuccess) {
            finish();
        }else if(event instanceof LoginOut){
            finish();
        }
    }

    ;

    /**
     * 填充页面数据
     *
     * @param userInfo
     */
    private void fillData(UserInfo userInfo) {

        ImageView ivHeader = (ImageView) scrollView.getPullRootView().findViewById(R.id.iv_header);
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

        TextView tvFocus = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_attention_num);
        if (!TextUtils.isEmpty(userInfo.getFocusCount())) {
            tvFocus.setText(userInfo.getFocusCount());
        }

        TextView tvFuns = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_fans_num);
        if (!TextUtils.isEmpty(userInfo.getFansCount())) {
            tvFuns.setText(userInfo.getFansCount());
        }

        TextView tvFriends = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_friends_num);
        if (!TextUtils.isEmpty(userInfo.getFriendCount())) {
            tvFriends.setText(userInfo.getFriendCount());
        }


        TextView tvPocket = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_my_pocket);
        if (!TextUtils.isEmpty(userInfo.getBalance())) {
            tvPocket.setText("余额" + userInfo.getBalance() + "元");
        }

        TextView tvGoingClasses = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_my_classes);
        if (!TextUtils.isEmpty(userInfo.getCurClassCount())) {
            tvGoingClasses.setText("正在进行" + userInfo.getCurClassCount() + "课程");
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DeviceUtil.dp2px(CustomeMainActivity.this, 60), DeviceUtil.dp2px(CustomeMainActivity.this, 60));
        params.topMargin = 16;
        params.leftMargin = 16;
        params.bottomMargin = 16;
        LinearLayout linearLayout = (LinearLayout) scrollView.getPullRootView().findViewById(R.id.ll_pictures);
        if (userInfo.getCoachDynamicPics() != null && userInfo.getCoachDynamicPics().length > 0) {

            for (String item : userInfo.getCoachDynamicPics()) {
                ImageView imageView = new ImageView(CustomeMainActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(params);
                ImageLoader.getInstance().displayImage(item, imageView, Options.getListOptions());
                if (linearLayout.getChildCount() < 3) {
                    linearLayout.addView(imageView);
                }
            }
        } else {
            ImageView imageView = new ImageView(CustomeMainActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.mipmap.icon_pic);
            linearLayout.addView(imageView);
        }

    }


    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        View headView = LayoutInflater.from(this).inflate(R.layout.custome_header_ui, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.coach_hade_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.custome_content_ui, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
        scrollView.setParallax(true);
        imageViewHeader = (ImageView) scrollView.getPullRootView().findViewById(R.id.iv_header);
        tvName = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_name);
        tvSigneture = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_motto);

    }

    private void initView() {
        //设置
        scrollView.getPullRootView().findViewById(R.id.iv_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SettingActivity.class);
            }
        });

        //礼物
        scrollView.getPullRootView().findViewById(R.id.iv_gift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MyTicketGiftActivity.class);
            }
        });


        //个人详情
        scrollView.getPullRootView().findViewById(R.id.tv_motto).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CustomDetailInfoActivity.class);
            }
        });
        //关注
        scrollView.getPullRootView().findViewById(R.id.ll_attenion_ui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(AttentionListActivity.class);
            }
        });

        //粉丝
        scrollView.getPullRootView().findViewById(R.id.ll_fans_ui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(FansActivity.class);
            }
        });


        //健身伙伴
        scrollView.getPullRootView().findViewById(R.id.ll_friends_ui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(HealthFriendsListActivity.class);
            }
        });


        //钱包
        scrollView.getPullRootView().findViewById(R.id.rl_my_packet_ui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(WalletActivity.class);
            }
        });


        //我的课程
        scrollView.getPullRootView().findViewById(R.id.rl_my_class_ui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MyClassesActivity.class);
            }
        });

        //动态
        scrollView.getPullRootView().findViewById(R.id.rl_my_dynamic_ui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CustomeDynamicActivity.class);
            }
        });

        //客服
        scrollView.getPullRootView().findViewById(R.id.rl_my_serve_ui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(HelpActivity.class);
            }
        });
    }

    private void addLisener() {

    }
}
