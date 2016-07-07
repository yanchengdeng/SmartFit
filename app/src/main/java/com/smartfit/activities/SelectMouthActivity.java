package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.adpters.SelectMouthAdapter;
import com.smartfit.beans.SelectMouth;
import com.smartfit.commons.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectMouthActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.listview)
    ListView listview;

    private List<SelectMouth> selectMouthList = new ArrayList<>();
    private SelectMouthAdapter adapter;

    private int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_mouth);
        ButterKnife.bind(this);
        initView();

    }

    private void initView() {
        tvTittle.setText("选择购买数量");
        tvFunction.setVisibility(View.VISIBLE);
        tvFunction.setText(getString(R.string.sure));
        for (int i = 1; i < 13; i++) {
            if (i == 1) {
                selectMouthList.add(new SelectMouth(i, true));
            } else {
                selectMouthList.add(new SelectMouth(i, false));
            }
        }
        adapter = new SelectMouthAdapter(this, selectMouthList);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (SelectMouth item : selectMouthList) {
                    item.setIsSelect(false);
                }
                selected = position;
                selectMouthList.get(position).setIsSelect(true);
                adapter.setData(selectMouthList);
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_function})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_function:
                    Intent intent = new Intent(SelectMouthActivity.this, EventActivityNewVersionActivity.class);
                    intent.putExtra(Constants.PASS_STRING, selected+1);
                    setResult(RESULT_OK, intent);
                    finish();
                break;
        }
    }
}
