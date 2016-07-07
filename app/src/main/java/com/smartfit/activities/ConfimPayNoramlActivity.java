package com.smartfit.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.beans.NewMouthServerEvent;
import com.smartfit.commons.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 确认支付
 * 除 包月外 ，其他支付 都当做一个商品来处理
 */
public class ConfimPayNoramlActivity extends BaseActivity {


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
    @Bind(R.id.tv_pay_money)
    TextView tvPayMoney;
    @Bind(R.id.btn_pay)
    Button btnPay;
    private NewMouthServerEvent newMouthServerEvent;

    private int pageIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confim_pay_noraml);
        ButterKnife.bind(this);
        initView();
        addLisener();


    }

    private void initView() {
        tvTittle.setText("支付确认");
        newMouthServerEvent = getIntent().getParcelableExtra(Constants.PASS_OBJECT);
        if (!TextUtils.isEmpty(newMouthServerEvent.getEventTitle())) {
            tvName.setText(newMouthServerEvent.getEventTitle());
        }

        if (!TextUtils.isEmpty(newMouthServerEvent.getEventPrice())) {
            tvMoney.setText("￥" + newMouthServerEvent.getEventPrice());
            tvPayMoney.setText("￥" + newMouthServerEvent.getEventPrice());
        }


    }


    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.PAGE_INDEX, 10);// 10 包季度 包半年
                bundle.putString(Constants.COURSE_ID, newMouthServerEvent.getId());
                bundle.putString(Constants.COURSE_MONEY, newMouthServerEvent.getEventPrice());
                openActivity(PayActivity.class, bundle);
            }
        });
    }

}
