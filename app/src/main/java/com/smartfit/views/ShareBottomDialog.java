package com.smartfit.views;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.flyco.animation.FlipEnter.FlipVerticalSwingEnter;
import com.flyco.dialog.widget.base.BottomBaseDialog;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.utils.ViewFindUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;


public class ShareBottomDialog extends BottomBaseDialog<ShareBottomDialog> {
    private LinearLayout mLlWechat;
    private LinearLayout mLlWechatFriend;
    private LinearLayout mLlSina;
    private ImageView ivClose;
    private Context context;

    public ShareBottomDialog(Context context, View animateView) {
        super(context, animateView);
        this.context = context;
    }

    public ShareBottomDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateView() {
        showAnim(new FlipVerticalSwingEnter());
        dismissAnim(null);
        View inflate = View.inflate(mContext, R.layout.dialog_share, null);
        mLlWechat = ViewFindUtils.find(inflate, R.id.ll_wechat);
        mLlWechatFriend = ViewFindUtils.find(inflate, R.id.ll_wechat_friend);
        mLlSina = ViewFindUtils.find(inflate, R.id.ll_sina);
        ivClose = ViewFindUtils.find(inflate,R.id.iv_close);
        return inflate;
    }

    @Override
    public void setUiBeforShow() {
        mLlWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction((Activity)context).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withText("hello umeng")
                        .share();
                dismiss();


            }
        });

        mLlWechatFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction((Activity)context).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE).setCallback(umShareListener)
                        .withText("hello umeng")
                        .share();

                dismiss();
            }
        });

        mLlSina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /** shareaction need setplatform , callbacklistener,and content(text,image).then share it **/
                new ShareAction((Activity)context).setPlatform(SHARE_MEDIA.SINA).setCallback(umShareListener)
                        .withText("hello umeng video")
                        .share();
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
