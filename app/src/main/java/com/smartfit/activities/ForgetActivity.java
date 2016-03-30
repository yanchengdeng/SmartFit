package com.smartfit.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.commons.Constants;
import com.smartfit.utils.MD5;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 忘记密码
 */
public class ForgetActivity extends BaseActivity {


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
    @Bind(R.id.iv_old_pass)
    ImageView ivOldPass;
    @Bind(R.id.et_old_password)
    EditText etOldPassword;
    @Bind(R.id.iv_new_password)
    ImageView ivNewPassword;
    @Bind(R.id.et_new_password)
    EditText etNewPassword;
    @Bind(R.id.btn_reset)
    Button btnReset;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }


    private void initView() {

        tvTittle.setText(getString(R.string.forget_pass));

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
        //重置密码
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etPhone.getEditableText().toString())) {
                    mSVProgressHUD.showInfoWithStatus(getString(R.string.phone_cannot_empty));
                } else {
                    if (etPhone.getEditableText().toString().length() == 11) {
                        if (TextUtils.isEmpty(etCode.getEditableText().toString())) {
                            mSVProgressHUD.showInfoWithStatus(getString(R.string.code_cannot_empty));
                        } else {
                            if (TextUtils.isEmpty(etOldPassword.getEditableText().toString())) {
                                mSVProgressHUD.showInfoWithStatus(getString(R.string.old_password_cannot_empty));
                            } else {
                                if (!TextUtils.isEmpty(etNewPassword.getEditableText().toString()))
                                {
                                   resetPassword(etPhone.getEditableText().toString(), etCode.getEditableText().toString(), etOldPassword.getEditableText().toString(), etNewPassword.getEditableText().toString());
                                }else{
                                    mSVProgressHUD.showInfoWithStatus(getString(R.string.new_password_cannot_empty));
                                }
                            }
                        }
                    } else {
                        mSVProgressHUD.showInfoWithStatus(getString(R.string.phone_format_error));
                    }
                }
            }

        });
    }

    private void resetPassword(String phone, String code, String oldPass, String newPass) {
        mSVProgressHUD.showWithStatus(getString(R.string.reset_ing, SVProgressHUD.SVProgressHUDMaskType.Clear));
        Map<String, String> data = new HashMap<>();
        data.put("mobileNo", phone);
        data.put("checkCode", code);
        data.put("password", MD5.getMessageDigest(oldPass.getBytes()));
        data.put("newPassword", MD5.getMessageDigest(newPass.getBytes()));
        PostRequest request = new PostRequest(Constants.RESET_PASSOWRD, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.showInfoWithStatus("重置成功");
                showResetSuccessDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
                showResetSuccessDialog();
            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(ForgetActivity.this);
        mQueue.add(request);

    }

    private void showResetSuccessDialog() {
        final NormalDialog dialog = new NormalDialog(ForgetActivity.this);
        dialog.content("重置密码成功！！")//
                .btnNum(1)
                .btnText("确定")//
//                .showAnim(mBasIn)//
//                .dismissAnim(mBasOut)//
                .show();
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
                SharedPreferencesUtils.getInstance().remove(Constants.PASSWORD);
                openActivity(LoginActivity.class);
                finish();
            }
        });
    }


    /**
     * 发送短信验证码
     *
     * @param phone
     */
    private void sendCode(final String phone) {
        Map<String, String> data = new HashMap<>();
        data.put("mobile", phone);
        PostRequest request = new PostRequest(Constants.SMS_SMSSEND, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                btnGetcode.setClickable(false);
                mSVProgressHUD.showInfoWithStatus(getString(R.string.send_success), SVProgressHUD.SVProgressHUDMaskType.Clear);
                countDownTimer.start();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(ForgetActivity.this);
        mQueue.add(request);


    }
}
