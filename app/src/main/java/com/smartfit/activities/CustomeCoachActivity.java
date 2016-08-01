package com.smartfit.activities;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
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
import com.smartfit.beans.CashTickeInfo;
import com.smartfit.beans.TicketInfo;
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
import com.smartfit.utils.Util;
import com.smartfit.wxapi.WXEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 我的主页 （教练身份）
 */
public class CustomeCoachActivity extends BaseActivity {

    private PullToZoomScrollViewEx scrollView;

    private EventBus eventBus;

    private ImageView imageViewHeader;

    private TextView tvName, tvSigneture, tvClassTittle;
    private TextView tvMouthSeaver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_coach);
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        eventBus = EventBus.getDefault();
        eventBus.register(this);
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
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        LinearLayout.LayoutParams localObject = new LinearLayout.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        scrollView.setHeaderLayoutParams(localObject);
        if (getIntent().getBooleanExtra("hava_buy_mouth", false)) {
            showCashTicketDialog();
        }
    }

    /**
     * 获取现金券id
     * 0:团操课
     * <p/>
     * 1:小班课
     * <p/>
     * 2:私教课
     * <p/>
     * 3:器械课
     * <p/>
     * 4:月卡  可不传
     */
    private void showCashTicketDialog() {
        Map<String, String> data = new HashMap<>();
        data.put("orgType", "4");
//        data.put("orgId",courseId);
        PostRequest request = new PostRequest(Constants.EVENT_GETAVAILABLECASHEVENT, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                CashTickeInfo cashTickeInfo = JsonUtils.objectFromJson(response, CashTickeInfo.class);
                if (cashTickeInfo != null && !TextUtils.isEmpty(cashTickeInfo.getId())) {
                    showCashDialog(cashTickeInfo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(CustomeCoachActivity.this);
        mQueue.add(request);
    }


    private void showCashDialog(final CashTickeInfo cashTickeInfo) {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_cash_ticket_content);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        TextView tvTittle = (TextView) dialog.getWindow().findViewById(R.id.tv_tittle);
        dialog.getWindow().findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.getWindow().findViewById(R.id.commit_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, cashTickeInfo.getId());
                bundle.putString("course_id", getIntent().getStringExtra("coursre_id"));
                bundle.putString(Constants.TICKET_SHARE_TYPE, "2");
                ArrayList<TicketInfo> ticketInfos = new ArrayList<TicketInfo>();
                TicketInfo ticketInfo = new TicketInfo();
                ticketInfo.setEventTitle(cashTickeInfo.getCashEventName());
                ticketInfos.add(ticketInfo);
                bundle.putParcelableArrayList(Constants.PASS_OBJECT, ticketInfos);
                openActivity(WXEntryActivity.class, bundle);

//                showShareWxDialog();

            }
        });
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

    private void loadViewForCode() {
        PullToZoomScrollViewEx scrollView = (PullToZoomScrollViewEx) findViewById(R.id.scroll_view);
        View headView = LayoutInflater.from(this).inflate(R.layout.coach_custome_header_ui, null, false);
        View zoomView = LayoutInflater.from(this).inflate(R.layout.coach_hade_zoom_view, null, false);
        View contentView = LayoutInflater.from(this).inflate(R.layout.coach_custome_content_ui, null, false);
        scrollView.setHeaderView(headView);
        scrollView.setZoomView(zoomView);
        scrollView.setScrollContentView(contentView);
        scrollView.setParallax(true);

        imageViewHeader = (ImageView) scrollView.getPullRootView().findViewById(R.id.iv_header);
        tvName = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_name);
        tvSigneture = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_motto);
        tvClassTittle = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_class_tittle);
        tvMouthSeaver = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_mouth_server);
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
        } else if (event instanceof LoginOut) {
            finish();
        }
    }


    private void initView() {
        getCoachInfo();


        if (SharedPreferencesUtils.getInstance().getBoolean(Constants.OPEN_COACH_AUTH, true)) {
            openAuth();
        } else {
            closeAuth();
        }


        //设置
        scrollView.getPullRootView().findViewById(R.id.iv_setting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SettingActivity.class);
            }
        });

        //礼物
    /*    scrollView.getPullRootView().findViewById(R.id.iv_gift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MyTicketGiftActivity.class);
            }
        });*/

        //  购买包月
        scrollView.getPullRootView().findViewById(R.id.ll_custome_ui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(EventActivityNewVersionActivity.class);
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


        //是否开启教练身份认证   默认 开启
        scrollView.getPullRootView().findViewById(R.id.rl_coach_auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scrollView.getPullRootView().findViewById(R.id.iv_close_coach_auth).getVisibility() == View.VISIBLE) {
                    changeStateLine(true);
                    SharedPreferencesUtils.getInstance().putBoolean(Constants.OPEN_COACH_AUTH, true);
                    openAuth();
                } else {
                    changeStateLine(false);
                    SharedPreferencesUtils.getInstance().putBoolean(Constants.OPEN_COACH_AUTH, false);
                    closeAuth();
                }
            }
        });


     /*   //
        scrollView.getPullRootView().findViewById(R.id.tv_coach_auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CoachDetailInfoActivity.class);
            }
        });*/

        //教练资料认证认证
        imageViewHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CoachDetailInfoActivity.class);
            }
        });


        //工作地点设置
        scrollView.getPullRootView().findViewById(R.id.rl_my_work_please_ui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MyWorkPointActivity.class);
            }
        });


        //钱包
        scrollView.getPullRootView().findViewById(R.id.rl_my_packet_ui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(WalletActivity.class);
            }
        });
        // 卷包
        scrollView.getPullRootView().findViewById(R.id.rl_my_ticket).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MyTicketGiftActivity.class);
            }
        });


        //我发起课程
        scrollView.getPullRootView().findViewById(R.id.rl_my_class_ui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCoachId = SharedPreferencesUtils.getInstance().getBoolean(Constants.OPEN_COACH_AUTH, true);
                if (isCoachId) {
                    openActivity(CoachClassesActivity.class);
                } else {
                    openActivity(MyClassesActivity.class);
                }
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


    private void changeStateLine(boolean isLine) {
        Map<String, String> maps = new HashMap<>();
        if (isLine) {
            maps.put("toState", "1");
        } else {
            maps.put("toState", "0");
        }
        PostRequest request = new PostRequest(Constants.COACH_CHGCOACHONLINESTATE, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                updateClassCount();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(CustomeCoachActivity.this);
        mQueue.add(request);
    }

    /**
     * 获取教练信息
     */
    private void getCoachInfo() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> maps = new HashMap<>();
        maps.put("uid", SharedPreferencesUtils.getInstance().getString(Constants.UID, ""));

        PostRequest request = new PostRequest(Constants.MAIN_PAGE_INFO, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                UserInfo userInfo = JsonUtils.objectFromJson(response, UserInfo.class);
                UserInfoDetail userInfoDetail = Util.getUserInfo(CustomeCoachActivity.this);
                if (null != userInfo) {
                    userInfoDetail.setBalance(userInfo.getBalance());
                    Util.saveUserInfo(userInfoDetail);
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
        request.headers = NetUtil.getRequestBody(CustomeCoachActivity.this);
        mQueue.add(request);
    }

    /**
     * 获取教练信息
     */
    private void updateClassCount() {
        Map<String, String> maps = new HashMap<>();
        maps.put("uid", SharedPreferencesUtils.getInstance().getString(Constants.UID, ""));
        PostRequest request = new PostRequest(Constants.MAIN_PAGE_INFO, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                UserInfo userInfo = JsonUtils.objectFromJson(response, UserInfo.class);
                if (null != userInfo) {
                    TextView tvGoingClasses = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_my_classes);
                    if (!TextUtils.isEmpty(userInfo.getCurClassCount())) {
                        tvGoingClasses.setText("正在进行" + userInfo.getCurClassCount() + "课程");
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(CustomeCoachActivity.this);
        mQueue.add(request);
    }


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
            tvMouthSeaver.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_wo_baoyue));
            tvMouthSeaver.setTextColor(getResources().getColor(R.color.common_header_bg));
            if (!TextUtils.isEmpty(userInfo.getMonthExpiredTime())) {
                long time = Long.parseLong(userInfo.getMonthExpiredTime()) - System.currentTimeMillis() / 1000;
                int day = (int) (time / (60 * 60 * 24));
                tvMouthSeaver.setText(String.format("剩余：%d天", day));
            }
        } else {
            tvVip.setVisibility(View.INVISIBLE);
            tvMouthSeaver.setBackgroundDrawable(getResources().getDrawable(R.drawable.icon_wo_nobaoyue));
            tvMouthSeaver.setTextColor(getResources().getColor(R.color.text_color_black));
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

        TextView tvTicketNum = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_my_ticket);
        if (!TextUtils.isEmpty(userInfo.getTicketNum())) {
            tvTicketNum.setText(userInfo.getTicketNum() + "张未使用");
        }

        TextView tvGoingClasses = (TextView) scrollView.getPullRootView().findViewById(R.id.tv_my_classes);
        if (!TextUtils.isEmpty(userInfo.getCurClassCount())) {
            tvGoingClasses.setText("正在进行" + userInfo.getCurClassCount() + "课程");
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DeviceUtil.dp2px(CustomeCoachActivity.this, 60), DeviceUtil.dp2px(CustomeCoachActivity.this, 60));
        params.topMargin = 16;
        params.leftMargin = 16;
        params.bottomMargin = 16;
        LinearLayout linearLayout = (LinearLayout) scrollView.getPullRootView().findViewById(R.id.ll_pictures);
        if (userInfo.getCoachDynamicPics() != null && userInfo.getCoachDynamicPics().length > 0) {

            for (String item : userInfo.getCoachDynamicPics()) {
                ImageView imageView = new ImageView(CustomeCoachActivity.this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setLayoutParams(params);
                ImageLoader.getInstance().displayImage(item, imageView, Options.getListOptions());
                if (linearLayout.getChildCount() < 3) {
                    linearLayout.addView(imageView);
                }
            }
        } else {
            ImageView imageView = new ImageView(CustomeCoachActivity.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(params);
            imageView.setImageResource(R.mipmap.icon_pic);
            linearLayout.addView(imageView);
        }
        if (!TextUtils.isEmpty(userInfo.getCoachId())) {
            SharedPreferencesUtils.getInstance().putString(Constants.COACH_ID, userInfo.getCoachId());
        }

    }


    private void openAuth() {
        scrollView.getPullRootView().findViewById(R.id.iv_open_coach_auth).setVisibility(View.VISIBLE);
        scrollView.getPullRootView().findViewById(R.id.iv_close_coach_auth).setVisibility(View.GONE);
        scrollView.getPullRootView().findViewById(R.id.rl_my_work_please_ui).setVisibility(View.VISIBLE);
        tvClassTittle.setText(getString(R.string.publish_class_products));
    }

    private void closeAuth() {
        scrollView.getPullRootView().findViewById(R.id.iv_open_coach_auth).setVisibility(View.GONE);
        scrollView.getPullRootView().findViewById(R.id.iv_close_coach_auth).setVisibility(View.VISIBLE);
        scrollView.getPullRootView().findViewById(R.id.rl_my_work_please_ui).setVisibility(View.GONE);
        tvClassTittle.setText(getString(R.string.my_classes));
    }

    private void addLisener() {

    }
}
