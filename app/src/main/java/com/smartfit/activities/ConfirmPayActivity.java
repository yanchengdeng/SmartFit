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
import com.smartfit.R;
import com.smartfit.beans.EntityCardInfo;
import com.smartfit.beans.MouthBillInfo;
import com.smartfit.beans.NewMonthServerInfo;
import com.smartfit.beans.UseableEventInfo;
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
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 支付确认  v1.0.3
 * <p/>
 * 只针对包月方式使用    ：当包月数目大于 1   时：包月可以同时使用优惠劵和实体卡，其他支付 不可同时使用（注：月季卡 和半年卡 不可使用优惠劵和实体卡）
 * <p/>
 * v1.1
 * 当月数  大于>=3个月时  可选择季度卡 支付   月数 >= 12  可使用年卡支付
 *
 * @author yanchengdeng
 *         create at 2016/7/7 18:15
 */
public class ConfirmPayActivity extends BaseActivity {


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
    @Bind(R.id.tv_pay_money)
    TextView tvPayMoney;
    @Bind(R.id.btn_pay)
    Button btnPay;
    @Bind(R.id.rl_select_card_ui)
    RelativeLayout rlSelectCardUI;
    private int GET_TICKET_CODE = 0x0010;
    private int GET_CARD_CODE = 0x0011;

    private int num;//购买月卡时： 优惠劵  和 实体卡  总和 不超过 购买月卡 月数

    private NewMonthServerInfo newMonthServerInfo;

    private int numTicket, numCards;//选择优惠劵数、选择实体卡


    private StringBuffer eventUserIds, StringCashIds, cardSNNumbers;

    private EventBus eventBus;

    private String mouthCourseid;

