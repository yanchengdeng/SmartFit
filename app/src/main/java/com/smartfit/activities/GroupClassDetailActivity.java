package com.smartfit.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.google.gson.JsonObject;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.adpters.DiscussItemAdapter;
import com.smartfit.beans.ClassCommend;
import com.smartfit.beans.ClassInfoDetail;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DeviceUtil;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.MyListView;
import com.smartfit.views.ShareBottomDialog;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Bind(R.id.tv_operate_address)
    TextView tvOperateAddress;
    @Bind(R.id.tv_class_tittle)
    TextView tvClassTittle;


    private DiscussItemAdapter adapter;
    private List<ClassCommend> discuss = new ArrayList<>();

    ClassInfoDetail classInfoDetail;

    private String id;

    private String type = "0";

    private int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_class_detail);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra(Constants.PASS_STRING);
        type = getIntent().getStringExtra(Constants.COURSE_TYPE);
        tvTittle.setText(getString(R.string.class_detail));
        ivFunction.setImageResource(R.mipmap.ic_more_share);
        ivFunction.setVisibility(View.VISIBLE);
        ratingBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 24));
        rollViewPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,(int)(DeviceUtil.getWidth(this)*0.75)));
        loadData();
        addLisener();

    }


    private void initView(ClassInfoDetail detail) {
        classInfoDetail = detail;
        if (!TextUtils.isEmpty(detail.getCourseName())) {
            tvClassTittle.setText(detail.getCourseName());
        }
        if (!TextUtils.isEmpty(detail.getCourseDetail())) {
            tvContent.setText(detail.getCourseDetail());
        }

        if (!TextUtils.isEmpty(detail.getCoachRealName())) {
            tvCoachName.setText(detail.getCoachRealName()+"教练");
        }

        if (!TextUtils.isEmpty(detail.getUserSex())) {
            if (detail.getUserSex().equals("0")) {
                tvCoachName.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.icon_woman),null);
            }else{
                tvCoachName.setCompoundDrawablesRelativeWithIntrinsicBounds(null,null,getResources().getDrawable(R.mipmap.icon_man),null);
            }
        }
        if (!TextUtils.isEmpty(detail.getCourseDetail())) {
            tvContent.setText(detail.getCourseDetail());
        }


        if (!TextUtils.isEmpty(detail.getStars())) {
            ratingBar.setRating(Float.parseFloat(detail.getStars()));
            tvCoachScore.setText(detail.getStars());
        }

        ImageLoader.getInstance().displayImage(detail.getVenueUrl(), ivSpaceIcon, Options.getListOptions());
        if (!TextUtils.isEmpty(detail.getVenueName())) {
            tvSpaceName.setText(detail.getVenueName());
        }

        if (!TextUtils.isEmpty(detail.getServiceDetails())) {
            tvCoachInfo.setText(detail.getServiceDetails());
        }

        if (!TextUtils.isEmpty(detail.getPrice())) {
            tvClassPrice.setText("￥"+detail.getPrice());
        }

        tvSpaceInfo.setText("暂无");
        ImageLoader.getInstance().displayImage(detail.getUserPicUrl(), ivCoachIcon);
        ImageLoader.getInstance().displayImage(detail.getVenueUrl(), ivOperatePerson);
        tvOperateAddress.setText("暂无");
        if (detail.getPersionList().length == 0) {
            TextView textView = new TextView(GroupClassDetailActivity.this);
            textView.setText(getString(R.string.no_report_members));
            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setTextColor(getResources().getColor(R.color.text_color_gray));
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DeviceUtil.dp2px(mContext, 40), DeviceUtil.dp2px(mContext, 40));
            params.gravity = Gravity.CENTER;
            params.leftMargin = 15;
            for (int i = 0; i < detail.getPersionList().length; i++) {
                ImageView imageView = new ImageView(GroupClassDetailActivity.this);
                imageView.setLayoutParams(params);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ImageLoader.getInstance().displayImage(detail.getPersionList()[i], imageView, Options.getHeaderOptions());
                llHaveOrderedMembers.addView(imageView);
            }
        }


        if (null != detail.getCoursePics() && detail.getCoursePics().length>0) {
            rollViewPager.setVisibility(View.VISIBLE);
        }else{
            rollViewPager.setVisibility(View.GONE);
        }
        rollViewPager.setPlayDelay(3000);
        rollViewPager.setAnimationDurtion(500);
        rollViewPager.setAdapter(new TestNomalAdapter(detail.getCoursePics(), detail.getCourseName()));
        rollViewPager.setHintView(new ColorPointHintView(this, getResources().getColor(R.color.common_header_bg), Color.WHITE));
        ratingBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 24));


//        loadCommend();


        adapter = new DiscussItemAdapter(discuss, this);
        lisviewDiscuss.setAdapter(adapter);


    }

    private void loadCommend() {

        Map<String, String> data = new HashMap<>();
        data.put("courseId", id);
        data.put("pageNO", String.valueOf(page));
        data.put("pageSize", "5");
        PostRequest request = new PostRequest(Constants.CLASS_COMMEND, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                List<ClassCommend> commends = JsonUtils.listFromJson(response.getAsJsonArray("list"), ClassCommend.class);
                if (null != commends && commends.size() > 0) {
                    discuss.addAll(commends);
                    page++;
                }
                if (discuss.size() > 0) {
                    adapter.setData(discuss);
                } else {
                    mSVProgressHUD.showInfoWithStatus(getString(R.string.no_commend));
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(GroupClassDetailActivity.this);
        mQueue.add(request);
    }

    private void loadData() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> data = new HashMap<>();
        data.put("courseId", id);
        data.put("courseType", type);
        PostRequest request = new PostRequest(Constants.SEARCH_CLASS_DETAIL, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                ClassInfoDetail detail = JsonUtils.objectFromJson(response.toString(), ClassInfoDetail.class);
                initView(detail);
                mSVProgressHUD.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(GroupClassDetailActivity.this);
        mQueue.add(request);


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
                loadCommend();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classInfoDetail != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.PAGE_INDEX, 1);
                    bundle.putString(Constants.COURSE_ID, classInfoDetail.getCourseId());
                    bundle.putString(Constants.COURSE_MONEY,classInfoDetail.getPrice());
                    openActivity(PayActivity.class, bundle);
                }else{
                    mSVProgressHUD.showInfoWithStatus("课程请求获取失败", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                }
            }
        });
    }


    private class TestNomalAdapter extends StaticPagerAdapter {

        private String[] imgs;


        private String courceName;
        private String[] infos = {
                "燃烧吧，脂肪君"

        };

        public TestNomalAdapter(String[] bigUrl, String courceName) {
            imgs = bigUrl;
            this.courceName = courceName;

        }


        @Override
        public View getView(ViewGroup container, int position) {
            View relativeLayout = LayoutInflater.from(getBaseContext()).inflate(R.layout.ad_common_view, null);
            ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.iv_cover_bg);
            TextView textView = (TextView) relativeLayout.findViewById(R.id.tv_tittle);
            ImageLoader.getInstance().displayImage(imgs[position], imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            textView.setText(infos[0] + "(" + courceName + ")");
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
