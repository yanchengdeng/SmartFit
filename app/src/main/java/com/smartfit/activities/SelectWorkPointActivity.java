package com.smartfit.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.smartfit.R;
import com.smartfit.adpters.SelectWorkPointAdapter;
import com.smartfit.beans.WorkPointAddress;
import com.smartfit.views.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author yanchengdneg
 *         选择 工作地点
 */
public class SelectWorkPointActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.iv_search)
    ImageView ivSearch;
    @Bind(R.id.et_search_content)
    EditText etSearchContent;
    @Bind(R.id.no_data)
    TextView noData;
    @Bind(R.id.listView)
    LoadMoreListView listView;

    private List<WorkPointAddress> datas = new ArrayList<>();

    private SelectWorkPointAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_work_point);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.work_address));
        ivFunction.setVisibility(View.VISIBLE);
        ivFunction.setImageResource(R.mipmap.icon_right);
        adapter = new SelectWorkPointAdapter(this,datas);
        listView.setAdapter(adapter);
        loadData();
    }

    private void loadData() {
        for(int i = 0 ;i<3;i++){
            datas.add(new WorkPointAddress());
        }
        adapter.setData(datas);
        listView.setVisibility(View.VISIBLE);

    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        ivFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSVProgressHUD.showSuccessWithStatus(getString(R.string.setting_success), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.ch_select);
                checkBox.setChecked(!checkBox.isChecked());
                datas.get(position).setIsCheck(checkBox.isChecked());
                adapter.setData(datas);
            }
        });

        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {

            }
        });
    }
}
