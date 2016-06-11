package com.smartfit.activities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
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
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.igexin.sdk.PushManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.MessageEvent.HxMessageList;
import com.smartfit.R;
import com.smartfit.SmartAppliction;
import com.smartfit.beans.AttentionBean;
import com.smartfit.beans.LingyunListInfo;
import com.smartfit.beans.LinyuCourseInfo;
import com.smartfit.beans.LinyuRecord;
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

import org.greenrobot.eventbus.EventBus;

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

    private EventBus eventBus;


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

        eventBus = EventBus.getDefault();


        String nativeCity = SharedPreferencesUtils.getInstance().getString(Constants.CITY_NAME, "");
        initLocation();
        if (TextUtils.isEmpty(nativeCity)) {

        } else {
            tvCityName.setText(nativeCity);
            getVenuList();
        }

        if (!TextUtils.isEmpty(SharedPreferencesUtils.getInstance().getString(Constants.UID, ""))) {
            getUserInfo();

        }

        addLisener();

        initConnectLisener();


        EMClient.getInstance().chatManager().addMessageListener(msgListener);

        if (NetUtil.isLogin(this)) {
            getShowerRecord();
        }


    }

    /**
     * 获取淋浴欠费接口
     */
    private void getShowerRecord() {
        PostRequest request = new PostRequest(Constants.TBEVENTRECORD_GETSHOWERRECORD, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc==", response.toString());
                LingyunListInfo lingyunListInfo = JsonUtils.objectFromJson(response, LingyunListInfo.class);
                if (lingyunListInfo != null && lingyunListInfo.getListData() != null && lingyunListInfo.getListData().size() > 0) {
                    createLinyuOrder(lingyunListInfo.getListData());
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
        request.headers = NetUtil.getRequestBody(MainActivity.this);
        mQueue.add(request);

    }

    /**
     * 生成淋浴订单
     *
     * @param listData
     */
    private void createLinyuOrder(final List<LinyuRecord> listData) {
        StringBuilder sbID =new StringBuilder();
        for (LinyuRecord item:listData){
            sbID.append(item.getRecordId()).append("|");
        }
        Map<String, String> map = new HashMap<>();
        map.put("recordIdStr", sbID.toString());
        PostRequest request = new PostRequest(Constants.ORDER_ORDERSHOWER, map,new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc==", response.toString());
                LinyuCourseInfo linyuCourseInfo = JsonUtils.objectFromJson(response,LinyuCourseInfo.class);
                if (linyuCourseInfo!=null){
                    Util.showLinyuRechagerDialog(MainActivity.this, linyuCourseInfo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.w("dyc", error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(MainActivity.this);
        mQueue.add(request);

    }


    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            //收到消息

            LogUtil.w("dyc", "收到消息1" + messages.get(0).getBody().toString());

            LogUtil.w("dyc", "--ss---" + Util.isInMessageList(MainActivity.this));
            if (Util.isInMessageList(MainActivity.this)) {
                eventBus.post(new HxMessageList(messages));
            } else {
                showNotificaiton(MainActivity.this, messages.get(0).getBody().toString().split(":")[1].replace("\"", ""));
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            LogUtil.w("dyc", "收到消息2" + messages.get(0).getBody().toString());
        }

        @Override
        public void onMessageReadAckReceived(List<EMMessage> messages) {
            //收到已读回执
            LogUtil.w("dyc", "收到消息3" + messages.get(0).getBody().toString());
        }

        @Override
        public void onMessageDeliveryAckReceived(List<EMMessage> message) {
            //收到已送达回执
            LogUtil.w("dyc", "收到消息4" + message.get(0).getBody().toString());
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
            LogUtil.w("dyc", "收到消息5" + message.getBody().toString());
        }
    };


    private void showNotificaiton(Context context, String msg) {
        Bitmap btm = BitmapFactory.decodeResource(context.getResources(),
                R.mipmap.message_icon);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.mipmap.message_icon)
                .setContentTitle("SmartFit")
                .setContentText(msg);
        mBuilder.setTicker(msg);//第一次提示消息的时候显示在通知栏上
        mBuilder.setNumber(12);
        mBuilder.setLargeIcon(btm);
        mBuilder.setAutoCancel(true);//自己维护通知的消失

        //构建一个Intent
        Intent resultIntent = new Intent(context,
                MessageActivity.class);
        //封装一个Intent
        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                context, 0, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        // 设置通知主题的意图
        mBuilder.setContentIntent(resultPendingIntent);
        //获取通知管理器对象
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }


    /**
     * 注册环信链接监听
     */
    private void initConnectLisener() {
        EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }

    //实现ConnectionListener接口
    private class MyConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (error == EMError.USER_REMOVED) {
                        // 显示帐号已经被移除
                        mSVProgressHUD.showInfoWithStatus("账号已被移除", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                        // 显示帐号在其他设备登陆
                        SharedPreferencesUtils.getInstance().remove(Constants.UID);
                        SharedPreferencesUtils.getInstance().remove(Constants.SID);
                        SharedPreferencesUtils.getInstance().remove(Constants.PASSWORD);

                        EMClient.getInstance().logout(true);
                        NormalDialogOneBtn();
                    }
                }
            });
        }
    }

    private void NormalDialogOneBtn() {

        final NormalDialog dialog = new NormalDialog(SmartAppliction.getInstance());
        if (dialog.isShowing()) {

        } else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.content("您的账号已在其他设备登陆")//
                    .btnNum(1)
                    .btnText("确定")//
//                .showAnim(mBasIn)//
//                .dismissAnim(mBasOut)//
                    .show();

            dialog.setOnBtnClickL(new OnBtnClickL() {
                @Override
                public void onBtnClick() {
                    dialog.dismiss();
                    openActivity(LoginActivity.class);

                }
            });
        }
    }

    private void getFriendsInfo() {
        PostRequest request = new PostRequest(Constants.USER_FRIENDLIST, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                List<AttentionBean> beans = JsonUtils.listFromJson(response.getAsJsonArray("list"), AttentionBean.class);
                if (beans != null && beans.size() > 0) {
                    SharedPreferencesUtils.getInstance().putString(Constants.LOCAL_FRIENDS_LIST, JsonUtils.toJson(beans));

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage());
            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(MainActivity.this);
        mQueue.add(request);
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
                        getFriendsInfo();
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
        cardSmartFit.setOnClickListener(new View.OnClickListener() {
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

        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
    }
}
