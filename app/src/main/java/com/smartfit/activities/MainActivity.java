package com.smartfit.activities;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.JsonObject;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.R;
import com.smartfit.SmartAppliction;
import com.smartfit.commons.Constants;
import com.smartfit.fragments.CustomAnimationDemoFragment;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements AMapLocationListener {

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
    @Bind(R.id.tv_city_name)
    TextView tvCityName;
    @Bind(R.id.iv_search)
    ImageView ivSearch;

    private static final Object TAG = new Object();


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

        initLocation();

        addLisener();
    }

    private void addLisener() {
        cardSmallClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.FRAGMENT_POSITION, 1);
                openActivity(MainBusinessActivity.class, bundle);
            }
        });

        cardFindPrivateCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.FRAGMENT_POSITION, 2);
                openActivity(MainBusinessActivity.class, bundle);
            }
        });

        cardGroupExersise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.FRAGMENT_POSITION, 0);
                openActivity(MainBusinessActivity.class, bundle);
            }
        });

        cardAerobicAppratus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.FRAGMENT_POSITION, 3);
                openActivity(MainBusinessActivity.class, bundle);
            }
        });

        //城市跳转
        tvCityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                openActivity(CityListActivity.class);
                mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
                Map<String, String> data = new HashMap<>();
                data.put("mobileNo", "13067389836");
                data.put("password", "123456");
                String body = NetUtil.getRequestBody(data,mContext);
                PostRequest request = new PostRequest(Constants.LOGIN_IN_METHOD,body, new Response.Listener<JsonObject>() {
                    @Override
                    public void onResponse(JsonObject response) {
                        mSVProgressHUD.dismiss();
                        LogUtil.d("dyc", response.toString());

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mSVProgressHUD.dismiss();
                        mSVProgressHUD.showInfoWithStatus(error.getLocalizedMessage());
                    }
                });
                request.setTag(TAG);
                mQueue.add(request);
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        NormalDialogStyleOne();
    }

    private void NormalDialogStyleOne() {
        final NormalDialog dialog = new NormalDialog(MainActivity.this);
        dialog.isTitleShow(false)//
                .bgColor(Color.parseColor("#383838"))//
                .cornerRadius(5)//
                .content("是否确定退出程序?")//
                .contentGravity(Gravity.CENTER)//
                .contentTextColor(Color.parseColor("#ffffff"))//
                .dividerColor(Color.parseColor("#222222"))//
                .btnTextSize(15.5f, 15.5f)//
                .btnTextColor(Color.parseColor("#ffffff"), Color.parseColor("#ffffff"))//
                .btnPressColor(Color.parseColor("#2B2B2B"))//
                .widthScale(0.85f)//
//                .showAnim(mBasIn)//
//                .dismissAnim(mBasOut)//
                .show();
        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                        finish();
                    }
                });
    }

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;


    private void initLocation() {

        locationClient = new AMapLocationClient(SmartAppliction.getInstance());
        locationOption = new AMapLocationClientOption();
        // 设置定位模式为高精度模式
        locationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        locationOption.setHttpTimeOut(5000);
        // 设置定位监听
        locationClient.setLocationListener(this);
        locationOption.setOnceLocation(true);
        //启动定位
        locationClient.startLocation();


    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (null != aMapLocation) {
            if (aMapLocation.getErrorCode() == 0) {
                tvCityName.setText(aMapLocation.getCity());
            } else {
                tvCityName.setText("定位失败");
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }
}
