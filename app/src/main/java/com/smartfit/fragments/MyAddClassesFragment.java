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

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.MyClassesActivity;
import com.smartfit.adpters.MyClassOrderStatusAdapter;
import com.smartfit.beans.UserInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.views.LoadMoreListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我加入的课程
 */
public class MyAddClassesFragment extends Fragment {


    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private MyClassOrderStatusAdapter adapter;
    private List<String> datas = new ArrayList<>();

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
        adapter = new MyClassOrderStatusAdapter(getActivity(), datas, true);
        listView.setAdapter(adapter);
        loadData();

        /***
         * 下拉刷新
         */
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        ((MyClassesActivity) getActivity()).mSVProgressHUD.showSuccessWithStatus(getString(R.string.update_already), SVProgressHUD.SVProgressHUDMaskType.Clear);
                    }
                }, 3000);
            }
        });


    }

    private void loadData() {
        ((BaseActivity) getActivity()).mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);

        Map<String, String> datas = new HashMap<>();
        datas.put("uid", SharedPreferencesUtils.getInstance().getString(Constants.UID, ""));
        datas.put("showType", "0");
        PostRequest request = new PostRequest(Constants.USER_CONTACTCOURSELIST, datas, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) getActivity()).mSVProgressHUD.showErrorWithStatus(error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(getActivity());
        ((BaseActivity) getActivity()).mQueue.add(request);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
