package com.smartfit.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.smartfit.R;
import com.smartfit.activities.MyClassesActivity;
import com.smartfit.adpters.CoachClassGoingStatusAdapter;
import com.smartfit.adpters.MyClassOrderStatusAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 *
 *教练正在进行的课程
 */
public class CoachGoingClassesFragment extends Fragment {


    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private CoachClassGoingStatusAdapter adapter;
    private List<String> datas = new ArrayList<>();
    private View footerView;
    private int page = 1;
    boolean isLoading = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_add_classes, null);
        ButterKnife.bind(this, view);
        intData();
        return view;
    }

    private void intData() {
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_loader_footer, null);
//        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footerView.setLayoutParams(lp);
        listView.addFooterView(footerView);
        adapter = new CoachClassGoingStatusAdapter(getActivity(), datas,true);
        listView.setAdapter(adapter);

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
                        ((MyClassesActivity) getActivity()).mSVProgressHUD.showSuccessWithStatus(getString(R.string.update_already), SVProgressHUD.SVProgressHUDMaskType.Black);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
