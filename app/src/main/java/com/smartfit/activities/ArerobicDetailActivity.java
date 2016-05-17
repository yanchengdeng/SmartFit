package com.smartfit.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.beans.ClassInfoDetail;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.Util;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 有氧呼吸详情课程
 */
public class ArerobicDetailActivity extends BaseActivity {

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
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_room)
    TextView tvRoom;
    @Bind(R.id.tv_distance)
    TextView tvDistance;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    private String courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arerobic_detail);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.aerobic_apparatus));
        courseId = getIntent().getStringExtra(Constants.PASS_STRING);
        getClassInfo();
    }

    private void getClassInfo() {

        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> data = new HashMap<>();
        data.put("courseId", courseId);
        data.put("courseType", "3");
        PostRequest request = new PostRequest(Constants.SEARCH_CLASS_DETAIL, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                ClassInfoDetail detail = JsonUtils.objectFromJson(response.toString(), ClassInfoDetail.class);
                fillData(detail);
                mSVProgressHUD.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(ArerobicDetailActivity.this);
        mQueue.add(request);

    }


    private void fillData(ClassInfoDetail detail) {
        /**
         * 订单状态（
         * 1我报名但未付款，
         * 2已经付款教练未接单，
         * 3已经付款教练接单（即正常），
         * 4课程已经结束
         * 5我退出该课程，
         * 6该课程被取消了，
         * 7课程已结束未评论
         * 8已评论）
         */


        if (!TextUtils.isEmpty(detail.getVenueName())) {
            tvAddress.setText(detail.getVenueName());
        }
        if (!TextUtils.isEmpty(detail.getClassroomName())) {
            tvRoom.setText(detail.getClassroomName());
        }

        tvDistance.setText("距离 " + Util.getDistance(detail.getLat(), detail.getLongit()));

        if (!TextUtils.isEmpty(detail.getStartTime())&& !TextUtils.isEmpty(detail.getEndTime())) {
            tvTime.setText(DateUtils.getData(detail.getStartTime()) + "~" + DateUtils.getDataTime(detail.getEndTime()));
        }

        if (!TextUtils.isEmpty(detail.getPrice())) {
            tvPrice.setText(detail.getPrice());
        } else {
            detail.setPrice("免费");
            tvPrice.setText("免费");
        }

        if (null != detail.getCoursePics() && detail.getCoursePics().length > 0) {
            rollViewPager.setVisibility(View.VISIBLE);
        } else {
            rollViewPager.setVisibility(View.GONE);
        }
        rollViewPager.setPlayDelay(3000);
        rollViewPager.setAnimationDurtion(500);
        rollViewPager.setAdapter(new TestNomalAdapter(detail.getCoursePics(), detail.getClassroomName()));
        rollViewPager.setHintView(new ColorPointHintView(this, getResources().getColor(R.color.common_header_bg), Color.WHITE));
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    private void  addLisener(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
            ImageLoader.getInstance().displayImage(imgs[position], imageView, Options.getListOptions());
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            textView.setText(infos[0] + "(" + courceName + ")");
            return relativeLayout;
        }


        @Override
        public int getCount() {
            return imgs.length;
        }
    }


}
