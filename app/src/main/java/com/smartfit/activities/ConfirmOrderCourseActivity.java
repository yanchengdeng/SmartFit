package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.MessageEvent.FinishActivityAfterPay;
import com.smartfit.MessageEvent.UpdateAreoClassDetail;
import com.smartfit.MessageEvent.UpdateCoachClass;
import com.smartfit.MessageEvent.UpdateCustomClassInfo;
import com.smartfit.MessageEvent.UpdateGroupClassDetail;
import com.smartfit.MessageEvent.UpdatePrivateClassDetail;
import com.smartfit.R;
import com.smartfit.beans.EventOrderResult;
import com.smartfit.beans.EventUserful;
import com.smartfit.beans.OrderCourse;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 支付 课程类
 */
public class ConfirmOrderCourseActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.iv_header)
    ImageView ivHeader;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_money)
    TextView tvMoney;
    @Bind(R.id.tv_user_ticket_tips)
    TextView tvUserTicketTips;
    @Bind(R.id.tv_user_ticket_usable)
    TextView tvUserTicketUsable;
    @Bind(R.id.tv_ticket_value)
    TextView tvTicketValue;
    @Bind(R.id.rl_user_ticket_ui)
    RelativeLayout rlUserTicketUi;
    @Bind(R.id.tv_user_card)
    TextView tvUserCard;
    @Bind(R.id.rl_select_card_ui)
    RelativeLayout rlSelectCardUi;
    @Bind(R.id.tv_pay_money)
    TextView tvPayMoney;
    @Bind(R.id.btn_pay)
    Button btnPay;
    @Bind(R.id.tv_no_need_pay)
    TextView tvNoNeedPay;
    @Bind(R.id.rl_server_mouth_no_pay)
    RelativeLayout rlServerMouthNoPay;
    @Bind(R.id.tv_buy_month_server)
    TextView tvBuyMonthServer;
    @Bind(R.id.rl_go_buy_month_server_ui)
    RelativeLayout rlGoBuyMonthServerUi;
    @Bind(R.id.tv_select_pay_type_tips)
    TextView tvSelectPayTypeTips;

    private int GET_TICKET_CODE = 0x0010;
    private int GET_CARD_CODE = 0x0011;

    //订单课程失败后提示
    private String messsge = "该课程已经开课。请预定其他课程";

    /****
     * 页面跳转 index
     * <p>
     * //定义  1 ：团体课  2.小班课  3.私教课 4.有氧器械  5 再次开课 （直接付款） 6  （学员）自定课程  7 教练自订课程  8 淋浴付费  9 包月支付  10 季度、半年
     */
    private int pageIndex = 1;

    private String payMoney = "0";
    private String courseId;

    private String orderCode;

    private String startTime, endTime;

    private String couserType = "0";

    private ArrayList<EventUserful> userfulEventes = new ArrayList<>();
    private String classRoomId;

    private String cardCode;

    private String orderID;//订单 id
    private EventBus eventBus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order_course);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        initView();
        addLisener();
    }


    @Subscribe
    public void onEvent(Object event) {
        if (event instanceof FinishActivityAfterPay) {
            finish();
        }
    }

    private void initView() {
        tvTittle.setText("支付确认");
        startTime = getIntent().getStringExtra("start");
        endTime = getIntent().getStringExtra("end");
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

        tvMoney.setText("￥" + payMoney);
        tvPayMoney.setText("￥" + payMoney);
        tvNoNeedPay.setText("-￥" + payMoney);


        if (NetUtil.getUserInfo() != null) {
            if (NetUtil.getISvip()) {
                if (pageIndex == 1 || pageIndex == 4) {
                    rlUserTicketUi.setVisibility(View.GONE);
                    rlSelectCardUi.setVisibility(View.GONE);
                    rlServerMouthNoPay.setVisibility(View.VISIBLE);
                    tvPayMoney.setText("￥" + 0);
                    payMoney = "0";
                    isUserdTicket = true;
                    tvSelectPayTypeTips.setVisibility(View.GONE);
                } else {
                    rlUserTicketUi.setVisibility(View.VISIBLE);
                    rlSelectCardUi.setVisibility(View.VISIBLE);
                    rlServerMouthNoPay.setVisibility(View.GONE);
                    tvPayMoney.setText("￥" + payMoney);
                }

            } else {
                rlUserTicketUi.setVisibility(View.VISIBLE);
                rlSelectCardUi.setVisibility(View.VISIBLE);
                rlServerMouthNoPay.setVisibility(View.GONE);
                tvPayMoney.setText("￥" + payMoney);
                isUserdTicket = false;
                if (pageIndex == 1 || pageIndex == 4) {
                    rlGoBuyMonthServerUi.setVisibility(View.VISIBLE);
                }
            }
            changePayButtonContent();
        }

        getUseFullEvent();

/**注意：包月  ：只增对 团操  和器械才免费  ：  其他有优惠券 或实体卡 可以使用 或是 本地金额支付、第三方支付
 *
 */
        if (pageIndex == 1) {
            tvName.setText("团体课预约");
        } else if (pageIndex == 2) {
            tvName.setText("小班课预约");
        } else if (pageIndex == 3) {
            tvName.setText("私教课预约");
        } else if (pageIndex == 4) {
            tvName.setText("有氧器械预约");
        } else if (pageIndex == 5) {
            tvName.setText("再次开课");
        } else if (pageIndex == 6) {
            tvName.setText("学员自定课程");
        } else if (pageIndex == 7) {
            tvName.setText("教练自定课程");
        } else if (pageIndex == 8) {
            tvName.setText("淋浴付费");
        }


    }
    /**
     * 获取可用卷
     */
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
                ArrayList<EventUserful> eventUserfuls = JsonUtils.listFromJson(response.getAsJsonArray("list"), EventUserful.class);
                if (eventUserfuls != null && eventUserfuls.size() > 0) {
                    selectTicketId = eventUserfuls.get(0).getId();
                    userfulEventes = eventUserfuls;
                    tvUserTicketUsable.setVisibility(View.VISIBLE);
                    tvUserTicketUsable.setText(String.format("%d张可用", eventUserfuls.size()));
                    if (eventUserfuls.get(0).getEventType().equals("21")) {
                        isUserdTicket = false;
                        isUserCashTicket = true;
                        tvTicketValue.setText(String.format("-￥%s", eventUserfuls.get(0).getTicketPrice()));
                        if (Float.parseFloat(eventUserfuls.get(0).getTicketPrice()) >= Float.parseFloat(payMoney)) {
                            tvPayMoney.setText("￥0");
                        } else {
                            tvPayMoney.setText("￥" + (Float.parseFloat(payMoney) - Float.parseFloat(eventUserfuls.get(0).getTicketPrice())));
                            payMoney = String.valueOf(Float.parseFloat(payMoney) - Float.parseFloat(eventUserfuls.get(0).getTicketPrice()));
                        }
                    } else {
                        tvTicketValue.setText(String.format("-￥%s", payMoney));
                        tvPayMoney.setText("￥0");
                        isUserdTicket = true;
                        isUserCashTicket = false;
                    }
                    changePayButtonContent();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage());

            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(ConfirmOrderCourseActivity.this);
        mQueue.add(request);

    }


    /***
     * 获取订单id
     */
    private void getOrderId() {
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
        } else if (pageIndex == 9) {
            orderID = orderCode;
            goPay();
        } else {
            if (TextUtils.isEmpty(orderCode)) {
                getOrderCorse();
            } else {
                orderID = orderCode;
                if (isUserCard) {
                    payCardPay();
                } else if (isUserdTicket) {
                    goPay();
                }
            }
        }
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
        request.headers = NetUtil.getRequestBody(ConfirmOrderCourseActivity.this);
        mQueue.add(request);

    }

    /**
     * 获取课程订单
     */
    private void getOrderCorse() {
        mSVProgressHUD.showWithStatus("获取订单信息", SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> map = new HashMap<>();
        map.put("courseId", courseId);
        if (isUserCashTicket)
            map.put("cashEventUserId", selectTicketId);
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
        request.headers = NetUtil.getRequestBody(ConfirmOrderCourseActivity.this);
        mQueue.add(request);
    }


    private void goPay() {
        //支付机械课程

//        if (userfulEventes.size() != 0) {
//            if (couserType.equals("3")) {
//                payAerobicByEvent();
//            } else {
//                if (!TextUtils.isEmpty(selectTicketId))
//                    payUserCouponWithEventUserId();
//            }

        if (isUserdTicket || isUserCashTicket) {
            if (isUserdTicket) {
                if (couserType.equals("3")) {
                    payAerobicByEvent();
                } else {
                    payUserCouponWithEventUserId();
                }
            } else {
                payOrder();
            }
        } else if (isUserCard) {
            payCardPay();
        }


//        } else {
//            mSVProgressHUD.dismiss();
//            mSVProgressHUD.showInfoWithStatus(getString(R.string.do_later), SVProgressHUD.SVProgressHUDMaskType.Clear);
//        }
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
        request.headers = NetUtil.getRequestBody(ConfirmOrderCourseActivity.this);
        mQueue.add(request);
    }


    private String areaCourseId;

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
                    areaCourseId = orderCourse.getGoodsId();
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
        request.headers = NetUtil.getRequestBody(ConfirmOrderCourseActivity.this);
        mQueue.add(request);
    }

    //是否使用优惠券、现金券 实体卡
    boolean isUserdTicket, isUserCashTicket, isUserCard;


    private String selectTicketId;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_TICKET_CODE && resultCode == RESULT_OK) {

            if (data.getExtras() != null && null != data.getParcelableExtra(Constants.PASS_STRING)) {
                payMoney = getIntent().getStringExtra(Constants.COURSE_MONEY);
                EventUserful item = data.getParcelableExtra(Constants.PASS_STRING);
                selectTicketId = item.getId();
                if (item.getEventType().equals("21")) {
                    isUserdTicket = false;
                    isUserCashTicket = true;
                    // 0现金券1满减券2折扣券
                    if (item.getCashEventType().equals("0")) {
                        tvTicketValue.setText(String.format("-￥%s", item.getTicketPrice()));
                        if (Float.parseFloat(item.getTicketPrice()) >= Float.parseFloat(payMoney)) {
                            tvPayMoney.setText("￥0");
                        } else {
                            tvPayMoney.setText("￥" + (Float.parseFloat(payMoney) - Float.parseFloat(item.getTicketPrice())));
                            payMoney = String.valueOf(Float.parseFloat(payMoney) - Float.parseFloat(item.getTicketPrice()));
                        }
                    } else if (item.getCashEventType().equals("1")) {
                        tvTicketValue.setText(String.format("-￥%s", item.getTicketPrice()));
                        if (Float.parseFloat(item.getTicketPrice()) >= Float.parseFloat(payMoney)) {
                            tvPayMoney.setText("￥0");
                        } else {
                            tvPayMoney.setText("￥" + (Float.parseFloat(payMoney) - Float.parseFloat(item.getTicketPrice())));
                            payMoney = String.valueOf(Float.parseFloat(payMoney) - Float.parseFloat(item.getTicketPrice()));
                        }

                    } else if (item.getCashEventType().equals("2")) {
                        float discount = (1 - Float.parseFloat(item.getTicketPrice())/10) * Float.parseFloat(payMoney);
                        tvTicketValue.setText(String.format("-￥%s", String.valueOf(discount)));
                        if (discount >= Float.parseFloat(payMoney)) {
                            tvPayMoney.setText("￥0");
                        } else {
                            tvPayMoney.setText("￥" + (Float.parseFloat(payMoney) - discount));
                            payMoney = String.valueOf(Float.parseFloat(payMoney) - discount);
                        }

                    }

                } else {
                    tvTicketValue.setText(String.format("-￥%s", payMoney));
                    tvPayMoney.setText("￥0");
                    isUserdTicket = true;
                    isUserCashTicket = false;
                }
            } else {
                payMoney = getIntent().getStringExtra(Constants.COURSE_MONEY);
                isUserdTicket = false;
                tvPayMoney.setText("￥" + payMoney);
                tvTicketValue.setText("");
                tvUserTicketUsable.setVisibility(View.VISIBLE);
                tvUserTicketUsable.setText(String.format("%d张可用", userfulEventes.size()));
            }

            changePayButtonContent();

        } else if (requestCode == GET_CARD_CODE && resultCode == RESULT_OK) {
            if (data.getExtras() != null && null != data.getExtras().getParcelableArrayList(Constants.PASS_OBJECT)) {
                isUserCard = true;
                tvUserCard.setText(String.format("-￥%s", payMoney));
                tvPayMoney.setText("￥0");
                ArrayList<String> codes = data.getStringArrayListExtra(Constants.PASS_OBJECT);
                if (codes != null && codes.size() > 0) {
                    cardCode = codes.get(0);
                }
                changePayButtonContent();
            }
        }
    }


    private void changePayButtonContent() {
        if (tvPayMoney.getText().toString().equals("￥0")) {
            btnPay.setText("确认提交");
        } else {
            btnPay.setText("确认支付");
        }
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//优惠劵
        rlUserTicketUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userfulEventes != null && userfulEventes.size() > 0) {
                    if (!isUserCard) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(Constants.PASS_OBJECT, userfulEventes);
                        openActivity(SelectUseableTicketForCourseActivity.class, bundle, GET_TICKET_CODE);
                    } else {
                        if (isUserCard)
                            mSVProgressHUD.showInfoWithStatus("已使用过实体卡", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    }
                } else {
                    mSVProgressHUD.showInfoWithStatus("无优惠券可用", SVProgressHUD.SVProgressHUDMaskType.Clear);
                }
            }
        });

        //实体卡
        rlSelectCardUi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUserCard && !isUserdTicket) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.PASS_STRING, 1);
                    if (couserType.equals("1")) {
                        bundle.putString(Constants.COURSE_TYPE, couserType);
                    } else if (couserType.equals("2")) {
                        bundle.putString(Constants.COURSE_TYPE, couserType);
                    } else if (couserType.equals("0") || couserType.equals("3")) {
                        bundle.putString(Constants.COURSE_TYPE, couserType);
                    }

                    openActivity(SelectCardToBuyActivity.class, bundle, GET_CARD_CODE);
                } else {
                    if (isUserdTicket)
                        mSVProgressHUD.showInfoWithStatus("已使用过优惠劵", SVProgressHUD.SVProgressHUDMaskType.Clear);
                }
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserCard || isUserdTicket) {
                    //直接支付 即优惠劵支付
                    getOrderId();
                } else {
                    if (payMoney.equals("0")) {
                        getOrderId();
                    } else {
                        //去支付
                        Bundle payBundle = getIntent().getExtras();
                        payBundle.putString(Constants.COURSE_MONEY, payMoney);
                        if (isUserCashTicket)
                            payBundle.putString("cash_ticket", selectTicketId);
                        openActivity(PayActivity.class, payBundle);
                        finish();
                    }

                }
            }
        });

        tvBuyMonthServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(EventActivityNewVersionActivity.class);
            }
        });

    }

    private void payCardPay() {
        mSVProgressHUD.showWithStatus("支付中...", SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> map = new HashMap<>();
        map.put("orderCode", orderID);
        map.put("cardSNNumber", cardCode);
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
        request.headers = NetUtil.getRequestBody(ConfirmOrderCourseActivity.this);
        mQueue.add(request);
    }

    /**
     * 自付完成后  需要操作的步骤
     */
    private void dealAfterPay() {
        if (pageIndex == 1) {
            eventBus.post(new UpdateGroupClassDetail());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        } else if (pageIndex == 3) {
            eventBus.post(new UpdatePrivateClassDetail());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        } else if (pageIndex == 4) {
            UpdateAreoClassDetail updateAreoClassDetail = new UpdateAreoClassDetail();
            updateAreoClassDetail.setId(areaCourseId);
            eventBus.post(updateAreoClassDetail);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        } else if (pageIndex == 5) {
            eventBus.post(new UpdateCoachClass());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        } else if (pageIndex == 6) {
            eventBus.post(new UpdateCustomClassInfo());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 1000);
        } else if (pageIndex == 7) {
            finish();
        } else if (pageIndex == 8 || pageIndex == 9 || pageIndex == 10) {
            eventBus.post(new FinishActivityAfterPay());
            Intent intent;
            if (TextUtils.isEmpty(NetUtil.getUserInfo().getCoachId())) {
                intent = new Intent(ConfirmOrderCourseActivity.this, CustomeMainActivity.class);
            } else {
                intent = new Intent(ConfirmOrderCourseActivity.this, CustomeCoachActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();

        }

    }

    /**
     * 使用活动卷支付
     */
    private void payUserCouponWithEventUserId() {
        if (TextUtils.isEmpty(selectTicketId)) {
            return;
        }
        mSVProgressHUD.setText("正在支付");
        Map<String, String> map = new HashMap<>();
        map.put("eventUserId", selectTicketId);
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
        request.headers = NetUtil.getRequestBody(ConfirmOrderCourseActivity.this);
        mQueue.add(request);
    }

    /**
     * 使用活动卷支付(支付有氧器械课程)
     */
    private void payAerobicByEvent() {
        if (TextUtils.isEmpty(selectTicketId)) {
            return;
        }
        mSVProgressHUD.setText("正在支付");
        Map<String, String> map = new HashMap<>();
        map.put("eventUserId", selectTicketId);
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
        request.headers = NetUtil.getRequestBody(ConfirmOrderCourseActivity.this);
        mQueue.add(request);
    }

}
