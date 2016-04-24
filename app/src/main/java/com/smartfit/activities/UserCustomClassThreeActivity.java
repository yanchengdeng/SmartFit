package com.smartfit.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.smartfit.R;
import com.smartfit.fragments.CustomClassThreeFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class UserCustomClassThreeActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
 public    ImageView ivFunction;
    @Bind(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_class_three);
        ButterKnife.bind(this);
        initView();
        initViewPage();
        addLisener();
    }

    private void initView() {
        tvTittle.setText("自订课程(3/4)");
        ivFunction.setVisibility(View.VISIBLE);
        ivFunction.setImageResource(R.mipmap.icon_next_w);
    }

    private void initViewPage(){
        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("评分", CustomClassThreeFragment.class)
                .add("价格", CustomClassThreeFragment.class)
                .add("性别", CustomClassThreeFragment.class)
                .create());


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
        viewPager.setCurrentItem(0);
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

            }
        });

    }


}
