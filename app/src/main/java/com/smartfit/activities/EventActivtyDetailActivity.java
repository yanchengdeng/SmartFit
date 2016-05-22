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
import com.smartfit.beans.EventDetailInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 活动详情
 *
 * @author yanchengdeng
 *         create at 2016/5/18 17:16
 */
public class EventActivtyDetailActivity extends BaseActivity {

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
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.btn_order)
    Button btnOrder;
    @Bind(R.id.tv_detial)
    TextView tvDetial;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.scrollView)
    ScrollView scrollView;

    private String id;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_activty_detail);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.actvity_detail));
        id = getIntent().getStringExtra(Constants.PASS_STRING);
        type = getIntent().getStringExtra("type");
        getDetalInfo();
    }

    private void getDetalInfo() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> maps = new HashMap<>();
        maps.put("eventId", id);
        maps.put("type", type);
        PostRequest request = new PostRequest(Constants.EVENT_GETEVENT, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();

                EventDetailInfo eventDetailInfo = JsonUtils.objectFromJson(response, EventDetailInfo.class);
                if (null != eventDetailInfo) {
                    fillData(eventDetailInfo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);

            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(EventActivtyDetailActivity.this);
        mQueue.add(request);
    }

    EventDetailInfo eventDetailInfo;
    private void fillData(EventDetailInfo eventDetailInfo) {
        this.eventDetailInfo = eventDetailInfo;
        if (!TextUtils.isEmpty(eventDetailInfo.getEventTitle())) {
            tvTitle.setText(eventDetailInfo.getEventTitle());
        }

        if (!TextUtils.isEmpty(eventDetailInfo.getEventDetial())) {
            tvDetial.setText(eventDetailInfo.getEventDetial());
        }

        if (!TextUtils.isEmpty(eventDetailInfo.getEventEndTime())) {
            tvTime.setText(String.format("活动截止时间：%s", DateUtils.getData(eventDetailInfo.getEventEndTime())));
        }

        if (!TextUtils.isEmpty(eventDetailInfo.getEventType())) {
            if (eventDetailInfo.getEventType().equals("3")) {
                tvTime.setVisibility(View.GONE);
            }
        }

        if (!TextUtils.isEmpty(eventDetailInfo.getEventPrice())) {
            tvPrice.setText(eventDetailInfo.getEventPrice());
        }
        if (null != eventDetailInfo.getPics() && eventDetailInfo.getPics().length > 0) {
            rollViewPager.setVisibility(View.VISIBLE);
        } else {
            rollViewPager.setVisibility(View.GONE);
        }
        rollViewPager.setPlayDelay(3000);
        rollViewPager.setAnimationDurtion(500);
        rollViewPager.setAdapter(new TestNomalAdapter(eventDetailInfo.getPics(), eventDetailInfo.getEventTitle()));
        rollViewPager.setHintView(new ColorPointHintView(this, getResources().getColor(R.color.common_header_bg), Color.WHITE));
        scrollView.fullScroll(ScrollView.FOCUS_UP);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.setVisibility(View.VISIBLE);
            }
        }, 500);

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

    @OnClick({R.id.iv_back, R.id.btn_order})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_order:
                if (eventDetailInfo != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.PAGE_INDEX, 7);// 7  活动 绑定
                    bundle.putString(Constants.COURSE_ID, eventDetailInfo.getId());
                    bundle.putString(Constants.COURSE_MONEY, eventDetailInfo.getEventPrice());
//                    bundle.putString(Constants.COURSE_ORDER_CODE, eventDetailInfo.getOrderCode());
                    openActivity(PayActivity.class, bundle);
                }
                break;
        }
    }
}
