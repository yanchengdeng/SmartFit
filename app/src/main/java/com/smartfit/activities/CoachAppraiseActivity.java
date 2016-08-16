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
import com.smartfit.adpters.CoachAppraiseAdapter;
import com.smartfit.adpters.CoachAppraiseAllAdapter;
import com.smartfit.beans.ClassCommend;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
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
 * 教练评价
 */
public class CoachAppraiseActivity extends BaseActivity {

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

    private int page = 1;
    private CoachAppraiseAllAdapter adapter;
    private List<ClassCommend> datas = new ArrayList<ClassCommend>();
    private String coachId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_appraise);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText("教练评价");
        adapter = new CoachAppraiseAllAdapter(this, datas);
        listView.setAdapter(adapter);
        coachId = getIntent().getStringExtra(Constants.PASS_STRING);
        loadData();
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        data.put("coachId", coachId);
        data.put("pageNo", String.valueOf(page));
        PostRequest request = new PostRequest(Constants.COMMENT_GETCOMMENTS_BYCOACHID, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc", response.toString());
                mSVProgressHUD.dismiss();
                List<ClassCommend> commentInfos = JsonUtils.listFromJson(response.getAsJsonArray("listData"), ClassCommend.class);
                datas.addAll(commentInfos);
                adapter.setData(datas);
                if (commentInfos != null && commentInfos.size() > 0) {
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                } else {
                    listView.onLoadMoreComplete();
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
                mSVProgressHUD.showInfoWithStatus(error.getMessage());
            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(CoachAppraiseActivity.this);
        mQueue.add(request);


    }

}
