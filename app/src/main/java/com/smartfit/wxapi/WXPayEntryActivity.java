package com.smartfit.wxapi;


import android.content.Intent;
import android.os.Bundle;

import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.GroupClassOrderSuccessActivity;
import com.smartfit.commons.Constants;
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
//        setContentView(R.layout.pay_result);
        
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
	public void onResp(BaseResp resp) {
		if(resp.errCode== BaseResp.ErrCode.ERR_OK){
			mSVProgressHUD.showSuccessWithStatus("支付成功");

		}else{
			mSVProgressHUD.showErrorWithStatus("支付失败");
			openActivity(GroupClassOrderSuccessActivity.class);
		}
	}
}