package com.smartfit.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.smartfit.R;
import com.smartfit.adpters.TicketGiftAdapter;
import com.smartfit.fragments.MyAddClassesFragment;
import com.smartfit.fragments.MyClassesOverFragment;
import com.smartfit.fragments.MyTickeFragment;
import com.smartfit.views.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的礼券
 *
 * @author yanchengdeng
 */
public class MyTicketGiftActivity extends BaseActivity {

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
        tvTittle.setText(getString(R.string.tick_gift));

    }


    private void initFragments() {
        Bundle useable = new Bundle();
        useable.putString("type", "1");
        Bundle userover = new Bundle();
        userover.putString("type", "2");
        MyTickeFragment  mf = new  MyTickeFragment();
        mf.setArguments(useable);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("可用", MyTickeFragment.class,useable)
                .add("已使用", MyTickeFragment.class,userover)
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
