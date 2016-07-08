package com.smartfit.wxapi;


import android.app.Activity;
import android.widget.Toast;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * Created by ntop on 15/9/4.
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType()== ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX){
            if (baseResp.errCode== BaseResp.ErrCode.ERR_OK){
                finish();
            }else{
                Toast.makeText(WXEntryActivity.this,"已取消分享",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
