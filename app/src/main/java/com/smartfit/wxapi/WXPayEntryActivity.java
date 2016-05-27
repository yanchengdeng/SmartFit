package com.smartfit.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.MessageEvent.FinishRechage;
import com.smartfit.MessageEvent.UpdateAreoClassDetail;
import com.smartfit.MessageEvent.UpdateCoachClass;
import com.smartfit.MessageEvent.UpdateCustomClassInfo;
import com.smartfit.MessageEvent.UpdateGroupClassDetail;
import com.smartfit.MessageEvent.UpdatePrivateClassDetail;
import com.smartfit.MessageEvent.UpdateWalletInfo;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.commons.AppManager;
import com.smartfit.commons.Constants;
import com.smartfit.utils.GetSingleRequestUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    private EventBus eventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        // 添加Activity到堆栈
        AppManager.getAppManager().addActivity(this);
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.white);//通知栏所需颜色
        eventBus = EventBus.getDefault();
        api = WXAPIFactory.createWXAPI(this, Constants.WXPay.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp req) {
//        LogUtil.w("dyc", "openid = " + req.openId + "..." + req.errCode + "..." + req.errStr + "..." + req.getType());
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
                LogUtil.w("dyc", "评论----微信");
                break;
            case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
                LogUtil.w("dyc", "从微信端登陆");
                break;
            default:
                break;
        }
        if (req.errCode == BaseResp.ErrCode.ERR_OK) {
            mSVProgressHUD.showSuccessWithStatus("支付成功", SVProgressHUD.SVProgressHUDMaskType.Clear);
            if (Constants.IS_PASS_FROM_ORDER) {
                dealAfterPay(Constants.PAGE_INDEX_FROM);
            } else {
                dealRecharge();
            }
        } else {
            mSVProgressHUD.showSuccessWithStatus("支付失败", SVProgressHUD.SVProgressHUDMaskType.Clear);
            finish();
        }
    }

    /**
     * 自付完成后  需要操作的步骤
     */
    private void dealAfterPay(int pageIndex) {
        eventBus.post(new FinishRechage());
        if (pageIndex == 1) {
            eventBus.post(new UpdateGroupClassDetail());
        } else if (pageIndex == 3) {
            eventBus.post(new UpdatePrivateClassDetail());
        } else if (pageIndex == 4) {
            eventBus.post(new UpdateAreoClassDetail());
        } else if (pageIndex == 5) {
            eventBus.post(new UpdateCoachClass());
        } else if (pageIndex == 6) {
            eventBus.post(new UpdateCustomClassInfo());
        } else if (pageIndex == 7) {
            finish();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);

    }


    private void dealRecharge() {
        PostRequest request = new PostRequest(Constants.USER_USERINFO, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                UserInfoDetail userInfoDetail = JsonUtils.objectFromJson(response, UserInfoDetail.class);
                if (userInfoDetail != null) {
                    SharedPreferencesUtils.getInstance().putString(Constants.SID, userInfoDetail.getSid());
                    SharedPreferencesUtils.getInstance().putString(Constants.UID, userInfoDetail.getUid());
                    SharedPreferencesUtils.getInstance().putString(Constants.IS_ICF, userInfoDetail.getIsICF());
                    SharedPreferencesUtils.getInstance().putString(Constants.USER_INFO, JsonUtils.toJson(userInfoDetail));
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        eventBus.post(new FinishRechage());
                        eventBus.post(new UpdateWalletInfo());
                        finish();
                    }
                }, 2000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(WXPayEntryActivity.this);
        GetSingleRequestUtils.getInstance(this).getRequestQueue().add(request);
    }
}