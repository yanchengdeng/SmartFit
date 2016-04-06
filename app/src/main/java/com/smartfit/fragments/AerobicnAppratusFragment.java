package com.smartfit.fragments;

import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.dialog.widget.popup.base.BasePopup;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.activities.AerobicAppratusDetailActivity;
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.MainBusinessActivity;
import com.smartfit.activities.OrderReserveActivity;
import com.smartfit.adpters.AerobincnAppratusItemAdapter;
import com.smartfit.adpters.ChooseAddressAdapter;
import com.smartfit.adpters.ChooseOrderAdapter;
import com.smartfit.adpters.SelectDateAdapter;
import com.smartfit.beans.CustomeDate;
import com.smartfit.beans.WorkPointAddress;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.DeviceUtil;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.utils.Util;
import com.smartfit.views.HorizontalListView;
import com.smartfit.views.LoadMoreListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/2/28.
 * 有氧机械
 */
public class AerobicnAppratusFragment extends Fragment {
    /****
     * 地址弹出选择框
     */
    AddressCustomPop addressCustomPop;
    /****
     * 排序选择框
     **/
    OrderCustomePop orderCustomePop;
    @Bind(R.id.ck_more_address)
    CheckBox ckMoreAddress;
    @Bind(R.id.tv_address)
    TextView tvAddress;
    @Bind(R.id.ck_more_select)
    CheckBox ckMoreSelect;
    @Bind(R.id.rl_condition_head)
    RelativeLayout rlConditionHead;
    @Bind(R.id.listview_date)
    HorizontalListView listviewDate;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.rl_order_time)
    RelativeLayout rlOrderTime;
    @Bind(R.id.no_data)
    TextView noData;
    @Bind(R.id.listView)
    LoadMoreListView listView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.iv_cover_bg)
    ImageView ivCoverBg;


    private int REQUEST_CODE_ORDER_TIME = 0x112;

    private int[] nomarlData = {R.mipmap.icon_1, R.mipmap.icon_2, R.mipmap.icon_3, R.mipmap.icon_4, R.mipmap.icon_5, R.mipmap.icon_6, R.mipmap.icon_7};
    private int[] selectData = {R.mipmap.icon_1_on, R.mipmap.icon_2_on, R.mipmap.icon_3_on, R.mipmap.icon_4_on, R.mipmap.icon_5_on, R.mipmap.icon_6_on, R.mipmap.icon_7_on};


    private int page = 1;
    private AerobincnAppratusItemAdapter adapter;
    private List<String> datas = new ArrayList<String>();

    private List<WorkPointAddress> addresses;

    private String selectType = "0";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_experience, container, false);
        ButterKnife.bind(this, view);

        orderCustomePop = new OrderCustomePop(getActivity());
        initDateSelect();
        initListView();
        addLisener();
        return view;
    }

    /**
     * 初始化数据列表加载
     */
    private void initListView() {
        adapter = new AerobincnAppratusItemAdapter(getActivity(), datas);
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
                        page = 1;
                        swipeRefreshLayout.setRefreshing(false);
                        ((MainBusinessActivity) getActivity()).mSVProgressHUD.showSuccessWithStatus(getString(R.string.update_already), SVProgressHUD.SVProgressHUDMaskType.Clear);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((MainBusinessActivity) getActivity()).openActivity(AerobicAppratusDetailActivity.class);
            }
        });
    }


    private void loadData() {
        if (page == 1 && !((BaseActivity) getActivity()).mSVProgressHUD.isShowing()) {

            ((BaseActivity) getActivity()).mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
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
                ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
            }
        }, 2000);
    }


    private List<CustomeDate> customeDates;
    private SelectDateAdapter selectDateAdapter;
    //选择日期    YYYY-MM-DD
    private String selectDate;

    /****
     * 初始化日期选择器
     **/
    private void initDateSelect() {
        customeDates = DateUtils.getWeekInfo();
        selectDateAdapter = new SelectDateAdapter(customeDates, getActivity());
        selectDateAdapter.setCurrentPositon(0);
        selectDate = Calendar.getInstance().get(Calendar.YEAR) + "-" + customeDates.get(0).getDate();
        listviewDate.setAdapter(selectDateAdapter);
        listviewDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectDateAdapter.setCurrentPositon(position);
                selectDate = Calendar.getInstance().get(Calendar.YEAR) + "-" + customeDates.get(position).getDate();
            }
        });
    }


    private void addLisener() {
        ckMoreAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String citycode = SharedPreferencesUtils.getInstance().getString(Constants.CITY_CODE, "");
                if (TextUtils.isEmpty(citycode)) {
                    ((BaseActivity) getActivity()).mSVProgressHUD.showInfoWithStatus(getString(R.string.no_city_location), SVProgressHUD.SVProgressHUDMaskType.Clear);
                } else {
                    if (!TextUtils.isEmpty(SharedPreferencesUtils.getInstance().getString(Constants.CITY_CODE, ""))) {
                        getVenueList();
                    }
                }
            }
        });

        ckMoreSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrderPop();
            }
        });


        rlOrderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderReserveActivity.class);
                Bundle bundle = new Bundle();
                String[] time = tvTime.getText().toString().split(":");
                bundle.putString("hour", time[0]);
                bundle.putString("min", time[1]);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_ORDER_TIME);
            }
        });
    }


    private void getVenueList() {
        Map<String, String> data = new HashMap<>();
        data.put("selDate", selectDate);
        data.put("ordeyBy", "0");
        PostRequest request = new PostRequest(Constants.GET_VENUElIST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                List<WorkPointAddress> reqestAddresses = JsonUtils.listFromJson(response.getAsJsonArray("list"), WorkPointAddress.class);
                if (reqestAddresses != null && reqestAddresses.size() > 0) {
                    addresses = reqestAddresses;
                    showAddressPop();
                } else {
                    ((BaseActivity) getActivity()).mSVProgressHUD.showInfoWithStatus(getString(R.string.no_address_list), SVProgressHUD.SVProgressHUDMaskType.Clear);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) getActivity()).mSVProgressHUD.showInfoWithStatus(getString(R.string.no_address_list), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(((BaseActivity) getActivity()).TAG);
        request.headers = NetUtil.getRequestBody(getActivity());
        ((BaseActivity) getActivity()).mQueue.add(request);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ORDER_TIME && resultCode == OrderReserveActivity.SELECT_VALUE_OVER) {
            if (!TextUtils.isEmpty(data.getExtras().getString(Constants.PASS_STRING))) {
                tvTime.setText(data.getStringExtra(Constants.PASS_STRING));
            }

        }
    }

    /****
     * 显示地址弹出框
     */
    private void showAddressPop() {
        addressCustomPop = new AddressCustomPop(getContext());
        ivCoverBg.setVisibility(View.VISIBLE);
        addressCustomPop
                .anchorView(rlConditionHead)
                .offset(0, -15)
                .gravity(Gravity.BOTTOM)
//                .showAnim(new BounceBottomEnter())
//                .dismissAnim(new SlideBottomExit())
                .dimEnabled(false)
                .show();

        addressCustomPop.setCanceledOnTouchOutside(false);
    }


    /******
     * 弹出排序框
     */
    private void showOrderPop() {
        ivCoverBg.setVisibility(View.VISIBLE);
        orderCustomePop
                .anchorView(rlConditionHead)
                .offset(0, -15)
                .gravity(Gravity.BOTTOM)
//                .showAnim(new BounceBottomEnter())
//                .dismissAnim(new SlideBottomExit())
                .dimEnabled(false)
                .show();

        orderCustomePop.setCanceledOnTouchOutside(false);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private class AddressCustomPop extends BasePopup<AddressCustomPop> {

        private ListView listView;

        private ImageView ivArrow;

        public AddressCustomPop(Context context) {
            super(context);
        }

        @Override
        public View onCreatePopupView() {
            View inflate = View.inflate(mContext, R.layout.popup_custom, null);
            listView = (ListView) inflate.findViewById(R.id.listView);
            ivArrow = (ImageView) inflate.findViewById(R.id.iv_more_address_arrow);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.LEFT;
            params.leftMargin = DeviceUtil.dp2px(getActivity(), getResources().getDimension(R.dimen.activity_horizontal_margin));
            ivArrow.setLayoutParams(params);
            listView.setAdapter(new ChooseAddressAdapter(getActivity(), addresses));
            return inflate;
        }

        @Override
        public void onBackPressed() {
            ivCoverBg.setVisibility(View.GONE);
            super.onBackPressed();
        }

        @Override
        public void setUiBeforShow() {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tvAddress.setText(addresses.get(position).getVenueName());
                    addressCustomPop.dismiss();
                    ivCoverBg.setVisibility(View.GONE);
                }
            });
        }
    }

    private class OrderCustomePop extends BasePopup<OrderCustomePop> {

        private ListView listView;

        private ImageView ivArrow;

        public OrderCustomePop(Context context) {
            super(context);
        }

        @Override
        public View onCreatePopupView() {
            View inflate = View.inflate(mContext, R.layout.popup_custom, null);
            listView = (ListView) inflate.findViewById(R.id.listView);
            listView.setAdapter(new ChooseOrderAdapter(getActivity()));
            ivArrow = (ImageView) inflate.findViewById(R.id.iv_more_address_arrow);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT;
            params.rightMargin = DeviceUtil.dp2px(getActivity(), getResources().getDimension(R.dimen.activity_horizontal_margin));
            ivArrow.setLayoutParams(params);
            return inflate;
        }

        @Override
        public void onBackPressed() {
            ivCoverBg.setVisibility(View.GONE);
            super.onBackPressed();
        }

        @Override
        public void setUiBeforShow() {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    orderCustomePop.dismiss();
                    selectType = Util.getSortList(getContext()).get(position).getId();
                    ivCoverBg.setVisibility(View.GONE);
                }
            });
        }
    }
}
