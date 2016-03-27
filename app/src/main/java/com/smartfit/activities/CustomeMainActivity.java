package com.smartfit.activities;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.google.gson.JsonObject;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.R;
import com.smartfit.commons.Constants;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 个人主页
 */
public class CustomeMainActivity extends BaseActivity {
    private PullToZoomScrollViewEx scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_main);
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
        getCustomeInfo();
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
    }

    private void getCustomeInfo() {
        Map<String, String> data = new HashMap<>();

        PostRequest request = new PostRequest(Constants.COACH_INFO, NetUtil.getRequestBody(data,this), new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc",response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage());
            }
        });
        request.setTag(new Object());
        mQueue.add(request);
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
