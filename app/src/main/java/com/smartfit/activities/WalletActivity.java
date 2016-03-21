package com.smartfit.activities;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ecloud.pulltozoomview.PullToZoomListViewEx;
import com.ecloud.pulltozoomview.PullToZoomScrollViewEx;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.R;
import com.smartfit.adpters.WalletAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 钱包
 */
public class WalletActivity extends BaseActivity {
    private PullToZoomListViewEx listView;


    private WalletAdapter walletAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.bar_regiter_bg);//通知栏所需颜色
        listView = (PullToZoomListViewEx) findViewById(R.id.listview);
        initView();
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (12.0F * (mScreenWidth / 16.0F)));
        listView.setHeaderLayoutParams(localObject);
        listView.setParallax(true);
        ListView lv =listView.getPullRootView();
        listView.setBackgroundColor(getResources().getColor(R.color.gray_light));
        lv.addHeaderView(LayoutInflater.from(this).inflate(R.layout.wallet_listview_header,null));
        lv.setHeaderDividersEnabled(false);
        lv.setFooterDividersEnabled(false);
        lv.setDividerHeight(0);
    }

    private void initView() {
        List<String> datas = new ArrayList<>();
        for(int i = 0 ;i<2;i++){
            datas.add("");
        }
        walletAdapter = new WalletAdapter(datas,this);
        listView.setAdapter(walletAdapter);
        listView.getPullRootView().findViewById(R.id.btn_recharge).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ReChargeActivity.class);
            }
        });

        listView.getPullRootView().findViewById(R.id.btn_withdraw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(WithDrawActivity.class);
            }
        });

        listView.getPullRootView().findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
