package com.smartfit.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.smartfit.MessageEvent.FinishActivityAfterPay;
import com.smartfit.R;
import com.smartfit.adpters.MothActivityAdatper;
import com.smartfit.beans.NewMonthServerInfo;
import com.smartfit.beans.NewMouthServerEvent;
import com.smartfit.commons.Constants;
import com.smartfit.utils.DateStyle;
import com.smartfit.utils.DateUtils;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.Options;
import com.smartfit.utils.PostRequest;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 购买 包月活动
 *
 * @author yanchengdeng
 *         create at 2016/7/7 16:22
 */
public class EventActivityNewVersionActivity extends BaseActivity {


    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.iv_header)
    CircleImageView ivHeader;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_experi_time)
    TextView tvExperiTime;
    @Bind(R.id.tv_mouth_server_num)
    TextView tvMouthServerNum;
    @Bind(R.id.tv_select_num)
    TextView tvSelectNum;
    @Bind(R.id.tv_count_money_tips)
    TextView tvCountMoneyTips;
    @Bind(R.id.tv_count_money)
    TextView tvCountMoney;
    @Bind(R.id.rl_selectd_num_ui)
    RelativeLayout rlSelectdNumUi;
    @Bind(R.id.grid)
    GridView grid;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.btn_order)
    Button btnOrder;
    private int REQUST_CODE_MOUTH = 0x011;

    private EventBus eventBus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_activity_new_version);
        ButterKnife.bind(this);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        initView();
        getData();
    }

    @Subscribe
    public void onEvent(Object event) {
        if (event instanceof FinishActivityAfterPay) {
            finish();
        }
    }


    private void initView() {
        tvTittle.setText(getString(R.string.buy_month));

    }

    NewMonthServerInfo newMonthServerInfo;

    private void getData() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> maps = new HashMap<>();
        maps.put("useMonthRang", String.valueOf(mouth));
        PostRequest request = new PostRequest(Constants.EVENT_GETMONTHSELLBOARD, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                newMonthServerInfo = JsonUtils.objectFromJson(response, NewMonthServerInfo.class);
                if (newMonthServerInfo != null) {
                    fillData();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(EventActivityNewVersionActivity.this);
        mQueue.add(request);
    }


    private void fillData() {

        if (!TextUtils.isEmpty(newMonthServerInfo.getNickName())) {
            tvName.setText(newMonthServerInfo.getNickName());
        }
        if (!TextUtils.isEmpty(newMonthServerInfo.getMonthExpiredTime())) {
            if (newMonthServerInfo.getMonthExpiredTime().equals("0")) {
                tvExperiTime.setText(getString(R.string.you_hava_not_mouth_server));
            } else {
                tvExperiTime.setText(String.format("您的包月有效期至：%s", DateUtils.getData(newMonthServerInfo.getMonthExpiredTime(), DateStyle.YYYY_MM_DD.getValue())));
            }
        }

        ImageLoader.getInstance().displayImage(newMonthServerInfo.getUserPicUrl(), ivHeader, Options.getHeaderOptions());

        if (!TextUtils.isEmpty(newMonthServerInfo.getDefaultMonthPrice())) {
            tvCountMoney.setText("￥" + newMonthServerInfo.getDefaultMonthPrice());
        }

        if (newMonthServerInfo.getNewestMonthEvents() != null && newMonthServerInfo.getNewestMonthEvents().size() > 0) {
            ArrayList<NewMouthServerEvent> newMouthServerEvents = newMonthServerInfo.getNewestMonthEvents();
            List<NewMouthServerEvent> noOutDate = new ArrayList<>();
            for (NewMouthServerEvent itm : newMouthServerEvents) {
                if (!TextUtils.isEmpty(itm.getEventEndTime())) {
                    if (Long.parseLong(itm.getEventEndTime()) > (System.currentTimeMillis() / 1000)) {
                        noOutDate.add(itm);
                    }
                }
            }
            if (noOutDate.size() > 0) {

                grid.setAdapter(new MothActivityAdatper(EventActivityNewVersionActivity.this, newMouthServerEvents));
                grid.setVisibility(View.VISIBLE);
            } else {
                grid.setVisibility(View.GONE);
            }
//            setListViewHeightBasedOnChildren(grid);
        }
    }


    @OnClick({R.id.iv_back, R.id.rl_selectd_num_ui, R.id.btn_order})
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_selectd_num_ui:
                openActivity(SelectMouthActivity.class, REQUST_CODE_MOUTH);
                break;

            case R.id.btn_order:
                //目前购买包月
                if (newMonthServerInfo != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(Constants.PAGE_INDEX, 9);
                    bundle.putParcelable(Constants.PASS_OBJECT, newMonthServerInfo);
                    bundle.putInt(Constants.PASS_STRING, mouth);
                    openActivity(ConfirmPayActivity.class, bundle);
                }
                break;
        }
    }

    int mouth = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUST_CODE_MOUTH && resultCode == RESULT_OK) {
            if (data.getIntExtra(Constants.PASS_STRING, 0) != 0) {
                mouth = data.getIntExtra(Constants.PASS_STRING, 1);
                tvSelectNum.setText(String.format("数量*%d", mouth));
                tvCountMoney.setText(String.format ("￥%.2f",Float.parseFloat(newMonthServerInfo.getDefaultMonthPrice()) * mouth));
                updateMouthInfo();
            }
        }
    }

    private void updateMouthInfo() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        Map<String, String> maps = new HashMap<>();
        maps.put("useMonthRang", String.valueOf(mouth));
        PostRequest request = new PostRequest(Constants.EVENT_GETMONTHSELLBOARD, maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                mSVProgressHUD.dismiss();
                newMonthServerInfo = JsonUtils.objectFromJson(response, NewMonthServerInfo.class);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(EventActivityNewVersionActivity.this);
        mQueue.add(request);
    }
}
