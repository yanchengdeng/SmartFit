package com.smartfit.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.smartfit.R;
import com.smartfit.adpters.DiscussItemAdapter;
import com.smartfit.views.MyListView;
import com.smartfit.views.ShareBottomDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.ShareContent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/***
 * 团体操课程详情
 *
 * @author yanchengdeng
 */
public class GroupClassDetailActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.roll_view_pager)
    RollPagerView rollViewPager;
    @Bind(R.id.tv_content)
    TextView tvContent;
    @Bind(R.id.iv_coach_icon)
    ImageView ivCoachIcon;
    @Bind(R.id.tv_coach_name)
    TextView tvCoachName;
    @Bind(R.id.tv_coach_info)
    TextView tvCoachInfo;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.tv_coach_score)
    TextView tvCoachScore;
    @Bind(R.id.iv_space_icon)
    ImageView ivSpaceIcon;
    @Bind(R.id.tv_space_name)
    TextView tvSpaceName;
    @Bind(R.id.tv_space_info)
    TextView tvSpaceInfo;
    @Bind(R.id.iv_operate_person)
    ImageView ivOperatePerson;
    @Bind(R.id.ll_have_ordered_members)
    LinearLayout llHaveOrderedMembers;
    @Bind(R.id.tv_class_time)
    TextView tvClassTime;
    @Bind(R.id.tv_class_price)
    TextView tvClassPrice;
    @Bind(R.id.lisview_discuss)
    MyListView lisviewDiscuss;
    @Bind(R.id.tv_more)
    TextView tvMore;
    @Bind(R.id.btn_order)
    Button btnOrder;
    @Bind(R.id.scrollView)
    ScrollView scrollView;


    private DiscussItemAdapter adapter;
    private List<String> discuss = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_class_detail);
        ButterKnife.bind(this);

        initView();
        addLisener();

    }

    private void initView() {
        tvTittle.setText(getString(R.string.class_detail));
        ivFunction.setImageResource(R.mipmap.ic_more_share);
        ivFunction.setVisibility(View.VISIBLE);
        rollViewPager.setPlayDelay(3000);
        rollViewPager.setAnimationDurtion(500);
        rollViewPager.setAdapter(new TestNomalAdapter());
        rollViewPager.setHintView(new ColorPointHintView(this, getResources().getColor(R.color.common_header_bg), Color.WHITE));
        ratingBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 24));

        adapter = new DiscussItemAdapter(discuss, this);
        lisviewDiscuss.setAdapter(adapter);
        loadData();

    }

    private void loadData() {
        for (int i = 0; i < 4; i++) {
            discuss.add("fsd" + i);
        }
        adapter.setData(discuss);
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
                ShareBottomDialog dialog = new ShareBottomDialog(GroupClassDetailActivity.this, scrollView);
                dialog.showAnim(new BounceTopEnter())//
                        .show();
            }
        });


        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(PayActivity.class);
            }
        });

    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
        }
    };

    private class TestNomalAdapter extends StaticPagerAdapter {
        private int[] imgs = {
                R.mipmap.bg_pic,
                R.mipmap.bg_pic,
                R.mipmap.bg_pic,
                R.mipmap.bg_pic,
        };


        private String[] infos = {
                "燃烧吧，脂肪君(课程名称)",
                "燃烧吧，脂肪君(课程名称)",
                "燃烧吧，脂肪君(课程名称)",
                "燃烧吧，脂肪君(课程名称)",
        };


        @Override
        public View getView(ViewGroup container, int position) {
            View relativeLayout = LayoutInflater.from(getBaseContext()).inflate(R.layout.ad_common_view, null);
            ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.iv_cover_bg);
            TextView textView = (TextView) relativeLayout.findViewById(R.id.tv_tittle);
            imageView.setImageResource(imgs[position]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            textView.setText(infos[position]);
            return relativeLayout;
        }


        @Override
        public int getCount() {
            return imgs.length;
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


}
