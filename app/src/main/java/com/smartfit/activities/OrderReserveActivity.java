package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.commons.Constants;
import com.smartfit.views.WheelView;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    private static final String[] HOURS = new String[]{"00", "01", "02", "03", "04", "05", "06", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"
            , "20", "21", "22", "23"};

    private static final String[] MINETS = new String[]{"00","15","30","45"};

    private String selectHour = "00";
    private String selectMinu = "00";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_reserve);
        ButterKnife.bind(this);
        tvTittle.setText(getString(R.string.order_experise_time));
        tvFunction.setVisibility(View.VISIBLE);
        tvFunction.setText(getString(R.string.sure));

        selectHour = getIntent().getStringExtra("hour");
        selectMinu= getIntent().getStringExtra("min");
        int hourPos = 0;
        int minPos = 0;
        for(int i = 0 ;i<HOURS.length;i++){
            if(HOURS[i].equals(selectHour)){
                hourPos = i;
            }
        }


        for(int i = 0 ;i<MINETS.length;i++){
            if(MINETS[i].equals(selectMinu)){
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
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STING, selectHour + ":" + selectMinu);
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


}