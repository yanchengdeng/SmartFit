package com.smartfit.wxapi;


import android.content.Intent;
import android.os.Bundle;

import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.commons.Constants;
import com.smartfit.utils.LogUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler{
	
	private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);
        
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
		LogUtil.w("dyc", "openid = " + req.openId+"..."+req.errCode+"..."+req.errStr+"..."+req.getType());
		mSVProgressHUD.dismiss();
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
		if(req.errCode== BaseResp.ErrCode.ERR_OK){
			mSVProgressHUD.showSuccessWithStatus("支付成功");

		}else{
			mSVProgressHUD.showInfoWithStatus("支付失败");
//			openActivity(GroupClassOrderSuccessActivity.class);
		}
	}
}