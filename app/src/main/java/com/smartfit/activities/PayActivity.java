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
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.smartfit.R;
import com.smartfit.commons.Constants;
import com.smartfit.utils.AliPayUtiils;
import com.smartfit.utils.MD5;
import com.smartfit.utils.PayResult;
import com.smartfit.utils.Util;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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


    private int payStyle = 1;// 1  微信   2 支付宝

    private String orderID;

    /****
     * 页面跳转 index
     * <p/>
     * //定义  1 ：团体课  2.小班课  3.私教课
     */
    private int pageIndex = 1;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.AliPay.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        mSVProgressHUD.showSuccessWithStatus("支付成功");


                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            mSVProgressHUD.showSuccessWithStatus("支付结果确认中");

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            mSVProgressHUD.showErrorWithStatus("支付失败");
                            if (pageIndex == 1) {
                                openActivity(GroupClassOrderSuccessActivity.class);
                            } else if (pageIndex == 2) {
                                openActivity(GroupClassOrderSuccessActivity.class);
                            } else {
                                openActivity(OrderPrivateEducationClassActivity.class);
                            }
                            finish();

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
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText("付款");
        pageIndex = getIntent().getIntExtra(Constants.PAGE_INDEX, 1);

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
                        mSVProgressHUD.showErrorWithStatus("您未安装微信客户端!", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                        return;
                    }
                }

                goPay();
            }
        });

    }

    private void goPay() {
        //TODO   orderID   = ;
        if (payStyle == 1) {
            //微信支付
            weixinPay();

        } else if (payStyle == 2) {
            //支付宝支付
            pay();

        }
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
            mSVProgressHUD.showWithStatus("正在生成订单");
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
            packageParams.add(new BasicNameValuePair("body", "标题"));//注：如果商品为中文时，需要转码 否则会报签名失败paySuccess.getTitle()
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
        String orderInfo = AliPayUtiils.getOrderInfo(getString(R.string.app_name), orderID, "0.01");//String.valueOf(payMoney)

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
