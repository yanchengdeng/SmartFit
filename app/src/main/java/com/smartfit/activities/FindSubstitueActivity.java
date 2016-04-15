package com.smartfit.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.adpters.FindSubstitutAdapter;
import com.smartfit.adpters.MemberListAdapter;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
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

/**
 * 找人代课
 *
 * @author yanchengdeng
 */
public class FindSubstitueActivity extends BaseActivity {

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
    private List<String> datas = new ArrayList<>();
    private FindSubstitutAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_substitue);
        ButterKnife.bind(this);
        initView();
        addLisener();

    }

    private void initView() {
        tvTittle.setText(getString(R.string.find_substitue));
        adapter = new FindSubstitutAdapter(this, datas);
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


        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                loadData();
            }
        });
    }

    private void loadData() {
        String venderId = getIntent().getStringExtra(Constants.PASS_STRING);
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);

        Map<String, String> data = new HashMap<>();
//        data.put("startTime", "");
        data.put("venueId", venderId);
//        data.put("endTime", );
//        data.put("courseType", );
//        data.put("sex", );
//        data.put("startPrice", );
//        data.put("endPrice",);
        PostRequest request = new PostRequest(Constants.COACH_LISTIDLECOACHESBYVENUEID, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc私教课", response.toString());
                mSVProgressHUD.dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                LogUtil.w("dyc", "..... " + error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(FindSubstitueActivity.this);
        mQueue.add(request);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    datas.add("模拟数据" + i + String.valueOf(page));
                }
                listView.setVisibility(View.VISIBLE);
                listView.onLoadMoreComplete();
                adapter.setData(datas);
                mSVProgressHUD.dismiss();
            }
        }, 2000);
    }

}
