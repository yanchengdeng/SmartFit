package com.smartfit.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
    ListView listView;
    @Bind(R.id.tv_count)
    TextView tvCount;
    @Bind(R.id.tv_search_condition)
    TextView tvSearchCondition;
    @Bind(R.id.no_data)
    TextView noData;

    private View footerView;
    private int page = 1;
    boolean isLoading = false;
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
        footerView = LayoutInflater.from(this).inflate(R.layout.list_loader_footer, null);
//        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footerView.setLayoutParams(lp);
        listView.addFooterView(footerView);
        adapter = new GroupExpericeItemAdapter(this, datas);
        listView.setAdapter(adapter);
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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastIndexInScreen = visibleItemCount + firstVisibleItem;
                if (lastIndexInScreen >= totalItemCount - 1 && !isLoading) {
                    isLoading = true;
                    page++;
                    loadData("");
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openActivity(GroupClassDetailActivity.class);
            }
        });


        /***搜索**/
        ivFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etContent.getEditableText().toString())) {
                    mSVProgressHUD.showErrorWithStatus("请输入搜索条件", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                    return;
                }
                page = 1;
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
        PostRequest request = new PostRequest(Constants.SEARCH_CLASS, NetUtil.getRequestBody(data, mContext), new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                List<ClassInfo> requestList = JsonUtils.listFromJson(response.getAsJsonArray("list"), ClassInfo.class);
                if (null != requestList && requestList.size() > 0) {
                    datas.addAll(requestList);
                    adapter.setData(datas);
                    tvCount.setText(String.valueOf(datas.size()));
                    tvSearchCondition.setText(String.format(getString(R.string.find_conditon_result), new Object[]{contions}));
                    listView.removeFooterView(footerView);
                    isLoading = false;
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
        mQueue.add(request);


    }

    private void noMoreData(List<ClassInfo> datas) {
        if (datas.size() > 0) {
            mSVProgressHUD.showInfoWithStatus(getString(R.string.no_more_data), SVProgressHUD.SVProgressHUDMaskType.Clear);
            listView.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }
    }
}
