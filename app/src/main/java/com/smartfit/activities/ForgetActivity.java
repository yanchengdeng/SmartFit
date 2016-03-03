package com.smartfit.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfit.R;

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
                btnGetcode.setClickable(false);
                mSVProgressHUD.showInfoWithStatus(getString(R.string.send_success));
                countDownTimer.start();

            }
        });

    }
}
