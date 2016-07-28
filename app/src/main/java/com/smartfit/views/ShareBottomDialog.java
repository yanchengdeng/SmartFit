package com.smartfit.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.widget.base.BottomBaseDialog;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.commons.Constants;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.utils.Util;
import com.smartfit.utils.ViewFindUtils;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;


public class ShareBottomDialog extends BottomBaseDialog<ShareBottomDialog> {
    private LinearLayout mLlWechat;
    private LinearLayout mLlWechatFriend;
    private TextView ivClose;
    private Context context;
    private IWXAPI api;
    public ShareBottomDialog(Context context, View animateView) {
        super(context, animateView);
        this.context = context;
        api = WXAPIFactory.createWXAPI(context, Constants.WXPay.APP_ID, false);
        api.registerApp(Constants.WXPay.APP_ID);
    }


    @Override
    public View onCreateView() {
        showAnim(new FlipVerticalSwingEnter());
        dismissAnim(null);
        View inflate = View.inflate(mContext, R.layout.dialog_share, null);
        mLlWechat = ViewFindUtils.find(inflate, R.id.ll_wechat);
        mLlWechatFriend = ViewFindUtils.find(inflate, R.id.ll_wechat_friend);
        ivClose = ViewFindUtils.find(inflate,R.id.iv_close);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mLlWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = "http://smartfit.esaydo.com/index/html/share.html?shareid=" ;
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = "好友送您SMART FIT健身大礼包啦";
                msg.description = "恭喜获得一个现金大礼包，分享好友后可立即领取";
                Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo_share);
                msg.thumbData = Util.bmpToByteArray(thumb, true);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;
                req.openId = "";
                api.sendReq(req);
                dismiss();

            }
        });

        mLlWechatFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WXWebpageObject webpage = new WXWebpageObject();
                webpage.webpageUrl = "http://smartfit.esaydo.com/index/html/share.html?shareid=" ;
                WXMediaMessage msg = new WXMediaMessage(webpage);
                msg.title = "好友送您SMART FIT健身大礼包啦";
                msg.description = "中国好朋友强塞一个SMARTFIT健身礼包给你！一懒众衫小，还不快起跑！丢一张健身礼券给你，做我陪练好不好！";
                Bitmap thumb = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo_share);
                msg.thumbData = Util.bmpToByteArray(thumb, true);
                SendMessageToWX.Req req = new SendMessageToWX.Req();
                req.transaction = buildTransaction("webpage");
                req.message = msg;
                req.scene = SendMessageToWX.Req.WXSceneSession;
                req.openId = "";
                api.sendReq(req);

                dismiss();
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
            ((BaseActivity)mContext).mSVProgressHUD.showSuccessWithStatus("分享成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            ((BaseActivity)mContext).mSVProgressHUD.showInfoWithStatus("分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            ((BaseActivity)mContext).mSVProgressHUD.showInfoWithStatus("分享取消了");
        }
    };


}
