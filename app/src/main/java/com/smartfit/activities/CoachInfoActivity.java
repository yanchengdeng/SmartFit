package com.smartfit.activities;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.R;
import com.smartfit.adpters.CoachAppraiseAdapter;
import com.smartfit.views.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 教练详情页
 */
public class CoachInfoActivity extends BaseActivity {
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

    private void getCoachInfo() {


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

    }

    MyListView myListView;
    RatingBar ratingBar;
    View tvMoreAppraise,tvCoachMoreInfo;

    private void initView() {
        myListView = (MyListView) scrollView.getPullRootView().findViewById(R.id.listView);
        List<String> appraises = new ArrayList<>();
        appraises.add("");
        myListView.setAdapter(new CoachAppraiseAdapter(this, appraises));
        tvMoreAppraise = scrollView.getPullRootView().findViewById(R.id.tv_read_more_discuss);
        tvCoachMoreInfo = scrollView.getPullRootView().findViewById(R.id.tv_read_more_info);
        ratingBar = (RatingBar) scrollView.getPullRootView().findViewById(R.id.ratingBar);
        ratingBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 24));
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                scrollView.scrollTo(0, 0);
            }
        });

    }


    private void addLisener(){

        scrollView.getPullRootView().findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvMoreAppraise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CoachAppraiseActivity.class);
            }
        });

        tvCoachMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CoachAuthentitionActivity.class);
            }
        });


        scrollView.getPullRootView().findViewById(R.id.rl_dynamic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CustomeDynamicActivity.class);
            }
        });

        //教练进行的课程
        scrollView.getPullRootView().findViewById(R.id.tv_his_classes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CoachClassesActivity.class);
            }
        });

    }
}
