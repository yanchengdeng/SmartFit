package com.smartfit.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.smartfit.MessageEvent.CancleClass;
import com.smartfit.R;
import com.smartfit.fragments.MyAbsentClassesFragment;
import com.smartfit.fragments.MyAddClassesFragment;
import com.smartfit.fragments.MyAddCreditRecordFragment;
import com.smartfit.fragments.MyAllCreditRecordFragment;
import com.smartfit.fragments.MyClassesOverFragment;
import com.smartfit.fragments.MyQueueClassesFragment;
import com.smartfit.fragments.MyReduceCreditRecordFragment;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的信用
 *
 * @author yanchengdeng
 *         create at 2016/8/26 17:15
 */
public class MyCreditActivity extends BaseActivity {

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
        setContentView(R.layout.activity_my_credit);
        ButterKnife.bind(this);
        initView();
        initFragments();
    }

    private void initView() {
        tvTittle.setText("我的信用");

    }


    private void initFragments() {

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("全部", MyAllCreditRecordFragment.class)
                .add("加分记录", MyAddCreditRecordFragment.class)
                .add("扣分记录", MyReduceCreditRecordFragment.class)
                .create());

        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(3);
        viewpagertab.setViewPager(viewpager);
        viewpager.setCurrentItem(0);


    }
}
