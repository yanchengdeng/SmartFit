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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.MessageEvent.UpdateAreoClassDetail;
import com.smartfit.MessageEvent.UpdateCoachClass;
import com.smartfit.MessageEvent.UpdateCustomClassInfo;
import com.smartfit.MessageEvent.UpdateGroupClassDetail;
import com.smartfit.MessageEvent.UpdatePrivateClassDetail;
import com.smartfit.R;
import com.smartfit.beans.OrderCourse;
import com.smartfit.beans.UserInfoDetail;
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
 * 支付页
 */
public class PayActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_pay_money)
    TextView tvPayMoney;
    @Bind(R.id.iv_wx_selected)
    ImageView ivWxSelected;
    @Bind(R.id.rl_wx)
    RelativeLayout rlWx;
    @Bind(R.id.iv_alipay_selected)
    ImageView ivAlipaySelected;
    @Bind(R.id.rl_alipay)
    RelativeLayout rlAlipay;
    @Bind(R.id.btn_pay)
    Button btnPay;
    @Bind(R.id.tv_ye_left)
    TextView tvYeLeft;
    @Bind(R.id.iv_ye_selected)
    ImageView ivYeSelected;
    @Bind(R.id.rl_ye)
    RelativeLayout rlYe;


    private int payStyle = 0;// 1  微信   2 支付宝

    private String orderID;

    //订单课程失败后提示
    private String messsge = "该课程已经开课。请预定其他课程";

    /****
     * 页面跳转 index
     * <p/>
     * //定义  1 ：团体课  2.小班课  3.私教课 4.有氧器械  5 再次开课 （直接付款） 6  （学员）自定课程  7 教练自订课程
     */
    private int pageIndex = 1;

    private String payMoney;

    private String leftMoney;

    private String courseId;

    private String orderCode;

    private String startTime, endTime;

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
                        dealAfterPay();

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        initView();
        addLisener();
    }

    private void getLeftMoney() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        PostRequest request = new PostRequest(Constants.USER_USERINFO, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                UserInfoDetail userInfoDetail = JsonUtils.objectFromJson(response, UserInfoDetail.class);
                if (userInfoDetail != null) {
                    if (!TextUtils.isEmpty(userInfoDetail.getBalance())) {
                        tvYeLeft.setText(userInfoDetail.getBalance());
                        leftMoney = userInfoDetail.getBalance();
                        if (pageIndex == 4) {
                            if (TextUtils.isEmpty(orderCode)) {
                                payAreao();
                            } else {
                                orderID = orderCode;
                                mSVProgressHUD.dismiss();
                                if (Float.parseFloat(payMoney)==0){
                                    payStyle = 0;
                                    rlYe.setClickable(true);
                                    ivYeSelected.setVisibility(View.VISIBLE);
                                    ivWxSelected.setVisibility(View.GONE);
                                    ivAlipaySelected.setVisibility(View.GONE);
                                    rlAlipay.setClickable(false);
                                    rlWx.setClickable(false);
                                }
                            }
                        } else if (pageIndex == 5) {
                            getOrderCorse();
                        } else if (pageIndex == 6) {
                            getOrderCorse();
                        } else {
                            if (TextUtils.isEmpty(orderCode)) {
                                getOrderCorse();
                            } else {
                                orderID = orderCode;
                                mSVProgressHUD.dismiss();
                                if (Float.parseFloat(payMoney)==0){
                                    payStyle = 0;
                                    rlYe.setClickable(true);
                                    ivYeSelected.setVisibility(View.VISIBLE);
                                    ivWxSelected.setVisibility(View.GONE);
                                    ivAlipaySelected.setVisibility(View.GONE);
                                    rlAlipay.setClickable(false);
                                    rlWx.setClickable(false);
                                }
                            }
                        }
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
        request.headers = NetUtil.getRequestBody(PayActivity.this);
        mQueue.add(request);
    }

    /**
     * 根据余额选择支付方式
     *
     * @param leftMoney
     * @param payMoney
     */
    private void selectPayStyle(String leftMoney, String payMoney) {

        if (Float.parseFloat(payMoney) == 0) {
            payStyle = 0;
            rlYe.setClickable(true);
            ivYeSelected.setVisibility(View.VISIBLE);
            ivWxSelected.setVisibility(View.GONE);
            ivAlipaySelected.setVisibility(View.GONE);
            rlAlipay.setClickable(false);
            rlWx.setClickable(false);
        } else {
            if (!TextUtils.isEmpty(leftMoney) && !TextUtils.isEmpty(payMoney)) {
                if (Float.parseFloat(leftMoney) >= Float.parseFloat(payMoney)) {
                    rlYe.setClickable(true);
                    ivYeSelected.setVisibility(View.VISIBLE);
                    ivWxSelected.setVisibility(View.GONE);
                    ivAlipaySelected.setVisibility(View.GONE);
                    payStyle = 0;
                } else {
                    rlYe.setClickable(false);
                    payStyle = 2;
                    ivWxSelected.setVisibility(View.GONE);
                    ivYeSelected.setVisibility(View.GONE);
                    ivAlipaySelected.setVisibility(View.VISIBLE);

                }
            }
        }
    }

    private void initView() {
        tvTittle.setText("付款");
        pageIndex = getIntent().getIntExtra(Constants.PAGE_INDEX, 1);
        courseId = getIntent().getStringExtra(Constants.COURSE_ID);
        payMoney = getIntent().getStringExtra(Constants.COURSE_MONEY);
        orderCode = getIntent().getStringExtra(Constants.COURSE_ORDER_CODE);
        if (pageIndex == 4) {
            startTime = getIntent().getStringExtra("start_time");
            endTime = getIntent().getStringExtra("end_time");
        }
        tvPayMoney.setText(payMoney + "元");
        getLeftMoney();
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        rlYe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payStyle = 0;
                ivAlipaySelected.setVisibility(View.GONE);
                ivWxSelected.setVisibility(View.GONE);
                ivYeSelected.setVisibility(View.VISIBLE);
            }
        });


        rlWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
