package com.smartfit.activities;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.MessageEvent.UpdateWeekDay;
import com.smartfit.MessageEvent.UpdateWeekList;
import com.smartfit.MessageEvent.UpdateWorkPointInfo;
import com.smartfit.R;
import com.smartfit.beans.WorkPoint;
import com.smartfit.beans.WorkPointAddress;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.WheelView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @yanchengdeng 编辑工作地点
 */
public class WorkPlaceEditActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.tv_work_date_tips)
    TextView tvWorkDateTips;
    @Bind(R.id.tv_work_date)
    TextView tvWorkDate;
    @Bind(R.id.wv_hour_start)
    WheelView wvHourStart;
    @Bind(R.id.wv_min_start)
    WheelView wvMinStart;
    @Bind(R.id.wv_hour_end)
    WheelView wvHourEnd;
    @Bind(R.id.wv_min_end)
    WheelView wvMinEnd;

    private static final String[] HOURS = new String[]{"00", "01", "02", "03", "04", "05", "06", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19"
            , "20", "21", "22", "23"};

    private static final String[] MINETS = new String[]{"00", "15", "30", "45"};
    @Bind(R.id.tv_work_point)
    TextView tvWorkPoint;

    private String startHour = "00";
    private String startMinu = "00";


    private String endHour = "00";
    private String endMinu = "00";

    private EventBus eventBus;

    private String selectStartHour = "00", selectStartMin = "00", selectEndHour = "00", selectEndMin = "00";

    private UpdateWorkPointInfo updateWeekDayTime;


    private WorkPoint workPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_place_edit);
        eventBus = EventBus.getDefault();
        workPoint = getIntent().getParcelableExtra(Constants.PASS_OBJECT);
        updateWeekDayTime = new UpdateWorkPointInfo();
        eventBus.register(this);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.work_eidt));
        ivFunction.setVisibility(View.VISIBLE);
        ivFunction.setImageResource(R.mipmap.icon_right);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour < 10) {
            startHour = "0" + hour;
        } else {
            startHour = String.valueOf(hour);
        }
        selectStartHour = startHour;
        selectEndHour = startHour;

        if (!TextUtils.isEmpty(workPoint.getWorkspaceCode())) {
            tvWorkPoint.setText(workPoint.getVenueName());
            updateWeekDayTime.setWorkPoint(workPoint.getVenueId());
            String[] startTime = DateUtils.getDataTime(workPoint.getStartTime().trim()).split(":");
            startHour = startTime[0].trim();
            startMinu = startTime[1].trim();
            String[] endTime = DateUtils.getDataTime(workPoint.getEndTime().trim()).split(":");
            endHour = endTime[0].trim();
            endMinu = endTime[1].trim();
            selectStartHour = startHour;
            selectStartMin = startMinu;
            selectEndHour = endHour;
            selectEndMin = endMinu;
            updateWeekDayTime.setStartTime(DateUtils.getDataTime(workPoint.getStartTime()));
            updateWeekDayTime.setEndTime(DateUtils.getDataTime(workPoint.getEndTime()));
            StringBuilder sbId = new StringBuilder();
            StringBuilder sbNames = new StringBuilder();
            for (String item : workPoint.getDaysOfWeek()) {
                sbId.append(item).append("|");
                if (item.equals("7")) {
                    sbNames.append("周日").append("、");
                } else if (item.equals("6")) {
                    sbNames.append("周六").append("、");
                } else if (item.equals("5")) {
                    sbNames.append("周五").append("、");
                } else if (item.equals("4")) {
                    sbNames.append("周四").append("、");
                } else if (item.equals("3")) {
                    sbNames.append("周三").append("、");
                } else if (item.equals("2")) {
                    sbNames.append("周二").append("、");
                } else if (item.equals("1")) {
                    sbNames.append("周一").append("、");
                }
            }
            updateWeekDayTime.setNames(sbNames.toString());
            tvWorkDate.setText(sbNames.toString());
            updateWeekDayTime.setIds(sbId.toString());
        }

        int hourPos = 0;
        int minPos = 0;

        int endHourPos = 0;
        int endMinPos = 0;
        for (int i = 0; i < HOURS.length; i++) {
            if (HOURS[i].equals(startHour)) {
                hourPos = i;
            }
        }


        for (int i = 0; i < MINETS.length; i++) {
            if (MINETS[i].equals(startMinu)) {
                minPos = i;
            }
        }


        for (int i = 0; i < HOURS.length; i++) {
            if (HOURS[i].equals(endHour)) {
                endHourPos = i;
            }
        }


        for (int i = 0; i < MINETS.length; i++) {
            if (MINETS[i].equals(endMinu)) {
                endMinPos = i;
            }
        }
        //起始时间设置
        wvHourStart.setOffset(1);
        wvHourStart.setItems(Arrays.asList(HOURS));
        wvHourStart.setSeletion(hourPos);
        wvMinStart.setOffset(1);
        wvMinStart.setItems(Arrays.asList(MINETS));
        wvMinStart.setSeletion(minPos);
        //结束时间设置
        wvHourEnd.setOffset(1);
        wvHourEnd.setItems(Arrays.asList(HOURS));
        wvHourEnd.setSeletion(endHourPos);
        wvMinEnd.setOffset(1);
        wvMinEnd.setItems(Arrays.asList(MINETS));
        wvMinEnd.setSeletion(endMinPos);

    }

    @Subscribe
    public void onEvent(Object event) {

        if (event instanceof UpdateWeekDay) {
            tvWorkDate.setText(((UpdateWeekDay) event).getNames());
            updateWeekDayTime.setNames(((UpdateWeekDay) event).getNames());
            updateWeekDayTime.setIds(((UpdateWeekDay) event).getIds());
        }

        if (event instanceof WorkPointAddress) {
            updateWeekDayTime.setWorkName(((WorkPointAddress) event).getVenueName());
            updateWeekDayTime.setWorkPoint(((WorkPointAddress) event).getVenueId());
            tvWorkPoint.setText(((WorkPointAddress) event).getVenueName());
        }
    }


    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        wvHourStart.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
