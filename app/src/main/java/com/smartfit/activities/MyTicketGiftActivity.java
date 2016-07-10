package com.smartfit.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.smartfit.R;
import com.smartfit.fragments.MyTickeFragment;

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
    public TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.btn_bind_card)
    Button btnBindCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_gift);
        ButterKnife.bind(this);

        initView();
        initFragments();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.tick_gift));
        tvFunction.setText(getString(R.string.tick_gift_share));
    }


    private void initFragments() {
        Bundle useable = new Bundle();
        useable.putString("type", "1");
        Bundle userover = new Bundle();
        userover.putString("type", "2");
        MyTickeFragment mf = new MyTickeFragment();
        mf.setArguments(useable);
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("未使用", MyTickeFragment.class, useable)
                .add("已使用", MyTickeFragment.class, userover)
                .create());

        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(2);
        viewpagertab.setViewPager(viewpager);
        viewpager.setCurrentItem(0);


    }


    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnBindCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(BindCardActivity.class);
            }
        });

        tvFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(MyTicketGiftShareActivity.class);
            }
        });
    }

}
