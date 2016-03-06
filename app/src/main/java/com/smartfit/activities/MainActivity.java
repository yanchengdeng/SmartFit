package com.smartfit.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.R;
import com.smartfit.commons.Constants;
import com.smartfit.fragments.CustomAnimationDemoFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.card_small_class)
    CardView cardSmallClass;
    @Bind(R.id.card_find_private_coach)
    CardView cardFindPrivateCoach;
    @Bind(R.id.card_group_exersise)
    CardView cardGroupExersise;
    @Bind(R.id.card_banner)
    CardView cardBanner;
    @Bind(R.id.card_aerobic_appratus)
    CardView cardAerobicAppratus;
    @Bind(R.id.card_smart_fit)
    CardView cardSmartFit;
    @Bind(R.id.container)
    FrameLayout container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.main_bar_bg);//通知栏所需颜色
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CustomAnimationDemoFragment())
                    .commit();
        }

        addLisener();
    }

    private void addLisener() {
        cardSmallClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.FRAGMENT_POSITION,1);
                openActivity(MainBusinessActivity.class,bundle);
            }
        });

        cardFindPrivateCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.FRAGMENT_POSITION,2);
                openActivity(MainBusinessActivity.class,bundle);
            }
        });

        cardGroupExersise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.FRAGMENT_POSITION,0);
                openActivity(MainBusinessActivity.class,bundle);
            }
        });

        cardAerobicAppratus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.FRAGMENT_POSITION,3);
                openActivity(MainBusinessActivity.class,bundle);
            }
        });
    }

}
