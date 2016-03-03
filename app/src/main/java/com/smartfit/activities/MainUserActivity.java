package com.smartfit.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.smartfit.R;
import com.smartfit.views.WheelView;

import java.util.Arrays;

import butterknife.ButterKnife;

/**
 * 用户个人主页
 */
public class MainUserActivity extends BaseActivity {
    private static final String TAG = MainUserActivity.class.getSimpleName();
    private static final String[] PLANETS = new String[]{"Mercury", "Venus", "Earth", "Mars", "Jupiter", "Uranus", "Neptune", "Pluto"};

    WheelView wva;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

         wva = (WheelView) findViewById(R.id.main_wv);

        wva.setOffset(1);
        wva.setItems(Arrays.asList(PLANETS));
        wva.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });

    }
}
