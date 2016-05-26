package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.views.WheelView;

import java.util.Arrays;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预约锻炼时间
 */
public class OrderReserveActivity extends BaseActivity {


    public static int SELECT_VALUE_OVER = 0x11;
    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.wv_hour)
    WheelView wvHour;
    @Bind(R.id.wv_min)
    WheelView wvMin;
    @Bind(R.id.tv_add_thirt_min)
    TextView tvAddThirtMin;
    @Bind(R.id.tv_add_sixty_min)
    TextView tvAddSixtyMin;
    @Bind(R.id.tv_one_ninty_min)
    TextView tvOneNintyMin;
    @Bind(R.id.tv_add_three_hour)
    TextView tvAddThreeHour;

    private static final String[] HOURS = new String[]{"00", "01", "02", "03", "04", "05", "06", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"
            , "20", "21", "22", "23"};

    private static final String[] MINETS = new String[]{"00", "15", "30", "45"};


    private String selectHour = "00";
    private String selectMinu = "30";

    private String addMinit = "60";//默认增加60分钟

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_reserve);
        ButterKnife.bind(this);
        tvTittle.setText(getString(R.string.order_experise_time));
        tvFunction.setVisibility(View.VISIBLE);
        tvFunction.setText(getString(R.string.sure));

        Calendar calendar
                = Calendar.getInstance();
        selectHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));


        int hourPos = 0;
        int minPos = 0;
        for (int i = 0; i < HOURS.length; i++) {
            if (HOURS[i].equals(selectHour)) {
                hourPos = i;
            }
        }


        for (int i = 0; i < MINETS.length; i++) {
            if (MINETS[i].equals(selectMinu)) {
                minPos = i;
            }
        }
        wvHour.setOffset(1);
        wvHour.setItems(Arrays.asList(HOURS));
        wvHour.setSeletion(hourPos);
        wvMin.setOffset(1);
        wvMin.setItems(Arrays.asList(MINETS));
        wvMin.setSeletion(minPos);

        addLisener();

    }

    private void addLisener() {
        wvHour.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
//                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                selectHour = item;
            }
        });

        wvMin.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
//                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                selectMinu = item;
            }
        });


        //确定
        tvFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(selectHour));
                calendar.set(Calendar.MINUTE, Integer.parseInt(selectMinu));
                calendar.add(Calendar.MINUTE, Integer.parseInt(addMinit));

                String endHour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
                if (Integer.parseInt(endHour) < 10) {
                    endHour = "0" + endHour;
                }

                String endHMin = String.valueOf(calendar.get(Calendar.MINUTE));
                if (Integer.parseInt(endHMin) < 10) {
                    endHMin = "0" + endHMin;
                }

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("time_before", selectHour + ":" + selectMinu);
                bundle.putString("time_after", endHour + ":" + endHMin);
                intent.putExtras(bundle);
                setResult(SELECT_VALUE_OVER, intent);
                finish();
            }
        });

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @OnClick({R.id.tv_add_thirt_min, R.id.tv_add_sixty_min, R.id.tv_one_ninty_min, R.id.tv_add_three_hour})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_thirt_min:
                addMinit = "60";
                tvAddThirtMin.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_gray));
                tvAddSixtyMin.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_white));
                tvOneNintyMin.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_white));
                tvAddThreeHour.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_white));
                break;
            case R.id.tv_add_sixty_min:
                addMinit = "90";
                tvAddThirtMin.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_white));
                tvAddSixtyMin.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_gray));
                tvOneNintyMin.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_white));
                tvAddThreeHour.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_white));
                break;
            case R.id.tv_one_ninty_min:
                addMinit = "120";
                tvAddThirtMin.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_white));
                tvAddSixtyMin.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_white));
                tvOneNintyMin.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_gray));
                tvAddThreeHour.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_white));
                break;
            case R.id.tv_add_three_hour:
                addMinit = "180";
                tvAddThirtMin.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_white));
                tvAddSixtyMin.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_white));
                tvOneNintyMin.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_white));
                tvAddThreeHour.setBackground(getResources().getDrawable(R.drawable.bg_dialog_selector_gray));
                break;
        }
    }
}
