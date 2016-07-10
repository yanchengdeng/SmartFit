package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.beans.PrivateCalssType;
import com.smartfit.beans.PrivateEducationClass;
import com.smartfit.beans.SelectCoachInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 设置 预约教练  时间
 */
public class SettingOrderCoachTimeActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_type_name)
    TextView tvTypeName;
    @Bind(R.id.tv_fee_tips)
    TextView tvFeeTips;
    @Bind(R.id.tv_fee)
    TextView tvFee;
    @Bind(R.id.rl_coach_info)
    RelativeLayout rlCoachInfo;
    @Bind(R.id.tv_coach_time)
    TextView tvCoachTime;
    @Bind(R.id.rl_coach_time)
    RelativeLayout rlCoachTime;
    @Bind(R.id.tv_order_time)
    TextView tvOrderTime;
    @Bind(R.id.rl_order_time)
    RelativeLayout rlOrderTime;
    @Bind(R.id.btn_order)
    Button btnOrder;

    private int REQUEST_CODE_ORDER_TIME = 0x11;

    //选择日期    YYYY-MM-DD
    private String selectDate;

    private String startTime, endTime,venueId;

    private SelectCoachInfo selectCoachInfo;
    private PrivateCalssType privateCalssType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_order_coach_time);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText("设置预定时间");
        startTime = "9:00";
        endTime = "10:00";
        selectCoachInfo = getIntent().getParcelableExtra("coach");
        privateCalssType = getIntent().getParcelableExtra(Constants.PASS_OBJECT);
        venueId = getIntent().getStringExtra("venueId");
        selectDate = getIntent().getStringExtra("dayTime");
        tvTypeName.setText(String.format("%s-%s",new Object[]{privateCalssType.getClassificationName(),selectCoachInfo.getNickName()}));
        tvFee.setText(String.format("%s/小时",selectCoachInfo.getMaxPrice()));
        tvCoachTime.setText(String.format("%s~%s",new Object[]{DateUtils.getTimeFromZeroToNow(selectCoachInfo.getStartTime()),DateUtils.getTimeFromZeroToNow(selectCoachInfo.getEndTime())}));

    }


    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //选取时间
        tvOrderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingOrderCoachTimeActivity.this, OrderReserveActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ORDER_TIME);
            }
        });


        //预定
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PrivateEducationClass  prvateEducation= new PrivateEducationClass();
                prvateEducation.setCoachId(selectCoachInfo.getCoachId());
                prvateEducation.setPrice(selectCoachInfo.getMaxPrice());
                prvateEducation.setUserPicUrl(selectCoachInfo.getUserPicUrl());
                prvateEducation.setNickName(selectCoachInfo.getNickName());
                prvateEducation.setSex(selectCoachInfo.getSex());
                prvateEducation.setStars(selectCoachInfo.getStars());
                ArrayList<PrivateEducationClass> listCoach = new ArrayList();
                listCoach.add(prvateEducation);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(Constants.PASS_OBJECT, listCoach);
                bundle.putSerializable(Constants.PASS_IDLE_CLASS_INFO, getIntent().getSerializableExtra(Constants.PASS_IDLE_CLASS_INFO));
                bundle.putString("start", selectDate + " " + startTime);
                bundle.putString("end", selectDate + " " + endTime);
                bundle.putBoolean("is_inclue_area_fee",true);//已包含场地费用
               openActivity(OrderPrivateEducationClassActivity.class, bundle);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ORDER_TIME && resultCode == OrderReserveActivity.SELECT_VALUE_OVER) {
            if (!TextUtils.isEmpty(data.getExtras().getString("time_before")) && !TextUtils.isEmpty(data.getExtras().getString("time_after"))) {
                tvOrderTime.setText(data.getExtras().getString("time_before") + " ~ " + data.getExtras().getString("time_after"));
                startTime =  data.getExtras().getString("time_before");
                endTime =  data.getExtras().getString("time_after");
            }
        }
    }
}
