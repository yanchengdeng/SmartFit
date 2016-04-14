package com.smartfit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.adpters.DiscussItemAdapter;
import com.smartfit.beans.ClassCommend;
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
import butterknife.OnClick;

/**
 * 课程更多评论
 */
public class ClassMoreCommentsActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.no_data)
    TextView noData;
    @Bind(R.id.listView)
    LoadMoreListView listView;

    private int page = 1;

    private List<ClassCommend> datas = new ArrayList<>();

    private DiscussItemAdapter adapter;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_more_comments);
        id = getIntent().getStringExtra(Constants.PASS_STRING);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.more_comments));
        adapter = new DiscussItemAdapter(datas, this);
        listView.setAdapter(adapter);
        loadCommend();

        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                loadCommend();
            }
        });
    }

    private void loadCommend() {

        if (page == 1)
            mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        final Map<String, String> data = new HashMap<>();
        data.put("courseId", id);
//        data.put("pageNO", String.valueOf(page));
//        data.put("pageSize", "20");
        PostRequest request = new PostRequest(Constants.CLASS_COMMEND, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                listView.onLoadMoreComplete();
                List<ClassCommend> commends = JsonUtils.listFromJson(response.getAsJsonArray("list"), ClassCommend.class);
                if (null != commends && commends.size() > 0) {
                    datas.addAll(commends);
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    adapter.setData(datas);
                } else {
                    if (datas.size() > 0) {
                        listView.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                    } else {
                        listView.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                }
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

                listView.onLoadMoreComplete();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(ClassMoreCommentsActivity.this);
        mQueue.add(request);
    }


    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
