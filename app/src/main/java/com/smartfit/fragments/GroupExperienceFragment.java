package com.smartfit.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.flyco.dialog.widget.popup.base.BasePopup;
import com.smartfit.R;
import com.smartfit.activities.MainActivity;
import com.smartfit.activities.OrderReserveActivity;
import com.smartfit.adpters.ChooseAddressAdapter;
import com.smartfit.adpters.ChooseOrderAdapter;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DeviceUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by dengyancheng on 16/2/28.
 * 团操课
 */
public class GroupExperienceFragment extends Fragment {


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
    @Bind(R.id.tv_week1)
    TextView tvWeek1;
    @Bind(R.id.iv_date1)
    ImageView ivDate1;
    @Bind(R.id.tv_week2)
    TextView tvWeek2;
    @Bind(R.id.iv_date2)
    ImageView ivDate2;
    @Bind(R.id.tv_week3)
    TextView tvWeek3;
    @Bind(R.id.iv_date3)
    ImageView ivDate3;
    @Bind(R.id.tv_week4)
    TextView tvWeek4;
    @Bind(R.id.iv_date4)
    ImageView ivDate4;
    @Bind(R.id.tv_week5)
    TextView tvWeek5;
    @Bind(R.id.iv_date5)
    ImageView ivDate5;
    @Bind(R.id.tv_week6)
    TextView tvWeek6;
    @Bind(R.id.iv_date6)
    ImageView ivDate6;
    @Bind(R.id.tv_week7)
    TextView tvWeek7;
    @Bind(R.id.iv_date7)
    ImageView ivDate7;
    @Bind(R.id.iv_cover_bg)
    ImageView ivCoverBg;
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.rl_order_time)
    RelativeLayout rlOrderTime;

    private int REQUEST_CODE_ORDER_TIME = 0x110;

    private int[] nomarlData = {R.mipmap.icon_1, R.mipmap.icon_2, R.mipmap.icon_3, R.mipmap.icon_4, R.mipmap.icon_5, R.mipmap.icon_6, R.mipmap.icon_7};
    private int[] selectData = {R.mipmap.icon_1_on, R.mipmap.icon_2_on, R.mipmap.icon_3_on, R.mipmap.icon_4_on, R.mipmap.icon_5_on, R.mipmap.icon_6_on, R.mipmap.icon_7_on};

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_experience, container, false);
        ButterKnife.bind(this, view);
        addressCustomPop = new AddressCustomPop(getActivity());
        orderCustomePop = new OrderCustomePop(getActivity());
        initDateSelect();
        addLisener();
        return view;
    }


    /****
     * 初始化日期选择器
     **/
    private void initDateSelect() {

    }


    private void addLisener() {
        ckMoreAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddressPop();
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
                Intent intent = new Intent(getActivity(),OrderReserveActivity.class);
                Bundle bundle = new Bundle();
                String[] time = tvTime.getText().toString().split(":");
                bundle.putString("hour",time[0]);
                bundle.putString("min",time[1]);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_ORDER_TIME);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_ORDER_TIME  && resultCode == OrderReserveActivity.SELECT_VALUE_OVER){
            if(!TextUtils.isEmpty(data.getExtras().getString(Constants.PASS_STING))){
                tvTime.setText(data.getStringExtra(Constants.PASS_STING));
            }

        }
    }

    /****
     * 显示地址弹出框
     */
    private void showAddressPop() {
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
            listView.setAdapter(new ChooseAddressAdapter(getActivity()));
            return inflate;
        }

        @Override
        public void setUiBeforShow() {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    tvAddress.setText("六一北路SF健身馆");
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
        public void setUiBeforShow() {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    orderCustomePop.dismiss();
                    ivCoverBg.setVisibility(View.GONE);
                }
            });
        }
    }


}
