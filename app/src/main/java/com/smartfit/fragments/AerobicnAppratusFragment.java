package com.smartfit.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.smartfit.beans.AreoInfo;
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
public class AerobicnAppratusFragment extends BaseFragment {
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
    ListView listView;
    @Bind(R.id.iv_cover_bg)
    ImageView ivCoverBg;


    private int REQUEST_CODE_ORDER_TIME = 0x112;


    private AerobincnAppratusItemAdapter adapter;
    private List<AreoInfo> datas = new ArrayList<AreoInfo>();

    private List<WorkPointAddress> addresses;

    private String venueId = "0";
    private String selectType = "0";

    private String startTime, endTime;

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean isLoaded = false;
    private int slectPostion = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_experience, container, false);
        ButterKnife.bind(this, view);
        rlOrderTime.setVisibility(View.VISIBLE);
        initDateSelect();
        initListView();
        addLisener();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            slectPostion = SharedPreferencesUtils.getInstance().getInt(Constants.SELECT_ADDRESS_VENER, 0);
            if (isPrepared) {
                doLoadData();
            }
        }
    }


    private void doLoadData() {
        String citycode = SharedPreferencesUtils.getInstance().getString(Constants.CITY_CODE, "");
        if (TextUtils.isEmpty(citycode)) {
            ((BaseActivity) getActivity()).mSVProgressHUD.showInfoWithStatus(getString(R.string.no_city_location), SVProgressHUD.SVProgressHUDMaskType.Clear);
        } else {
            List<WorkPointAddress> workPointAddresses = Util.getVenueList();
            if (workPointAddresses != null && workPointAddresses.size() > 0 && workPointAddresses.size() > slectPostion) {
                addresses = workPointAddresses;
                tvAddress.setText(addresses.get(slectPostion).getVenueName());
                venueId = addresses.get(slectPostion).getVenueId();
                startTime = "9:00";
                endTime = "10:00";
                lazyLoad();
            } else {
                getVenueList();
            }
        }
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }
        loadData();
    }

    /**
     * 初始化数据列表加载
     */
    private void initListView() {
        adapter = new AerobincnAppratusItemAdapter(getActivity(), datas);
        listView.setAdapter(adapter);
        isPrepared = true;
        doLoadData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.COURSE_ID, datas.get(position).getClassroomId());
                bundle.putString("start", String.valueOf(DateUtils.getTheDateTimeMillions(selectDate + " " + startTime)));
                bundle.putString("end", String.valueOf(DateUtils.getTheDateTimeMillions(selectDate + " " + endTime)));
                bundle.putString("open_time",datas.get(position).getOpenAppointmentTime());
                ((MainBusinessActivity) getActivity()).openActivity(AerobicAppratusDetailActivity.class, bundle);
            }
        });
    }


    private void loadData() {
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            ((BaseActivity) getActivity()).mSVProgressHUD.showInfoWithStatus("请选择预约时间", SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }

        datas.clear();
        ((BaseActivity) getActivity()).mSVProgressHUD.showWithStatus(getString(R.string.loading, SVProgressHUD.SVProgressHUDMaskType.ClearCancel));
        Map<String, String> data = new HashMap<>();
        data.put("orderBy", selectType);
        data.put("venueId", venueId);
        data.put("startTime", String.valueOf(DateUtils.getTheDateTimeMillions(selectDate + " " + startTime)));
        data.put("endTime", String.valueOf(DateUtils.getTheDateTimeMillions(selectDate + " " + endTime)));
        PostRequest request = new PostRequest(Constants.CLASSROOM_GETIDLEAEROBICCLASSROOMS, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                isLoaded = true;
                datas.clear();
                ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
                List<AreoInfo> requestList = JsonUtils.listFromJson(response.getAsJsonArray("list"), AreoInfo.class);
                if (null != requestList && requestList.size() > 0) {
                    datas.addAll(requestList);
                    adapter.setData(datas);
                }
                noMoreData(datas);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                noMoreData(datas);
                ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(getActivity());
        ((BaseActivity) getActivity()).mQueue.add(request);

    }


    private void noMoreData(List<AreoInfo> datas) {
        if (datas.size() > 0) {
            listView.setVisibility(View.VISIBLE);
            noData.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            noData.setVisibility(View.VISIBLE);
        }

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
                isLoaded = false;
                lazyLoad();
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
                    if (addresses != null && addresses.size() > 0) {
                        if (null != addressCustomPop) {
                            if (addressCustomPop.isShowing()) {
                                addressCustomPop.dismiss();
                                ivCoverBg.setVisibility(View.GONE);
                            } else {
                                addressCustomPop.show();
                                ivCoverBg.setVisibility(View.VISIBLE);
                            }
                        } else {
                            showAddressPop();
                        }
                    } else {
                        ((BaseActivity) getActivity()).mSVProgressHUD.showInfoWithStatus(getString(R.string.no_address_list), SVProgressHUD.SVProgressHUDMaskType.Clear);
                    }
                }
            }
        });

        ckMoreSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != orderCustomePop) {
                    if (orderCustomePop.isShowing()) {
                        orderCustomePop.dismiss();
                        ivCoverBg.setVisibility(View.GONE);
                    } else {
                        orderCustomePop.show();
                        ivCoverBg.setVisibility(View.VISIBLE);
                    }
                } else {
                    showOrderPop();
                }
            }
        });


        rlOrderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderReserveActivity.class);

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
            if (!TextUtils.isEmpty(data.getExtras().getString("time_before")) && !TextUtils.isEmpty(data.getExtras().getString("time_after"))) {
                tvTime.setText(data.getExtras().getString("time_before") + " - " + data.getExtras().getString("time_after"));
                startTime = data.getExtras().getString("time_before");
                endTime = data.getExtras().getString("time_after");
                isLoaded = false;
                lazyLoad();
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
        addressCustomPop.setCanceledOnTouchOutside(true);

        addressCustomPop.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                addressCustomPop.dismiss();
                ivCoverBg.setVisibility(View.GONE);
            }
        });
        addressCustomPop.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                addressCustomPop.dismiss();
                ivCoverBg.setVisibility(View.GONE);
            }
        });

    }


    /******
     * 弹出排序框
     */
    private void showOrderPop() {
        ivCoverBg.setVisibility(View.VISIBLE);
        orderCustomePop = new OrderCustomePop(getActivity());
        orderCustomePop
                .anchorView(rlConditionHead)
                .offset(0, -15)
                .gravity(Gravity.BOTTOM)
//                .showAnim(new BounceBottomEnter())
//                .dismissAnim(new SlideBottomExit())
                .dimEnabled(false)
                .show();
        orderCustomePop.setCanceledOnTouchOutside(true);
        orderCustomePop.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                ivCoverBg.setVisibility(View.GONE);
            }
        });

        orderCustomePop.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
                ivCoverBg.setVisibility(View.GONE);
            }
        });

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
                    venueId = addresses.get(position).getVenueId();
                    SharedPreferencesUtils.getInstance().putInt(Constants.SELECT_ADDRESS_VENER, position);
                    isLoaded = false;
                    lazyLoad();
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
                    isLoaded = false;
                    lazyLoad();
                }
            });
        }
    }
}
