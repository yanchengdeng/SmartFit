package com.smartfit.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 充值
 */
public class ReChargeActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_pay_money)
    EditText tvPayMoney;
    @Bind(R.id.iv_wx)
    ImageView ivWx;
    @Bind(R.id.tv_wx)
    TextView tvWx;
    @Bind(R.id.iv_wx_selected)
    ImageView ivWxSelected;
    @Bind(R.id.rl_wx)
    RelativeLayout rlWx;
    @Bind(R.id.iv_alipay)
    ImageView ivAlipay;
    @Bind(R.id.tv_alipay)
    TextView tvAlipay;
    @Bind(R.id.iv_alipay_selected)
    ImageView ivAlipaySelected;
    @Bind(R.id.rl_alipay)
    RelativeLayout rlAlipay;
    @Bind(R.id.btn_pay)
    Button btnPay;


    private int payStyle = 1;// 1  微信   2 支付宝

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_charge);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }


    private void initView() {
        tvTittle.setText("充值");

    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        rlWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payStyle = 1;
                ivWxSelected.setVisibility(View.VISIBLE);
                ivAlipaySelected.setVisibility(View.GONE);
            }
        });


        rlAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payStyle = 2;
                ivAlipaySelected.setVisibility(View.VISIBLE);
                ivWxSelected.setVisibility(View.GONE);
            }
        });


        //支付
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payStyle == 1) {
                    if (!checkWeiXin()) {
                        mSVProgressHUD.showInfoWithStatus("您未安装微信客户端!", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                        return;
                    }
                }
                Map<String, String> maps = new HashMap<String, String>();
                maps.put("orderCode", "20160417102810068344137");
                maps.put("type", "1");
                PostRequest request = new PostRequest(Constants.PAY_PAYMOCK, maps, new Response.Listener<JsonObject>() {
                    @Override
                    public void onResponse(JsonObject response) {
                        mSVProgressHUD.showSuccessWithStatus(getString(R.string.recharge_success), SVProgressHUD.SVProgressHUDMaskType.Clear);
                        getUserInfo();
                        mSVProgressHUD.dismiss();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mSVProgressHUD.showErrorWithStatus(error.getMessage());
                    }
                });
                request.setTag(new Object());
                request.headers = NetUtil.getRequestBody(ReChargeActivity.this);
                mQueue.add(request);

            }
        });
    }


    private void getUserInfo() {
        PostRequest request = new PostRequest(Constants.USER_USERINFO, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                UserInfoDetail userInfoDetail = JsonUtils.objectFromJson(response, UserInfoDetail.class);
                if (userInfoDetail != null) {
                    SharedPreferencesUtils.getInstance().putString(Constants.SID, userInfoDetail.getSid());
                    SharedPreferencesUtils.getInstance().putString(Constants.UID, userInfoDetail.getUid());
                    SharedPreferencesUtils.getInstance().putString(Constants.IS_ICF, userInfoDetail.getIsICF());
                    SharedPreferencesUtils.getInstance().putString(Constants.USER_INFO,JsonUtils.toJson(userInfoDetail));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(ReChargeActivity.this);
        mQueue.add(request);
    }
    private boolean checkWeiXin() {
        try {
            getPackageManager().getApplicationInfo("com.tencent.mm", PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}
