package com.smartfit.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfit.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 支付确认  v1.0.3
 *
 * @author yanchengdeng
 *         create at 2016/7/7 18:15
 */
public class ConfirmPayActivity extends Activity {

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
    @Bind(R.id.tv_user_ticket)
    TextView tvUserTicket;
    @Bind(R.id.tv_user_card)
    TextView tvUserCard;
    @Bind(R.id.tv_pay_money)
    TextView tvPayMoney;
    @Bind(R.id.btn_pay)
    Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_pay);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_user_ticket, R.id.tv_user_card, R.id.btn_pay})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                break;
            case R.id.tv_user_ticket:
                break;
            case R.id.tv_user_card:
                break;
            case R.id.btn_pay:
                break;
        }
    }
}
