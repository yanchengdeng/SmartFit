package com.smartfit.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.smartfit.R;
import com.smartfit.adpters.FansAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 关注列表
 */
public class AttentionListActivity extends BaseActivity {

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
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.et_search_content)
    EditText etSearchContent;


    private View footerView;
    private int page = 1;
    boolean isLoading = false;
    private FansAdapter adapter;
    private List<String> datas = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attention_list);
        ButterKnife.bind(this);
        initView();
        addLisener();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.attention));
        footerView = LayoutInflater.from(this).inflate(R.layout.list_loader_footer, null);
//        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footerView.setLayoutParams(lp);
        listView.addFooterView(footerView);
        adapter = new FansAdapter(this, datas);
        listView.setAdapter(adapter);
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
                        mSVProgressHUD.showSuccessWithStatus(getString(R.string.update_already), SVProgressHUD.SVProgressHUDMaskType.Black);
                    }
                }, 3000);
            }
        });


        /**
         * 加载更多
         */

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastIndexInScreen = visibleItemCount + firstVisibleItem;
                if (lastIndexInScreen >= totalItemCount - 1 && !isLoading) {
                    isLoading = true;
                    page++;
                    loadData();
                }
            }
        });

    }


    private void loadData() {
        for (int i = 0; i < 10; i++) {
            datas.add("模拟数据" + i + String.valueOf(page));
        }
        adapter.setData(datas);
        listView.removeFooterView(footerView);
        isLoading = false;
    }
}
