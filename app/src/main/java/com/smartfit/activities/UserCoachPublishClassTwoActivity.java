package com.smartfit.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.adpters.CustomClassVenueAdapter;
import com.smartfit.beans.CustomClassVenue;
import com.smartfit.beans.CustomClassVenueItem;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
*教练发布课程第二步
*@author yanchengdeng
*create at 2016/5/3 11:57
*
*/
public class UserCoachPublishClassTwoActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.rl_select_time)
    RelativeLayout rlSelectTime;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.no_data)
    TextView noData;

    private static final int REQUEST_CODE_ORDER_TIME = 0x0011;
    private String classId;

    private String startTime, endTime;

    List<CustomClassVenue> customClassVenues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_coach_publish_class_two);
        ButterKnife.bind(this);
        classId = getIntent().getStringExtra(Constants.PASS_STRING);
        intView();

        addLisener();

    }

    private void intView() {
        tvTittle.setText("自订课程(2/3)");
        ivFunction.setVisibility(View.VISIBLE);
        ivFunction.setImageResource(R.mipmap.icon_next_w);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ORDER_TIME && resultCode == OrderReserveActivity.SELECT_VALUE_OVER) {
            if (!TextUtils.isEmpty(data.getExtras().getString("time_before")) && !TextUtils.isEmpty(data.getExtras().getString("time_after"))) {
                tvTime.setText(data.getExtras().getString("time_before") + " - " + data.getExtras().getString("time_after"));
                Calendar calendar = Calendar.getInstance();

                String selectDate = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                startTime = selectDate + " " + data.getExtras().getString("time_before");
                endTime = selectDate + " " + data.getExtras().getString("time_after");

                loadData();
            }

        }
    }

    private void loadData() {

        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        data.put("startTime", String.valueOf(DateUtils.getTheDateTimeMillions(startTime)));
        data.put("endTime", String.valueOf(DateUtils.getTheDateTimeMillions(endTime)));
        data.put("classroomType", "2");
        data.put("courseTypeCode", classId);
        data.put("cityCode", SharedPreferencesUtils.getInstance().getString(Constants.CITY_CODE, ""));

        PostRequest request = new PostRequest(Constants.CLASSIF_GETIDLECLASSROOMSVENUELIST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();

                customClassVenues
                        = JsonUtils.listFromJson(response.getAsJsonArray("list"), CustomClassVenue.class);
                if (customClassVenues != null && customClassVenues.size() > 0) {
                    if (null != customClassVenues.get(0).getClassroomList() && customClassVenues.get(0).getClassroomList().get(0) != null && customClassVenues.get(0).getClassroomList().size() > 0) {
                        customClassVenues.get(0).getClassroomList().get(0).setIsCheck(true);
                    }
                    listView.setAdapter(new CustomClassVenueAdapter(UserCoachPublishClassTwoActivity.this, customClassVenues));
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

        rlSelectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserCoachPublishClassTwoActivity.this, OrderReserveActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ORDER_TIME);
            }
        });

        ivFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != customClassVenues && customClassVenues.size() > 0) {
                    int seletcPosition = 0;
                    String venuePrice ="0";
                    String roomId= "0";
                    for (int i = 0; i < customClassVenues.size(); i++) {
                        for (CustomClassVenueItem subItem : customClassVenues.get(i).getClassroomList()) {
                            if (subItem.isCheck()) {
                                seletcPosition = i;
                                venuePrice = subItem.getClassroomPrice();
                                roomId = subItem.getClassroomId();
                            }
                        }
                    }

                    Bundle bundle = new Bundle();
                    bundle.putString("startTime", String.valueOf(DateUtils.getTheDateTimeMillions(startTime)));
                    bundle.putString("endTime", String.valueOf(DateUtils.getTheDateTimeMillions(endTime)));
                    bundle.putString("courseClassId", classId);
                    bundle.putString("venueId", customClassVenues.get(seletcPosition).getVenueId());
                    bundle.putString("roomId", roomId);
                    bundle.putString("venuePrice",venuePrice);
                    openActivity(UserCoachPublishClassThreeActivity.class, bundle);
                } else {
                    mSVProgressHUD.showInfoWithStatus("未选择教室", SVProgressHUD.SVProgressHUDMaskType.Clear);
                }
            }
        });
    }
}
