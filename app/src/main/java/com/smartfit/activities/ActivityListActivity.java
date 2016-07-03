package com.smartfit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.adpters.EventActivityAdapter;
import com.smartfit.beans.EventActivity;
import com.smartfit.beans.EventActvityList;
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
 * 活动列表
 *
 * @author yanchengdeng
 *         create at 2016/5/18 16:32
 */
public class ActivityListActivity extends BaseActivity {

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

    private EventActivityAdapter eventActivityAdapter;
    List<EventActivity> datas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_list);
        ButterKnife.bind(this);
        initView();
        getData();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.actvity));
        eventActivityAdapter = new EventActivityAdapter(this, datas);
        listView.setAdapter(eventActivityAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, datas.get(position).getId());
                bundle.putString("type", datas.get(position).getEventType());
                openActivity(EventActivtyDetailActivity.class, bundle);
            }
        });
    }

    private void getData() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> maps = new HashMap<>();
        maps.put("queryType","1");
        PostRequest request = new PostRequest(Constants.EVENT_LISTEVENTS, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                EventActvityList eventActivities = JsonUtils.objectFromJson(response, EventActvityList.class);
                if (eventActivities != null && eventActivities.getListData() != null && eventActivities.getListData().size() > 0) {
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.INVISIBLE);
                    datas.addAll(eventActivities.getListData());
                    eventActivityAdapter.setData(datas);
                    listView.onLoadMoreComplete();
                } else {
                    listView.setVisibility(View.INVISIBLE);
                    noData.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
                listView.setVisibility(View.INVISIBLE);
                noData.setVisibility(View.VISIBLE);
                listView.onLoadMoreComplete();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(ActivityListActivity.this);
        mQueue.add(request);
    }


    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
