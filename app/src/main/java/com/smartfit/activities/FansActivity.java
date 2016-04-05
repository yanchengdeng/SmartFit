package com.smartfit.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.adpters.FansAdapter;
import com.smartfit.beans.AttentionBean;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.LoadMoreListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/***
 * 粉丝  界面
 */
public class FansActivity extends BaseActivity {

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
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.no_data)
    TextView noData;

    private int page = 1;
    private FansAdapter adapter;
    private List<AttentionBean> datas = new ArrayList<AttentionBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.fans));
        adapter = new FansAdapter(this, datas);
        listView.setAdapter(adapter);
        loadData();
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /***
         * 下拉刷新
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                        page = 1;
                      loadData();
            }
        });


        /**
         * 加载更多
         */
        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                loadData();
            }
        });


    }


    private void loadData() {
        if (page == 1) {
            mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        }

        final Map<String, String> data = new HashMap<>();
        PostRequest request = new PostRequest(Constants.USER_FANSLIST,data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                swipeRefreshLayout.setRefreshing(false);
                mSVProgressHUD.dismiss();
                List<AttentionBean> beans = JsonUtils.listFromJson(response.getAsJsonArray("list"), AttentionBean.class);
                if (beans != null && beans.size() > 0) {
                    datas.addAll(beans);
                    adapter.setData(datas);
                } else {
                    if (datas.size() > 0) {
                        listView.onLoadMoreComplete();
                        showDataView();
                    } else {
                        showNoData();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage());

            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(FansActivity.this);
        mQueue.add(request);
    }

    private void showNoData() {
        listView.setVisibility(View.GONE);
        noData.setVisibility(View.VISIBLE);
        listView.onLoadMoreComplete();
        return;
    }

    private void showDataView() {
        listView.setVisibility(View.VISIBLE);
        noData.setVisibility(View.GONE);
    }
}
