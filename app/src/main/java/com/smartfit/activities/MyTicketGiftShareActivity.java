package com.smartfit.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.MessageEvent.ShareTicketSuccess;
import com.smartfit.R;
import com.smartfit.adpters.TicketGiftShareAdapter;
import com.smartfit.beans.TicketInfo;
import com.smartfit.beans.TicketListInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.LoadMoreListView;
import com.smartfit.wxapi.WXEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 确定卷分享  列表
 *
 * @author dengyancheng
 */
public class MyTicketGiftShareActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.no_data)
    TextView noData;
    @Bind(R.id.listView)
    LoadMoreListView listView;

    private int page = 1;

    private EventBus eventBus;

    private TicketGiftShareAdapter adapter;
    private List<TicketInfo> datas = new ArrayList<TicketInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_gift_share);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        intView();
        getData();
        addLisener();
    }

    @Subscribe
    public void onEvent(Object event) {
        if (event instanceof ShareTicketSuccess) {
            finish();
        }
    }


    private void intView() {
        tvTittle.setText(getString(R.string.tick_gift));
        adapter = new TicketGiftShareAdapter(this, datas);
        listView.setAdapter(adapter);
        tvFunction.setText("确定分享");
    }

    private void getData() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> maps = new HashMap<>();
        maps.put("pageNo", String.valueOf(page));
        maps.put("pageSize", "100");
        maps.put("type", "1");
        PostRequest request = new PostRequest(Constants.EVENT_LISTUSEREVENT, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                TicketListInfo ticketInfos = JsonUtils.objectFromJson(response, TicketListInfo.class);
                if (ticketInfos.getListData() != null && ticketInfos.getListData().size() > 0) {
                    for (TicketInfo item : ticketInfos.getListData()) {
                        if (item.getEventType().equals("21")) {
                            datas.addAll(ticketInfos.getListData());
                        }

                    }
                    if (datas.size() > 0) {
                        tvFunction.setVisibility(View.VISIBLE);
                        adapter.setData(datas);
                        listView.setVisibility(View.VISIBLE);
                        noData.setVisibility(View.GONE);
                    } else {
                        listView.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
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
                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
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
        request.headers = NetUtil.getRequestBody(MyTicketGiftShareActivity.this);
        mQueue.add(request);
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datas.size() > 0) {
                    if (countSelectNum(datas).size() > 0) {
                        Bundle bundle = new Bundle();
                        ArrayList<TicketInfo> ticketInfos = countSelectNum(datas);
                        bundle.putParcelableArrayList(Constants.PASS_OBJECT, ticketInfos);
                        openActivity(WXEntryActivity.class, bundle);
                    } else {
                        mSVProgressHUD.showInfoWithStatus("未选择券包", SVProgressHUD.SVProgressHUDMaskType.Clear);
                    }
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.ch_select);
                checkBox.setChecked(!checkBox.isChecked());
                datas.get(position).setIsCheck(checkBox.isChecked());
            }
        });

    }

    /**
     * 计算已经选择券
     *
     * @param datas
     */
    private ArrayList<TicketInfo> countSelectNum(List<TicketInfo> datas) {
        ArrayList<TicketInfo> selectPricates = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isCheck() == true) {
                selectPricates.add(datas.get(i));
            }
        }
        return selectPricates;
    }


}
