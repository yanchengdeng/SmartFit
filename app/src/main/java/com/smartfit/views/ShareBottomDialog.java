package com.smartfit.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.widget.base.BottomBaseDialog;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.beans.ShareInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.GetSingleRequestUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.Util;
import com.smartfit.utils.ViewFindUtils;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.Map;


public class ShareBottomDialog extends BottomBaseDialog<ShareBottomDialog> {
    private LinearLayout mLlWechat;
    private LinearLayout mLlWechatFriend;
    private TextView ivClose;
    private Context context;
    private IWXAPI api;
    private String CASH_URL = "http://smartfit.esaydo.com/index/html/cashshare/get.html?shareid=";
    ShareInfo shareInfo;

    public ShareBottomDialog(Context context, View animateView, String cashId, String courseType, String courseId) {
        super(context, animateView);
        this.context = context;
        api = WXAPIFactory.createWXAPI(context, Constants.WXPay.APP_ID, false);
        api.registerApp(Constants.WXPay.APP_ID);
        doShareCash(cashId, courseType, courseId);

    }

    /**
     * 分享现金券
     *
     * @param uid
     */
    private void doShareCash(String uid, String courseType, String courseId) {
        Map<String, String> msp = new HashMap();
        msp.put("cashEventId", uid);
//        msp.put("checkCode", code);
//        msp.put("shareToUid", uid);
        msp.put("courseType", courseType);
        msp.put("courseId", courseId);
        PostRequest request = new PostRequest(Constants.EVENT_SHARECASHEVENTUSER, msp, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                shareInfo = JsonUtils.objectFromJson(response, ShareInfo.class);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(context);
        GetSingleRequestUtils.getInstance(context).getRequestQueue().add(request);

    }


    @Override
    public View onCreateView() {
        showAnim(new FlipVerticalSwingEnter());
        dismissAnim(null);
        View inflate = View.inflate(mContext, R.layout.dialog_share, null);
        mLlWechat = ViewFindUtils.find(inflate, R.id.ll_wechat);
        mLlWechatFriend = ViewFindUtils.find(inflate, R.id.ll_wechat_friend);
        ivClose = ViewFindUtils.find(inflate, R.id.iv_close);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mLlWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareInfo != null) {
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = CASH_URL + shareInfo.getId();
                    ;
                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.title = "好友送您SMART FIT健身大礼包啦";
                    msg.description = "包月仅需139元,自由器械+海量团操轻松拥有！";
                    Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.wx_red_package);
                    msg.thumbData = Util.bmpToByteArray(thumb, true);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = msg;
                    req.scene = SendMessageToWX.Req.WXSceneSession;
                    req.openId = "";
                    api.sendReq(req);
                    dismiss();
                }

            }
        });

        mLlWechatFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shareInfo != null) {
                    WXWebpageObject webpage = new WXWebpageObject();
                    webpage.webpageUrl = CASH_URL + shareInfo.getId();
                    WXMediaMessage msg = new WXMediaMessage(webpage);
                    msg.title = "好友送您SMART FIT健身大礼包啦";
                    msg.description = "包月仅需139元,自由器械+海量团操轻松拥有！";
                    Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.wx_red_package);
                    msg.thumbData = Util.bmpToByteArray(thumb, true);
                    SendMessageToWX.Req req = new SendMessageToWX.Req();
                    req.transaction = buildTransaction("webpage");
                    req.message = msg;
                    req.scene = SendMessageToWX.Req.WXSceneTimeline;
                    req.openId = "";
                    api.sendReq(req);
                    dismiss();
                }
            }
        });


        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            ((BaseActivity) mContext).mSVProgressHUD.showSuccessWithStatus("分享成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ((BaseActivity) mContext).mSVProgressHUD.showInfoWithStatus("分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ((BaseActivity) mContext).mSVProgressHUD.showInfoWithStatus("分享取消了");
        }
    };


}
