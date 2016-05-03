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
import com.smartfit.adpters.AerobincnAppratusItemAdapter;
import com.smartfit.adpters.FindSubstitutAdapter;
import com.smartfit.adpters.MemberListAdapter;
import com.smartfit.beans.MyAddClass;
import com.smartfit.beans.SustituteCoach;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
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

    private MyAddClass addClass;

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
        addClass = (MyAddClass) getIntent().getSerializableExtra(Constants.PASS_OBJECT);
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
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        final String requestHost;
        //自订课程获取空闲教练
        if (addClass.getCourseType().equals("1")) {
            requestHost = Constants.COACH_LISTIDLECOACHESBYVENUEIDANDCOURSETYPECODE;
            data.put("startTime", addClass.getStartTime());
            data.put("venueId", addClass.getVenueId());
            data.put("endTime", addClass.getEndTime());
            //TODO
            data.put("courseTypeCode", "11");
        } else {
            //私教课下 获取空闲教练
            requestHost = Constants.COACH_LISTIDLECOACHESBYVENUEID;
            data.put("sex", "");
            data.put("startPrice", "");
            data.put("endPrice", "");
            data.put("startTime", addClass.getStartTime());
            data.put("venueId", addClass.getVenueId());
            data.put("endTime", addClass.getEndTime());
            data.put("courseType", addClass.getCourseType());
        }
        PostRequest request = new PostRequest(requestHost, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                List<SustituteCoach> sustituteCoaches = JsonUtils.listFromJson(response.getAsJsonArray("list"), SustituteCoach.class);
                if (sustituteCoaches != null && sustituteCoaches.size() > 0) {
                    listView.setAdapter(new FindSubstitutAdapter(FindSubstitueActivity.this, sustituteCoaches,addClass.getId()));
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                } else {
                    noData.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
                noData.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(FindSubstitueActivity.this);
        mQueue.add(request);
    }

}
