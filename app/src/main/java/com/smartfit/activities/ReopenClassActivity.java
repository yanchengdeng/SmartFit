package com.smartfit.activities;

import android.app.Activity;
import android.os.Bundle;

import com.smartfit.R;
import com.smartfit.commons.Constants;

/**
 * 教练再次开课界面
 *
 * @author yanchengdeng
 *         create at 2016/5/4 18:19
 */
public class ReopenClassActivity extends BaseActivity {

    private String courserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reopen_class);

        courserId = getIntent().getStringExtra(Constants.PASS_STRING);
    }
}
