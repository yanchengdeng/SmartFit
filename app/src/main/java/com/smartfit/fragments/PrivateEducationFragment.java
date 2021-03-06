package com.smartfit.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.smartfit.activities.MainBusinessActivity;
import com.smartfit.activities.OrderPrivateEducationClassActivity;
import com.smartfit.activities.OrderReserveActivity;
import com.smartfit.activities.SelectedCoachActivity;
import com.smartfit.adpters.ChooseAddressAdapter;
import com.smartfit.adpters.ChooseCoachTypeAdapter;
import com.smartfit.adpters.PrivateEducationAdapter;
import com.smartfit.adpters.PrivateEducationClassTypeAdapter;
import com.smartfit.adpters.SelectDateAdapter;
import com.smartfit.beans.CustomeDate;
import com.smartfit.beans.IdleClassInfo;
import com.smartfit.beans.IdleClassListInfo;
import com.smartfit.beans.PrivateCalssType;
import com.smartfit.beans.PrivateEducationClass;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/2/28.
 * 私教课
 */
public class PrivateEducationFragment extends Fragment {
    /****
     * 地址弹出选择框
     */
    AddressCustomPop addressCustomPop;
    /****
     * 条件筛选框
     **/
    ConditionSelectPop conditionSelectPop;
    /**
     * 类型筛选匡
     */

    SelectCoachTypePop selectCoachTypePop;
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
    @Bind(R.id.btn_selected)
    Button btnSelected;
    @Bind(R.id.iv_cover_bg)
    ImageView ivCoverBg;
    @Bind(R.id.ll_list_view_cover)
    LinearLayout llListViewCover;
    @Bind(R.id.ck_more_type)
    CheckBox ckMoreType;


    private int REQUEST_CODE_ORDER_TIME = 0x112;


    private PrivateEducationAdapter adapter;
    private List<PrivateEducationClass> datas = new ArrayList<PrivateEducationClass>();//私教课程数据
    private List<PrivateCalssType> privateCalssTypes = new ArrayList<>();//私教类别课程
    private List<WorkPointAddress> addresses;
    private List<String> types = new ArrayList<>();
    private String venueId = "0";
    private String sex, startTime, endTime, startPrice, endPrice;