//                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                selectStartHour = item;
            }
        });

        wvMinStart.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
//                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                selectStartMin = item;
            }
        });

        wvHourEnd.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
//                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                selectEndHour = item;
            }
        });

        wvMinEnd.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
//                Log.d(TAG, "selectedIndex: " + selectedIndex + ", item: " + item);
                selectEndMin = item;
            }
        });


        tvWorkPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SelectWorkPointActivity.class);
            }
        });

        tvWorkDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(SelectWorkWeekDayActivity.class);
            }
        });


        ivFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tvWorkDate.getText().equals(getString(R.string.click_setting))) {
                    mSVProgressHUD.showInfoWithStatus(("请选择周期"), SVProgressHUD.SVProgressHUDMaskType.Clear);
                } else {
                    if (TextUtils.isEmpty(updateWeekDayTime.getWorkPoint())) {
                        mSVProgressHUD.showInfoWithStatus("请选择工作地点", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    } else {
                        if (DateUtils.isThanOneHour(selectStartHour + ":" + selectStartMin, selectEndHour + ":" + selectEndMin)) {
                            updateWeekDayTime.setStartTime(selectStartHour + ":" + selectStartMin);
                            updateWeekDayTime.setEndTime(selectEndHour + ":" + selectEndMin);
                            addWorkPlace();
                        } else {
                            mSVProgressHUD.showInfoWithStatus("时间不低于一个小时", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        }
                    }
                }
            }
        });
    }

    private void addWorkPlace() {

        Date dateTime = new Date(System.currentTimeMillis());
        String today = DateUtils.DateToString(dateTime, "yyyy-MM-dd ");
        Map<String, String> datas = new HashMap<>();
        if (!TextUtils.isEmpty(workPoint.getWorkspaceCode())) {
            datas.put("workspaceCode", workPoint.getWorkspaceCode());
        }
        datas.put("venueId", updateWeekDayTime.getWorkPoint());
        datas.put("startTime", String.valueOf(DateUtils.getTheDateTimeMillions(today + updateWeekDayTime.getStartTime())));
        datas.put("endTime", String.valueOf(DateUtils.getTheDateTimeMillions(today + updateWeekDayTime.getEndTime())));
        datas.put("daysOfWeekString", updateWeekDayTime.getIds());

        PostRequest request = new PostRequest(Constants.WORKSPACE_ADD, datas, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                if (!TextUtils.isEmpty(workPoint.getWorkspaceCode())) {
                    mSVProgressHUD.showSuccessWithStatus("已更改", SVProgressHUD.SVProgressHUDMaskType.Clear);
                } else {
                    mSVProgressHUD.showSuccessWithStatus("已添加", SVProgressHUD.SVProgressHUDMaskType.Clear);
                }
                mSVProgressHUD.dismiss();
                eventBus.post(new UpdateWeekList());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1500);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(WorkPlaceEditActivity.this);
        mQueue.add(request);
    }
}
