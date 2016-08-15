package com.smartfit.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.smartfit.R;
import com.smartfit.adpters.CoachAppraiseAdapter;
import com.smartfit.adpters.GroupExpericeItemAdapter;
import com.smartfit.beans.ClassCommend;
import com.smartfit.beans.CommentInfo;
import com.smartfit.views.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/***
 * 教练评价
 */
public class CoachAppraiseActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.listView)
    LoadMoreListView listView;

    private int page = 1;
    private CoachAppraiseAdapter adapter;
    private List<ClassCommend> datas = new ArrayList<ClassCommend>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_appraise);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText("教练评价");
        adapter = new CoachAppraiseAdapter(this, datas);
        listView.setAdapter(adapter);

    }

    private void addLisener(){
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 加载更多
         */

        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                loadData("");
            }
        });


    }

    private void loadData(String contions) {

        adapter.setData(datas);

    }

}
