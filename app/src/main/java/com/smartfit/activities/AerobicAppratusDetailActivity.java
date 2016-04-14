package com.smartfit.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.smartfit.utils.DeviceUtil;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 有氧器械   详情页
 */
public class AerobicAppratusDetailActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.tv_room)
    TextView tvRoom;
    @Bind(R.id.tv_distance)
    TextView tvDistance;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.btn_order)
    Button btnOrder;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.roll_view_pager)
    RollPagerView rollViewPager;
    @Bind(R.id.scrollView)
    ScrollView scrollView;


    private String courseId;

    private ClassInfoDetail classInfoDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aerobic_appratus_detail);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.aerobic_apparatus));
        courseId = getIntent().getStringExtra(Constants.COURSE_ID);
        rollViewPager.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (DeviceUtil.getWidth(this) * 0.75)));
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
                if (detail != null) {
                    classInfoDetail = detail;
                    fillData(detail);
                }
                mSVProgressHUD.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(AerobicAppratusDetailActivity.this);
        mQueue.add(request);
    }

    private void fillData(ClassInfoDetail detail) {
        if (!TextUtils.isEmpty(detail.getVenueName())) {
            tvAddress.setText(detail.getVenueName());
        }
        if (!TextUtils.isEmpty(detail.getCourseName())) {
            tvRoom.setText(detail.getCourseName());
        }

        tvDistance.setText("距离 0km");

        if (!TextUtils.isEmpty(detail.getStartDate())) {
            tvTime.setText(DateUtils.getData(detail.getStartDate()));
        }

        if (!TextUtils.isEmpty(detail.getPrice())) {
            tvPrice.setText(detail.getPrice());
        }

        if (null != detail.getCoursePics() && detail.getCoursePics().length > 0) {
            rollViewPager.setVisibility(View.VISIBLE);
        } else {
            rollViewPager.setVisibility(View.GONE);
        }
        rollViewPager.setPlayDelay(3000);
        rollViewPager.setAnimationDurtion(500);
        rollViewPager.setAdapter(new TestNomalAdapter(detail.getCoursePics(), detail.getCourseName()));
        rollViewPager.setHintView(new ColorPointHintView(this, getResources().getColor(R.color.common_header_bg), Color.WHITE));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
                scrollView.setVisibility(View.VISIBLE);
            }
        }, 500);
    }


    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classInfoDetail != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.PAGE_INDEX, 3);
                    bundle.putString(Constants.COURSE_ID, classInfoDetail.getCourseId());
                    bundle.putString(Constants.COURSE_MONEY, classInfoDetail.getPrice());
                    openActivity(PayActivity.class, bundle);
                } else {
                    mSVProgressHUD.showInfoWithStatus("课程请求获取失败", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                }
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


}
