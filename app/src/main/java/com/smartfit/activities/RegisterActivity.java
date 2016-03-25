package com.smartfit.activities;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.commons.Constants;
import com.smartfit.utils.MD5;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity {


    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.iv_phone)
    ImageView ivPhone;
    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.iv_code)
    ImageView ivCode;
    @Bind(R.id.et_code)
    EditText etCode;
    @Bind(R.id.btn_getcode)
    Button btnGetcode;
    @Bind(R.id.iv_user_name)
    ImageView ivUserName;
    @Bind(R.id.et_user_name)
    EditText etUserName;
    @Bind(R.id.iv_new_password)
    ImageView ivNewPassword;
    @Bind(R.id.et_new_password)
    EditText etNewPassword;
    @Bind(R.id.ck_remeber)
    CheckBox ckRemeber;
    @Bind(R.id.tv_deal)
    TextView tvDeal;
    @Bind(R.id.btn_regist)
    Button btnRegist;
    private CountDownTimer countDownTimer;
    private static final Object TAG = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.register));
        tvDeal.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线

        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btnGetcode.setText("倒计时" + String.valueOf((int) (millisUntilFinished / 1000)) + "秒");
            }

            @Override
            public void onFinish() {
                btnGetcode.setText(getString(R.string.get_code));
                btnGetcode.setClickable(true);
            }
        };

    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etPhone.setText("13067380836");

        btnGetcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhone.getEditableText().toString().isEmpty()) {
                    mSVProgressHUD.showInfoWithStatus(getString(R.string.phone_cannot_empty));
                } else {
                    if (etPhone.getEditableText().toString().length() == 11) {
                        sendCode(etPhone.getEditableText().toString());
                    } else {
                        mSVProgressHUD.showInfoWithStatus(getString(R.string.phone_format_error));
                    }
                }
            }
        });


        //注册
        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPhone.getEditableText().toString().isEmpty()) {
                    mSVProgressHUD.showInfoWithStatus(getString(R.string.phone_cannot_empty));
                } else {
                    if (etPhone.getEditableText().toString().length() == 11) {
                        if (TextUtils.isEmpty(etCode.getEditableText().toString())) {
                            mSVProgressHUD.showInfoWithStatus(getString(R.string.code_cannot_empty));
                        } else {
                            if (TextUtils.isEmpty(etUserName.getEditableText().toString())) {
                                mSVProgressHUD.showInfoWithStatus(getString(R.string.user_name_cannot_empty));
                            } else {
                                if (TextUtils.isEmpty(etNewPassword.getEditableText().toString())) {
                                    mSVProgressHUD.showInfoWithStatus(getString(R.string.passowr_cannot_empt));
                                } else {
                                    if(ckRemeber.isChecked()){
                                        doRegister(etPhone.getEditableText().toString(), etCode.getEditableText().toString(), etUserName.getEditableText().toString(), etNewPassword.getEditableText().toString());
                                    }else{
                                        mSVProgressHUD.showInfoWithStatus(getString(R.string.have_not_agree_deal));
                                    }
                                }
                            }
                        }
                    } else {
                        mSVProgressHUD.showInfoWithStatus(getString(R.string.phone_format_error));
                    }
                }
            }
        });


        tvDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(DealInfoActivity.class);
            }
        });

    }

    private void doRegister(String phone, String code, String name, String password) {
        mSVProgressHUD.showWithStatus(getString(R.string.register_ing), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        data.put("mobileNo", phone);
        data.put("checkCode", code);
        data.put("account", name);
        data.put("password", MD5.getMessageDigest(password.getBytes()));
        PostRequest request = new PostRequest(Constants.REGISTER_METHOD, NetUtil.getRequestBody(data, mContext), new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.showSuccessWithStatus(getString(R.string.register_success), SVProgressHUD.SVProgressHUDMaskType.Clear);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage());
            }
        });
        request.setTag(TAG);
        mQueue.add(request);
    }

    /**
     * 发送短信验证码
     *
     * @param phone
     */
    private void sendCode(final String phone) {
        Map<String, String> data = new HashMap<>();
        data.put("mobile", phone);
        PostRequest request = new PostRequest(Constants.SMS_SMSSEND, NetUtil.getRequestBody(data, mContext), new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                btnGetcode.setClickable(false);
                mSVProgressHUD.showInfoWithStatus(getString(R.string.send_success));
                countDownTimer.start();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage());
            }
        });
        request.setTag(TAG);
        mQueue.add(request);
    }


}
