package com.smartfit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.adpters.TicketGiftAdapter;
import com.smartfit.beans.TicketInfo;
import com.smartfit.beans.TicketListInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.LoadMoreListView;
import com.umeng.socialize.PlatformConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/11.
 */
public class MyTickeFragment extends Fragment {

    @Bind(R.id.no_data)
    TextView noData;
    @Bind(R.id.listView)
    LoadMoreListView listView;
    private int page = 1;


    private TicketGiftAdapter adapter;
    private List<TicketInfo> datas = new ArrayList<TicketInfo>();

    private String type;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            type = getArguments().getString("type");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null)
            type = getArguments().getString("type");
        View view = inflater.inflate(R.layout.activity_my_ticket_gift, null);
        ButterKnife.bind(this, view);
        intView();
        return view;
    }


    private void intView() {
        adapter = new TicketGiftAdapter(getActivity(), datas);
        listView.setAdapter(adapter);
        loadData();
    }

    private void loadData() {

//        ((BaseActivity)getActivity()).mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> maps = new HashMap<>();
        maps.put("pageNo", String.valueOf(page));
        maps.put("pageSize", "20");
        maps.put("type", type);
        PostRequest request = new PostRequest(Constants.EVENT_LISTUSEREVENT, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                TicketListInfo ticketInfos = JsonUtils.objectFromJson(response, TicketListInfo.class);
                if (ticketInfos.getListData() != null && ticketInfos.getListData().size() > 0) {
                    datas.addAll(ticketInfos.getListData());
                    adapter.setData(datas);
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                } else {
                    if (datas.size() > 0) {
                        listView.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                    } else {
                        listView.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                }
                listView.onLoadMoreComplete();
                ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
                if (datas.size() > 0) {
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);
                } else {
                    listView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
                listView.onLoadMoreComplete();
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
