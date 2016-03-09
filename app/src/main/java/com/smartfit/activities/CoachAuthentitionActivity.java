package com.smartfit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.views.SelectableRoundedImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

/***
 * 教练认证信息
 */
public class CoachAuthentitionActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.iv_icon)
    SelectableRoundedImageView ivIcon;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_base_info)
    TextView tvBaseInfo;
    @Bind(R.id.tv_auth_info)
    TextView tvAuthInfo;
    @Bind(R.id.tv_auth_info_more)
    TextView tvAuthInfoMore;
    @Bind(R.id.tv_classes)
    TextView tvClasses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_authentition);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTittle.setText("认证资料");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
