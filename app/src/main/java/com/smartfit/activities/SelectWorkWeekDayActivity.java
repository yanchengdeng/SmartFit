package com.smartfit.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.smartfit.MessageEvent.UpdateWeekDay;
import com.smartfit.R;
import com.smartfit.adpters.SelectWorkDateAdapter;
import com.smartfit.beans.WorkDay;
import com.smartfit.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 选择工作日期
 */
public class SelectWorkWeekDayActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.listView)
    ListView listView;

    private EventBus eventBus;

    private List<WorkDay> workDayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_work_week_day);
        eventBus = EventBus.getDefault();
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        workDayList.add(new WorkDay("1", "周一"));
        workDayList.add(new WorkDay("2", "周二"));
        workDayList.add(new WorkDay("3", "周三"));
        workDayList.add(new WorkDay("4", "周四"));
        workDayList.add(new WorkDay("5", "周五"));
        workDayList.add(new WorkDay("6", "周六"));
        workDayList.add(new WorkDay("7", "周日"));
        tvTittle.setText(getString(R.string.select_work_date));
        ivFunction.setVisibility(View.VISIBLE);
        ivFunction.setImageResource(R.mipmap.icon_right);
        listView.setAdapter(new SelectWorkDateAdapter(this, workDayList));

    }

    private void addLisener() {

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.ch_select);
                checkBox.setChecked(!checkBox.isChecked());
                workDayList.get(position).setIsChecked(checkBox.isChecked());
            }
        });

        ivFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                StringBuilder names = new StringBuilder();
                for (WorkDay item : workDayList) {
                    if (item.isChecked()) {
                        sb.append(item.getId()).append("|");
                        names.append(item.getName()).append("、");
                    }
                }
                UpdateWeekDay updateWeekDay = new UpdateWeekDay();
                updateWeekDay.setIds(sb.toString());
                updateWeekDay.setNames(names.toString());
                eventBus.post(updateWeekDay);
                finish();
            }
        });

    }
}