//                payStyle = 1;
//                ivWxSelected.setVisibility(View.VISIBLE);
//                ivYeSelected.setVisibility(View.GONE);
//                ivAlipaySelected.setVisibility(View.GONE);
            }
        });


        rlAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payStyle = 2;
                ivAlipaySelected.setVisibility(View.VISIBLE);
                ivWxSelected.setVisibility(View.GONE);
                ivYeSelected.setVisibility(View.GONE);
            }
        });


        //支付
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 改过来
                if (payStyle == 1) {
                    if (!checkWeiXin()) {
                        mSVProgressHUD.showInfoWithStatus("您未安装微信客户端!", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                        return;
                    }
                }
                goPay();
            }
        });


    }

    /**
     * 获取课程订单
     */
    private void getOrderCorse() {
        mSVProgressHUD.setText("获取订单信息");
        Map<String, String> map = new HashMap<>();
        map.put("courseId", courseId);
//        map.put("uid", SharedPreferencesUtils.getInstance().getString(Constants.UID, ""));
        PostRequest request = new PostRequest(Constants.ORDER_ORDERCOURSE, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc", response.toString());
                OrderCourse orderCourse = JsonUtils.objectFromJson(response.toString(), OrderCourse.class);
                if (orderCourse != null) {
                    if (!TextUtils.isEmpty(orderCourse.getOrderCode())) {
                        orderID = orderCourse.getOrderCode();
                    }

                    selectPayStyle(leftMoney, orderCourse.getOrderPrice());

                }
                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                messsge = error.getMessage();
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(PayActivity.this);
        mQueue.add(request);


    }

    private void goPay() {

        if (TextUtils.isEmpty(orderID)) {
            mSVProgressHUD.showInfoWithStatus(messsge, SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }
        if (payStyle == 1) {
            //微信支付
            weixinPay();

        } else if (payStyle == 2) {
            //支付宝支付
            pay();

        } else if (payStyle == 0) {
            payOrder();
        }
    }

    /***
     * 支付有氧课程
     */
    private void payAreao() {


        mSVProgressHUD.setText("创建订单中");
        Map<String, String> map = new HashMap<>();
        map.put("classroomId", courseId);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        PostRequest request = new PostRequest(Constants.ORDER_ORDERAEROBIC, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                OrderCourse orderCourse = JsonUtils.objectFromJson(response.toString(), OrderCourse.class);
                if (orderCourse != null) {
                    if (!TextUtils.isEmpty(orderCourse.getOrderCode())) {
                        orderID = orderCourse.getOrderCode();
                    }
                    selectPayStyle(leftMoney, orderCourse.getOrderPrice());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                messsge = error.getMessage();
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(PayActivity.this);
        mQueue.add(request);
    }

    /***
     * 本地余额支付订单
     */
    private void payOrder() {
        mSVProgressHUD.showWithStatus("正在支付", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> map = new HashMap<>();
        map.put("orderCode", orderID);
        map.put("uid", SharedPreferencesUtils.getInstance().getString(Constants.UID, ""));
        PostRequest request = new PostRequest(Constants.PAY_BALANCEPAY, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.showSuccessWithStatus("支付成功", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                mSVProgressHUD.dismiss();
                dealAfterPay();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(PayActivity.this);
        mQueue.add(request);
    }

    /**
     * 自付完成后  需要操作的步骤
     */
    private void dealAfterPay() {
        if (pageIndex == 1) {
            eventBus.post(new UpdateGroupClassDetail());
        } else if (pageIndex == 3) {
            eventBus.post(new UpdatePrivateClassDetail());
        } else if (pageIndex == 4) {
            eventBus.post(new UpdateAreoClassDetail());
        } else if (pageIndex == 5) {
            eventBus.post(new UpdateCoachClass());
        } else if (pageIndex == 6) {
            eventBus.post(new UpdateCustomClassInfo());
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);

    }


    private IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    private Map<String, String> resultunifiedorder;
    private PayReq req;
    private StringBuffer sb;

    /***
     * 微信支付
     */
    private void weixinPay() {
        req = new PayReq();
        sb = new StringBuffer();
        msgApi.registerApp(Constants.WXPay.APP_ID);
        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
        getPrepayId.execute();

    }


    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

        @Override
        protected void onPreExecute() {
            mSVProgressHUD.showWithStatus("正在生成订单", SVProgressHUD.SVProgressHUDMaskType.Clear);
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            mSVProgressHUD.dismiss();
            sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
            Log.w("dyc", sb.toString());
            resultunifiedorder = result;
            genPayReq();
            sendPayReq();
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();
            mSVProgressHUD.dismiss();
        }


        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = null;
            try {
                entity = new String(genProductArgs().toString().getBytes(), "ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Log.e("dyc", entity);
            byte[] buf = Util.httpPost(url, entity);
            String content = new String(buf);
            Log.e("dyc", content);
            Map<String, String> xml = decodeXml(content);

            return xml;
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


    public Map<String, String> decodeXml(String content) {

        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            //实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;

    }


    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }


    private String genOutTradNo() {
        Random random = new Random();
//        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
        return orderID;
    }


    //
    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();

        try {
            String nonceStr = genNonceStr();


            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", Constants.WXPay.APP_ID));
            packageParams.add(new BasicNameValuePair("body", "smartFit"));//注：如果商品为中文时，需要转码 否则会报签名失败paySuccess.getTitle()
            packageParams.add(new BasicNameValuePair("mch_id", Constants.WXPay.MCH_ID));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
            packageParams.add(new BasicNameValuePair("notify_url", Constants.Net.WX_PAY_CALLBACK));
            packageParams.add(new BasicNameValuePair("out_trade_no", genOutTradNo()));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", "1"));//支付单位是分 String.valueOf((int)(payMoney*100)))
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));
            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));
            String xmlstring = toXml(packageParams);

            return xmlstring;
        } catch (Exception e) {
            return null;
        }
    }


    /*****
     * 生成订单
     */
    private void genPayReq() {

        req.appId = Constants.WXPay.APP_ID;
        req.partnerId = Constants.WXPay.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());


        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        req.sign = genAppSign(signParams);
        sb.append("sign\n" + req.sign + "\n\n");
        Log.w("dyc", sb.toString());
        Log.e("orion", signParams.toString());
    }

    /*****
     * 发送订单
     */
    private void sendPayReq() {
        msgApi.registerApp(Constants.WXPay.APP_ID);
        msgApi.sendReq(req);
        finish();
    }

    /**
     * 生成签名
     */

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.WXPay.API_KEY);
        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", packageSign);
        return packageSign;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.WXPay.API_KEY);

        this.sb.append("sign str\n" + sb.toString() + "\n\n");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {
            sb.append("<" + params.get(i).getName() + ">");
            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        Log.e("orion", sb.toString());
        return sb.toString();
    }


    /***
     * 支付费用
     */
    private void pay() {
        mSVProgressHUD.showWithStatus("加载中...");
        // 订单
        String orderInfo = AliPayUtiils.getOrderInfo(getString(R.string.app_name), orderID, payMoney);//String.valueOf(payMoney)

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
                PayTask alipay = new PayTask(PayActivity.this);
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
