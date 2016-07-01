package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.R;
import com.smartfit.beans.CityBean;
import com.smartfit.commons.AppManager;
import com.smartfit.commons.Constants;
import com.smartfit.utils.GetSingleRequestUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.views.viewpagerindicator.CirclePageIndicator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SplashActivity extends FragmentActivity {


    @Bind(R.id.iv_ad_bg)
    ImageView ivAdBg;
    @Bind(R.id.guide_ui)
    RelativeLayout guideUi;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.indicator)
    CirclePageIndicator indicator;
    @Bind(R.id.btnHome)
    Button btnHome;
    @Bind(R.id.tv_second_jump)
    TextView tvSecondJump;


    private CountDownTimer countDownTimer;

    private GalleryPagerAdapter adapter;
    private int[] images = {
            R.mipmap.login_bg,
            R.mipmap.login_bg,
            R.mipmap.login_bg,
            R.mipmap.login_bg
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                LogUtil.w("dyc", millisUntilFinished + "");
                tvSecondJump.setText(String.format("%s跳过", String.valueOf(millisUntilFinished / 1000)));
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        };
        AppManager.getAppManager().addActivity(this);
        boolean firstTimeUse = SharedPreferencesUtils.getInstance().getBoolean(Constants.FRIST_OPEN_APP, true);
        //TODO   后期可添加闪屏图
        if (false) {
            initGuideGallery();
        } else {
            initLaunchLogo();
        }
    }

    private void initLaunchLogo() {
        View guideImage = findViewById(R.id.guide_ui);
        guideImage.setVisibility(View.VISIBLE);
        getLauchPic();

    }

    /**
     * 获取启动图片
     */
    private void getLauchPic() {
        PostRequest request = new PostRequest(Constants.AD_ADSNAP, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                //TODO   代替换
                String ads = "http://f1.bj.anqu.com/orgin/NDNiNQ==/allimg/120607/29-12060G60516.jpg";
                ImageLoader.getInstance().displayImage(ads, ivAdBg);
                requestCityList();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                countDownTimer.start();
            }
        });
        request.headers = NetUtil.getRequestBody(SplashActivity.this);
        GetSingleRequestUtils.getInstance(this).getRequestQueue().add(request);


    }

    private void initGuideGallery() {
        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesUtils.getInstance().putBoolean(Constants.FRIST_OPEN_APP, false);

                requestCityList();
            }
        });

        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        indicator.setVisibility(View.VISIBLE);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setVisibility(View.VISIBLE);

        adapter = new GalleryPagerAdapter();
        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == images.length - 1) {
                    btnHome.setVisibility(View.VISIBLE);
                    btnHome.startAnimation(fadeIn);
                } else {
                    btnHome.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    private void requestCityList() {
        Map<String, String> data = new HashMap<>();
        PostRequest request = new PostRequest(Constants.GET_CITY_LIST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                List<CityBean> cityBeans = JsonUtils.listFromJson(response.getAsJsonArray("list"), CityBean.class);
                if (cityBeans != null && cityBeans.size() > 0) {
                    SharedPreferencesUtils.getInstance().putString(Constants.CITY_LIST_INOF, JsonUtils.toJson(cityBeans));
                }
                countDownTimer.start();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                countDownTimer.start();
            }
        });
        request.headers = NetUtil.getRequestBody(SplashActivity.this);
        GetSingleRequestUtils.getInstance(SplashActivity.this).getRequestQueue().add(request);
    }

    public class GalleryPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView item = new ImageView(SplashActivity.this);
            item.setScaleType(ImageView.ScaleType.CENTER_CROP);
            item.setImageResource(images[position]);
            container.addView(item);
            return item;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
