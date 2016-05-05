package com.smartfit.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.R;
import com.smartfit.commons.AppManager;
import com.smartfit.utils.GetSingleRequestUtils;
import com.smartfit.utils.IntentUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * 基类 处理功能
 */
public class BaseActivity extends FragmentActivity {

    public SVProgressHUD mSVProgressHUD;
    public RequestQueue mQueue;
    public Context mContext;
    public static final Object TAG = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = BaseActivity.this;
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.common_header_bg);//通知栏所需颜色
        mQueue = GetSingleRequestUtils.getInstance(this).getRequestQueue();
        mSVProgressHUD = new SVProgressHUD(this);
        initConnectLisener();
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
                        NormalDialogOneBtn();
                    } else {

                    }
                }
            });
        }
    }

    private void NormalDialogOneBtn() {
        final NormalDialog dialog = new NormalDialog(mContext);
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

    @TargetApi(19)
    protected void setTranslucentStatus() {
        Window window = getWindow();
        // Translucent status bar
        window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // Translucent navigation bar
//        window.setFlags(
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity从堆栈中移除
        AppManager.getAppManager().finishActivity(this);
    }

    /***
     * 打开一个新界面
     *
     * @param pClass
     */
    public void openActivity(Class<?> pClass) {
        openActivity(pClass, null, 0);
    }


    /***
     * 打开一个新界面
     *
     * @param pClass
     */
    public void openActivity(Class<?> pClass, Bundle pBundle) {
        openActivity(pClass, pBundle, 0);
    }

    /***
     * 打开新界面
     *
     * @param pClass
     * @param requestCode 请求码
     */
    public void openActivity(Class<?> pClass, int requestCode) {
        openActivity(pClass, null, requestCode);
    }

    /***
     * 打开新界面
     *
     * @param pClass      界面类
     * @param pBundle     携带参数
     * @param requestCode 请求码
     */
    public void openActivity(Class<?> pClass, Bundle pBundle, int requestCode) {
        Intent intent = new Intent(mContext, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        IntentUtils.startActivity(mContext, intent, requestCode);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (mSVProgressHUD.isShowing()) {
                mSVProgressHUD.dismiss();
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


}
