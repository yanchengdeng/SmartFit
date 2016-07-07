package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.smartfit.R;
import com.smartfit.adpters.TicketSelectToBuyAdapter;
import com.smartfit.beans.UseableEventInfo;
import com.smartfit.commons.Constants;
import com.smartfit.views.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 选择优惠劵去购买
 *
 * @author dengyancheng
 */
public class SelectTicketToBuyActivity extends BaseActivity {

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


    private TicketSelectToBuyAdapter adapter;
    private List<UseableEventInfo> datas = new ArrayList<UseableEventInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_gift_share);
        ButterKnife.bind(this);
        intView();
        addLisener();
    }

    private void intView() {
        tvTittle.setText(getString(R.string.tick_gift));
        datas = getIntent().getParcelableArrayListExtra(Constants.PASS_OBJECT);
        adapter = new TicketSelectToBuyAdapter(this, datas);
        listView.setAdapter(adapter);
        tvFunction.setText("确定");
        tvFunction.setVisibility(View.VISIBLE);
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
                if (datas.size() > 0) {
                    if (countSelectNum(datas).size() > 0) {
                        ArrayList<UseableEventInfo> ticketInfos = countSelectNum(datas);
                        Intent intent = new Intent(SelectTicketToBuyActivity.this,ConfirmPayActivity.class);
                        intent.putParcelableArrayListExtra(Constants.PASS_OBJECT,ticketInfos);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        mSVProgressHUD.showInfoWithStatus("未选择券包", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.ch_select);
                checkBox.setChecked(!checkBox.isChecked());
                datas.get(position).setIsCheck(checkBox.isChecked());
            }
        });

    }

    /**
     * 计算已经选择券
     *
     * @param datas
     */
    private ArrayList<UseableEventInfo> countSelectNum(List<UseableEventInfo> datas) {
        ArrayList<UseableEventInfo> selectPricates = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isCheck() == true) {
                selectPricates.add(datas.get(i));
            }
        }
        return selectPricates;
    }


}
