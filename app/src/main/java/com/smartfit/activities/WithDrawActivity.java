package com.smartfit.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.beans.UserInfoDetail;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.MD5;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 提现
 */
public class WithDrawActivity extends BaseActivity {


    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_have_money)
    TextView tvHaveMoney;
    @Bind(R.id.tv_withdraw)
    EditText tvWithdraw;
    @Bind(R.id.tv_name)
    EditText tvName;
    @Bind(R.id.tv_bank_type)
    EditText tvBankType;
    @Bind(R.id.tv_bank_account)
    EditText tvBankAccount;
    @Bind(R.id.tv_login_password)
    EditText tvLoginPassword;
    @Bind(R.id.btn_withdraw)
    Button btnWithdraw;


    private String leftMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw);
        ButterKnife.bind(this);
        initView();
        getLeftMoney();
        addLisener();
    }

    private void getLeftMoney() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        PostRequest request = new PostRequest(Constants.USER_USERINFO, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                UserInfoDetail userInfoDetail = JsonUtils.objectFromJson(response, UserInfoDetail.class);
                if (userInfoDetail != null) {
                    if (!TextUtils.isEmpty(userInfoDetail.getBalance())) {
                        tvHaveMoney.setText(userInfoDetail.getBalance() + "元");
                        leftMoney = userInfoDetail.getBalance();
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
        request.headers = NetUtil.getRequestBody(WithDrawActivity.this);
        mQueue.add(request);
    }


    private void initView() {
        tvTittle.setText(getString(R.string.withdraw));

    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                withDraw(tvWithdraw.getText().toString(), tvName.getText().toString(), tvBankType.getText().toString(), tvBankAccount.getText().toString(), tvLoginPassword.getText().toString());
            }
        });


    }

    /**
     * @param money
     * @param name
     * @param bankType
     * @param bankAccount
     * @param password    银行账号
     *                    开户银行
     *                    开户名
     *                    账号密码
     *                    <p/>
     *                    <p/>
     *                    cash
     *                    account
     *                    accountBank
     *                    accountMen
     *                    password
     */
    private void withDraw(String money, String name, String bankType, String bankAccount, String password) {
        if (TextUtils.isEmpty(money)) {
            mSVProgressHUD.showInfoWithStatus("请输入金额", SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }

        if (!TextUtils.isDigitsOnly(money)){
            mSVProgressHUD.showInfoWithStatus("金额只能为数字", SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }

        if (TextUtils.isEmpty(name)) {
            mSVProgressHUD.showInfoWithStatus("请输入银行账号", SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }

        if (TextUtils.isEmpty(bankType)) {
            mSVProgressHUD.showInfoWithStatus("请输入开户行", SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }
        if (TextUtils.isEmpty(bankAccount)) {
            mSVProgressHUD.showInfoWithStatus("请输入开户名", SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            mSVProgressHUD.showInfoWithStatus("请输入登陆密码", SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }
        mSVProgressHUD.showWithStatus("提现中...", SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> maps = new HashMap<>();
        maps.put("cash", money);
        maps.put("account", name);
        maps.put("accountBank", bankType);
        maps.put("accountMen", bankAccount);
        maps.put("password", MD5.getMessageDigest(password.getBytes()));

        PostRequest request = new PostRequest(Constants.USER_APPLYCASH, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.showSuccessWithStatus("已提现成功", SVProgressHUD.SVProgressHUDMaskType.Clear);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(WithDrawActivity.this);
        mQueue.add(request);

    }
}
