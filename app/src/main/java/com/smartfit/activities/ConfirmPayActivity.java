package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.beans.MouthBillInfo;
import com.smartfit.beans.NewMonthServerInfo;
import com.smartfit.beans.UseableEventInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 支付确认  v1.0.3
 * <p/>
 * 只针对包月方式使用    ：当包月数目大于 1   时：包月可以同时使用优惠劵和实体卡，其他支付 不可同时使用（注：月季卡 和半年卡 不可使用优惠劵和实体卡）
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

    private int numTicket;//选择优惠劵数

    /****
     * 页面跳转 index
     * <p/>
     * //定义  1 ：团体课  2.小班课  3.私教课 4.有氧器械  5 再次开课 （直接付款） 6  （学员）自定课程  7 教练自订课程  8 淋浴付费  9 包月支付
     */
    private int pageIndex = 1;


    private StringBuffer eventUserIds, cardSNNumbers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pay);
        ButterKnife.bind(this);
        initView();


    }

    private void initView() {
        tvTittle.setText("支付确认");
        num = getIntent().getIntExtra(Constants.PASS_STRING, 1);
        newMonthServerInfo = getIntent().getParcelableExtra(Constants.PASS_OBJECT);
        tvName.setText(String.format("%d个包月服务", num));
        tvMoney.setText("￥" + String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * num));
        ivHeader.setImageResource(R.mipmap.icon_yueka);
        pageIndex = getIntent().getIntExtra(Constants.PAGE_INDEX, 1);
        if (num == 1) {
            if (newMonthServerInfo.getEventListDTOs() != null && newMonthServerInfo.getEventListDTOs().size() > 0) {
                tvUserTicketUsable.setVisibility(View.VISIBLE);
                tvUserTicketUsable.setText(String.format("%d张可用", newMonthServerInfo.getEventListDTOs().size()));
                tvTicketValue.setText(String.format("-￥%s", newMonthServerInfo.getDefaultMonthPrice()));
                tvPayMoney.setText(String.format("￥0"));
            }
        } else {
            tvPayMoney.setText(String.format("￥%s", String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * num)));
        }
    }

    ArrayList<UseableEventInfo> ticketInfos;
    ArrayList<String> cardNums;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GET_TICKET_CODE && resultCode == RESULT_OK) {
            if (data.getExtras() != null) {
                ticketInfos = data.getParcelableArrayListExtra(Constants.PASS_OBJECT);
                if (ticketInfos != null && ticketInfos.size() > 0) {
                    numTicket = ticketInfos.size();
                    tvTicketValue.setText(String.format("-￥%s", String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * ticketInfos.size())));
                    countLeftMoney(ticketInfos, cardNums);
                }
            }


        } else if (requestCode == GET_CARD_CODE && resultCode == RESULT_OK) {
            if (data.getExtras() != null) {
                cardNums = data.getStringArrayListExtra(Constants.PASS_OBJECT);
                if (cardNums != null && cardNums.size() > 0) {
                    tvUserCard.setText(String.format("-￥%s", String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * cardNums.size())));
                    countLeftMoney(ticketInfos, cardNums);
                }
            }
        }
    }

    private void countLeftMoney(ArrayList<UseableEventInfo> ticketInfos, ArrayList<String> cardNums) {
        int count = ((ticketInfos != null && ticketInfos.size() > 0) ? ticketInfos.size() : 0) + ((cardNums != null && cardNums.size() > 0) ? cardNums.size() : 0);

        if (count > 0) {
            tvPayMoney.setText(String.format("￥%s", String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * (num - count))));
        } else {
            tvPayMoney.setText(String.format("￥%s", String.valueOf(Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * num)));
        }
    }

    @OnClick({R.id.iv_back, R.id.rl_user_ticket_ui, R.id.rl_select_card_ui, R.id.btn_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_user_ticket_ui:
                if (newMonthServerInfo.getEventListDTOs() != null && newMonthServerInfo.getEventListDTOs().size() > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(Constants.PASS_OBJECT, newMonthServerInfo.getEventListDTOs());
                    openActivity(SelectTicketToBuyActivity.class, bundle, GET_TICKET_CODE);
                } else {
                    mSVProgressHUD.showInfoWithStatus("无可用优惠劵", SVProgressHUD.SVProgressHUDMaskType.Clear);
                }
                break;
            case R.id.rl_select_card_ui:
                if (num - numTicket > 0) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.PASS_STRING, num - numTicket);
                    openActivity(SelectCardToBuyActivity.class, bundle, GET_CARD_CODE);
                } else {
                    mSVProgressHUD.showInfoWithStatus("已使用优惠劵", SVProgressHUD.SVProgressHUDMaskType.Clear);
                }
                break;
            case R.id.btn_pay:
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
            for (UseableEventInfo ticket : ticketInfos) {
                eventUserIds.append(ticket.getId()).append("|");
            }
            maps.put("eventUserIds", eventUserIds.toString());//券
        }

        if (cardNums != null && cardNums.size() > 0) {
            eventUserIds = new StringBuffer();
            for (String card : cardNums) {
                eventUserIds.append(card).append("|");
            }
            maps.put("cardSNNumbers", eventUserIds.toString());//卡号
        }

        PostRequest request = new PostRequest(Constants.ORDER_ORDERMONTHSELL, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                MouthBillInfo billInfo = JsonUtils.objectFromJson(response, MouthBillInfo.class);
                if (billInfo != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.PAGE_INDEX, 9);// 7  包月支付
//                    bundle.putString(Constants.COURSE_ID, billInfo.getId());
                    bundle.putString(Constants.COURSE_MONEY, billInfo.getOrderPrice());
                    bundle.putString(Constants.COURSE_ORDER_CODE, billInfo.getOrderCode());
                    openActivity(PayActivity.class, bundle);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(ConfirmPayActivity.this);
        mQueue.add(request);
    }
}
