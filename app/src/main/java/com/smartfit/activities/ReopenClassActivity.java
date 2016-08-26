package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.MessageEvent.UpdateCoachClass;
import com.smartfit.R;
import com.smartfit.adpters.SelectDateAdapter;
import com.smartfit.beans.ClassInfoDetail;
import com.smartfit.beans.CustomeDate;
import com.smartfit.beans.LingyunListInfo;
import com.smartfit.beans.LinyuCourseInfo;
import com.smartfit.beans.LinyuRecord;
import com.smartfit.beans.ReopenClassInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.Util;
import com.smartfit.views.HorizontalListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 教练再次开课界面
 *
 * @author yanchengdeng
 *         create at 2016/5/4 18:19
 */
public class ReopenClassActivity extends BaseActivity {


    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.listview_date)
    HorizontalListView listviewDate;
    @Bind(R.id.tv_time)
    TextView tvTime;
    @Bind(R.id.rl_order_time)
    RelativeLayout rlOrderTime;
    @Bind(R.id.iv_icon)
    ImageView ivIcon;
    @Bind(R.id.rl_icon_ui)
    RelativeLayout rlIconUi;
    @Bind(R.id.tv_class_tittle)
    TextView tvClassTittle;
    @Bind(R.id.tv_couch)
    TextView tvCouch;
    @Bind(R.id.ratingBar)
    RatingBar ratingBar;
    @Bind(R.id.tv_join)
    TextView tvJoin;
    @Bind(R.id.tv_class_time)
    TextView tvClassTime;
    @Bind(R.id.ll_context_ui)
    RelativeLayout llContextUi;
    @Bind(R.id.tv_price)
    TextView tvPrice;
    @Bind(R.id.tv_price_info)
    TextView tvPriceInfo;
    @Bind(R.id.ll_price_ui)
    LinearLayout llPriceUi;
    @Bind(R.id.tv_reopen)
    TextView tvReopen;
    private String courserId;
    private String type;


    private List<CustomeDate> customeDates;
    private SelectDateAdapter selectDateAdapter;
    //选择日期    YYYY-MM-DD
    private String selectDate;

    private int REQUEST_CODE_ORDER_TIME = 0x118;

    private String startTime, endTime;

    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reopen_class);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        courserId = getIntent().getStringExtra(Constants.PASS_STRING);
        type = getIntent().getStringExtra("type");
        initView();
        loadData();
        initDateSelect();
    }

    @Subscribe
    public void onEvent(UpdateCoachClass event) {/* Do something */
        finish();
    }

    private void loadData() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> data = new HashMap<>();
        data.put("courseId", courserId);
        data.put("courseType", type);

        PostRequest request = new PostRequest(Constants.SEARCH_CLASS_DETAIL, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                ClassInfoDetail detail = JsonUtils.objectFromJson(response.toString(), ClassInfoDetail.class);
                initClassInfo(detail);
                mSVProgressHUD.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(ReopenClassActivity.this);
        mQueue.add(request);


    }

    private void initClassInfo(ClassInfoDetail detail) {
        if (!TextUtils.isEmpty(detail.getStars())) {
            ratingBar.setRating(Float.parseFloat(detail.getStars()));
        }

        if (!TextUtils.isEmpty(detail.getCoachRealName())) {
           tvCouch.setText("教练  " + detail.getCoachRealName());
        }

        if (!TextUtils.isEmpty(detail.getCourseName())) {
           tvClassTittle.setText(detail.getCourseName());
        }

//        if (!TextUtils.isEmpty(detail.getPersonCount()) && !TextUtils.isEmpty(detail.getClassroomPersonCount())) {
//           tvJoin.setText(detail.getPartCount() + "/" + detail.getClassroomPersonCount() + "人");
//        }

        if (!TextUtils.isEmpty(detail.getStartDate()) && !TextUtils.isEmpty(detail.getEndTime())) {
           tvClassTime.setText(DateUtils.getDataTime(detail.getStartDate())+ "-" + DateUtils.getDataTime(detail.getEndTime()));
        }


        if (!TextUtils.isEmpty(detail.getPrice())) {
           tvPrice.setText(detail.getPrice() + "元");
        }

        ImageLoader.getInstance().displayImage(detail.getVenueUrl(), ivIcon, Options.getListOptions());
        LinearLayout.LayoutParams  params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 24);
       ratingBar.setLayoutParams(params);
    }

    /****
     * 初始化日期选择器
     **/
    private void initDateSelect() {
        customeDates = DateUtils.getWeekInfo();
        selectDateAdapter = new SelectDateAdapter(customeDates, this);
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

        rlOrderTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReopenClassActivity.this, OrderReserveActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ORDER_TIME);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ORDER_TIME && resultCode == OrderReserveActivity.SELECT_VALUE_OVER) {
            if (!TextUtils.isEmpty(data.getExtras().getString("time_before")) && !TextUtils.isEmpty(data.getExtras().getString("time_after"))) {
                tvTime.setText(data.getExtras().getString("time_before") + " - " + data.getExtras().getString("time_after"));
                startTime = selectDate + " " + data.getExtras().getString("time_before");
                endTime = selectDate + " " + data.getExtras().getString("time_after");
            }

        }
    }


    private void initView() {
        tvTittle.setText(getString(R.string.reopen_class));
    }


    private void doReopenClass() {
        if (TextUtils.isEmpty(startTime) || TextUtils.isEmpty(endTime)) {
            mSVProgressHUD.showInfoWithStatus("请选择预约时间", SVProgressHUD.SVProgressHUDMaskType.Clear);
            return;
        }

        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> maps = new HashMap<>();
        maps.put("courseId", courserId);
        maps.put("startTime", String.valueOf(DateUtils.getTheDateTimeMillions(startTime)));
        maps.put("endTime", String.valueOf(DateUtils.getTheDateTimeMillions(endTime)));
        PostRequest request = new PostRequest(Constants.COURSE_COACHREOPENCOURSE, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                ReopenClassInfo reopenClassInfo = JsonUtils.objectFromJson(response,ReopenClassInfo.class);
                if (reopenClassInfo != null){
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.PAGE_INDEX, 5);//     再次开课    5   一样处理
                    bundle.putString(Constants.COURSE_ID, reopenClassInfo.getId());
                    bundle.putString(Constants.COURSE_MONEY, reopenClassInfo.getTotalPrice());
                    bundle.putString(Constants.COURSE_TYPE,type);
                    openActivity(ConfirmOrderCourseActivity.class, bundle);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(ReopenClassActivity.this);
        mQueue.add(request);

    }




    @OnClick({R.id.iv_back, R.id.rl_order_time, R.id.tv_reopen})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_order_time:
                break;
            case R.id.tv_reopen:
                getShowerRecord();

                break;
        }
    }

    /**
     * 获取淋浴欠费接口
     */
    private void getShowerRecord() {
        PostRequest request = new PostRequest(Constants.TBEVENTRECORD_GETSHOWERRECORD, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc==", response.toString());
                LingyunListInfo lingyunListInfo = JsonUtils.objectFromJson(response, LingyunListInfo.class);
                if (lingyunListInfo != null && lingyunListInfo.getListData() != null && lingyunListInfo.getListData().size() > 0) {
                    createLinyuOrder(lingyunListInfo.getListData());
                }else{
                    doReopenClass();
                }

                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                mSVProgressHUD.showErrorWithStatus(error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(ReopenClassActivity.this);
        mQueue.add(request);

    }

    /**
     * 生成淋浴订单
     *
     * @param listData
     */
    private void createLinyuOrder(final List<LinyuRecord> listData) {
        StringBuilder sbID =new StringBuilder();
        for (LinyuRecord item:listData){
            sbID.append(item.getRecordId()).append("|");
        }
        Map<String, String> map = new HashMap<>();
        map.put("recordIdStr", sbID.toString());
        PostRequest request = new PostRequest(Constants.ORDER_ORDERSHOWER, map,new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc==", response.toString());
                LinyuCourseInfo linyuCourseInfo = JsonUtils.objectFromJson(response,LinyuCourseInfo.class);
                if (linyuCourseInfo!=null){
                    Util.showLinyuRechagerDialog(ReopenClassActivity.this, linyuCourseInfo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.w("dyc", error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(ReopenClassActivity.this);
        mQueue.add(request);

    }

}
