package com.smartfit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.views.SelectableRoundedImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 用户资料详情
 */
public class CustomDetailInfoActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.iv_headerr)
    SelectableRoundedImageView ivHeaderr;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_sex)
    TextView tvSex;
    @Bind(R.id.tv_motto)
    TextView tvMotto;
    @Bind(R.id.tv_bind_phone)
    TextView tvBindPhone;
    @Bind(R.id.tv_update_pass)
    TextView tvUpdatePass;
    @Bind(R.id.tv_coach_auth_status)
    TextView tvCoachAuthStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_detail_info);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.account_info));


    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        tvUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ForgetActivity.class);
            }
        });


        tvCoachAuthStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(CoachAuthBaseActivity.class);
            }
        });

    }
}
