package com.smartfit.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.adpters.ListMessageAllInfoAdaper;
import com.smartfit.beans.ListMessageAllInfoItem;
import com.smartfit.beans.MessageAllInfo;
import com.smartfit.commons.Constants;
import com.smartfit.fragments.CustomAnimationDemoFragment;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import java.util.ArrayList;
import java.util.List;

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
    ListView listView;



    private List<ListMessageAllInfoItem> listMessageAllInfoItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        initView();
        addLisener();
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new CustomAnimationDemoFragment())
                    .commit();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getMessageList();
    }

    private void getMessageList() {
        listMessageAllInfoItems.clear();
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        PostRequest request = new PostRequest(Constants.MESSAGE_GETMESSAGEMAIN, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                MessageAllInfo messageAllInfo = JsonUtils.objectFromJson(response, MessageAllInfo.class);
                if (messageAllInfo != null) {
                    if (!TextUtils.isEmpty(messageAllInfo.getUnReadSysCount()) && null != messageAllInfo.getSysMessage()) {
                        listMessageAllInfoItems.add(new ListMessageAllInfoItem(messageAllInfo.getUnReadSysCount(), messageAllInfo.getSysMessage()));
                    }
                    if (!TextUtils.isEmpty(messageAllInfo.getUnReadCourseCount()) && null != messageAllInfo.getCourseMessage()) {
                        listMessageAllInfoItems.add(new ListMessageAllInfoItem(messageAllInfo.getUnReadCourseCount(), messageAllInfo.getCourseMessage()));
                    }
                    if (!TextUtils.isEmpty(messageAllInfo.getUnReadFriendCount()) && null != messageAllInfo.getFriendMessage()) {
                        listMessageAllInfoItems.add(new ListMessageAllInfoItem(messageAllInfo.getUnReadFriendCount(), messageAllInfo.getFriendMessage()));
                    }

                    if (listMessageAllInfoItems.size() > 0) {
                        listView.setAdapter(new ListMessageAllInfoAdaper(MessageActivity.this, listMessageAllInfoItems));
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
                if (listMessageAllInfoItems.size() > 0) {
                    listView.setAdapter(new ListMessageAllInfoAdaper(MessageActivity.this, listMessageAllInfoItems));
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                } else {
                    listView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
                mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(this);
        mQueue.add(request);
    }

    private void initView() {
        tvTittle.setText("消息");
        ivBack.setVisibility(View.INVISIBLE);
    }

    private void addLisener() {


        /***
         * 1-系统消息；2-课程邀请消息；3-预约成功消息；4-课程请求消息；5-好友邀请消息；6-订单成功消息
         */

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                ListMessageAllInfoItem item = listMessageAllInfoItems.get(position);
                if (!TextUtils.isEmpty(item.getSysMessage().getType())) {
                    if (item.getSysMessage().getType().equals("1")) {

                    } else if (item.getSysMessage().getType().equals("2")) {

                    } else if (item.getSysMessage().getType().equals("3")) {

                    } else if (item.getSysMessage().getType().equals("4")) {
                        bundle.putString(Constants.PASS_STRING, TextUtils.isEmpty(item.getSysMessage().getTime()) ? "" : item.getSysMessage().getTime());
                        openActivity(CourseMessageActivity.class, bundle);

                    } else if (item.getSysMessage().getType().equals("5")) {

                    } else if (item.getSysMessage().getType().equals("6")) {
//                        bundle.putString(Constants.PASS_STRING, TextUtils.isEmpty(item.getSysMessage().getTime()) ? "" : item.getSysMessage().getTime());
//                        openActivity(CourseMessageActivity.class, bundle);
                    }
                }


            }
        });

    }
}
