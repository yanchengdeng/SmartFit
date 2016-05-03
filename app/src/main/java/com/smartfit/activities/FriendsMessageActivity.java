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
import com.smartfit.adpters.FriendsMesageAdatper;
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
import butterknife.OnClick;

/**
 * 好友消息
 *
 * @author yanchengdeng
 *         create at 10:38
 */
public class FriendsMessageActivity extends BaseActivity {

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

    private FriendsMesageAdatper friendsMesageAdatper;

    private boolean isLoadEnd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_message);
        ButterKnife.bind(this);
        initView();
        addLienser();
        initData();

    }

    private void initView() {
        tvTittle.setText(getString(R.string.friends_message));
        friendsMesageAdatper = new FriendsMesageAdatper(this,messageLists);
        listView.setAdapter(friendsMesageAdatper);


    }

    private void addLienser() {
        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (!isLoadEnd)
                initData();
            }
        });

    }

    private void initData() {
        isLoadEnd = false;
        if (page == 1)
            mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        data.put("queryType", MessageGroupType.MESSAGE_GROUP_TYPE_FRIENDS);
        data.put("pageNo", String.valueOf(page));
        PostRequest request = new PostRequest(Constants.MESSAGE_LIST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                MessageList submessages = JsonUtils.objectFromJson(response, MessageList.class);
                if (submessages != null && submessages.getListData() != null && submessages.getListData().size() > 0) {
                    messageLists.addAll(submessages.getListData());
                    friendsMesageAdatper.setData(messageLists);
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                    page++;
                } else {
                    if (messageLists.size() > 0) {
                        listView.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                        mSVProgressHUD.showInfoWithStatus("已加载到底", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        isLoadEnd = true;
                        listView.onLoadMoreComplete();
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

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}
