package com.smartfit.activities;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.ecloud.pulltozoomview.PullToZoomListViewEx;
import com.flyco.dialog.widget.popup.base.BaseBubblePopup;
import com.google.gson.JsonObject;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.smartfit.MessageEvent.UpdateCustomClassInfo;
import com.smartfit.R;
import com.smartfit.adpters.CustomClassAdapter;
import com.smartfit.beans.UserCustomClass;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义课程
 */
public class CustomeClassActivity extends BaseActivity {
    private PullToZoomListViewEx listView;

    private View ViewHeader;
    private View tvOrder;
    private List<UserCustomClass> datas = new ArrayList<UserCustomClass>();
    private CustomClassAdapter adapter;
    ListView lv;

    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_class);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        // 修改状态栏颜色，4.4+生效
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus();
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.bar_regiter_bg);//通知栏所需颜色
        listView = (PullToZoomListViewEx) findViewById(R.id.listview);
        initView();
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        int mScreenHeight = localDisplayMetrics.heightPixels;
        int mScreenWidth = localDisplayMetrics.widthPixels;
        AbsListView.LayoutParams localObject = new AbsListView.LayoutParams(mScreenWidth, (int) (9.0F * (mScreenWidth / 16.0F)));
        listView.setHeaderLayoutParams(localObject);
        listView.setParallax(true);
        lv = listView.getPullRootView();
        listView.setBackgroundColor(getResources().getColor(R.color.gray_light));
        ViewHeader = LayoutInflater.from(this).inflate(R.layout.custom_listview_header, null);
        lv.addHeaderView(ViewHeader);
        tvOrder = ViewHeader.findViewById(R.id.tv_order);
        lv.setDividerHeight(0);
        adapter = new CustomClassAdapter(this, datas);
        lv.setAdapter(adapter);
        initData("0");//（0：按距离 1：按空缺人数 2：按价格 ）
        addLisener();

    }

    @Subscribe
    public void onEvent(UpdateCustomClassInfo event) {/* Do something */
        initData("0");
    }

    private void addLisener() {
        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomBubblePopup customBubblePopup = new CustomBubblePopup(mContext);
//                customBubblePopup.setCanceledOnTouchOutside(false);
                customBubblePopup
                        .gravity(Gravity.BOTTOM)
                        .anchorView(tvOrder)
                        .bubbleColor(getResources().getColor(R.color.common_header_bg))
                        .triangleWidth(20)
                        .triangleHeight(10)
                        .showAnim(null)
                        .dismissAnim(null)
                        .show();
            }
        });
    }

    private void initData(String type) {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> maps = new HashMap<>();
        maps.put("orderBy", type);
        maps.put("longitude", SharedPreferencesUtils.getInstance().getString(Constants.CITY_LONGIT,""));
        maps.put("latitude",SharedPreferencesUtils.getInstance().getString(Constants.CITY_LAT,""));
        PostRequest request = new PostRequest(Constants.CLASSIF_GETSELFDESIGNCOURSELIST, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                List<UserCustomClass> userCustomClasses = JsonUtils.listFromJson(response.getAsJsonArray("list"),UserCustomClass.class);
                adapter.setData(userCustomClasses);


                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(CustomeClassActivity.this);
        mQueue.add(request);
    }

    private void initView() {
        listView.getPullRootView().findViewById(R.id.btn_custom_class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(UserCustomClassOneActivity.class);
            }
        });
    }


    private class CustomBubblePopup extends BaseBubblePopup<CustomBubblePopup> {

        private TextView tvDistance, tvEmpty, tvPrice;

        public CustomBubblePopup(Context context) {
            super(context);
        }

        @Override
        public View onCreateBubbleView() {
            View inflate = View.inflate(mContext, R.layout.pop_custom_class_bubble, null);
            tvDistance = (TextView) inflate.findViewById(R.id.tv_distance_order);
            tvEmpty = (TextView) inflate.findViewById(R.id.tv_empty_order);
            tvPrice = (TextView) inflate.findViewById(R.id.tv_price_order);
            return inflate;
        }

        @Override
        public void setUiBeforShow() {
            super.setUiBeforShow();

            tvDistance.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    initData("0");
                }
            });
            tvEmpty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    initData("1");
                }
            });

            tvPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    initData("2");
                }
            });
        }
    }

}
