package com.smartfit.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.MessageEvent.UpdatePrivateClassDetail;
import com.smartfit.MessageEvent.UpdateRoom;
import com.smartfit.R;
import com.smartfit.adpters.PrivateEducationOrderAdapter;
import com.smartfit.beans.IdleClassListInfo;
import com.smartfit.beans.PrivateClassOrderInfo;
import com.smartfit.beans.PrivateEducationClass;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;
import com.smartfit.utils.Util;
import com.smartfit.views.MyListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;

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
    private PrivateEducationOrderAdapter adapter;
    private ArrayList<PrivateEducationClass> privateEducationClasses;
    private IdleClassListInfo idleClassListInfo;
    private String startTime, endTime;

    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_private_education_class);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTittle.setText(getString(R.string.private_education));
        privateEducationClasses = getIntent().getParcelableArrayListExtra(Constants.PASS_OBJECT);
        idleClassListInfo = (IdleClassListInfo) getIntent().getSerializableExtra(Constants.PASS_IDLE_CLASS_INFO);
        adapter = new PrivateEducationOrderAdapter(this, privateEducationClasses);
        listView.setAdapter(adapter);
        startTime = getIntent().getStringExtra("start");
        endTime = getIntent().getStringExtra("end");

        tvClassTime.setText(startTime + " ~ " + DateUtils.getDataType(endTime));
        ImageLoader.getInstance().displayImage(idleClassListInfo.getVenueUrl(), ivSpaceIcon, Options.getListOptions());
        if (!TextUtils.isEmpty(idleClassListInfo.getLat()) && !TextUtils.isEmpty(idleClassListInfo.getLongit())) {
            tvSpaceInfo.setText(Util.getDistance(idleClassListInfo.getLat(),idleClassListInfo.getLongit()));
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
                openActivity(OtherCustomeMainActivity.class,bundle);
            }
        });
    }

    @Subscribe
    public void onEvent(Object event) {/* Do something */
        if (event instanceof UpdateRoom) {
            initRoom(((UpdateRoom) event).getPositon());
        }
        if (event instanceof UpdatePrivateClassDetail){
            btnOrder.setVisibility(View.INVISIBLE);
            tvWaitingAccept.setVisibility(View.VISIBLE);
        }
    }

    private void initRoom(int positon) {


        if (!TextUtils.isEmpty(idleClassListInfo.getClassroomList().get(positon).getClassroomName())) {
            tvRoomName.setText(idleClassListInfo.getClassroomList().get(positon).getClassroomName());
        }

        tvRoomTime.setText(idleClassListInfo.getClassroomList().get(positon).getClassroomPrice() + "/小时");
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


        if (!TextUtils.isEmpty(idleClassListInfo.getClassroomList().get(positon).getClassroomPrice())) {
            monney += Float.parseFloat(idleClassListInfo.getClassroomList().get(positon).getClassroomPrice());
        }
        tvClassPrice.setText(String.format("%.2f",monney * Float.parseFloat(DateUtils.getHour(startTime, endTime))));
    }


    @OnClick({R.id.tv_room_time, R.id.btn_order,R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_room_time:
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PASS_IDLE_CLASS_INFO, idleClassListInfo);
                openActivity(SelectVenueRoomsActivity.class, bundle);
                break;
            case R.id.btn_order:
                orderPrivateClass();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
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
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.PAGE_INDEX, 3);
                    bundle.putString(Constants.COURSE_ID, privateClassOrderInfo.getCourseId());
                    bundle.putString(Constants.COURSE_MONEY, tvClassPrice.getText().toString());
                    bundle.putString(Constants.COURSE_TYPE,"2");
                    openActivity(PayActivity.class, bundle);
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
