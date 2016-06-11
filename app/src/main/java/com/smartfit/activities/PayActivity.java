package com.smartfit.activities;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.smartfit.MessageEvent.UpdateAreoClassDetail;
import com.smartfit.MessageEvent.UpdateCoachClass;
import com.smartfit.MessageEvent.UpdateCustomClassInfo;
import com.smartfit.MessageEvent.UpdateGroupClassDetail;
import com.smartfit.MessageEvent.UpdatePrivateClassDetail;
import com.smartfit.R;
import com.smartfit.adpters.UserEventAdapter;
import com.smartfit.beans.EventOrderResult;
import com.smartfit.beans.EventUserful;
import com.smartfit.beans.OrderCourse;
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.beans.WXPayAppId;
import com.smartfit.commons.Constants;
import com.smartfit.utils.AliPayUtiils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PayResult;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.views.MyListView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Bind(R.id.tv_user_event_tittle)
    TextView tvUserEventTittle;
    @Bind(R.id.listview)
    MyListView listview;
    @Bind(R.id.iv_wx)
    ImageView ivWx;
    @Bind(R.id.tv_wx)
    TextView tvWx;
    @Bind(R.id.iv_alipay)
    ImageView ivAlipay;
    @Bind(R.id.tv_alipay)
    TextView tvAlipay;
    @Bind(R.id.tv_card_bind_tips)
    TextView tvCardBindTips;
    @Bind(R.id.tv_card_code)
    TextView tvCardCode;
    @Bind(R.id.et_card_code)
    EditText etCardCode;
    @Bind(R.id.tv_vertify_card_code)
    TextView tvVertifyCardCode;
    @Bind(R.id.rl_card_code_ui)
    RelativeLayout rlCardCodeUi;
    @Bind(R.id.tv_card_code_pay_tips)
    TextView tvCardCodePayTips;


    private int payStyle = 0;//0 本地余额支付 1  微信   2 支付宝 3.优惠券  4 实体卡支付

    private String orderID;//订单 id

    //订单课程失败后提示
    private String messsge = "该课程已经开课。请预定其他课程";

    /****
     * 页面跳转 index
     * <p/>
     * //定义  1 ：团体课  2.小班课  3.私教课 4.有氧器械  5 再次开课 （直接付款） 6  （学员）自定课程  7 教练自订课程  8 淋浴付费
     */
    private int pageIndex = 1;

    private String payMoney = "0";

    private String leftMoney = "0";

    private String courseId;

    private String orderCode;

    private String startTime, endTime;

    private String couserType = "0";

    private EventBus eventBus;

    private List<EventUserful> userfulEventes = new ArrayList<>();
    private String classRoomId;

    private String cardCode;


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
        eventBus.register(this);
        initView();
        addLisener();
    }

    @Subscribe
    public void onEvent(Object event) {
        if (event instanceof FinishRechage) {
            finish();
        }
    }


    UserInfoDetail userInfo;

    private void getLeftMoney() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        PostRequest request = new PostRequest(Constants.USER_USERINFO, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                UserInfoDetail userInfoDetail = JsonUtils.objectFromJson(response, UserInfoDetail.class);
                userInfo = userInfoDetail;
                tvYeLeft.setText(userInfoDetail.getBalance());
                leftMoney = userInfoDetail.getBalance();
                selectPayStyle(userInfo.getBalance(), payMoney);
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
     * 活动  （包月、绑定）下单
     */
    private void getEventOrder() {
        mSVProgressHUD.showWithStatus("创建订单中", SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> maps = new HashMap<>();
        maps.put("eventId", courseId);
        PostRequest request = new PostRequest(Constants.ORDER_ORDEREVENT, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                EventOrderResult eventOrderResult = JsonUtils.objectFromJson(response, EventOrderResult.class);
                if (eventOrderResult != null) {
                    if (!TextUtils.isEmpty(eventOrderResult.getOrderCode())) {
                        orderID = eventOrderResult.getOrderCode();
                    }
                }
                goPay();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);

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
        } else {
            if (!TextUtils.isEmpty(leftMoney) && !TextUtils.isEmpty(payMoney)) {
                if (Float.parseFloat(leftMoney) >= Float.parseFloat(payMoney)) {
                    rlYe.setClickable(true);
                    ivYeSelected.setVisibility(View.VISIBLE);
                    ivWxSelected.setVisibility(View.GONE);
                    ivAlipaySelected.setVisibility(View.GONE);
                    payStyle = 0;
                } else {
                    rlYe.setClickable(true);
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
        couserType = getIntent().getStringExtra(Constants.COURSE_TYPE);
        if (pageIndex == 4) {
            startTime = getIntent().getStringExtra("start_time");
            endTime = getIntent().getStringExtra("end_time");
            classRoomId = getIntent().getStringExtra("classroom");
        }
        tvPayMoney.setText(payMoney + "元");
        Constants.PAGE_INDEX_FROM = pageIndex;
        if (pageIndex != 7 && pageIndex != 8) {
            getUseFullEvent();

        }

        if (!TextUtils.isEmpty(couserType) && Integer.parseInt(couserType)<5  && pageIndex<5) {
            showCardPay();
        }
        getLeftMoney();

    }

    private void showCardPay() {
        tvCardBindTips.setVisibility(View.VISIBLE);
        rlCardCodeUi.setVisibility(View.VISIBLE);
        tvCardCodePayTips.setVisibility(View.VISIBLE);
    }

    /**
     * 获取可用的活动卷
     */
    private void getUseFullEvent() {
        String courseEventId = "";
        if (couserType.equals("0")) {
            courseEventId = "";
        } else if (couserType.equals("1")) {
            courseEventId = courseId;
        } else if (couserType.equals("2")) {
            courseEventId = courseId;
        } else if (couserType.equals("3")) {
            courseEventId = "";
        }
        Map<String, String> maps = new HashMap<>();
        maps.put("courseId", courseEventId);
        maps.put("courseType", couserType);
        PostRequest request = new PostRequest(Constants.EVENT_GETUSEFULLEVENTS, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                List<EventUserful> eventUserfuls = JsonUtils.listFromJson(response.getAsJsonArray("list"), EventUserful.class);
                if (eventUserfuls != null && eventUserfuls.size() > 0) {
                    listview.setAdapter(new UserEventAdapter(PayActivity.this, eventUserfuls));
                    userfulEventes = eventUserfuls;
                } else {
                    listview.setVisibility(View.GONE);
                    tvUserEventTittle.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage());
                listview.setVisibility(View.GONE);
                tvUserEventTittle.setVisibility(View.GONE);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(PayActivity.this);
        mQueue.add(request);
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                reInitUserEvent();
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.ch_select);
                checkBox.setChecked(!checkBox.isChecked());
                userfulEventes.get(position).setIsCheck(checkBox.isChecked());
                payStyle = 3;
                ivAlipaySelected.setVisibility(View.GONE);
                ivWxSelected.setVisibility(View.GONE);
                ivYeSelected.setVisibility(View.GONE);
            }
        });

        rlYe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Float.parseFloat(leftMoney) < Float.parseFloat(payMoney)) {
                    mSVProgressHUD.showInfoWithStatus("余额不足", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    return;
                }
                reInitUserEvent();
                payStyle = 0;
                ivAlipaySelected.setVisibility(View.GONE);
                ivWxSelected.setVisibility(View.GONE);
                ivYeSelected.setVisibility(View.VISIBLE);
            }
        });


        rlWx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Float.parseFloat(payMoney) == 0) {
                    mSVProgressHUD.showInfoWithStatus("推荐余额直接支付", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    return;
                }
                payStyle = 1;
                ivWxSelected.setVisibility(View.VISIBLE);
                ivYeSelected.setVisibility(View.GONE);
                ivAlipaySelected.setVisibility(View.GONE);
                reInitUserEvent();
            }
        });


        rlAlipay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Float.parseFloat(payMoney) == 0) {
                    mSVProgressHUD.showInfoWithStatus("推荐余额直接支付", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    return;
                }
                payStyle = 2;
                ivAlipaySelected.setVisibility(View.VISIBLE);
                ivWxSelected.setVisibility(View.GONE);
                ivYeSelected.setVisibility(View.GONE);
                reInitUserEvent();
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

                getOrderId();

            }
        });


        /***
         * 验证实体卡
         */
        tvVertifyCardCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vertyfiyCode(etCardCode.getEditableText().toString());
            }
        });
    }

    /**
     * 验证实体卡
     *
     * @param code
     */
    private void vertyfiyCode(final String code) {
        if (TextUtils.isEmpty(code)) {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.card_tips), SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }

        if (code.length() != 13) {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.card_lenght_tips), SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("cardSNNumber", code);
        map.put("cardType", couserType);
        mSVProgressHUD.showWithStatus(getString(R.string.vertify_ing), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        PostRequest request = new PostRequest(Constants.EVENT_CARDCHECK, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                cardCode = code;
                tvVertifyCardCode.setText(String.format("抵扣%s元", payMoney));
                tvVertifyCardCode.setBackground(null);
                tvVertifyCardCode.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.icon_pt_check_on), null);
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus("已验证,可直接使用", SVProgressHUD.SVProgressHUDMaskType.Clear);
                payStyle = 4;
                ivWxSelected.setVisibility(View.GONE);
                ivYeSelected.setVisibility(View.GONE);
                ivAlipaySelected.setVisibility(View.GONE);
                reInitUserEvent();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(PayActivity.this);
        mQueue.add(request);
    }


    /**
     * 重置优惠券选择
     */
    private void reInitUserEvent() {
        if (userfulEventes.size() > 0) {
            for (EventUserful item : userfulEventes) {
                item.setIsCheck(false);
            }
            ((UserEventAdapter) listview.getAdapter()).notifyDataSetChanged();
        }
    }

    /**
     * 获取课程订单
     */
    private void getOrderCorse() {
        mSVProgressHUD.showWithStatus("获取订单信息", SVProgressHUD.SVProgressHUDMaskType.Clear);
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
                }
                goPay();
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
     * 获取订单id
     */
    private void getOrderId() {
        if (userInfo != null) {
            if (!TextUtils.isEmpty(userInfo.getBalance())) {
                tvYeLeft.setText(userInfo.getBalance());
                leftMoney = userInfo.getBalance();
                if (pageIndex == 4) {
                    if (TextUtils.isEmpty(orderCode)) {
                        payAreao();
                    } else {
                        orderID = orderCode;
                        goPay();
                    }
                } else if (pageIndex == 5) {
                    getOrderCorse();
                } else if (pageIndex == 6) {
                    getOrderCorse();
                } else if (pageIndex == 7) {
                    getEventOrder();
                } else if (pageIndex == 8) {
                    orderID = orderCode;
                    goPay();
                } else {
                    if (TextUtils.isEmpty(orderCode)) {
                        getOrderCorse();
                    } else {
                        orderID = orderCode;
                        goPay();
                    }
                }
            }
        } else {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.do_later), SVProgressHUD.SVProgressHUDMaskType.Clear);
        }
    }


    private void goPay() {

        if (TextUtils.isEmpty(orderID)) {
            mSVProgressHUD.dismiss();
            mSVProgressHUD.showInfoWithStatus(messsge, SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }
        if (payStyle == 1) {
            //微信支付
            wxPrePay();

        } else if (payStyle == 2) {
            //支付宝支付
            pay();

        } else if (payStyle == 0) {
            payOrder();
        } else if (payStyle == 3) {

            //支付机械课程
            if (userfulEventes.size() != 0) {
                if (couserType.equals("3")) {
                    payAerobicByEvent();
                } else {
                    payUserCouponWithEventUserId();
                }
            }
        }else if (payStyle==4){
            payCardPay();
        }
    }

    private void payCardPay() {
        mSVProgressHUD.showWithStatus("支付中...", SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> map = new HashMap<>();
        map.put("orderCode", orderID);
        map.put("cardSNNumber",cardCode);
        PostRequest request = new PostRequest(Constants.PAY_CARDPAY, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc", response.toString());
                mSVProgressHUD.dismiss();
                dealAfterPay();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mSVProgressHUD.dismiss();
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(PayActivity.this);
        mQueue.add(request);
    }

    /**
     * 服务端签名  微信支付
     */
    private void wxPrePay() {
        btnPay.setEnabled(false);
        mSVProgressHUD.showWithStatus("获取订单信息", SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> map = new HashMap<>();
        map.put("orderCode", orderID);
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
        request.headers = NetUtil.getRequestBody(PayActivity.this);
        mQueue.add(request);

    }

    private IWXAPI api;

    /**
     * 调用微信支付
     *
     * @param wxPayAppId
     */
    private void weixinPay(WXPayAppId wxPayAppId) {
        Constants.IS_PASS_FROM_ORDER = true;
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
            mSVProgressHUD.showInfoWithStatus("正常调起支付", SVProgressHUD.SVProgressHUDMaskType.Clear);
            // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
            api.sendReq(req);
        } else {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.do_later), SVProgressHUD.SVProgressHUDMaskType.Clear);
            mSVProgressHUD.dismiss();
        }
    }

    /**
     * 获取选择的优惠券
     *
     * @return
     */
    private EventUserful getSeletctedEvent() {
        for (EventUserful item : userfulEventes) {
            if (item.isCheck()) {
                return item;
            }
        }
        return userfulEventes.get(0);
    }

    /**
     * 使用活动卷支付
     */
    private void payUserCouponWithEventUserId() {
        mSVProgressHUD.setText("正在支付");
        Map<String, String> map = new HashMap<>();
        map.put("eventUserId", getSeletctedEvent().getId());
        map.put("courseId", courseId);
        map.put("orderCode", orderID);
        PostRequest request = new PostRequest(Constants.PAY_PAYBYEVENT, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus("支付成功", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                dealAfterPay();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(PayActivity.this);
        mQueue.add(request);
    }

    /**
     * 使用活动卷支付(支付有氧器械课程)
     */
    private void payAerobicByEvent() {
        mSVProgressHUD.setText("正在支付");
        Map<String, String> map = new HashMap<>();
        map.put("eventUserId", getSeletctedEvent().getId());
        map.put("orderCode", orderID);
        PostRequest request = new PostRequest(Constants.PAY_PAYAEROBICBYEVENT, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus("支付成功", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                dealAfterPay();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(PayActivity.this);
        mQueue.add(request);
    }

    /***
     * 支付有氧课程
     */
    private void payAreao() {


        mSVProgressHUD.showWithStatus("创建订单中", SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> map = new HashMap<>();
        map.put("classroomId", classRoomId);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        PostRequest request = new PostRequest(Constants.ORDER_ORDERAEROBIC, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                OrderCourse orderCourse = JsonUtils.objectFromJson(response.toString(), OrderCourse.class);
                if (orderCourse != null) {
                    if (!TextUtils.isEmpty(orderCourse.getOrderCode())) {
                        orderID = orderCourse.getOrderCode();
                    }
                }

                goPay();
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

        mSVProgressHUD.setText("正在支付");
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
        } else if (pageIndex == 7) {
            finish();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1000);

    }


    private boolean checkWeiXin() {
        try {
            getPackageManager().getApplicationInfo("com.tencent.mm", PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    private boolean checkALi() {
        try {
            getPackageManager().getApplicationInfo("com.eg.android.AlipayGphone", PackageManager.GET_UNINSTALLED_PACKAGES);
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
