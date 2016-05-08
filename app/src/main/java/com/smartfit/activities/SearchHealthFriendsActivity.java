package com.smartfit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

/**
 * 查找健身伙伴
 */
public class SearchHealthFriendsActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.et_search_content)
    EditText etSearchContent;
    @Bind(R.id.no_data)
    TextView noData;
    @Bind(R.id.listView)
    LoadMoreListView listView;

    private int page = 1;
    private FansAdapter adapter;
    private List<AttentionBean> datas = new ArrayList<AttentionBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_health_friends);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.search_health_friends));
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


        ivFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SearchHealthFriendsActivity.class);
            }
        });


        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchFriends(etSearchContent.getEditableText().toString());
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

    /**
     * 搜索好友
     * @param key
     */
    private void searchFriends(String key) {
        datas.clear();
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        data.put("nickName",key);
        PostRequest request = new PostRequest(Constants.USER_SEARCHFRIENDLIST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                List<AttentionBean> beans = JsonUtils.listFromJson(response.getAsJsonArray("list"), AttentionBean.class);
                if (beans != null && beans.size() > 0) {
                    datas.addAll(beans);
                    adapter.setData(datas);
                    showDataView();
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
                showNoData();
            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(SearchHealthFriendsActivity.this);
        mQueue.add(request);

    }


    private void loadData() {
        if (page == 1) {
            mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        }
        Map<String, String> data = new HashMap<>();
        PostRequest request = new PostRequest(Constants.USER_SEARCHFRIENDLIST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                List<AttentionBean> beans = JsonUtils.listFromJson(response.getAsJsonArray("list"), AttentionBean.class);
                if (beans != null && beans.size() > 0) {
                    datas.addAll(beans);
                    adapter.setData(datas);
                    showDataView();
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
                showNoData();
            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(SearchHealthFriendsActivity.this);
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