package com.smartfit.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartfit.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * FAQ 内容
 *
 * @author yanchengdeng
 *         create at 2016/5/4 16:26
 */
public class FaqContentActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.tv_tips)
    TextView tvTips;
    private int key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq_content);
        ButterKnife.bind(this);
        tvTittle.setText("FAQ");

        key = getIntent().getIntExtra("key", 0);
        String[] contents = getResources().getStringArray(R.array.fqa_content);

        String[] tittle = getResources().getStringArray(R.array.fqa_tittle);
        if (!TextUtils.isEmpty(tittle[key])) {
            tvTips.setText(tittle[key]);
        }

        if (!TextUtils.isEmpty(contents[key])) {
            tvContent.setText(contents[key]);
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
