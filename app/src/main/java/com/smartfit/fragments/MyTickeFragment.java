package com.smartfit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.MessageEvent.BindCard;
import com.smartfit.R;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.MyTicketGiftActivity;
import com.smartfit.adpters.TicketGiftAdapter;
import com.smartfit.beans.TicketInfo;
import com.smartfit.beans.TicketListInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.LoadMoreListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/4/11.
 */
public class MyTickeFragment extends BaseFragment {

    @Bind(R.id.no_data)
    TextView noData;
    @Bind(R.id.listView)
    LoadMoreListView listView;
    private int page = 1;


    private TicketGiftAdapter adapter;
    private List<TicketInfo> datas = new ArrayList<TicketInfo>();

    private String type;

    private boolean isLoaded;//是否加载完毕
    private boolean isPre;//是否初始化好

    private EventBus eventBus;

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
        View view = inflater.inflate(R.layout.fragment_my_ticket_gift, null);
        ButterKnife.bind(this, view);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        isPre = true;
        intView();
        lazyLoad();
        return view;
    }

    @Subscribe
    public void onEvent(Object event) {
        if (event instanceof BindCard) {
            datas.clear();
            isPre = true;
            isLoaded = false;//是否加载完毕
            lazyLoad();
        }
    }

    private void intView() {
        adapter = new TicketGiftAdapter(getActivity(), datas);
        listView.setAdapter(adapter);
    }

    private void loadData() {

        ((BaseActivity) getActivity()).mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> maps = new HashMap<>();
        maps.put("pageNo", String.valueOf(page));
        maps.put("pageSize", "100");
        maps.put("type", type);
        PostRequest request = new PostRequest(Constants.EVENT_LISTUSEREVENT, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                isLoaded = true;
                TicketListInfo ticketInfos = JsonUtils.objectFromJson(response, TicketListInfo.class);
                if (ticketInfos.getListData() != null && ticketInfos.getListData().size() > 0) {
                    if (type.equals("1")) {
                        ((MyTicketGiftActivity) getActivity()).tvFunction.setVisibility(View.VISIBLE);
                    }
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
                isLoaded = true;
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


    @Override
    protected void lazyLoad() {
        if (!isLoaded && isVisible && isPre) {
            loadData();
        }

    }
}
