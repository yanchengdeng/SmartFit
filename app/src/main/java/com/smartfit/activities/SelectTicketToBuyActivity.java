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
import com.smartfit.utils.LogUtil;
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


    private int maxSelectNum;//最多可选择 优惠券数


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
        maxSelectNum = getIntent().getIntExtra(Constants.PASS_STRING, 1);
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
                        Intent intent = new Intent(SelectTicketToBuyActivity.this, ConfirmPayActivity.class);
                        intent.putParcelableArrayListExtra(Constants.PASS_OBJECT, ticketInfos);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        ArrayList<UseableEventInfo> ticketInfos = new ArrayList<UseableEventInfo>();
                        Intent intent = new Intent(SelectTicketToBuyActivity.this, ConfirmPayActivity.class);
                        intent.putParcelableArrayListExtra(Constants.PASS_OBJECT, ticketInfos);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (countSelectNumMouth(datas) >= maxSelectNum) {
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.ch_select);
                    if (checkBox.isChecked()) {
                        checkBox.setChecked(!checkBox.isChecked());
                        datas.get(position).setIsCheck(checkBox.isChecked());
                    } else {
                        mSVProgressHUD.showInfoWithStatus(String.format("已选择%d张", maxSelectNum), SVProgressHUD.SVProgressHUDMaskType.Clear);
                    }
                } else {
                    CheckBox cashBox = (CheckBox) view.findViewById(R.id.ch_select);
                    if (!checkHasContainCashTicket(countSelectNum(datas))) {
                        cashBox.setChecked(!cashBox.isChecked());
                        datas.get(position).setIsCheck(cashBox.isChecked());
                    } else {
                        if (datas.get(position).getEventType().equals("21")) {
                            mSVProgressHUD.showInfoWithStatus("已选过现金券", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        } else {
                            cashBox.setChecked(!cashBox.isChecked());
                            datas.get(position).setIsCheck(cashBox.isChecked());
                        }
                    }
                }
            }
        });
    }

    /**
     * 是否包含  现金券
     *
     * @param useableEventInfos
     * @return 未包含 false 已包含  true
     */
    private boolean checkHasContainCashTicket(ArrayList<UseableEventInfo> useableEventInfos) {
        boolean isContain = false;
        if (useableEventInfos == null) {
            return false;
        } else {
            for (UseableEventInfo item : useableEventInfos) {
                if (item.getEventType().equals("21")) {
                    isContain = true;
                }
            }
            return isContain;
        }
    }

    /**
     * 计算已经选择券所代表的月数
     *
     * @param datas
     */
    private int countSelectNumMouth(List<UseableEventInfo> datas) {
        int selectMouth = 0;
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isCheck() == true) {
                if (datas.get(i).getEventType().equals("3")) {
                    selectMouth = selectMouth + 1;
                }else if(datas.get(i).getEventType().equals("11")){
                    selectMouth = selectMouth + 1;
                }else if(datas.get(i).getEventType().equals("12")){
                    selectMouth = selectMouth + 3;
                }else if(datas.get(i).getEventType().equals("13")){
                    selectMouth = selectMouth + 12;
                }else if(datas.get(i).getEventType().equals("14")){
                    selectMouth = selectMouth + 1;
                } else if (datas.get(i).getEventType().equals("15")) {
                    selectMouth = selectMouth + 3;
                } else if (datas.get(i).getEventType().equals("16")) {
                    selectMouth = selectMouth + 12;
                } else if (datas.get(i).getEventType().equals("21")) {
                    selectMouth = selectMouth + 1;
                }
            }
        }
        LogUtil.w("dyc", "---" + selectMouth);
        return selectMouth;
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
