package com.smartfit.activities;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.MessageEvent.LoginOut;
import com.smartfit.R;
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.utils.Util;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 设置页
 */
public class SettingActivity extends BaseActivity {

    @Bind(R.id.iv_header)
    ImageView ivHeader;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.iv_accept_msg_not)
    ImageView ivAcceptMsgNot;
    @Bind(R.id.iv_accept_msg)
    ImageView ivAcceptMsg;
    @Bind(R.id.iv_accept_ring_not)
    ImageView ivAcceptRingNot;
    @Bind(R.id.iv_accept_ring)
    ImageView ivAcceptRing;
    @Bind(R.id.iv_accept_quiet_not)
    ImageView ivAcceptQuietNot;
    @Bind(R.id.iv_quiet_msg)
    ImageView ivQuietMsg;
    @Bind(R.id.tv_about_us)
    TextView tvAboutUs;
    @Bind(R.id.tv_check_udpate)
    TextView tvCheckUdpate;
    @Bind(R.id.tv_goal_score)
    TextView tvGoalScore;
    @Bind(R.id.tv_logout)
    TextView tvLogout;
    @Bind(R.id.iv_close)
    ImageView ivClose;
    @Bind(R.id.rl_msg_ui)
    RelativeLayout rlMsgUi;
    @Bind(R.id.rl_ring_ui)
    RelativeLayout rlRingUi;
    @Bind(R.id.rl_quite_ui)
    RelativeLayout rlQuiteUi;
    private EventBus eventBus;


    UserInfoDetail userInfoDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.bar_regiter_bg);//通知栏所需颜色
        initView();
        addLisener();

    }

    @Override
    public void onResume() {
        super.onResume();
        String userInfo = SharedPreferencesUtils.getInstance().getString(Constants.USER_INFO, "");
        if (!TextUtils.isEmpty(userInfo)) {
            UserInfoDetail userInfoDetail = JsonUtils.objectFromJson(userInfo, UserInfoDetail.class);
            ImageLoader.getInstance().displayImage(userInfoDetail.getUserPicUrl(), ivHeader, Options.getListOptions());
            if (!TextUtils.isEmpty(userInfoDetail.getNickName())) {
                tvName.setText(userInfoDetail.getNickName());
            }
        }
    }

    private void initView() {
        userInfoDetail = Util.getUserInfo(SettingActivity.this);
        if (userInfoDetail != null) {
            if (userInfoDetail.getOpenPush().equals("0")) {
                ivAcceptMsg.setVisibility(View.VISIBLE);
                ivAcceptMsgNot.setVisibility(View.GONE);
            } else {
                ivAcceptMsg.setVisibility(View.GONE);
                ivAcceptMsgNot.setVisibility(View.VISIBLE);
            }

            if (userInfoDetail.getOpenSound().equals("0")) {
                ivAcceptRing.setVisibility(View.VISIBLE);
                ivAcceptRingNot.setVisibility(View.GONE);
            } else {
                ivAcceptRing.setVisibility(View.GONE);
                ivAcceptRingNot.setVisibility(View.VISIBLE);
            }
        }


    }

    private void addLisener() {
        rlMsgUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfoDetail != null) {
                    doChangePush(userInfoDetail.getOpenPush());
                } else {
                    mSVProgressHUD.showInfoWithStatus(getString(R.string.no_login), SVProgressHUD.SVProgressHUDMaskType.Clear);
                }

            }
        });


        rlRingUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userInfoDetail != null) {
                    doRingPush(userInfoDetail.getOpenSound());
                } else {
                    mSVProgressHUD.showInfoWithStatus(getString(R.string.no_login), SVProgressHUD.SVProgressHUDMaskType.Clear);
                }
            }
        });


        rlQuiteUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivQuietMsg.getVisibility() == View.VISIBLE) {
                    ivQuietMsg.setVisibility(View.GONE);
                    ivAcceptQuietNot.setVisibility(View.VISIBLE);
                    mSVProgressHUD.showInfoWithStatus("关闭震动");
                } else {
                    ivQuietMsg.setVisibility(View.VISIBLE);
                    ivAcceptQuietNot.setVisibility(View.GONE);
                    mSVProgressHUD.showInfoWithStatus("关闭震动");
                }
            }
        });

        tvAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(AboutUsActivity.class);
            }
        });

        tvCheckUdpate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSVProgressHUD.showSuccessWithStatus("已是最新版本", SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });

        tvGoalScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSVProgressHUD.showInfoWithStatus("评分");
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.getInstance().remove(Constants.UID);
                SharedPreferencesUtils.getInstance().remove(Constants.SID);
                SharedPreferencesUtils.getInstance().remove(Constants.PASSWORD);

                EMClient.getInstance().logout(false, new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        openActivity(LoginActivity.class);
                        eventBus.post(new LoginOut());
                        finish();
                    }

                    @Override
                    public void onError(int i, String s) {
                        openActivity(LoginActivity.class);
                        eventBus.post(new LoginOut());
                        finish();
                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 更新推送设置
     * 0开启1关闭
     */
    private void doChangePush(final String key) {
        final String nextPush;
        if (key.equals("0")) {
            nextPush = "1";
        } else {
            nextPush = "0";
        }
        Map<String, String> msp = new HashMap();
        msp.put("toPushState", nextPush);
        PostRequest request = new PostRequest(Constants.USER_CHGOPENPUSH, msp, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                userInfoDetail.setOpenPush(nextPush);
                updateSettingPush(userInfoDetail);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(SettingActivity.this);
        mQueue.add(request);


    }

    /**
     * 更新铃声设置
     * 0开启1关闭
     */
    private void doRingPush(final String key) {
        final String nextRigng;
        if (key.equals("0")) {
            nextRigng = "1";
        } else {
            nextRigng = "0";
        }
        Map<String, String> msp = new HashMap();
        msp.put("toPushState", nextRigng);
        PostRequest request = new PostRequest(Constants.USER_CHGOPENSOUND, msp, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                userInfoDetail.setOpenSound(nextRigng);
                UpdateSettingRing(userInfoDetail);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(SettingActivity.this);
        mQueue.add(request);


    }

    /**
     * 更新用户设置
     *
     * @param userInfoDetail
     */
    private void updateSettingPush(UserInfoDetail userInfoDetail) {
        if (userInfoDetail.getOpenPush().equals("0")) {
            ivAcceptMsg.setVisibility(View.VISIBLE);
            ivAcceptMsgNot.setVisibility(View.GONE);
            mSVProgressHUD.showSuccessWithStatus("打开消息", SVProgressHUD.SVProgressHUDMaskType.Clear);
        } else {
            ivAcceptMsg.setVisibility(View.GONE);
            ivAcceptMsgNot.setVisibility(View.VISIBLE);
            mSVProgressHUD.showSuccessWithStatus("关闭消息", SVProgressHUD.SVProgressHUDMaskType.Clear);
        }


        Util.saveUserInfo(userInfoDetail);

    }

    private void UpdateSettingRing(UserInfoDetail userInfoDetail) {
        if (userInfoDetail.getOpenSound().equals("0")) {
            ivAcceptRing.setVisibility(View.VISIBLE);
            ivAcceptRingNot.setVisibility(View.GONE);
            mSVProgressHUD.showSuccessWithStatus("打开声音", SVProgressHUD.SVProgressHUDMaskType.Clear);
        } else {
            ivAcceptRing.setVisibility(View.GONE);
            ivAcceptRingNot.setVisibility(View.VISIBLE);
            mSVProgressHUD.showSuccessWithStatus("关闭声音", SVProgressHUD.SVProgressHUDMaskType.Clear);
        }
        Util.saveUserInfo(userInfoDetail);
    }

}
