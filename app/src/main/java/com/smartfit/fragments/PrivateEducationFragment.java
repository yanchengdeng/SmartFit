package com.smartfit.fragments;

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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.dialog.widget.popup.base.BasePopup;
import com.smartfit.R;
import com.smartfit.activities.MainBusinessActivity;
import com.smartfit.activities.OrderReserveActivity;
import com.smartfit.activities.PayActivity;
import com.smartfit.adpters.ChooseAddressAdapter;
import com.smartfit.adpters.PrivateEducationAdapter;
import com.smartfit.beans.PrivateEducationClass;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DeviceUtil;

import java.util.ArrayList;
import java.util.List;

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
    @Bind(R.id.ll_week1)
    LinearLayout llWeek1;
    @Bind(R.id.ll_week2)
    LinearLayout llWeek2;
    @Bind(R.id.ll_week3)
    LinearLayout llWeek3;
    @Bind(R.id.ll_week4)
    LinearLayout llWeek4;
    @Bind(R.id.ll_week5)
    LinearLayout llWeek5;
    @Bind(R.id.ll_week6)
    LinearLayout llWeek6;
    @Bind(R.id.ll_week7)
    LinearLayout llWeek7;
    @Bind(R.id.btn_selected)
    Button btnSelected;

    private int REQUEST_CODE_ORDER_TIME = 0x112;

    private int[] nomarlData = {R.mipmap.icon_1, R.mipmap.icon_2, R.mipmap.icon_3, R.mipmap.icon_4, R.mipmap.icon_5, R.mipmap.icon_6, R.mipmap.icon_7};
    private int[] selectData = {R.mipmap.icon_1_on, R.mipmap.icon_2_on, R.mipmap.icon_3_on, R.mipmap.icon_4_on, R.mipmap.icon_5_on, R.mipmap.icon_6_on, R.mipmap.icon_7_on};


    private View footerView;
    private int page = 1;
    boolean isLoading = false;
    private PrivateEducationAdapter adapter;
    private List<PrivateEducationClass> datas = new ArrayList<PrivateEducationClass>();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_private_education, container, false);
        ButterKnife.bind(this, view);
        addressCustomPop = new AddressCustomPop(getActivity());
        conditionSelectPop = new ConditionSelectPop(getActivity());
        initDateSelect();
        initListView();
        addLisener();
        return view;
    }

    /**
     * 初始化数据列表加载
     */
    private void initListView() {
        footerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_loader_footer, null);
