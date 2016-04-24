package com.smartfit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.MessageEvent.UpdateDynamic;
import com.smartfit.R;
import com.smartfit.adpters.DynamicAdapter;
import com.smartfit.beans.DynamicInfo;
import com.smartfit.beans.DynamicListData;
import com.smartfit.commons.Constants;
import com.smartfit.fragments.CustomAnimationDemoFragment;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.LoadMoreListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 用户动态列表
 */
public class CustomeDynamicActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.listView)
    LoadMoreListView listView;
    @Bind(R.id.no_data)
    TextView noData;


    private EventBus eventBus;


    private int page = 1;
    private DynamicAdapter adapter;
    private List<DynamicInfo> datas = new ArrayList<DynamicInfo>();
    private boolean isLoadMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custome_dynamic);
        ButterKnife.bind(this);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CustomAnimationDemoFragment())
                    .commit();
        }
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        tvTittle.setText("动态");
        ivFunction.setVisibility(View.VISIBLE);
        ivFunction.setImageResource(R.mipmap.icon_wrig);
        ivBack.setVisibility(View.GONE);
        adapter = new DynamicAdapter(this, datas);
        listView.setAdapter(adapter);
        loadData();
        addLisener();
    }


    private void addLisener() {

        ivFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(PublishCustomeDynamicActivity.class);
            }
        });


        /**
         * 加载更多
         */
        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (isLoadMore) {
                    loadData();
                }
            }
        });
    }


    @Subscribe
    public void onEvent(UpdateDynamic event) {
        datas.clear();
        isLoadMore = true;
        page = 1;
        loadData();
    /* Do something */
    }

    private void loadData() {
        if (page == 1)
            mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);

        HashMap<String, String> map = new HashMap<>();
        map.put("pageNo", String.valueOf(page));
        map.put("pageSize", "20");
        PostRequest request = new PostRequest(Constants.DYNAMIC_GETDYNAMICLIST, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                mSVProgressHUD.dismiss();
                DynamicListData dynamicListData = JsonUtils.objectFromJson(response, DynamicListData.class);
                if (dynamicListData != null) {
                    if (dynamicListData.getListData() != null && dynamicListData.getListData().size() > 0) {
                        datas.addAll(dynamicListData.getListData());
                        listView.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                        adapter.setData(datas);
                        page++;
                    } else {
                        if (datas.size() > 0) {
                            isLoadMore = false;
                            listView.setVisibility(View.VISIBLE);
                            noData.setVisibility(View.GONE);
                            listView.onLoadMoreComplete();
                            mSVProgressHUD.showInfoWithStatus("已加载到底", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        } else {
                            listView.setVisibility(View.GONE);
                            noData.setVisibility(View.VISIBLE);
                        }
                    }
                }

                listView.onLoadMoreComplete();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                if (datas.size() > 0) {
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                } else {
                    listView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(CustomeDynamicActivity.this);
        mQueue.add(request);

    }

}
