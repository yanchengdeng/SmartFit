package com.smartfit.activities;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.smartfit.R;
import com.smartfit.commons.Constants;

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
    TextView tvPayMoney;
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
                mSVProgressHUD.showInfoWithStatus("稍后推出");
            }
        });

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
