package com.smartfit.wxapi;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.MessageEvent.ShareTicketSuccess;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.SelectShareFriendsActivity;
import com.smartfit.adpters.ShareTicketAdapter;
import com.smartfit.beans.ShareInfo;
import com.smartfit.beans.TicketInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.GetSingleRequestUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.utils.Util;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ntop on 15/9/4.
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {


    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_share_smart)
    TextView tvShareSmart;
    @Bind(R.id.tv_share_wx)
    TextView tvShareWx;
    @Bind(R.id.listview)
    ListView listview;


    private ArrayList<TicketInfo> ticketInfos;

    private String ticketIds;

    private IWXAPI api;


    private int REQUSET_FRIENDS = 0x011;

    private EventBus eventBus;

    private String ticketType = "1";

    private String cashEventId;
    private String courseId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_share_ticket);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        api = WXAPIFactory.createWXAPI(this, Constants.WXPay.APP_ID, false);
        api.registerApp(Constants.WXPay.APP_ID);
        initView();
        addLisener();
        api.handleIntent(getIntent(), this);

    }

    @Subscribe
    public void onEvent(ShareTicketSuccess event) {
        finish();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.tick_gift_share));
        if (!TextUtils.isEmpty(getIntent().getStringExtra(Constants.TICKET_SHARE_TYPE))) {
            ticketType = getIntent().getStringExtra(Constants.TICKET_SHARE_TYPE);
        }

        if (ticketType.equals("1")) {
            ticketInfos = getIntent().getParcelableArrayListExtra(Constants.PASS_OBJECT);

            StringBuffer stringBuffer = new StringBuffer();

            StringBuffer ticketsId = new StringBuffer();


            if (ticketInfos != null) {
                for (int i = 0; i < ticketInfos.size(); i++) {
                    if (i == ticketInfos.size() - 1) {
                        ticketsId.append(ticketInfos.get(i).getId());
                    } else {
                        ticketsId.append(ticketInfos.get(i).getId()).append("|");
                    }
                    stringBuffer.append(ticketInfos.get(i).getEventTitle()).append("\n");

                }
                listview.setAdapter(new ShareTicketAdapter(WXEntryActivity.this, ticketInfos));

            }

            ticketIds = ticketsId.toString();
        } else if (ticketType.equals("2")) {
            cashEventId = getIntent().getStringExtra(Constants.PASS_STRING);
            courseId = getIntent().getStringExtra("course_id");
        }
    }

    private void addLisener() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvShareSmart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openActivity(SelectShareFriendsActivity.class, REQUSET_FRIENDS);
            }
        });


        tvShareWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uid = "";
                showShareDialog("");
            }
        });
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    String uid;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUSET_FRIENDS && resultCode == RESULT_OK) {
            uid = data.getStringExtra("uid");
            if (!TextUtils.isEmpty(uid)) {
                showShareDialog(uid);
            }
        }
    }

    /**
     * 发送验证码
     *
     * @param uid
     * @param tvCountDown
     */
    private void sendShareCode(final String uid, final TextView tvCountDown) {
        Map<String, String> msp = new HashMap();
        msp.put("mobile", NetUtil.getPhone());
        PostRequest request = new PostRequest(Constants.SMS_SMSSend, msp, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.showSuccessWithStatus("已发送，注意查收", SVProgressHUD.SVProgressHUDMaskType.Clear);
                countDownTimer = new CountDownTimer(60 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tvCountDown.setClickable(false);
                        tvCountDown.setText(String.format("重获验证码(%ds)", millisUntilFinished / 1000));
                    }

                    @Override
                    public void onFinish() {
                        tvCountDown.setText("重取验证码");
                        tvCountDown.setClickable(true);
                    }
                };

                countDownTimer.start();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(WXEntryActivity.this);
        mQueue.add(request);
    }


    private CountDownTimer countDownTimer;
    AlertDialog dialog;


    /**
     * 弹出分享对话框
     *
     * @param uid
     */
    private void showShareDialog(final String uid) {
        dialog = new AlertDialog.Builder(mContext).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_share_ticket);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        TextView tvTittle = (TextView) dialog.getWindow().findViewById(R.id.tv_tittle);
        final EditText etContent = (EditText) dialog.getWindow().findViewById(R.id.et_code);

        if (!TextUtils.isEmpty(NetUtil.getPhone())) {
            StringBuffer sb = new StringBuffer();
            sb.append(NetUtil.getPhone().substring(0, 7)).append("****");
            tvTittle.setText(String.format(getString(R.string.share_ticket_tips), sb.toString()));
        }


        final TextView tvCountDown = (TextView) dialog.getWindow().findViewById(R.id.et_code_count_down);

        tvCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendShareCode(uid, tvCountDown);
            }
        });

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
                if (TextUtils.isEmpty(etContent.getEditableText().toString())) {
                    mSVProgressHUD.showInfoWithStatus("验证码为空");
                    return;
                } else {

                    if (ticketType.equals("1")) {
                        doShareTicket(uid, etContent.getEditableText().toString());
                    } else if (ticketType.equals("2")) {
                        doShareCash(uid, etContent.getEditableText().toString());
                    }
                }
            }
        });
    }

    ShareInfo shareInfo;


    /**
     * 分享券
     *
     * @param uid
     * @param code
     */
    private void doShareTicket(final String uid, String code) {
        Map<String, String> msp = new HashMap();
        msp.put("eventUserIds", ticketIds);
        msp.put("checkCode", code);
        msp.put("shareToUid", uid);
        PostRequest request = new PostRequest(Constants.EVENT_SHAREEVENTUSER, msp, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                shareInfo = JsonUtils.objectFromJson(response, ShareInfo.class);
                if (shareInfo != null) {
                    if (TextUtils.isEmpty(uid)) {
                        shareToWX(shareInfo);
                    } else {
                        mSVProgressHUD.showSuccessWithStatus("分享成功", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                eventBus.post(new ShareTicketSuccess());
                                finish();
                            }
                        }, 2000);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(WXEntryActivity.this);
        mQueue.add(request);

    }

    /**
     * 分享现金券
     *
     * @param uid
     * @param code
     */
    private void doShareCash(final String uid, String code) {
        Map<String, String> msp = new HashMap();
        msp.put("cashEventId", cashEventId);
        msp.put("checkCode", code);
        msp.put("shareToUid", uid);
        msp.put("courseId", courseId);
        PostRequest request = new PostRequest(Constants.EVENT_SHARECASHEVENTUSER, msp, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                shareInfo = JsonUtils.objectFromJson(response, ShareInfo.class);
                if (shareInfo != null) {
                    if (TextUtils.isEmpty(uid)) {
                        if (ticketType.equals("1")) {
                            shareToWX(shareInfo);
                        } else if (ticketType.equals("2")) {
                            shareCashTowx();
                        }
                    } else {
                        mSVProgressHUD.showSuccessWithStatus("分享成功", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                eventBus.post(new ShareTicketSuccess());
                                finish();
                            }
                        }, 2000);
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(WXEntryActivity.this);
        mQueue.add(request);

    }

    /**
     * 直接分享现金券
     */
    private void shareCashTowx() {
        SharedPreferencesUtils.getInstance().putString(Constants.SHARE_ID, shareInfo.getId());
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://smartfit.esaydo.com/index/html/share.html?shareid=" + shareInfo.getId();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "好友送您SMART FIT健身大礼包啦";
        msg.description = "恭喜获得一个现金大礼包，分享好友后可立即领取";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.wx_red_package);
        msg.thumbData = Util.bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        req.openId = "";
        api.sendReq(req);
    }

    private void shareToWX(ShareInfo shareInfo) {
        SharedPreferencesUtils.getInstance().putString(Constants.SHARE_ID, shareInfo.getId());
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://smartfit.esaydo.com/index/html/share.html?shareid=" + shareInfo.getId();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "好友送您SMART FIT健身大礼包啦";
        msg.description = "中国好朋友强塞一个SMARTFIT健身礼包给你！一懒众衫小，还不快起跑！丢一张健身礼券给你，做我陪练好不好！";
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.logo_share);
        msg.thumbData = Util.bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        req.openId = "";
        api.sendReq(req);
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.errCode == BaseResp.ErrCode.ERR_OK) {
            if (shareInfo != null) {
                if (ticketType.equals("1")) {
                    commitShareEvent();
                } else if (ticketType.equals("2")) {
                    mSVProgressHUD.showSuccessWithStatus("分享成功", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            eventBus.post(new ShareTicketSuccess());
                            finish();
                        }
                    }, 2000);
                }
            } else {
                finish();
            }
        } else {
            mSVProgressHUD.showInfoWithStatus("取消分享", SVProgressHUD.SVProgressHUDMaskType.Clear);
        }
    }


    /**
     * 分享成功 回调
     */
    private void commitShareEvent() {
        Map<String, String> msp = new HashMap();
        msp.put("sharedId", SharedPreferencesUtils.getInstance().getString(Constants.SHARE_ID, ""));
        PostRequest request = new PostRequest(Constants.EVENT_SHARESUCCESSCALLBACK, msp, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.showSuccessWithStatus("分享成功", SVProgressHUD.SVProgressHUDMaskType.Clear);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        eventBus.post(new ShareTicketSuccess());
                        finish();
                    }
                }, 2000);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(WXEntryActivity.this);
        GetSingleRequestUtils.getInstance(this).getRequestQueue().add(request);
    }


}