    private int CoachType = 0;//获取当前列表数据类型     0：约教练  1：约课程
    private int slectPostion = 0;
    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean isLoaded = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_education, container, false);
        ButterKnife.bind(this, view);
        initDateSelect();
        initListView();
        addLisener();
        types.add("约教练");
        types.add("约课程");
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isPrepared) {
            slectPostion = SharedPreferencesUtils.getInstance().getInt(Constants.SELECT_ADDRESS_VENER, 0);
            doLoadData();
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
                if (CoachType == 0) {
                    loadData();
                } else {
                    getIdleCoachList();
                    getValueClassficaiton();
                }
            } else {
                getVenueList();
            }
        }
    }

    /**
     * 初始化数据列表加载
     */
    private void initListView() {

        doLoadData();
        isPrepared = true;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (CoachType == 0) {
                    CheckBox checkBox = (CheckBox) view.findViewById(R.id.ch_select);
                    checkBox.setChecked(!checkBox.isChecked());
                    datas.get(position).setIsCheck(checkBox.isChecked());
                    List<PrivateEducationClass> selectPricates = countSelectNum(datas);
                    if (selectPricates.size() == 0) {
                        btnSelected.setText("请选择教练");
                    } else {
                        btnSelected.setText("已选择" + selectPricates.size() + "个");
                    }
                } else {
                    if (idleClass != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("venueId", venueId);
                        bundle.putParcelable(Constants.PASS_OBJECT, privateCalssTypes.get(position));
                        bundle.putString("dayTime", selectDate);
                        bundle.putSerializable(Constants.PASS_IDLE_CLASS_INFO, idleClass);
                        ((MainBusinessActivity) getActivity()).openActivity(SelectedCoachActivity.class, bundle);
                    } else {
                        ((MainBusinessActivity) getActivity()).mSVProgressHUD.showInfoWithStatus("暂无空闲场馆");
                    }
                }
            }
        });

        btnSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<PrivateEducationClass> selectPricates = countSelectNum(datas);
                if (selectPricates.size() == 0) {
                    ((MainBusinessActivity) getActivity()).mSVProgressHUD.showInfoWithStatus("请选择教练");
                } else {
                    if (idleClass != null) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(Constants.PASS_OBJECT, selectPricates);
                        bundle.putSerializable(Constants.PASS_IDLE_CLASS_INFO, idleClass);
                        bundle.putString("start", selectDate + " " + startTime);
                        bundle.putString("end", selectDate + " " + endTime);
                        bundle.putBoolean("is_inclue_area_fee", false);//是否  已包含场地费用
                        ((MainBusinessActivity) getActivity()).openActivity(OrderPrivateEducationClassActivity.class, bundle);
                    } else {
                        ((MainBusinessActivity) getActivity()).mSVProgressHUD.showInfoWithStatus("暂无空闲场馆");
                    }
                }
            }
        });

    }

    /**
     * 计算已经选择教练人数
     *
     * @param datas
     */
    private ArrayList<PrivateEducationClass> countSelectNum(List<PrivateEducationClass> datas) {
        ArrayList<PrivateEducationClass> selectPricates = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isCheck() == true) {
                selectPricates.add(datas.get(i));
            }
        }
        return selectPricates;
    }

    IdleClassListInfo idleClass;

    private String priceDisce[];


    /**
     * 获取空闲教室
     */
    private void getIdleCoachList() {

        Map<String, String> data = new HashMap<>();
        data.put("startTime", String.valueOf(DateUtils.getTheDateTimeMillions(selectDate + " " + startTime)));
        data.put("endTime", String.valueOf(DateUtils.getTheDateTimeMillions(selectDate + " " + endTime)));
        data.put("venueId", venueId);
        data.put("classroomType", "2");
        PostRequest request = new PostRequest(Constants.CLASSIF_LISTTHEVENUEIDLECLASSROOMS, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                btnSelected.setText("请选择教练");
                IdleClassListInfo idleClassListInfo = JsonUtils.objectFromJson(response, IdleClassListInfo.class);
                if (idleClassListInfo != null && idleClassListInfo.getClassroomList() != null && idleClassListInfo.getClassroomList().size() > 0) {
                    idleClass = idleClassListInfo;
                    if (CoachType == 0) {
                        List<Float> prices = new ArrayList<>();
                        for (IdleClassInfo item : idleClassListInfo.getClassroomList()) {
                            prices.add(Float.parseFloat(item.getClassroomPrice()));
                        }

                        for (PrivateEducationClass item : datas) {

                            //只有一个价格  显示    教练费用为250/小时
                            if (Collections.max(prices).equals(Collections.min(prices))) {
                                Float price = Float.parseFloat(item.getPrice()) + Collections.max(prices);
                                item.setShowPriceInfo(String.format("%s/小时", new Object[]{String.valueOf(price)}));
                            } else {
                                Float priceLow = Float.parseFloat(item.getPrice()) + Collections.min(prices);
                                Float priceHight = Float.parseFloat(item.getPrice()) + Collections.max(prices);
                                LogUtil.w("dyc", "低：" + priceLow + "  高：" + priceHight);
                                item.setShowPriceInfo(String.format("%1$s-%2$s/小时", new Object[]{String.valueOf(priceLow), String.valueOf(priceHight)}));
                            }
                        }
                        listView.setAdapter(new PrivateEducationAdapter(getActivity(), datas));
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                btnSelected.setText("请选择教练");
                Log.w("dyc", error.getLocalizedMessage() + error.getMessage());

            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(getActivity());
        ((BaseActivity) getActivity()).mQueue.add(request);
    }

    /**
     * 获取场地空闲教练
     */
    private void loadData() {


        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {

            ((BaseActivity) getActivity()).mSVProgressHUD.showInfoWithStatus("请选择预约时间", SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }

        ((BaseActivity) getActivity()).mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        Map<String, String> data = new HashMap<>();
        data.put("startTime", String.valueOf(DateUtils.getTheDateTimeMillions(selectDate + " " + startTime)));
        data.put("endTime", String.valueOf(DateUtils.getTheDateTimeMillions(selectDate + " " + endTime)));
        if (!TextUtils.isEmpty(startPrice)) {
            data.put("startPrice", startPrice);
            data.put("endPrice", endPrice);
        }
        if (!TextUtils.isEmpty(sex)) {
            data.put("sex", sex);
        }
        data.put("venueId", venueId);
        data.put("courseType", "2");
        PostRequest request = new PostRequest(Constants.COACH_LISTIDLECOACHESBYVENUEID, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                btnSelected.setText("请选择教练");
                if (getActivity() != null)
                    ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
                if (rlOrderTime != null)
                    rlOrderTime.setVisibility(View.VISIBLE);
                if (btnSelected != null)
                    btnSelected.setVisibility(View.VISIBLE);
                List<PrivateEducationClass> privateEducationClasses = JsonUtils.listFromJson(response.getAsJsonArray("list"), PrivateEducationClass.class);
                if (privateEducationClasses != null && privateEducationClasses.size() > 0) {
                    datas = privateEducationClasses;
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.INVISIBLE);
                    llListViewCover.setVisibility(View.GONE);
                    getIdleCoachList();

                } else {
                    listView.setVisibility(View.INVISIBLE);
                    noData.setVisibility(View.VISIBLE);
                    llListViewCover.setVisibility(View.GONE);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
                listView.setVisibility(View.INVISIBLE);
                noData.setVisibility(View.VISIBLE);
                llListViewCover.setVisibility(View.GONE);
                rlOrderTime.setVisibility(View.VISIBLE);
                btnSelected.setVisibility(View.VISIBLE);
                btnSelected.setText("请选择教练");
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(getActivity());
        ((BaseActivity) getActivity()).mQueue.add(request);

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
                if (CoachType == 0) {
                    loadData();
                } else {
                    getIdleCoachList();
                    getValueClassficaiton();
                }
            }
        });
    }


    private void addLisener() {
        /**
         * 场馆选择
         */
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

        /**
         * 筛选条件
         */
        ckMoreSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != conditionSelectPop) {
                    if (conditionSelectPop.isShowing()) {
                        conditionSelectPop.dismiss();
                        ivCoverBg.setVisibility(View.GONE);
                    } else {
                        conditionSelectPop.show();
                        ivCoverBg.setVisibility(View.VISIBLE);
                    }
                } else {
                    showOrderPop();
                }
            }
        });

        /**
         * 选择教练类型
         */
        ckMoreType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != selectCoachTypePop) {
                    if (selectCoachTypePop.isShowing()) {
                        selectCoachTypePop.dismiss();
                        ivCoverBg.setVisibility(View.GONE);
                    } else {
                        selectCoachTypePop.show();
                        ivCoverBg.setVisibility(View.VISIBLE);
                    }
                } else {
                    showPrivateCoachType();
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
                List<WorkPointAddress> reqeustList = JsonUtils.listFromJson(response.getAsJsonArray("list"), WorkPointAddress.class);
                if (reqeustList != null && reqeustList.size() > 0) {
                    addresses = reqeustList;
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
                loadData();
            }

        }
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


    /**
     * 弹出选择私教课类型
     */
    private void showPrivateCoachType() {
        selectCoachTypePop = new SelectCoachTypePop(getActivity());
        ivCoverBg.setVisibility(View.VISIBLE);
        selectCoachTypePop
                .anchorView(rlConditionHead)
                .offset(0, -15)
                .gravity(Gravity.BOTTOM)
//                .showAnim(new BounceBottomEnter())
//                .dismissAnim(new SlideBottomExit())
                .dimEnabled(false)
                .show();

        selectCoachTypePop.setCanceledOnTouchOutside(true);

        selectCoachTypePop.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                selectCoachTypePop.dismiss();
                ivCoverBg.setVisibility(View.GONE);
            }
        });
        selectCoachTypePop.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                selectCoachTypePop.dismiss();
                ivCoverBg.setVisibility(View.GONE);
            }
        });
    }

    /******
     * 弹出排序框
     */
    private void showOrderPop() {
        ivCoverBg.setVisibility(View.VISIBLE);
        conditionSelectPop = new ConditionSelectPop(getActivity());
        conditionSelectPop
                .anchorView(rlConditionHead)
                .offset(0, -15)
                .gravity(Gravity.BOTTOM)
//                .showAnim(new BounceBottomEnter())
//                .dismissAnim(new SlideBottomExit())
                .dimEnabled(false)
                .show();

        conditionSelectPop.setCanceledOnTouchOutside(true);

        conditionSelectPop.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                conditionSelectPop.dismiss();
                ivCoverBg.setVisibility(View.GONE);
            }
        });
        conditionSelectPop.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                conditionSelectPop.dismiss();
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
                    venueId = addresses.get(position).getVenueId();
                    SharedPreferencesUtils.getInstance().putInt(Constants.SELECT_ADDRESS_VENER, position);
                    ivCoverBg.setVisibility(View.GONE);
                    if (CoachType == 0) {
                        loadData();
                    } else {
                        getIdleCoachList();
                        getValueClassficaiton();
                    }
                }
            });
        }
    }


    private class SelectCoachTypePop extends BasePopup<SelectCoachTypePop> {


        private ListView listView;

        private ImageView ivArrow;

        public SelectCoachTypePop(Context context) {
            super(context);
        }


        @Override
        public View onCreatePopupView() {
            View inflate = View.inflate(mContext, R.layout.popup_custom, null);
            listView = (ListView) inflate.findViewById(R.id.listView);
            ivArrow = (ImageView) inflate.findViewById(R.id.iv_more_address_arrow);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT;
            params.rightMargin = DeviceUtil.dp2px(getActivity(), getResources().getDimension(R.dimen.activity_horizontal_margin));
            ivArrow.setLayoutParams(params);
            listView.setAdapter(new ChooseCoachTypeAdapter(getActivity(), types));
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
                    LogUtil.w("dyc", types.get(position));


                    selectCoachTypePop.dismiss();
                    ivCoverBg.setVisibility(View.GONE);
                    if (position == 0) {
                        ckMoreType.setText(types.get(0));
                        ckMoreSelect.setVisibility(View.VISIBLE);
                        CoachType = 0;
                        loadData();
                    } else {
                        CoachType = 1;
                        ckMoreType.setText(types.get(1));
                        ckMoreSelect.setVisibility(View.GONE);
                        getIdleCoachList();
                        getValueClassficaiton();
                    }
                }
            });
        }
    }


    private void getValueClassficaiton() {
        ((BaseActivity) getActivity()).mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);

        Map<String, String> data = new HashMap<>();
        data.put("venueId", venueId);
        data.put("dayTime", String.valueOf(DateUtils.getTheDateMillions(selectDate)));
        PostRequest request = new PostRequest(Constants.CLASSIF_LISTPRIVATEVALUEBLECLASSIFICATION, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                rlOrderTime.setVisibility(View.GONE);
                btnSelected.setVisibility(View.GONE);
                privateCalssTypes = JsonUtils.listFromJson(response.getAsJsonArray("list"), PrivateCalssType.class);
                if (privateCalssTypes != null && privateCalssTypes.size() > 0) {
                    listView.setAdapter(new PrivateEducationClassTypeAdapter(getActivity(), privateCalssTypes));
                    listView.setVisibility(View.VISIBLE);
                    noData.setVisibility(View.GONE);

                } else {
                    listView.setVisibility(View.GONE);
                    noData.setVisibility(View.VISIBLE);
                }
                ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((BaseActivity) getActivity()).mSVProgressHUD.dismiss();
                noData.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                rlOrderTime.setVisibility(View.GONE);
                btnSelected.setVisibility(View.GONE);

            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(getActivity());
        ((BaseActivity) getActivity()).mQueue.add(request);


    }

    private class ConditionSelectPop extends BasePopup<ConditionSelectPop> {

        Button btnBoy;
        Button btnGirl;
        Button btnSex;
        Button btnLessFifty;
        Button btnLessTwoHundred;
        Button btnLessFiveHundred;
        Button btnMoreFiveHundred;
        Button btnPrice;
        Button btnReset;
        Button btnSure;
        ImageView ivArrow;

        public ConditionSelectPop(Context context) {
            super(context);
        }

        @Override
        public View onCreatePopupView() {
            View inflate = View.inflate(mContext, R.layout.popup_condition_select, null);
            ivArrow = (ImageView) inflate.findViewById(R.id.iv_more_address_arrow);
            btnBoy = (Button) inflate.findViewById(R.id.btn_boy);
            btnGirl = (Button) inflate.findViewById(R.id.btn_girl);
            btnSex = (Button) inflate.findViewById(R.id.btn_sex);
            btnLessFifty = (Button) inflate.findViewById(R.id.btn_less_fifty);
            btnLessTwoHundred = (Button) inflate.findViewById(R.id.btn_less_two_hundred);
            btnLessFiveHundred = (Button) inflate.findViewById(R.id.btn_less_five_hundred);
            btnMoreFiveHundred = (Button) inflate.findViewById(R.id.btn_more_five_hundred);
            btnPrice = (Button) inflate.findViewById(R.id.btn_price);
            btnReset = (Button) inflate.findViewById(R.id.btn_reset);
            btnSure = (Button) inflate.findViewById(R.id.btn_sure);
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
            btnBoy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetSex();
                    btnBoy.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                    sex = "0";
                }
            });

            btnGirl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetSex();
                    btnGirl.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                    sex = "1";
                }
            });

            btnSex.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetSex();
                    btnSex.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                    sex = "";
                }
            });

            btnLessFifty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPrice();
                    v.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                    startPrice = "0";
                    endPrice = "50";
                }
            });
            btnLessTwoHundred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPrice();
                    v.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                    startPrice = "50";
                    endPrice = "200";
                }
            });

            btnLessFiveHundred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPrice();
                    v.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                    startPrice = "200";
                    endPrice = "500";
                }
            });

            btnMoreFiveHundred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPrice();
                    v.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                    startPrice = "500";
                    endPrice = String.valueOf(Integer.MAX_VALUE);
                }
            });

            btnPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPrice();
                    v.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                    startPrice = "";
                    endPrice = "";
                }
            });


            btnReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPrice();
                    resetSex();
                    btnSex.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                    btnPrice.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                    sex = "";
                    startPrice = "";
                    endPrice = "";
                }
            });

            btnSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivCoverBg.setVisibility(View.GONE);
                    conditionSelectPop.dismiss();
                    loadData();
                }
            });
        }

        private void resetSex() {
            btnBoy.setBackgroundResource(R.drawable.bg_dialog_selector_white);
            btnGirl.setBackgroundResource(R.drawable.bg_dialog_selector_white);
            btnSex.setBackgroundResource(R.drawable.bg_dialog_selector_white);
        }

        private void resetPrice() {
            btnLessFifty.setBackgroundResource(R.drawable.bg_dialog_selector_white);
            btnLessFiveHundred.setBackgroundResource(R.drawable.bg_dialog_selector_white);
            btnLessTwoHundred.setBackgroundResource(R.drawable.bg_dialog_selector_white);
            btnMoreFiveHundred.setBackgroundResource(R.drawable.bg_dialog_selector_white);
            btnPrice.setBackgroundResource(R.drawable.bg_dialog_selector_white);
        }

    }


}
