package com.smartfit.activities;

import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.smartfit.R;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.views.WheelView;

import java.util.Arrays;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 选择工作日期
 *
 * @author yanchengdeng
 */
public class SelectWorkPointTimeActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.wv_hour_start)
    WheelView wvHourStart;
    @Bind(R.id.wv_min_start)
    WheelView wvMinStart;
    @Bind(R.id.wv_hour_end)
    WheelView wvHourEnd;
    @Bind(R.id.wv_min_end)
    WheelView wvMinEnd;


    private static final String[] HOURS = new String[]{"00", "01", "02", "03", "04", "05", "06", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"
            , "20", "21", "22", "23"};

    private static final String[] MINETS = new String[]{"00","15","30","45"};

    private String startHour = "00";
    private String startMinu = "00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_work_point_time);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.work_time));
        ivFunction.setVisibility(View.VISIBLE);
        ivFunction.setImageResource(R.mipmap.icon_right);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        LogUtil.w("dyc",hour+"xxxxx");
        if(hour<10){
            startHour = "0"+hour;
        }else{
            startHour = String.valueOf(hour);
        }

        int hourPos = 0;
        int minPos = 0;
        for(int i = 0 ;i<HOURS.length;i++){
            if(HOURS[i].equals(startHour)){
                hourPos = i;
            }
        }


        for(int i = 0 ;i<MINETS.length;i++){
            if(MINETS[i].equals(startMinu)){
                minPos = i;
            }
        }
        //起始时间设置
        wvHourStart.setOffset(1);
        wvHourStart.setItems(Arrays.asList(HOURS));
        wvHourStart.setSeletion(hourPos);
        wvMinStart.setOffset(1);
        wvMinStart.setItems(Arrays.asList(MINETS));
        wvMinStart.setSeletion(minPos);
        //结束时间设置
        wvHourEnd.setOffset(1);
        wvHourEnd.setItems(Arrays.asList(HOURS));
        wvHourEnd.setSeletion(hourPos);
        wvMinEnd.setOffset(1);
        wvMinEnd.setItems(Arrays.asList(MINETS));
        wvMinEnd.setSeletion(minPos);

    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSVProgressHUD.showSuccessWithStatus(getString(R.string.setting_success), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
    }
}
