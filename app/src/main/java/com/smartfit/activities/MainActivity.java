package com.smartfit.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.igexin.sdk.PushManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.R;
import com.smartfit.SmartAppliction;
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.beans.WorkPointAddress;
import com.smartfit.commons.AppManager;
import com.smartfit.commons.Constants;
import com.smartfit.fragments.CustomAnimationDemoFragment;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.MD5;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.utils.Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements AMapLocationListener {

    @Bind(R.id.card_small_class)
    LinearLayout cardSmallClass;
    @Bind(R.id.card_find_private_coach)
    LinearLayout cardFindPrivateCoach;
    @Bind(R.id.card_group_exersise)
    LinearLayout cardGroupExersise;
    @Bind(R.id.card_banner)
    LinearLayout cardBanner;
    @Bind(R.id.card_aerobic_appratus)
    LinearLayout cardAerobicAppratus;
    @Bind(R.id.card_smart_fit)
    LinearLayout cardSmartFit;
    @Bind(R.id.container)
    FrameLayout container;
    @Bind(R.id.tv_city_name)
    TextView tvCityName;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.et_search_content)
    EditText etSearchContent;
    private static final Object TAG = new Object();
    @Bind(R.id.tv_go_class)
    TextView tvGoClass;
    @Bind(R.id.tv_go_purse)
    TextView tvGoPurse;
    @Bind(R.id.tv_go_server)
    TextView tvGoServer;
    @Bind(R.id.ll_activity_bind_up)
    LinearLayout llActivityBindUp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        PushManager.getInstance().initialize(this.getApplicationContext());
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.common_header_bg);//通知栏所需颜色
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CustomAnimationDemoFragment())
                    .commit();
        }

        String nativeCity = SharedPreferencesUtils.getInstance().getString(Constants.CITY_NAME, "");
        if (TextUtils.isEmpty(nativeCity)) {
            initLocation();
        } else {
            tvCityName.setText(nativeCity);
            getVenuList();
        }

        if (!TextUtils.isEmpty(SharedPreferencesUtils.getInstance().getString(Constants.UID, ""))) {
            getUserInfo();
        }

        addLisener();

    }


    private void getUserInfo() {
        String account = SharedPreferencesUtils.getInstance().getString(Constants.ACCOUNT, "");
        String password = SharedPreferencesUtils.getInstance().getString(Constants.PASSWORD, "");
        if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(password)) {
            Map<String, String> data = new HashMap<>();
            data.put("mobileNo", account);
            data.put("password", MD5.getMessageDigest(password.getBytes()));
            PostRequest request = new PostRequest(Constants.LOGIN_IN_METHOD, data, new Response.Listener<JsonObject>() {
                @Override
                public void onResponse(final JsonObject response) {
                    UserInfoDetail userInfoDetail = JsonUtils.objectFromJson(response, UserInfoDetail.class);
                    if (userInfoDetail != null) {
                        SharedPreferencesUtils.getInstance().putString(Constants.SID, userInfoDetail.getSid());
                        SharedPreferencesUtils.getInstance().putString(Constants.UID, userInfoDetail.getUid());
                        SharedPreferencesUtils.getInstance().putString(Constants.IS_ICF, userInfoDetail.getIsICF());
                        SharedPreferencesUtils.getInstance().putString(Constants.USER_INFO, JsonUtils.toJson(userInfoDetail));
                        if (!TextUtils.isEmpty(userInfoDetail.getCoachId())) {
                            SharedPreferencesUtils.getInstance().putString(Constants.COACH_ID, userInfoDetail.getCoachId());
                        }
                        LoginHX(userInfoDetail.getUid());
                        String client = SharedPreferencesUtils.getInstance().getString(Constants.CLINET_ID, "");
                        if (!TextUtils.isEmpty(client) && NetUtil.isLogin(MainActivity.this)) {
                            bindClient(client);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            request.setTag(TAG);
            request.headers = NetUtil.getRequestBody(MainActivity.this);
            mQueue.add(request);
        }

    }

    private void bindClient(String client) {

        Map<String, String> msp = new HashMap();
        msp.put("clientId", client);
        PostRequest request = new PostRequest(Constants.USER_SYNCLIENTIID, msp, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(MainActivity.this);
        mQueue.add(request);
    }

    private void LoginHX(String uid) {
        String hxAccount = "user_" + uid;
        //登录
        EMClient.getInstance().login(hxAccount, MD5.getMessageDigest(hxAccount.getBytes()), new EMCallBack() {
            @Override
            public void onSuccess() {
                LogUtil.w("dyc", "环信登陆陈宫");
            }

            @Override
            public void onError(int i, String s) {
                LogUtil.w("dyc", "环信登陆失败" + i + "..." + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }


    private void addLisener() {
        cardSmallClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtil.isLogin(getApplicationContext())) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.FRAGMENT_POSITION, 1);
                    openActivity(MainBusinessActivity.class, bundle);
                } else {
                    openActivity(LoginActivity.class);
                }
            }
        });

        cardFindPrivateCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtil.isLogin(getApplicationContext())) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.FRAGMENT_POSITION, 2);
                    openActivity(MainBusinessActivity.class, bundle);
                } else {
                    openActivity(LoginActivity.class);
                }
            }
        });

        //活动买入上
        llActivityBindUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtil.isLogin(getApplicationContext())) {
                    openActivity(ActivityListActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
            }
        });

        cardGroupExersise.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View v) {
                                                     if (NetUtil.isLogin(getApplicationContext())) {
                                                         Bundle bundle = new Bundle();
                                                         bundle.putInt(Constants.FRAGMENT_POSITION, 0);
                                                         openActivity(MainBusinessActivity.class, bundle);
                                                     } else {
                                                         openActivity(LoginActivity.class);
                                                     }
                                                 }
                                             }

        );

        cardAerobicAppratus.setOnClickListener(new View.OnClickListener()

                                               {
                                                   @Override
                                                   public void onClick(View v) {
                                                       if (NetUtil.isLogin(getApplicationContext())) {
                                                           Bundle bundle = new Bundle();
                                                           bundle.putInt(Constants.FRAGMENT_POSITION, 3);
                                                           openActivity(MainBusinessActivity.class, bundle);
                                                       } else {
                                                           openActivity(LoginActivity.class);
                                                       }
                                                   }
                                               }

        );

        //自订课程
        cardSmartFit.setOnClickListener(new View.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(View v) {
                                                //TODO 计入  活动
                                                if (NetUtil.isLogin(getApplicationContext())) {
//                                                    openActivity(CustomeClassActivity.class);
                                                    openActivity(ActivityListActivity.class);
                                                } else {
                                                    openActivity(LoginActivity.class);
                                                }
                                            }
                                        }
        );

        //广告
        //TODO  进入活动
        cardBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtil.isLogin(getApplicationContext())) {
                    openActivity(ActivityListActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
            }
        });

        //城市跳转
        tvCityName.setOnClickListener(new View.OnClickListener()

                                      {
                                          @Override
                                          public void onClick(View v) {
                                              openActivity(CityListActivity.class, 10);
                                          }
                                      }

        );

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key", etSearchContent.getEditableText().toString());
                openActivity(SearchClassActivity.class, bundle);
            }
        });

        tvGoClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtil.isLogin(MainActivity.this)) {
                    openActivity(MainBusinessActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
            }
        });

        tvGoPurse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetUtil.isLogin(MainActivity.this)) {
                    openActivity(WalletActivity.class);
                } else {
                    openActivity(LoginActivity.class);
                }
            }
        });

        tvGoServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NormalDialogStyleTwo();
            }
        });
    }


    private void NormalDialogStyleTwo() {
        final NormalDialog dialog = new NormalDialog(MainActivity.this);
        dialog.content("确定要拨打客服电话吗？")//
                .style(NormalDialog.STYLE_TWO)//
                .titleTextSize(23)//
                        //.showAnim(mBasIn)//
                        //.dismissAnim(mBasOut)//
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
                        try {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +
                                    getString(R.string.contact_phone_server)));
                            startActivity(intent);
                        } catch (Exception E) {
                            mSVProgressHUD.showInfoWithStatus("您的设备没有打电话功能哦~", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        }
                    }
                });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return doubleClickExist();
        }
        return super.onKeyDown(keyCode, event);
    }

    private long mExitTime;

    /****
     * 连续两次点击退出
     */
    private boolean doubleClickExist() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
            return true;
        } else {
            AppManager.getAppManager().AppExit(this);
            finish();
        }

        return false;
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
                locationClient.stopLocation();
                LogUtil.w("dyc", "定位：" + aMapLocation.getCity());
                tvCityName.setText(aMapLocation.getCity());
                SharedPreferencesUtils.getInstance().putString(Constants.CITY_LAT, String.format("%.4f", aMapLocation.getLatitude()));
                SharedPreferencesUtils.getInstance().putString(Constants.CITY_LONGIT, String.format("%.4f", aMapLocation.getLongitude()));
            } else {
                if (TextUtils.isEmpty(SharedPreferencesUtils.getInstance().getString(Constants.CITY_NAME, ""))) {
                    tvCityName.setText(SharedPreferencesUtils.getInstance().getString(Constants.CITY_NAME, ""));
                } else {
                    tvCityName.setText("定位失败");
                }
            }
        }
        if (!tvCityName.getText().toString().equals("定位失败")) {
            String cityName = tvCityName.getText().toString();
            if (cityName.contains("市")) {
                cityName = cityName.substring(0, cityName.length() - 1);
            }
            Util.setCityInfo(cityName);
            getVenuList();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == 11) {
            String city = SharedPreferencesUtils.getInstance().getString(Constants.CITY_NAME, "");
            String code = SharedPreferencesUtils.getInstance().getString(Constants.CITY_CODE, "");
            tvCityName.setText(city);
            LogUtil.w("dyc", "执行--" + city + ".." + code);
            getVenuList();
        }
    }

    /**
     * 获取场馆信息
     *
     * @param
     */
    private void getVenuList() {
        Map<String, String> data = new HashMap<>();
        data.put("cityCode", SharedPreferencesUtils.getInstance().getString(Constants.CITY_CODE, ""));
        PostRequest request = new PostRequest(Constants.GET_VENUElIST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                List<WorkPointAddress> reqeustAddresses = JsonUtils.listFromJson(response.getAsJsonArray("list"), WorkPointAddress.class);
                if (reqeustAddresses != null && reqeustAddresses.size() > 0) {
                    SharedPreferencesUtils.getInstance().putString(Constants.VENUE_LIST_INFO, JsonUtils.toJson(reqeustAddresses));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.setTag(MainActivity.TAG);
        request.headers = NetUtil.getRequestBody(MainActivity.this);
        mQueue.add(request);
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