    private String payMoney;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pay);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        initView();
    }

    @Subscribe
    public void onEvent(Object event) {
        if (event instanceof FinishActivityAfterPay) {
            finish();
        }
    }

    private void initView() {
        tvTittle.setText("支付确认");
        num = getIntent().getIntExtra(Constants.PASS_STRING, 1);
        newMonthServerInfo = getIntent().getParcelableExtra(Constants.PASS_OBJECT);
        tvName.setText(String.format("%d个包月服务", num));
        tvMoney.setText("￥" + String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * num));
        ivHeader.setImageResource(R.drawable.icon_yueka);
        payMoney = newMonthServerInfo.getDefaultMonthPrice();
        if (num == 1) {
            if (newMonthServerInfo.getEventListDTOs() != null && newMonthServerInfo.getEventListDTOs().size() > 0) {
                tvUserTicketUsable.setVisibility(View.VISIBLE);
                tvUserTicketUsable.setText(String.format("%d张可用", newMonthServerInfo.getEventListDTOs().size()));

                UseableEventInfo item = newMonthServerInfo.getEventListDTOs().get(0);
                ticketInfos = new ArrayList<>();
                ticketInfos.add(item);
                if (item.getEventType().equals("3")) {
                    tvTicketValue.setText(String.format("-￥%s", newMonthServerInfo.getDefaultMonthPrice()));
                    tvPayMoney.setText(String.format("￥0"));
                } else if (item.getEventType().equals("21")) {
                    if (item.getCashEventType().equals("0")) {
                        tvTicketValue.setText(String.format("-￥%s", item.getTicketPrice()));
                        if (Float.parseFloat(item.getTicketPrice()) >= Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice())) {
                            tvPayMoney.setText("￥0");
                        } else {
                            tvPayMoney.setText("￥" + (Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) - Float.parseFloat(item.getTicketPrice())));
                            payMoney = String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) - Float.parseFloat(item.getTicketPrice()));
                        }
                    } else if (item.getCashEventType().equals("1")) {
                        tvTicketValue.setText(String.format("-￥%s", item.getTicketPrice()));
                        if (Float.parseFloat(item.getTicketPrice()) >= Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice())) {
                            tvPayMoney.setText("￥0");
                        } else {
                            tvPayMoney.setText("￥" + (Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) - Float.parseFloat(item.getTicketPrice())));
                            payMoney = String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) - Float.parseFloat(item.getTicketPrice()));
                        }
                    } else if (item.getCashEventType().equals("2")) {
                        float discount = (1 - Float.parseFloat(item.getTicketPrice()) / 10) * Float.parseFloat(payMoney);
                        discount = Float.parseFloat(String.format("%.2f", discount));
                        tvTicketValue.setText(String.format("-￥%s", String.valueOf(discount)));
                        if (discount >= Float.parseFloat(payMoney)) {
                            tvPayMoney.setText("￥0");
                        } else {
                            tvPayMoney.setText("￥" + (Float.parseFloat(payMoney) - discount));
                            payMoney = String.valueOf(Float.parseFloat(payMoney) - discount);
                        }
                    }
                }
            } else {
                tvPayMoney.setText(String.format("￥%s", String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * num)));
            }
            if (newMonthServerInfo.getEventListDTOs() != null && newMonthServerInfo.getEventListDTOs().size() > 0) {
                tvUserTicketUsable.setVisibility(View.VISIBLE);
                tvUserTicketUsable.setText(String.format("%d张可用", newMonthServerInfo.getEventListDTOs().size()));
                numTicket = num;
            }
        } else {
            if (newMonthServerInfo.getEventListDTOs() != null && newMonthServerInfo.getEventListDTOs().size() > 0) {
                tvUserTicketUsable.setVisibility(View.VISIBLE);
                tvUserTicketUsable.setText(String.format("%d张可用", newMonthServerInfo.getEventListDTOs().size()));
            }

            tvPayMoney.setText(String.format("￥%s", String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * num)));
        }
        changePayButtonContent();
    }

    ArrayList<UseableEventInfo> ticketInfos;
    ArrayList<EntityCardInfo> cardNums;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_TICKET_CODE && resultCode == RESULT_OK) {
            if (data.getExtras() != null) {
                ticketInfos = data.getParcelableArrayListExtra(Constants.PASS_OBJECT);
                if (ticketInfos != null && ticketInfos.size() > 0) {
                    numTicket = ticketInfos.size();
//                    tvTicketValue.setText(String.format("-￥%s", String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * ticketInfos.size())));
                    countLeftMoney(ticketInfos, cardNums);
                } else {
                    //不选择优惠券时
                    ticketInfos = new ArrayList<>();
                    numTicket = 0;
                    tvTicketValue.setText("");
                    tvPayMoney.setText(String.format("￥%s", String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * num)));
                    changePayButtonContent();
                }
            }


        } else if (requestCode == GET_CARD_CODE && resultCode == RESULT_OK) {
            if (data.getExtras() != null) {
                cardNums = data.getParcelableArrayListExtra(Constants.PASS_OBJECT);
                if (cardNums != null && cardNums.size() > 0) {
                    numCards = cardNums.size();
                    //计算实体卡 实际是多少个月
                    int entityCardNum = 0;
                    for (EntityCardInfo item : cardNums) {
                        if (item.getType().equals("14")) {
                            entityCardNum = entityCardNum + 1;
                        } else if (item.getType().equals("15")) {
                            entityCardNum = entityCardNum + 3;
                        } else if (item.getType().equals("16")) {
                            entityCardNum = entityCardNum + 12;
                        }
                    }
                    tvUserCard.setText(String.format("-￥%s", String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * entityCardNum)));
                    countLeftMoney(ticketInfos, cardNums);
                }
            }
        }
    }

    int count;

    private void countLeftMoney(ArrayList<UseableEventInfo> ticketInfos, ArrayList<EntityCardInfo> cardNums) {
        //选择的优惠券包含的月数
        int slectMoutNum = countSelectNumMouth(ticketInfos);


        count = ((slectMoutNum > 0) ? slectMoutNum : 0) + ((cardNums != null && cardNums.size() > 0) ? cardNums.size() : 0);

        if (ticketInfos != null && ticketInfos.size() > 0) {
            UseableEventInfo cashEventInfo = null;
            for (UseableEventInfo item : ticketInfos) {
                if (item.getEventType().equals("21")) {
                    cashEventInfo = item;
                }
            }

            if (cashEventInfo != null) {
                Float ticketValue = 0f;
                if (cashEventInfo.getCashEventType().equals("0")) {
                    ticketValue = Float.parseFloat(cashEventInfo.getTicketPrice());
//                            + Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * (ticketInfos.size() - 1);
                } else if (cashEventInfo.getCashEventType().equals("1")) {
                    ticketValue = Float.parseFloat(cashEventInfo.getTicketPrice());
//                            + Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * (ticketInfos.size() - 1);
                } else if (cashEventInfo.getCashEventType().equals("2")) {
                    float discount = (1 - Float.parseFloat(cashEventInfo.getTicketPrice()) / 10) * Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * (num);
                    discount = Float.parseFloat(String.format("%.2f", discount));
                    ticketValue = discount;
                }
                tvTicketValue.setText(String.format("-￥%.2f", Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * slectMoutNum + ticketValue));
                Float payFloat = Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * (num - slectMoutNum) - ticketValue;
                if (payFloat<0){
                    payFloat  = 0f;
                }
                tvPayMoney.setText(String.format("￥%.2f", payFloat));
            } else {
                tvPayMoney.setText(String.format("￥%s", String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * (num - count))));
                tvTicketValue.setText(String.format("-￥%s", String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * count)));
            }
        } else {
            tvPayMoney.setText(String.format("￥%s", String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * num)));
        }

        changePayButtonContent();
    }

    private void changePayButtonContent() {
        if (tvPayMoney.getText().toString().equals("￥0")) {
            btnPay.setText("确认提交");
        } else {
            btnPay.setText("确认支付");
        }
    }

    /**
     * 计算已经选择券所代表的月数
     *
     * @param datas
     */
    private int countSelectNumMouth(List<UseableEventInfo> datas) {
        if (datas != null && datas.size() > 0) {
            int selectMouth = 0;
            for (int i = 0; i < datas.size(); i++) {
                if (datas.get(i).isCheck() == true) {
                    if (datas.get(i).getEventType().equals("3")) {
                        selectMouth = selectMouth + 1;
                    } else if (datas.get(i).getEventType().equals("15")) {
                        selectMouth = selectMouth + 3;
                    } else if (datas.get(i).getEventType().equals("16")) {
                        selectMouth = selectMouth + 12;
                    }
                }
            }
            LogUtil.w("dyc", "---" + selectMouth);
            return selectMouth;
        } else {
            return 0;
        }
    }

    @OnClick({R.id.iv_back, R.id.rl_user_ticket_ui, R.id.rl_select_card_ui, R.id.btn_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_user_ticket_ui:
                //购买一个月  服务 ：如果有优惠劵默认选第一个张
                if (newMonthServerInfo.getEventListDTOs() != null && newMonthServerInfo.getEventListDTOs().size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(Constants.PASS_OBJECT, newMonthServerInfo.getEventListDTOs());
                    bundle.putInt(Constants.PASS_STRING, num - numCards);
                    openActivity(SelectTicketToBuyActivity.class, bundle, GET_TICKET_CODE);
                } else {
                    mSVProgressHUD.showInfoWithStatus("无可用优惠劵", SVProgressHUD.SVProgressHUDMaskType.Clear);
                }
                break;
            case R.id.rl_select_card_ui:
                if (num - numTicket > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.PASS_STRING, num - numTicket);
                    bundle.putString(Constants.COURSE_TYPE, "0");
                    openActivity(SelectCardToBuyActivity.class, bundle, GET_CARD_CODE);
                } else {
                    mSVProgressHUD.showInfoWithStatus("已使用优惠劵", SVProgressHUD.SVProgressHUDMaskType.Clear);
                }
                break;
            case R.id.btn_pay:
                //全部抵用，不用支付
                doOrderMouthBill();
                break;
        }
    }


    /**
     * 月卡下单
     */
    private void doOrderMouthBill() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> maps = new HashMap<>();
        maps.put("useMonthRang", String.valueOf(num));
        if (ticketInfos != null && ticketInfos.size() > 0) {
            eventUserIds = new StringBuffer();
            StringCashIds = new StringBuffer();
            for (UseableEventInfo ticket : ticketInfos) {

                if (ticket.getEventType().equals("3") || ticket.getEventType().equals("15") || ticket.getEventType().equals("16")) {
                    eventUserIds.append(ticket.getId()).append("|");
                }
                if (ticket.getEventType().equals("21")) {
                    StringCashIds.append(ticket.getId());
                }
            }
            if (eventUserIds != null && eventUserIds.length() > 0) {
                if (eventUserIds.toString().substring(eventUserIds.length() - 1, eventUserIds.length()).equals("|")) {
                    maps.put("eventUserIds", eventUserIds.toString().substring(0, eventUserIds.length() - 1));//券
                } else {
                    maps.put("eventUserIds", eventUserIds.toString());//券
                }
            }
            if (StringCashIds != null && StringCashIds.length() > 0) {
                maps.put("cashEventUserId", StringCashIds.toString());
            }
        }

        if (cardNums != null && cardNums.size() > 0) {
            cardSNNumbers = new StringBuffer();
            for (EntityCardInfo card : cardNums) {
                cardSNNumbers.append(card.getCode()).append("|");
            }
            maps.put("cardSNNumbers", cardSNNumbers.toString());//卡号
        }

        PostRequest request = new PostRequest(Constants.ORDER_ORDERMONTHSELL, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                MouthBillInfo billInfo = JsonUtils.objectFromJson(response, MouthBillInfo.class);
                if (billInfo != null) {
                    mouthCourseid = billInfo.getId();
                    if (Float.parseFloat(billInfo.getOrderPrice()) == 0.0) {
                        payOrder(billInfo.getOrderCode());

                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putInt(Constants.PAGE_INDEX, 9);// 7  包月支付
                        bundle.putString(Constants.COURSE_ID, billInfo.getGoodsId());
                        bundle.putString(Constants.COURSE_MONEY, billInfo.getOrderPrice());
                        bundle.putString(Constants.COURSE_ORDER_CODE, billInfo.getOrderCode());
                        openActivity(PayActivity.class, bundle);
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
        request.headers = NetUtil.getRequestBody(ConfirmPayActivity.this);
        mQueue.add(request);
    }


    /***
     * 本地余额支付订单
     */
    private void payOrder(String orderID) {

        mSVProgressHUD.setText("正在支付");
        Map<String, String> map = new HashMap<>();
        map.put("orderCode", orderID);
        map.put("uid", SharedPreferencesUtils.getInstance().getString(Constants.UID, ""));
        PostRequest request = new PostRequest(Constants.PAY_BALANCEPAY, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                mSVProgressHUD.showSuccessWithStatus("已支付完成", SVProgressHUD.SVProgressHUDMaskType.Clear);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        eventBus.post(new FinishActivityAfterPay());
                        Intent intent;
                        if (TextUtils.isEmpty(NetUtil.getUserInfo().getCoachId())) {
                            intent = new Intent(ConfirmPayActivity.this, CustomeMainActivity.class);
                            intent.putExtra("hava_buy_mouth", true);
                            intent.putExtra("coursre_id", mouthCourseid);
                        } else {
                            intent = new Intent(ConfirmPayActivity.this, CustomeCoachActivity.class);
                            intent.putExtra("hava_buy_mouth", true);
                            intent.putExtra("coursre_id", mouthCourseid);
                        }
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                }, 1500);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(ConfirmPayActivity.this);
        mQueue.add(request);
    }
}
