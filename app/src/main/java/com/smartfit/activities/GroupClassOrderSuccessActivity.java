package com.smartfit.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.smartfit.R;
import com.smartfit.views.SelectableRoundedImageView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.Bind;
import butterknife.ButterKnife;

/***
 * 团体课 预定成功
 */
public class GroupClassOrderSuccessActivity extends BaseActivity {

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
    @Bind(R.id.tv_class_name)
    TextView tvClassName;
    @Bind(R.id.tv_content)
    ImageView tvContent;
    @Bind(R.id.tv_save_to_phone)
    TextView tvSaveToPhone;
    @Bind(R.id.tv_share_friends)
    TextView tvShareFriends;
    @Bind(R.id.iv_coach_icon)
    SelectableRoundedImageView ivCoachIcon;
    @Bind(R.id.tv_coach_name)
    TextView tvCoachName;
    @Bind(R.id.tv_coach_info)
    TextView tvCoachInfo;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.tv_coach_score)
    TextView tvCoachScore;
    @Bind(R.id.iv_space_icon)
    SelectableRoundedImageView ivSpaceIcon;
    @Bind(R.id.tv_space_name)
    TextView tvSpaceName;
    @Bind(R.id.tv_space_info)
    TextView tvSpaceInfo;
    @Bind(R.id.iv_operate_person)
    SelectableRoundedImageView ivOperatePerson;
    @Bind(R.id.ll_have_ordered_members)
    LinearLayout llHaveOrderedMembers;
    @Bind(R.id.tv_class_time)
    TextView tvClassTime;
    @Bind(R.id.tv_class_price)
    TextView tvClassPrice;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.tv_contact_coach)
    TextView tvContactCoach;
    @Bind(R.id.tv_invite_friends)
    TextView tvInviteFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_class_order_success);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }


    private void initView() {
        tvTittle.setText(getString(R.string.class_detail));
        rollViewPager.setPlayDelay(3000);
        rollViewPager.setAnimationDurtion(500);
        rollViewPager.setAdapter(new TestNomalAdapter());
        rollViewPager.setHintView(new ColorPointHintView(this, getResources().getColor(R.color.common_header_bg), Color.WHITE));
        ratingBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 24));
    }


    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //保存到手机
        tvSaveToPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //发送给朋友
        tvShareFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShareAction(GroupClassOrderSuccessActivity.this).setPlatform(SHARE_MEDIA.WEIXIN).setCallback(umShareListener)
                        .withText("hello umeng")
                        .share();
            }
        });

        //联系教练
        tvContactCoach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(ContactCoachActivity.class);
            }
        });


    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
           mSVProgressHUD.showSuccessWithStatus("分享成功啦");
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            mSVProgressHUD.showInfoWithStatus("分享失败啦");
            tvShareFriends.setBackgroundColor(getResources().getColor(R.color.gray));
            tvShareFriends.setText("已发送");

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
          mSVProgressHUD.showInfoWithStatus("分享取消了");
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


}
