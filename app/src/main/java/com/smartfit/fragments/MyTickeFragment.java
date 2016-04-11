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
import com.smartfit.commons.Constants;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/11.
 */
public class MyTickeFragment extends Fragment {

    @Bind(R.id.no_data)
    TextView noData;
    @Bind(R.id.listView)
    ListView listView;


    private TicketGiftAdapter adapter;
    private List<String> datas = new ArrayList<String>();

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
        PostRequest request = new PostRequest("", new Response.Listener<JsonObject>() {
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
