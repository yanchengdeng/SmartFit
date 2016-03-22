package com.smartfit.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.smartfit.R;
import com.smartfit.adpters.FansAdapter;
import com.smartfit.views.LoadMoreListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/***
 * 粉丝  界面
 */
public class FansActivity extends BaseActivity {

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
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.no_data)
    TextView noData;

    private int page = 1;
    private FansAdapter adapter;
    private List<String> datas = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.fans));
        adapter = new FansAdapter(this, datas);
        listView.setAdapter(adapter);
        loadData();
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /***
         * 下拉刷新
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        page = 1;
                        swipeRefreshLayout.setRefreshing(false);
                        mSVProgressHUD.showSuccessWithStatus(getString(R.string.update_already), SVProgressHUD.SVProgressHUDMaskType.Clear);
                    }
                }, 3000);
            }
        });


        /**
         * 加载更多
         */
        listView.setOnLoadMoreListener(new LoadMoreListView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                page++;
                loadData();
            }
        });


    }


    private void loadData() {

        if (page == 1) {
            mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    datas.add("模拟数据" + i + String.valueOf(page));
                }
                listView.setVisibility(View.VISIBLE);
                listView.onLoadMoreComplete();
                adapter.setData(datas);
                mSVProgressHUD.dismiss();
            }
        }, 2000);
    }
}
