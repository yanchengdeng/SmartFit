package com.smartfit.activities;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.MessageEvent.LoginOut;
import com.smartfit.R;
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.Options;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.views.SelectableRoundedImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 设置页
 */
public class SettingActivity extends BaseActivity {

    @Bind(R.id.iv_header)
    SelectableRoundedImageView ivHeader;
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
        }
    }

    private void initView() {

    }

    private void addLisener() {
        rlMsgUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivAcceptMsg.getVisibility() == View.VISIBLE) {
                    ivAcceptMsg.setVisibility(View.GONE);
                    ivAcceptMsgNot.setVisibility(View.VISIBLE);
                    mSVProgressHUD.showInfoWithStatus("关闭消息");
                } else {
                    ivAcceptMsg.setVisibility(View.VISIBLE);
                    ivAcceptMsgNot.setVisibility(View.GONE);
                    mSVProgressHUD.showInfoWithStatus("打开消息");
                }
            }
        });


        rlRingUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivAcceptRing.getVisibility() == View.VISIBLE) {
                    ivAcceptRing.setVisibility(View.GONE);
                    ivAcceptRingNot.setVisibility(View.VISIBLE);
                    mSVProgressHUD.showInfoWithStatus("关闭声音");
                } else {
                    ivAcceptRing.setVisibility(View.VISIBLE);
                    ivAcceptRingNot.setVisibility(View.GONE);
                    mSVProgressHUD.showInfoWithStatus("打开声音");
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
                openActivity(LoginActivity.class);
                eventBus.post(new LoginOut());
                finish();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
