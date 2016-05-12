package com.smartfit.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.smartfit.MessageEvent.UpdateCustomClassInfo;
import com.smartfit.R;
import com.smartfit.beans.PrivateEducationClass;
import com.smartfit.fragments.CustomClassThreeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author yanchengdeng
 *         create at 2016/4/25 11:30
 *         用户自定义课程第三步
 */
public class UserCustomClassThreeActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    public ImageView ivFunction;
    @Bind(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @Bind(R.id.viewpager)
    ViewPager viewpager;

    private String startTime, endTime, courseClassId, venueId,roomId,venuePrice;

    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_class_three);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        initView();
        initViewPage();
        addLisener();
    }

    @Subscribe
    public void onEvent(UpdateCustomClassInfo event) {/* Do something */
        finish();
    }

    private void initView() {
        tvTittle.setText("自订课程(3/4)");
        ivFunction.setVisibility(View.VISIBLE);
        ivFunction.setImageResource(R.mipmap.icon_next_w);

        /**
         *  bundle.putString("startTime", String.valueOf(DateUtils.getTheDateTimeMillions(startTime)));
         bundle.putString("endTime", String.valueOf(DateUtils.getTheDateTimeMillions(endTime)));
         bundle.putString("courseClassId",classId);
         bundle.putString("roomId", roomId);
         bundle.putString("venuePrice",venuePrice);
         */
        startTime = getIntent().getStringExtra("startTime");
        endTime = getIntent().getStringExtra("endTime");
        courseClassId = getIntent().getStringExtra("courseClassId");
        venueId = getIntent().getStringExtra("venueId");
        roomId =getIntent().getStringExtra("roomId");
        venuePrice = getIntent().getStringExtra("venuePrice");
    }

    ViewPager viewPager;
    FragmentPagerItemAdapter adapter;

    private void initViewPage() {
        Bundle bundle = getIntent().getExtras();
        CustomClassThreeFragment custom = new CustomClassThreeFragment();
        custom.setArguments(bundle);
        adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("评分", CustomClassThreeFragment.class, bundle)
                .add("价格", CustomClassThreeFragment.class, bundle)
                .add("性别", CustomClassThreeFragment.class, bundle)
                .create());


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);
        final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);
        viewPager.setCurrentItem(1);

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
                CustomClassThreeFragment fragment = (CustomClassThreeFragment) adapter.getItem(viewpager.getCurrentItem());
                if (fragment.datas.size() > 0) {
                    List<PrivateEducationClass> selectPricates = countSelectNum(fragment.datas);
                    if (selectPricates.size() == 0) {
                        mSVProgressHUD.showInfoWithStatus("请选择教练", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString("startTime", startTime);
                        bundle.putString("endTime", endTime);
                        bundle.putString("courseClassId", courseClassId);
                        bundle.putString("venueId", venueId);
                        bundle.putString("venuePrice", venuePrice);
                        bundle.putString("roomId", roomId);
                        float price = 0;
                        StringBuilder stringBuilder = new StringBuilder();
                        for (PrivateEducationClass item : selectPricates) {
                            stringBuilder.append(item.getCoachId()).append("|");
                            if (!TextUtils.isEmpty(item.getPrice())) {
                                if (Float.parseFloat(item.getPrice()) > price) {
                                    price = Float.parseFloat(item.getPrice());
                                }
                            }
                        }
                        bundle.putString("coachPrice", String.valueOf(price));
                        bundle.putString("coachId", stringBuilder.toString());
                        openActivity(UserCustomClassFourActivity.class, bundle);
                    }
                } else {
                    mSVProgressHUD.showInfoWithStatus("暂无教练,请耐心等待", SVProgressHUD.SVProgressHUDMaskType.Clear);
                }
            }
        });

    }

    /**
     * 计算已经选择教练人数
     *
     * @param datas
     */
    private ArrayList<PrivateEducationClass> countSelectNum(List<PrivateEducationClass> datas) {
        ArrayList<PrivateEducationClass> selectPricates = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isCheck() == true) {
                selectPricates.add(datas.get(i));
            }
        }
        return selectPricates;
    }


}
