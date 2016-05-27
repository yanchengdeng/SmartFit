package com.smartfit.activities;

import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.MessageEvent.FinishRechage;
import com.smartfit.MessageEvent.UpdateWalletInfo;
import com.smartfit.R;
import com.smartfit.beans.RechargeInfo;
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.beans.WXPayAppId;
import com.smartfit.commons.Constants;
import com.smartfit.utils.AliPayUtiils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.MD5;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PayResult;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.utils.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    private Float money;

    private String orderId;

    private EventBus eventBus;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mSVProgressHUD.dismiss();
            switch (msg.what) {
                case Constants.AliPay.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        mSVProgressHUD.showSuccessWithStatus("支付成功", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        getUserInfo();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            mSVProgressHUD.showSuccessWithStatus("支付结果确认中", SVProgressHUD.SVProgressHUDMaskType.Clear);

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            mSVProgressHUD.showSuccessWithStatus("支付失败", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    });

    private void doCharge() {
        Map<String, String> maps = new HashMap<>();
        maps.put("chargePrice", String.valueOf(money));
        PostRequest request = new PostRequest(Constants.ORDER_ORDERCHARGE, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                RechargeInfo rechargeInfo = JsonUtils.objectFromJson(response, RechargeInfo.class);
                orderId = rechargeInfo.getOrderCode();
                goPay();

                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(ReChargeActivity.this);
        mQueue.add(request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_charge);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    @Subscribe
    public void onEvent(Object event) {
        if (event instanceof FinishRechage) {
            finish();
        }
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

                if (payStyle == 2) {
                    if (!checkALi()) {
                        mSVProgressHUD.showInfoWithStatus("您未安装支付宝客户端!", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                        return;
                    }
                }
                if (TextUtils.isEmpty(tvPayMoney.getEditableText().toString())) {
                    mSVProgressHUD.showInfoWithStatus("请填写金额", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    return;
                }
                money = Float.parseFloat(String.format("%.2f", Float.parseFloat(tvPayMoney.getEditableText().toString())));

                doCharge();


            }
        });
    }


    private boolean checkALi() {
        try {
            getPackageManager().getApplicationInfo("com.eg.android.AlipayGphone", PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void goPay() {
        if (payStyle == 1) {
            //微信支付
            wxPrePay();

        } else if (payStyle == 2) {
            //支付宝支付
            pay();

        }
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
                    SharedPreferencesUtils.getInstance().putString(Constants.USER_INFO, JsonUtils.toJson(userInfoDetail));
                }

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        eventBus.post(new UpdateWalletInfo());
                        finish();
                    }
                }, 1000);
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


    /**
     * 服务端签名  微信支付
     */
    private void wxPrePay() {
        Constants.IS_PASS_FROM_ORDER = false;
        btnPay.setEnabled(false);
        mSVProgressHUD.showWithStatus("获取订单信息", SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> map = new HashMap<>();
        map.put("orderCode", orderId);
        PostRequest request = new PostRequest(Constants.PAY_WEIXINPREPAY, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc", response.toString());
                WXPayAppId wxPayAppId = JsonUtils.objectFromJson(response, WXPayAppId.class);
                weixinPay(wxPayAppId);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnPay.setEnabled(true);
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(ReChargeActivity.this);
        mQueue.add(request);

    }


    private IWXAPI api;

    /**
     * 调用微信支付
     *
     * @param wxPayAppId
     */
    private void weixinPay(WXPayAppId wxPayAppId) {
        api = WXAPIFactory.createWXAPI(this, Constants.WXPay.APP_ID);
        api.registerApp(Constants.WXPay.APP_ID);
        if (wxPayAppId != null) {
            PayReq req = new PayReq();
            req.appId = Constants.WXPay.APP_ID;
            req.partnerId = wxPayAppId.getPartnerid();
            req.prepayId = wxPayAppId.getPrepayid();
            req.nonceStr = wxPayAppId.getNoncestr();
            req.timeStamp = wxPayAppId.getTimestamp();
            req.packageValue = "Sign=WXPay";
            req.sign = wxPayAppId.getSign();
            req.extData = "SmartFit"; // optional
            mSVProgressHUD.setText("正常调起支付");
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            mSVProgressHUD.showInfoWithStatus("正常调起支付", SVProgressHUD.SVProgressHUDMaskType.Clear);
            api.sendReq(req);
        } else {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.do_later), SVProgressHUD.SVProgressHUDMaskType.Clear);
            mSVProgressHUD.dismiss();
        }
    }


    private boolean checkWeiXin() {
        try {
            getPackageManager().getApplicationInfo("com.tencent.mm", PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    private String genOutTradNo() {
        Random random = new Random();
//        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
        return orderId;
    }


    /***
     * 支付费用
     */
    private void pay() {
        mSVProgressHUD.showWithStatus("充值中...");
        // 订单
        Log.w("dyc", genOutTradNo() + "====" + money);
        String orderInfo = AliPayUtiils.getOrderInfo(getString(R.string.app_name), genOutTradNo(), String.valueOf(money));//String.valueOf(payMoney)

        // 对订单做RSA 签名
        String sign = AliPayUtiils.sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + AliPayUtiils.getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(ReChargeActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = Constants.AliPay.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


}
