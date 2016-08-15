package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.smartfit.R;
import com.smartfit.commons.Constants;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsActivity extends BaseActivity {


    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_app_name)
    TextView tvAppName;
    @Bind(R.id.tv_version)
    TextView tvVersion;
    @Bind(R.id.tv_new_function)
    TextView tvNewFunction;
    @Bind(R.id.tv_check_udpate)
    TextView tvCheckUdpate;
    @Bind(R.id.tv_user_deal)
    TextView tvUserDeal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ButterKnife.bind(this);
        tvTittle.setText(getString(R.string.about_us));
    }

    @OnClick({R.id.iv_back, R.id.tv_new_function, R.id.tv_check_udpate, R.id.tv_user_deal})
    public void onClick(View view) {
        Intent intent = new Intent(AboutUsActivity.this, AdActivity.class);
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_new_function:
                intent.putExtra(Constants.PASS_STRING, "https://www.baidu.com/");
                intent.putExtra("name", "新功能介绍");
                startActivity(intent);
                break;
            case R.id.tv_check_udpate:
                mSVProgressHUD.showSuccessWithStatus("已是最新版本", SVProgressHUD.SVProgressHUDMaskType.Clear);
                break;
            case R.id.tv_user_deal:
                intent.putExtra(Constants.PASS_STRING, "https://www.baidu.com/");
                intent.putExtra("name", "使用协议");
                startActivity(intent);
                break;
        }
    }
}
