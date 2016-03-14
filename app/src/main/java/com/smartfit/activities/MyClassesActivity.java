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
import com.smartfit.fragments.MyAddClassesFragment;
import com.smartfit.fragments.MyClassesOverFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的课程
 *
 * @author dengyancheng
 * @date 2016-03-11
 */
public class MyClassesActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_classes);
        ButterKnife.bind(this);
        initView();
        initFragments();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.my_classes));

    }


    private void initFragments() {

         FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.my_add_classes, MyAddClassesFragment.class)
                .add(R.string.already_over, MyClassesOverFragment.class)
                .create());

        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(2);
        viewpagertab.setViewPager(viewpager);
        viewpager.setCurrentItem(0);


    }


    private void addLisener(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}