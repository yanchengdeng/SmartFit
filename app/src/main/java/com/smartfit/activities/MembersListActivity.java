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
import com.smartfit.adpters.MemberListAdapter;
import com.smartfit.beans.MemeberInfo;
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

/**
 * @author yanchengdeng
 *         成员名单
 */
public class MembersListActivity extends BaseActivity {

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

    private String crousrID;


    private int page = 1;
    private MemberListAdapter adapter;
    private List<MemeberInfo> datas = new ArrayList<MemeberInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_list);
        ButterKnife.bind(this);
        initView();
        addLisener();

    }

    private void initView() {
        tvTittle.setText(getString(R.string.number_list));
        adapter = new MemberListAdapter(this, datas);
        listView.setAdapter(adapter);
        crousrID = getIntent().getStringExtra(Constants.PASS_STRING);
        loadData();

    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void loadData() {
        if (page == 1) {
            mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        }

        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> data = new HashMap<>();
        data.put("courseId", crousrID);
        final PostRequest request = new PostRequest(Constants.COURSE_MEMBERLIST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                List<MemeberInfo> memeberInfos = JsonUtils.listFromJson(response.getAsJsonArray("list"), MemeberInfo.class);
                if (memeberInfos != null && memeberInfos.size() > 0) {
                    datas.addAll(memeberInfos);
                    adapter.setData(datas);
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                } else {
                    listView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                listView.setVisibility(View.GONE);
                noData.setVisibility(View.VISIBLE);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(MembersListActivity.this);
        mQueue.add(request);
    }
}
