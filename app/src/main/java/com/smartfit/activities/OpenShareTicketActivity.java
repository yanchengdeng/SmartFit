package com.smartfit.activities;

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
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.beans.ShareInfo;
import com.smartfit.beans.TicketInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.Util;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author dengyancheng
 *         卷分享
 */
public class OpenShareTicketActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_share_tickets)
    TextView tvShareTickets;
    @Bind(R.id.tv_share_smart)
    TextView tvShareSmart;
    @Bind(R.id.tv_share_wx)
    TextView tvShareWx;


    private ArrayList<TicketInfo> ticketInfos;

    private String ticketIds;

    private IWXAPI api;


    private int REQUSET_FRIENDS = 0x011;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_share_ticket);
        api = WXAPIFactory.createWXAPI(this, Constants.WXPay.APP_ID);
        ButterKnife.bind(this);
        initView();
        addLisener();

    }

    private void initView() {
        tvTittle.setText(getString(R.string.tick_gift_share));
        ticketInfos = getIntent().getParcelableArrayListExtra(Constants.PASS_OBJECT);

        StringBuffer stringBuffer = new StringBuffer();

        StringBuffer ticketsId = new StringBuffer();
        for (TicketInfo item : ticketInfos) {
            stringBuffer.append(item.getEventTitle()).append("\n");
            ticketsId.append(item.getId()).append("|");
        }

        ticketIds = ticketsId.toString();
        tvShareTickets.setText(stringBuffer.toString());
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
                        tvCountDown.setText("重获验证码(60)");
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
        request.headers = NetUtil.getRequestBody(OpenShareTicketActivity.this);
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

                    doShareTicket(uid, etContent.getEditableText().toString());
                }
            }
        });

    }

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
                ShareInfo shareInfo = JsonUtils.objectFromJson(response, ShareInfo.class);
                if (shareInfo != null) {
                    if (TextUtils.isEmpty(uid)) {
                        shareToWX(shareInfo);
                    } else {
                        mSVProgressHUD.showSuccessWithStatus("分享成功", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 1500);
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
        request.headers = NetUtil.getRequestBody(OpenShareTicketActivity.this);
        mQueue.add(request);

    }

    private void shareToWX(ShareInfo shareInfo) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://smartfit.esaydo.com/index/html/share.html?shareid=" + shareInfo.getSharedUid();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = shareInfo.getTitle();
        msg.description = shareInfo.getContent();
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_logo);
        msg.thumbData = Util.bmpToByteArray(thumb, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        req.openId = "";
        api.sendReq(req);
    }

}
