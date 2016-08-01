package com.smartfit.activities;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.smartfit.MessageEvent.UpdatePrivateClassDetail;
import com.smartfit.MessageEvent.UpdateRoom;
import com.smartfit.R;
import com.smartfit.adpters.PrivateEducationOrderAdapter;
import com.smartfit.beans.CashTickeInfo;
import com.smartfit.beans.ClassInfoDetail;
import com.smartfit.beans.IdleClassListInfo;
import com.smartfit.beans.LingyunListInfo;
import com.smartfit.beans.LinyuCourseInfo;
import com.smartfit.beans.LinyuRecord;
import com.smartfit.beans.PrivateClassOrderInfo;
import com.smartfit.beans.PrivateEducationClass;
import com.smartfit.beans.TicketInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.SharedPreferencesUtils;
import com.smartfit.utils.Util;
import com.smartfit.views.MyListView;
import com.smartfit.views.ShareBottomDialog;
import com.smartfit.wxapi.WXEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 开试预约私教课程
 */
public class OrderPrivateEducationClassActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.listView)
    MyListView listView;
    @Bind(R.id.iv_space_icon)
    ImageView ivSpaceIcon;
    @Bind(R.id.tv_space_name)
    TextView tvSpaceName;
    @Bind(R.id.tv_space_info)
    TextView tvSpaceInfo;
    @Bind(R.id.tv_class_time)
    TextView tvClassTime;
    @Bind(R.id.tv_room_name)
    TextView tvRoomName;
    @Bind(R.id.tv_room_time)
    TextView tvRoomTime;
    @Bind(R.id.tv_class_price)
    TextView tvClassPrice;
    @Bind(R.id.btn_order)
    Button btnOrder;
    @Bind(R.id.tv_waiting_accept)
    TextView tvWaitingAccept;
    @Bind(R.id.tv_class_name)
    TextView tvClassName;
    @Bind(R.id.iv_scan_bar)
    ImageView ivScanBar;
    @Bind(R.id.ll_view_scan_code)
    LinearLayout llViewScanCode;
    @Bind(R.id.tv_scan_code_info)
    TextView tvScanCodeInfo;
    @Bind(R.id.tv_save_to_phone)
    TextView tvSaveToPhone;
    @Bind(R.id.tv_share_friends)
    TextView tvShareFriends;
    @Bind(R.id.ll_scan_bar)
    LinearLayout llScanBar;
    @Bind(R.id.iv_send_red)
    ImageView ivSendRed;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    private PrivateEducationOrderAdapter adapter;
    private ArrayList<PrivateEducationClass> privateEducationClasses;
    private IdleClassListInfo idleClassListInfo;
    private String startTime, endTime;

    private EventBus eventBus;

    private String courseId;

    private boolean isInclueAreaFee;//1.0.3 接口中的教练价格 已包含产地费  如果是 true 则不需要再加场地费用
    private String cashEventId;
    private String cashEventName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_private_education_class);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        ButterKnife.bind(this);
        initView();
        addLisener();
        showCashTicketDialog();

    }

    /**
     * 获取现金券id
     * 0:团操课
     * <p/>
     * 1:小班课
     * <p/>
     * 2:私教课
     * <p/>
     * 3:器械课
     * <p/>
     * 4:月卡
     */
    private void showCashTicketDialog() {
        Map<String, String> data = new HashMap<>();
        data.put("orgType", "2");
        data.put("orgId",courseId);
        PostRequest request = new PostRequest(Constants.EVENT_GETAVAILABLECASHEVENT, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                CashTickeInfo cashTickeInfo = JsonUtils.objectFromJson(response, CashTickeInfo.class);
                if (cashTickeInfo != null && !TextUtils.isEmpty(cashTickeInfo.getId())) {
                    cashEventId = cashTickeInfo.getId();
                    cashEventName = cashTickeInfo.getCashEventName();
                    ivSendRed.setVisibility(View.VISIBLE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(OrderPrivateEducationClassActivity.this);
        mQueue.add(request);
    }

    /**
     * 获取现金券内容
     *
     * @param shareId
     */
    private void getShareContent(String shareId) {
        Map<String, String> data = new HashMap<>();
        data.put("shareId", shareId);
        data.put("mobileNo", SharedPreferencesUtils.getInstance().getString(Constants.ACCOUNT, "0"));
        PostRequest request = new PostRequest(Constants.EVENT_GETSHAREDMAINPAGE, data, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.dismiss();
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(OrderPrivateEducationClassActivity.this);
        mQueue.add(request);
    }


    private void initView() {
        tvTittle.setText(getString(R.string.private_education));
        privateEducationClasses = getIntent().getParcelableArrayListExtra(Constants.PASS_OBJECT);
        idleClassListInfo = (IdleClassListInfo) getIntent().getSerializableExtra(Constants.PASS_IDLE_CLASS_INFO);

        startTime = getIntent().getStringExtra("start");
        endTime = getIntent().getStringExtra("end");
        isInclueAreaFee = getIntent().getBooleanExtra("is_inclue_area_fee", false);

        tvClassTime.setText(startTime + " ~ " + DateUtils.getDataType(endTime));
        ImageLoader.getInstance().displayImage(idleClassListInfo.getVenueUrl(), ivSpaceIcon, Options.getListOptions());
        if (!TextUtils.isEmpty(idleClassListInfo.getLat()) && !TextUtils.isEmpty(idleClassListInfo.getLongit())) {
            tvSpaceInfo.setText(Util.getDistance(idleClassListInfo.getLat(), idleClassListInfo.getLongit()));
        }
        if (!TextUtils.isEmpty(idleClassListInfo.getVenueName())) {
            tvSpaceName.setText(idleClassListInfo.getVenueName());
        }
        initRoom(0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, privateEducationClasses.get(position).getUid());
                openActivity(OtherCustomeMainActivity.class, bundle);
            }
        });
    }

    @Subscribe
    public void onEvent(Object event) {/* Do something */
        if (event instanceof UpdateRoom) {
            initRoom(((UpdateRoom) event).getPositon());
        }
        if (event instanceof UpdatePrivateClassDetail) {
            btnOrder.setVisibility(View.INVISIBLE);
            tvWaitingAccept.setVisibility(View.VISIBLE);
            getCourseCode();
            showCashTicketDialog();
        }
    }

    private String codeBar;

    private void getCourseCode() {
        if (!TextUtils.isEmpty(courseId)) {
            Map<String, String> data = new HashMap<>();
            data.put("courseId", courseId);
            data.put("courseType", "2");
            PostRequest request = new PostRequest(Constants.SEARCH_CLASS_DETAIL, data, new Response.Listener<JsonObject>() {
                @Override
                public void onResponse(JsonObject response) {
                    ClassInfoDetail detail = JsonUtils.objectFromJson(response.toString(), ClassInfoDetail.class);
                    if (!TextUtils.isEmpty(detail.getQrcodeUrl())) {
                        llScanBar.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(detail.getQrcodeUrl(), ivScanBar, Options.getListOptions());
                        codeBar = detail.getQrcodeUrl();
                    }
                    if (!TextUtils.isEmpty(detail.getOrderStatus())) {

                        if (DateUtils.isQeWorked(detail.getStartTime())) {
                            llViewScanCode.setVisibility(View.VISIBLE);
                            tvScanCodeInfo.setVisibility(View.GONE);
                            tvSaveToPhone.setText(getString(R.string.save_to_phone));
                        } else {
                            llViewScanCode.setVisibility(View.GONE);
                            tvScanCodeInfo.setVisibility(View.VISIBLE);
                            tvScanCodeInfo.setText(String.format("课程二维码在开课前两个小时才会生效，您可以将如下链接保存：%1$s/sys/upload/qrCodeImg?courseId=%2$s&uid=%3$s", new Object[]{Constants.Net.URL, detail.getCourseId(), SharedPreferencesUtils.getInstance().getString(Constants.UID, "")}));
                            tvSaveToPhone.setText(getString(R.string.copy_link));
                        }

                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mSVProgressHUD.dismiss();

                }
            });
            request.setTag(new Object());
            request.headers = NetUtil.getRequestBody(OrderPrivateEducationClassActivity.this);
            mQueue.add(request);
        }

    }

    private void addLisener() {
        //保存手机
        tvSaveToPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvSaveToPhone.getText().equals(getString(R.string.copy_link))) {
                    Util.copyToClob(tvScanCodeInfo.getText().toString(), OrderPrivateEducationClassActivity.this);
                    mSVProgressHUD.showSuccessWithStatus("复制成功", SVProgressHUD.SVProgressHUDMaskType.Clear);
                } else if (!TextUtils.isEmpty(codeBar)) {
                    mSVProgressHUD.showWithStatus(getString(R.string.save_ing), SVProgressHUD.SVProgressHUDMaskType.Clear);
                    ImageLoader.getInstance().loadImage(codeBar, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {

                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            mSVProgressHUD.dismiss();
                            mSVProgressHUD.showInfoWithStatus("保存失败", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            mSVProgressHUD.dismiss();
                            Util.saveImageToPhoto(OrderPrivateEducationClassActivity.this, loadedImage);
                            mSVProgressHUD.showSuccessWithStatus("保存成功", SVProgressHUD.SVProgressHUDMaskType.Clear);

                        }

                        @Override
                        public void onLoadingCancelled(String imageUri, View view) {
                            mSVProgressHUD.dismiss();
                            mSVProgressHUD.showInfoWithStatus("保存失败", SVProgressHUD.SVProgressHUDMaskType.Clear);
                        }
                    });
                }
            }
        });

        ivSendRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCashDialog();
            }
        });
    }


    private void showCashDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(mContext).create();
        dialog.show();
        dialog.getWindow().setContentView(R.layout.dialog_cash_ticket_content);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        TextView tvTittle = (TextView) dialog.getWindow().findViewById(R.id.tv_tittle);
        dialog.getWindow().findViewById(R.id.cancel_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        dialog.getWindow().findViewById(R.id.commit_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

              /*  Bundle bundle = new Bundle();
                bundle.putString(Constants.PASS_STRING, cashEventId);
                bundle.putString("course_id", courseId);
                bundle.putString(Constants.TICKET_SHARE_TYPE, "2");
                ArrayList<TicketInfo> ticketInfos = new ArrayList<TicketInfo>();
                TicketInfo ticketInfo = new TicketInfo();
                ticketInfo.setEventTitle(cashEventName);
                ticketInfos.add(ticketInfo);
                bundle.putParcelableArrayList(Constants.PASS_OBJECT, ticketInfos);
                openActivity(WXEntryActivity.class, bundle);*/

                showShareWxDialog();

            }
        });
    }

    private void showShareWxDialog() {
        ShareBottomDialog dialog = new ShareBottomDialog(OrderPrivateEducationClassActivity.this, scrollView);
        dialog.showAnim(new BounceTopEnter())//
                .show();
    }

    private void initRoom(int positon) {


        if (!TextUtils.isEmpty(idleClassListInfo.getClassroomList().get(positon).getClassroomName())) {
            tvRoomName.setText(idleClassListInfo.getClassroomList().get(positon).getClassroomName());
        }


//        tvRoomTime.setText(idleClassListInfo.getClassroomList().get(positon).getClassroomPrice() + "/小时");
        countPrice(positon);
    }

    /**
     * 计算价格
     *
     * @param positon
     */
    private void countPrice(int positon) {
        float monney = 0;

        for (PrivateEducationClass item : privateEducationClasses) {
            if (monney < Float.parseFloat(item.getPrice())) {
                monney = Float.parseFloat(item.getPrice());
            }
        }


        if (!TextUtils.isEmpty(idleClassListInfo.getClassroomList().get(positon).getClassroomPrice()) && !isInclueAreaFee) {
            monney += Float.parseFloat(idleClassListInfo.getClassroomList().get(positon).getClassroomPrice());
        }


        for (PrivateEducationClass item : privateEducationClasses) {
            if (isInclueAreaFee) {
                item.setShowPrice(item.getPrice());
            } else {
                item.setShowPrice(String.valueOf(Float.parseFloat(item.getPrice()) + Float.parseFloat(idleClassListInfo.getClassroomList().get(positon).getClassroomPrice())));
            }
        }

        adapter = new PrivateEducationOrderAdapter(this, privateEducationClasses);
        listView.setAdapter(adapter);


        tvClassPrice.setText(String.format("%.2f", monney * Float.parseFloat(DateUtils.getHour(startTime, endTime))));
    }


    @OnClick({R.id.tv_room_time, R.id.btn_order, R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_room_time:
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PASS_IDLE_CLASS_INFO, idleClassListInfo);
                openActivity(SelectVenueRoomsActivity.class, bundle);
                break;
            case R.id.btn_order:
                getShowerRecord();

                break;
            case R.id.iv_back:
                finish();
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
                } else {
                    orderPrivateClass();
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
        request.headers = NetUtil.getRequestBody(OrderPrivateEducationClassActivity.this);
        mQueue.add(request);

    }

    /**
     * 生成淋浴订单
     *
     * @param listData
     */
    private void createLinyuOrder(final List<LinyuRecord> listData) {
        StringBuilder sbID = new StringBuilder();
        for (LinyuRecord item : listData) {
            sbID.append(item.getRecordId()).append("|");
        }
        Map<String, String> map = new HashMap<>();
        map.put("recordIdStr", sbID.toString());
        PostRequest request = new PostRequest(Constants.ORDER_ORDERSHOWER, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc==", response.toString());
                LinyuCourseInfo linyuCourseInfo = JsonUtils.objectFromJson(response, LinyuCourseInfo.class);
                if (linyuCourseInfo != null) {
                    Util.showLinyuRechagerDialog(OrderPrivateEducationClassActivity.this, linyuCourseInfo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LogUtil.w("dyc", error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(OrderPrivateEducationClassActivity.this);
        mQueue.add(request);

    }


    /**
     * 预约私教课
     */
    private void orderPrivateClass() {
        StringBuilder stringBuilder = new StringBuilder();
        for (PrivateEducationClass item : privateEducationClasses) {
            stringBuilder.append(item.getCoachId()).append("|");
        }


        HashMap<String, String> maps = new HashMap<>();
        maps.put("coachIdList", stringBuilder.toString());
        maps.put("startTime", String.valueOf(DateUtils.getTheDateTimeMillions(startTime)));
        maps.put("endTime", String.valueOf(DateUtils.getTheDateTimeMillions(endTime)));
        maps.put("latitude", idleClassListInfo.getLat());
        maps.put("longitude", idleClassListInfo.getLongit());
        maps.put("classroomId", idleClassListInfo.getClassroomList().get(0).getClassroomId());


        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        PostRequest request = new PostRequest(Constants.COACH_BESPOKECOACH, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                PrivateClassOrderInfo privateClassOrderInfo = JsonUtils.objectFromJson(response.toString(), PrivateClassOrderInfo.class);
                if (privateClassOrderInfo != null) {
                    courseId = privateClassOrderInfo.getCourseId();
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.PAGE_INDEX, 3);
                    bundle.putString(Constants.COURSE_ID, privateClassOrderInfo.getCourseId());
                    bundle.putString(Constants.COURSE_MONEY, tvClassPrice.getText().toString());
                    bundle.putString(Constants.COURSE_TYPE, "2");
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
        request.headers = NetUtil.getRequestBody(OrderPrivateEducationClassActivity.this);
        mQueue.add(request);


    }
}
