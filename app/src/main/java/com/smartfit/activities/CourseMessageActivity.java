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
import com.smartfit.adpters.CourseMessagItemAdapter;
import com.smartfit.beans.MesageInfo;
import com.smartfit.beans.MessageList;
import com.smartfit.commons.Constants;
import com.smartfit.commons.MessageGroupType;
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
 * 课程消息
 */
public class CourseMessageActivity extends BaseActivity {

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

    private List<MesageInfo> messageLists = new ArrayList<>();


    private CourseMessagItemAdapter courseMessagItemAdapter;

    private boolean isLoadEnd = false;//已加载到底

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_message);
        ButterKnife.bind(this);
        intView();
        addLisener();
        getMessageData();


    }

    private void intView() {
        tvTittle.setText(getString(R.string.course_messages));
        courseMessagItemAdapter = new CourseMessagItemAdapter(this, messageLists);
        listView.setAdapter(courseMessagItemAdapter);

    }


    private void getMessageData() {
        if (page == 1)
            mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        isLoadEnd = false;
        Map<String, String> data = new HashMap<>();
        data.put("queryType", MessageGroupType.MESSAGE_GROUP_TYPE_COURSE);
        data.put("pageNo", String.valueOf(page));
        PostRequest request = new PostRequest(Constants.MESSAGE_LIST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                MessageList submessages = JsonUtils.objectFromJson(response, MessageList.class);
                if (submessages != null && submessages.getListData() != null && submessages.getListData().size() > 0) {
                    messageLists.addAll(submessages.getListData());
                    courseMessagItemAdapter.setData(messageLists);
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    if (submessages.getListData().size()==Constants.SIZE){
                        page++;
                    }else{
                        listView.onLoadMoreComplete();
                        isLoadEnd = true;
                    }
                } else {
                    if (messageLists.size() > 0) {
                        listView.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                        mSVProgressHUD.showInfoWithStatus("已加载到底", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        listView.onLoadMoreComplete();
                        isLoadEnd = true;
                    } else {
                        listView.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                }
                listView.onLoadMoreComplete();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (messageLists.size() > 0) {
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                } else {
                    listView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
                listView.onLoadMoreComplete();
                mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(this);
        mQueue.add(request);
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
                if (!isLoadEnd)
                    getMessageData();
            }
        });

    }


}
