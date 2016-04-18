package com.smartfit.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.adpters.MessageAdapter;
import com.smartfit.beans.MesageInfo;
import com.smartfit.beans.MessageList;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.LoadMoreListView;
import com.umeng.socialize.PlatformConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessageActivity extends BaseActivity {

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
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;


    private List<MesageInfo> messageLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        initView();
        addLisener();
        getMessageData();
    }

    private void initView() {
        tvTittle.setText("消息");
        ivBack.setVisibility(View.INVISIBLE);
    }

    private void addLisener() {

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessageData();
            }
        });


        /***
         * 1-系统消息；2-课程邀请消息；3-预约成功消息；4-课程请求消息；5-好友邀请消息；6-订单成功消息
         */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (messageLists.get(position).getType().equals("3")||messageLists.get(position).getType().equals("2")||messageLists.get(position).getType().equals("4")){

                }
            }
        });

    }


    private void getMessageData() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        data.put("type", "");
        data.put("latestTime", "");
        PostRequest request = new PostRequest(Constants.MESSAGE_LIST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                swipeRefreshLayout.setRefreshing(false);
                messageLists.clear();
                MessageList submessages = JsonUtils.objectFromJson(response, MessageList.class);
                if (submessages != null && submessages.getListData() != null && submessages.getListData().size() > 0) {
                    messageLists.addAll(submessages.getListData());
                    listView.setAdapter(new MessageAdapter(MessageActivity.this, messageLists));
                    listView.setVisibility(View.VISIBLE);
                    listView.onLoadMoreComplete();
                    noData.setVisibility(View.GONE);
                } else {
                    listView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                    listView.onLoadMoreComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listView.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
                mSVProgressHUD.dismiss();
                swipeRefreshLayout.setRefreshing(false); listView.onLoadMoreComplete();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(this);
        mQueue.add(request);
    }


}
