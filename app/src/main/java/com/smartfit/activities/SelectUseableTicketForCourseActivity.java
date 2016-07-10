package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.smartfit.R;
import com.smartfit.adpters.TicketSelectToBuyCourseAdapter;
import com.smartfit.beans.EventUserful;
import com.smartfit.commons.Constants;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 选择优惠劵    供支付课程费用
 */
public class SelectUseableTicketForCourseActivity extends BaseActivity {

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

    private ArrayList<EventUserful> userfulEventes;

    private TicketSelectToBuyCourseAdapter adapter;

    private int selectPosion = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_useable_ticket_for_course);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.tick_gift));
        tvFunction.setVisibility(View.VISIBLE);
        tvFunction.setText(getString(R.string.sure));

        userfulEventes = getIntent().getParcelableArrayListExtra(Constants.PASS_OBJECT);
        adapter = new TicketSelectToBuyCourseAdapter(this, userfulEventes);
        listView.setAdapter(adapter);
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPosion == -1) {
                    mSVProgressHUD.showInfoWithStatus("未选择优惠劵", SVProgressHUD.SVProgressHUDMaskType.Clear);
                } else {
                    Intent intent = new Intent(SelectUseableTicketForCourseActivity.this, ConfirmOrderCourseActivity.class);
                    intent.putExtra(Constants.PASS_STRING, userfulEventes.get(selectPosion).getId());
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (EventUserful item : userfulEventes) {
                    item.setIsCheck(false);
                }
                selectPosion = position;

                userfulEventes.get(position).setIsCheck(true);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
