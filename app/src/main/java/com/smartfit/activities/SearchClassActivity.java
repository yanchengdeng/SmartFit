package com.smartfit.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.adpters.GroupExpericeItemAdapter;
import com.smartfit.beans.ClassInfo;
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
 * 搜索课程  页
 */

public class SearchClassActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.et_content)
    EditText etContent;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.listView)
    LoadMoreListView listView;
    @Bind(R.id.tv_count)
    TextView tvCount;
    @Bind(R.id.tv_search_condition)
    TextView tvSearchCondition;
    @Bind(R.id.no_data)
    TextView noData;
    @Bind(R.id.ll_search_result)
    LinearLayout llSearchResult;

    private int page = 1;
    private GroupExpericeItemAdapter adapter;
    private List<ClassInfo> datas = new ArrayList<ClassInfo>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_class);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        adapter = new GroupExpericeItemAdapter(this, datas);
        listView.setAdapter(adapter);
        loadData("");
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
                loadData("");
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, datas.get(position).getCourseId());
                bundle.putString(Constants.COURSE_TYPE, TextUtils.isEmpty(datas.get(position).getCourseType())? "0":datas.get(position).getCourseType());
                openActivity(GroupClassDetailActivity.class, bundle);
            }
        });


        /***搜索**/
        ivFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etContent.getEditableText().toString())) {
                    mSVProgressHUD.showInfoWithStatus("请输入搜索条件", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                    return;
                }
                page = 1;
                datas.clear();
                loadData(etContent.getEditableText().toString());
            }
        });
    }

    private void loadData(final String contions) {
        if (page == 1) {
            mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        }
        Map<String, String> data = new HashMap<>();
        data.put("keyword", contions);
        PostRequest request = new PostRequest(Constants.SEARCH_CLASS, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                List<ClassInfo> requestList = JsonUtils.listFromJson(response.getAsJsonArray("list"), ClassInfo.class);
                if (null != requestList && requestList.size() > 0) {
                    datas.addAll(requestList);
                    adapter.setData(datas);
                    listView.setVisibility(View.VISIBLE);
                    listView.onLoadMoreComplete();
                    tvCount.setText(String.valueOf(datas.size()));
                    if (!TextUtils.isEmpty(contions)) {
                        tvSearchCondition.setText(String.format(getString(R.string.find_conditon_result), new Object[]{contions}));
                        llSearchResult.setVisibility(View.VISIBLE);
                    } else {
                        llSearchResult.setVisibility(View.GONE);
                    }
                } else {
                    noMoreData(datas);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                noMoreData(datas);
            }
        });
        request.setTag(TAG);
        request.headers = NetUtil.getRequestBody(SearchClassActivity.this);
        mQueue.add(request);


    }

    private void noMoreData(List<ClassInfo> datas) {
        if (datas.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
            llSearchResult.setVisibility(View.VISIBLE);
            listView.onLoadMoreComplete();
            return;
        } else {
            listView.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
            llSearchResult.setVisibility(View.GONE);
            listView.onLoadMoreComplete();
            return;
        }
    }
}
