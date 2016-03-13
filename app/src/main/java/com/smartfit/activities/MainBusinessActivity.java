package com.smartfit.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.smartfit.R;
import com.smartfit.commons.Constants;
import com.smartfit.fragments.AerobicnAppratusFragment;
import com.smartfit.fragments.GroupExperienceFragment;
import com.smartfit.fragments.PrivateEducationFragment;
import com.smartfit.fragments.SmallClassFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主业务页  包括 团体操  小班  等
 */
public class MainBusinessActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.viewpager)
    ViewPager viewpager;


    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_business);
        ButterKnife.bind(this);

        initFragments();

        addLisener();
    }

    private void initFragments() {

        currentPosition = getIntent().getIntExtra(Constants.FRAGMENT_POSITION, 0);
        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add(R.string.group_experien, GroupExperienceFragment.class)
                .add(R.string.small_class, SmallClassFragment.class)
                .add(R.string.private_education, PrivateEducationFragment.class)
                .add(R.string.aerobic_apparatus, AerobicnAppratusFragment.class)
                .create());


        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
        viewPager.setCurrentItem(currentPosition);


    }


    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SearchClassActivity.class);
            }
        });

    }

}