//        不知道为什么在xml设置的“android:layout_width="match_parent"”无效了，需要在这里重新设置
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        footerView.setLayoutParams(lp);
        listView.addFooterView(footerView);
        adapter = new PrivateEducationAdapter(getActivity(), datas);
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
                        loadData();
                        swipeRefreshLayout.setRefreshing(false);
                        ((MainBusinessActivity) getActivity()).mSVProgressHUD.showSuccessWithStatus(getString(R.string.update_already), SVProgressHUD.SVProgressHUDMaskType.Black);
                    }
                }, 3000);
            }
        });


        /**
         * 加载更多
         */

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastIndexInScreen = visibleItemCount + firstVisibleItem;
                if (lastIndexInScreen >= totalItemCount - 1 && !isLoading) {
                    isLoading = true;
                    page++;
                    loadData();
                }
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CheckBox checkBox = (CheckBox) view.findViewById(R.id.ch_select);
                checkBox.setChecked(!checkBox.isChecked());
                datas.get(position).setIsCheck(checkBox.isChecked());
                List<PrivateEducationClass> selectPricates = countSelectNum(datas);
                if (selectPricates.size() == 0) {
                    btnSelected.setText("请选择教练");
                } else {
                    btnSelected.setText("已选择" + selectPricates.size() + "个");
                }

            }
        });

        btnSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<PrivateEducationClass> selectPricates = countSelectNum(datas);
                if (selectPricates.size() == 0) {
                    ((MainBusinessActivity) getActivity()).mSVProgressHUD.showInfoWithStatus("请选择教练");
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.PAGE_INDEX, 3);
                    ((MainBusinessActivity) getActivity()).openActivity(PayActivity.class, bundle);
                }
            }
        });

    }

    /**
     * 计算已经选择教练人数
     *
     * @param datas
     */
    private List<PrivateEducationClass> countSelectNum(List<PrivateEducationClass> datas) {
        List<PrivateEducationClass> selectPricates = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).isCheck() == true) {
                selectPricates.add(datas.get(i));
            }
        }

        return selectPricates;

    }


    private void loadData() {
        for (int i = 0; i < 10; i++) {
            PrivateEducationClass item = new PrivateEducationClass();
            item.setName("王小二教练" + i + String.valueOf(page));
            datas.add(item);
        }

        adapter.setData(datas);
        listView.removeFooterView(footerView);
        isLoading = false;
    }


    /****
     * 初始化日期选择器
     **/
    private void initDateSelect() {
        llWeek1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDateOrigan();
                tvWeek1.setTextColor(getResources().getColor(R.color.white));
                ivDate1.setImageResource(R.mipmap.icon_1_on);
            }
        });

        llWeek2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDateOrigan();
                tvWeek2.setTextColor(getResources().getColor(R.color.white));
                ivDate2.setImageResource(R.mipmap.icon_2_on);
            }
        });

        llWeek3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDateOrigan();
                tvWeek3.setTextColor(getResources().getColor(R.color.white));
                ivDate3.setImageResource(R.mipmap.icon_3_on);
            }
        });

        llWeek4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDateOrigan();
                tvWeek4.setTextColor(getResources().getColor(R.color.white));
                ivDate4.setImageResource(R.mipmap.icon_4_on);
            }
        });

        llWeek5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDateOrigan();
                tvWeek5.setTextColor(getResources().getColor(R.color.white));
                ivDate5.setImageResource(R.mipmap.icon_5_on);
            }
        });

        llWeek6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDateOrigan();
                tvWeek6.setTextColor(getResources().getColor(R.color.white));
                ivDate6.setImageResource(R.mipmap.icon_6_on);
            }
        });

        llWeek7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initDateOrigan();
                tvWeek7.setTextColor(getResources().getColor(R.color.white));
                ivDate7.setImageResource(R.mipmap.icon_7_on);
            }
        });
    }

    private void initDateOrigan() {
        tvWeek1.setTextColor(getResources().getColor(R.color.text_color_gray));
        tvWeek2.setTextColor(getResources().getColor(R.color.text_color_gray));
        tvWeek3.setTextColor(getResources().getColor(R.color.text_color_gray));
        tvWeek4.setTextColor(getResources().getColor(R.color.text_color_gray));
        tvWeek5.setTextColor(getResources().getColor(R.color.text_color_gray));
        tvWeek6.setTextColor(getResources().getColor(R.color.text_color_gray));
        tvWeek7.setTextColor(getResources().getColor(R.color.text_color_gray));
        ivDate1.setImageResource(R.mipmap.icon_1);
        ivDate2.setImageResource(R.mipmap.icon_2);
        ivDate3.setImageResource(R.mipmap.icon_3);
        ivDate4.setImageResource(R.mipmap.icon_4);
        ivDate5.setImageResource(R.mipmap.icon_5);
        ivDate6.setImageResource(R.mipmap.icon_6);
        ivDate7.setImageResource(R.mipmap.icon_7);


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ORDER_TIME && resultCode == OrderReserveActivity.SELECT_VALUE_OVER) {
            if (!TextUtils.isEmpty(data.getExtras().getString(Constants.PASS_STING))) {
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
        conditionSelectPop
                .anchorView(rlConditionHead)
                .offset(0, -15)
                .gravity(Gravity.BOTTOM)
//                .showAnim(new BounceBottomEnter())
//                .dismissAnim(new SlideBottomExit())
                .dimEnabled(false)
                .show();

        conditionSelectPop.setCanceledOnTouchOutside(false);
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

        @Override
        public void onBackPressed() {
            ivCoverBg.setVisibility(View.GONE);
            super.onBackPressed();
        }
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
        Button btnTimeAm;
        Button btnTimePm;
        Button btnTimeNight;
        Button btnTimeAll;
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
            btnTimeAm = (Button) inflate.findViewById(R.id.btn_time_am);
            btnTimePm = (Button) inflate.findViewById(R.id.btn_time_pm);
            btnTimeNight = (Button) inflate.findViewById(R.id.btn_time_night);
            btnTimeAll = (Button) inflate.findViewById(R.id.btn_time_all);
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
                }
            });

            btnGirl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetSex();
                    btnGirl.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                }
            });

            btnSex.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetSex();
                    btnSex.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                }
            });

            btnLessFifty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPrice();
                    v.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                }
            });
            btnLessTwoHundred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPrice();
                    v.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                }
            });

            btnLessFiveHundred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPrice();
                    v.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                }
            });

            btnMoreFiveHundred.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPrice();
                    v.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                }
            });

            btnPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPrice();
                    v.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                }
            });

            btnTimeAm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetTime();
                    v.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                }
            });
            btnTimePm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetTime();
                    v.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                }
            });

            btnTimeNight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetTime();
                    v.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                }
            });

            btnTimeAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetTime();
                    v.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                }
            });

            btnReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetPrice();
                    resetTime();
                    resetSex();
                    btnSex.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                    btnPrice.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                    btnTimeAll.setBackgroundResource(R.drawable.bg_dialog_selector_gray);
                }
            });

            btnSure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ivCoverBg.setVisibility(View.GONE);
                    conditionSelectPop.dismiss();
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

        private void resetTime() {
            btnTimeAm.setBackgroundResource(R.drawable.bg_dialog_selector_white);
            btnTimeAll.setBackgroundResource(R.drawable.bg_dialog_selector_white);
            btnTimePm.setBackgroundResource(R.drawable.bg_dialog_selector_white);
            btnTimeNight.setBackgroundResource(R.drawable.bg_dialog_selector_white);
        }
    }


}
