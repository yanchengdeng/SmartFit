package com.smartfit.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.smartfit.R;
import com.smartfit.beans.FlashPageInfo;
import com.smartfit.beans.MainAdInfo;
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
import butterknife.OnClick;

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


    int secods[] = new int[]{2, 1, 0};
    int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        countDownTimer = new CountDownTimer(3000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                LogUtil.w("dyc", "执行：" + index);
                if (index < secods.length) {
                    tvSecondJump.setText(String.format("%s 跳过", String.valueOf(secods[index])));
                }
                index++;
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
        getMainAd();
        getLauchPic();
        goToMain();

    }

    private void goToMain() {
        String flashAds = SharedPreferencesUtils.getInstance().getString(Constants.SPLASH_ADS, "");
        if (!TextUtils.isEmpty(flashAds)) {
            FlashPageInfo flashPageInfo = JsonUtils.objectFromJson(flashAds, FlashPageInfo.class);
            if (flashPageInfo != null && flashPageInfo.getPics() != null && flashPageInfo.getPics().length > 0) {
                if (Long.parseLong(flashPageInfo.getAdEndTime()) > (System.currentTimeMillis() / 1000)) {
                    getImagetBitMpa(flashPageInfo.getPics()[0]);
                } else {
                    toMainActivity();
                }
            } else {
                toMainActivity();
            }
        } else {
            toMainActivity();
        }

    }


    private void toMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        }, 1500);
    }

    /**
     * 获取首页广告
     */
    private void getMainAd() {
        Map<String, String> data = new HashMap<>();
        data.put("platform", "2");
        PostRequest request = new PostRequest(Constants.AD_ADFIRST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                List<MainAdInfo> mainAdInfos = JsonUtils.listFromJson(response.getAsJsonArray("list"), MainAdInfo.class);
                if (mainAdInfos != null && mainAdInfos.size() > 0) {
                    SharedPreferencesUtils.getInstance().putString(Constants.MIAN_ADS, JsonUtils.toJson(mainAdInfos));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.headers = NetUtil.getRequestBody(SplashActivity.this);
        GetSingleRequestUtils.getInstance(this).getRequestQueue().add(request);


    }


    /**
     * 获取启动图片
     */
    private void getLauchPic() {
        PostRequest request = new PostRequest(Constants.AD_ADSNAP, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                List<FlashPageInfo> flashPageInfo = JsonUtils.listFromJson(response.getAsJsonArray("list"), FlashPageInfo.class);
                if (flashPageInfo != null && flashPageInfo.size() > 0 && flashPageInfo.get(0).getPics().length > 0) {
                    SharedPreferencesUtils.getInstance().putString(Constants.SPLASH_ADS, JsonUtils.toJson(flashPageInfo.get(0)));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        request.headers = NetUtil.getRequestBody(SplashActivity.this);
        GetSingleRequestUtils.getInstance(this).getRequestQueue().add(request);


    }

    private void getImagetBitMpa(String url) {
        ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                ivAdBg.setImageBitmap(loadedImage);
                startAdCountDown();
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    private void initGuideGallery() {
        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fadein);
        btnHome = (Button) findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferencesUtils.getInstance().putBoolean(Constants.FRIST_OPEN_APP, false);

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


    private void startAdCountDown() {
        tvSecondJump.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                countDownTimer.start();
            }
        }, 1000);
    }

    @OnClick({R.id.iv_ad_bg, R.id.tv_second_jump})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_ad_bg:
                String flashAds = SharedPreferencesUtils.getInstance().getString(Constants.SPLASH_ADS, "");
                if (!TextUtils.isEmpty(flashAds)) {
                    FlashPageInfo flashPageInfo = JsonUtils.objectFromJson(flashAds, FlashPageInfo.class);
                    if (flashPageInfo != null && flashPageInfo.getPics() != null && flashPageInfo.getPics().length > 0) {
                        if (Long.parseLong(flashPageInfo.getAdEndTime()) > (System.currentTimeMillis() / 1000)) {
                            if (countDownTimer != null) {
                                countDownTimer.cancel();
                            }
                            Intent intent = new Intent(SplashActivity.this, AdActivity.class);
                            intent.putExtra(Constants.PASS_STRING, flashPageInfo.getLink());
                            intent.putExtra("name", flashPageInfo.getAdName());
                            intent.putExtra("is_from_main", true);
                            startActivity(intent);
                            finish();
                        }
                    }
                }
                break;
            case R.id.tv_second_jump:
                if (countDownTimer != null) {
                    countDownTimer.cancel();
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
                break;
        }
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
