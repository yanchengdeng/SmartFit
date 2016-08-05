package com.smartfit.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
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
import com.smartfit.activities.BaseActivity;
import com.smartfit.activities.GroupClassDetailActivity;
import com.smartfit.activities.LoginActivity;
import com.smartfit.activities.MainBusinessActivity;
import com.smartfit.adpters.ChooseAddressAdapter;
import com.smartfit.adpters.ChooseOrderAdapter;
import com.smartfit.adpters.GroupExpericeItemAdapter;
import com.smartfit.adpters.SelectDateAdapter;
import com.smartfit.beans.ClassInfo;
import com.smartfit.beans.CustomeDate;
import com.smartfit.beans.WorkPointAddress;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.DeviceUtil;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
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
 * 团操课
 */
public class GroupExperienceFragment extends BaseFragment {


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




    private GroupExpericeItemAdapter adapter;
    private List<ClassInfo> datas = new ArrayList<ClassInfo>();

    private List<WorkPointAddress> addresses;
    private String selectType = "0";
    private String venueId = "0";
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
        initDateSelect();
        initListView();
        addLisener();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            slectPostion = SharedPreferencesUtils.getInstance().getInt(Constants.SELECT_ADDRESS_VENER,0);
            if (isPrepared){
                goLoadData();
            }
        }
    }

    private void goLoadData(){
            String citycode = SharedPreferencesUtils.getInstance().getString(Constants.CITY_CODE, "");
            if (TextUtils.isEmpty(citycode)) {
                ((BaseActivity) getActivity()).mSVProgressHUD.showInfoWithStatus(getString(R.string.no_city_location), SVProgressHUD.SVProgressHUDMaskType.Clear);
            } else {
                List<WorkPointAddress> workPointAddresses = Util.getVenueList();
                if (workPointAddresses != null && workPointAddresses.size() > 0 && workPointAddresses.size()>slectPostion) {
                    addresses = workPointAddresses;
                    tvAddress.setText(addresses.get(slectPostion).getVenueName());
                    venueId = addresses.get(slectPostion).getVenueId();
                    isLoaded = false;
                    lazyLoad();
                } else {
                    getVenueList();
                }
            }
    }

    /**
     * 初始化数据列表加载
     */
    private void initListView() {
        isPrepared = true;
        adapter = new GroupExpericeItemAdapter(getActivity(), datas);
        listView.setAdapter(adapter);
        goLoadData();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!NetUtil.isLogin(getActivity())) {
                    ((BaseActivity) getActivity()).openActivity(LoginActivity.class);
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, datas.get(position).getCourseId());
                bundle.putString(Constants.COURSE_TYPE, "0");
                bundle.putInt(Constants.PAGE_INDEX,1);
                ((MainBusinessActivity) getActivity()).openActivity(GroupClassDetailActivity.class, bundle);
            }
        });
    }


    private void loadData() {
        datas.clear();
        ((BaseActivity) getActivity()).mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        data.put("time", String.valueOf(DateUtils.getTheDateMillions(selectDate)));
        data.put("orderBy", selectType);
        data.put("venueId", venueId);
//        data.put("coachSex", "0");
//        data.put("priceRang", "0");
//        data.put("timeRang", "0");
        data.put("courseType", "0");
        PostRequest request = new PostRequest(Constants.GET_CLASS_LIST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
                isLoaded = true;
                datas.clear();
                List<ClassInfo> requestList = JsonUtils.listFromJson(response.getAsJsonArray("list"), ClassInfo.class);
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
                LogUtil.w("dyc", "..... " + error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(getActivity());
        ((BaseActivity) getActivity()).mQueue.add(request);
    }


    private void noMoreData(List<ClassInfo> datas) {
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

    }

    private void getVenueList() {
        Map<String, String> data = new HashMap<>();
        data.put("cityCode", SharedPreferencesUtils.getInstance().getString(Constants.CITY_CODE, ""));
        PostRequest request = new PostRequest(Constants.GET_VENUElIST, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                List<WorkPointAddress> reqeustAddresses = JsonUtils.listFromJson(response.getAsJsonArray("list"), WorkPointAddress.class);
                if (reqeustAddresses != null && reqeustAddresses.size() > 0) {
                    addresses = reqeustAddresses;
                    venueId = addresses.get(0).getVenueId();
                    isLoaded = false;
                    lazyLoad();
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

    }

    /****
     * 显示地址弹出框
     */
    private void showAddressPop() {
        addressCustomPop = new AddressCustomPop(getActivity());
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
                dialog.dismiss();
                ivCoverBg.setVisibility(View.GONE);
            }
        }) ;

        addressCustomPop.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
                ivCoverBg.setVisibility(View.GONE);
            }
        });
    }


    /******
     * 弹出排序框
     */
    private void showOrderPop() {
        ivCoverBg.setVisibility(View.VISIBLE);
        orderCustomePop =new OrderCustomePop(getActivity());
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
                orderCustomePop.dismiss();
                ivCoverBg.setVisibility(View.GONE);
            }
        });
        orderCustomePop.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                orderCustomePop.dismiss();
                ivCoverBg.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || isLoaded) {
            return;
        }
        loadData();
    }


    private class AddressCustomPop extends BasePopup<AddressCustomPop> {

        private ListView listView;

        private ImageView ivArrow;


        public AddressCustomPop(FragmentActivity activity) {
            super(activity);

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
                    venueId = addresses.get(position).getVenueId();
                    SharedPreferencesUtils.getInstance().putInt(Constants.SELECT_ADDRESS_VENER,position);
                    isLoaded = false;
                    lazyLoad();
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
                    isLoaded = false;
                    lazyLoad();
                    ivCoverBg.setVisibility(View.GONE);
                }
            });
        }
    }


}
