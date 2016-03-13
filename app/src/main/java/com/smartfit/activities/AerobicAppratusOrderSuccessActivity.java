package com.smartfit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfit.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 有氧器械  预约成功
 */
public class AerobicAppratusOrderSuccessActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.iv_img)
    ImageView ivImg;
    @Bind(R.id.tv_class_name)
    TextView tvClassName;
    @Bind(R.id.tv_content)
    ImageView tvContent;
    @Bind(R.id.tv_save_to_phone)
    TextView tvSaveToPhone;
    @Bind(R.id.tv_share_friends)
    TextView tvShareFriends;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_room)
    TextView tvRoom;
    @Bind(R.id.tv_distance)
    TextView tvDistance;
    @Bind(R.id.tv_price)
    TextView tvPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aerobic_appratus_order_success);
        ButterKnife.bind(this);
        initView();

        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.aerobic_apparatus));

    }


    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


}
