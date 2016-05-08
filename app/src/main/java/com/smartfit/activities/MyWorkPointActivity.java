package com.smartfit.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.JsonObject;
import com.smartfit.MessageEvent.UpdateWeekList;
import com.smartfit.R;
import com.smartfit.adpters.WorkPointAdapter;
import com.smartfit.beans.WorkPoint;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.MyListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 我的工作地点  （教练身份）
 */
public class MyWorkPointActivity extends BaseActivity {

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
    @Bind(R.id.tv_add_address)
    TextView tvAddAddress;

    private EventBus eventBus;


    private WorkPointAdapter adapter;
    private List<WorkPoint> datas = new ArrayList<>();

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            NormalDialogStyleTwo(msg.what);
            return false;
        }
    });


    private void NormalDialogStyleTwo(final int posiotn) {
        final NormalDialog dialog = new NormalDialog(MyWorkPointActivity.this);
        dialog.content("确定要删除吗？")//
                .style(NormalDialog.STYLE_TWO)//
                .titleTextSize(23)//
                        //.showAnim(mBasIn)//
                        //.dismissAnim(mBasOut)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        if (TextUtils.isEmpty(datas.get(posiotn).getWorkspaceCode())) {
                            dialog.dismiss();
                            datas.remove(posiotn);
                            adapter.setData(datas);
                        } else {
                            doRemoveItem(posiotn);
                            dialog.dismiss();
                        }
                    }
                });
    }

    private void doRemoveItem(final int posiotn) {
        Map<String,String> maps = new HashMap<>();
        maps.put("workspaceCode",datas.get(posiotn).getWorkspaceCode());
        mSVProgressHUD.showWithStatus("正在删除", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        final PostRequest request = new PostRequest(Constants.WORKSPACE_DELETE,maps, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                datas.remove(posiotn);
                updateTittles(datas);
                adapter.setData(datas);
                mSVProgressHUD.showSuccessWithStatus("已删除", SVProgressHUD.SVProgressHUDMaskType.Clear);
                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(MyWorkPointActivity.this);
        mQueue.add(request);
    }

    private void updateTittles(List<WorkPoint> datas) {
        for (int  i = 0;i<datas.size();i++){
            datas.get(i).setTittle("我的工作" + (i + 1));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_work_point);
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        ButterKnife.bind(this);
        initView();
        getCoachWorkPlaces();
        addLisener();
    }


    @Subscribe
    public void onEvent(Object event) {

        if (event instanceof UpdateWeekList) {
            datas.clear();
            getCoachWorkPlaces();
        }
    }

    private void getCoachWorkPlaces() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        final PostRequest request = new PostRequest(Constants.WORKSPACE_LIST, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                List<WorkPoint> workPoints = JsonUtils.listFromJson(response.getAsJsonArray("list"), WorkPoint.class);
                if (workPoints != null && workPoints.size() > 0) {
                    updateListView(workPoints);
                }
                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showInfoWithStatus(error.getMessage(), SVProgressHUD.SVProgressHUDMaskType.Clear);
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(MyWorkPointActivity.this);
        mQueue.add(request);
    }

    /**
     * 更新 工作地点列表
     *
     * @param workPoints
     */
    private void updateListView(List<WorkPoint> workPoints) {
        List<WorkPoint> lastWorkPoinst = new ArrayList<>();
        if (workPoints.size() > 0) {
            lastWorkPoinst.addAll(workPoints);
        }
        WorkPoint workPoint = new WorkPoint();
        workPoint.setTittle("我的工作" + (workPoints.size() + 1));
        workPoint.setStartTime(getString(R.string.click_setting));
        workPoint.setVenueName(getString(R.string.click_setting));
        lastWorkPoinst.add(workPoint);

        for (int i = 0; i < lastWorkPoinst.size(); i++) {
            lastWorkPoinst.get(i).setTittle("我的工作" + (i + 1));
        }

        datas = lastWorkPoinst;
        adapter.setData(lastWorkPoinst);
    }

    private void initView() {
        tvTittle.setText(getString(R.string.my_work));
        adapter = new WorkPointAdapter(this, datas, handler);
        listView.setAdapter(adapter);
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //添加新工作地点
        tvAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (datas.size()>5){
                    mSVProgressHUD.showInfoWithStatus("最多添加5个", SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
                    return;
                }
                WorkPoint workPoint = new WorkPoint();
                workPoint.setTittle("我的工作" + (datas.size() + 1));
                workPoint.setStartTime(getString(R.string.click_setting));
                workPoint.setVenueName(getString(R.string.click_setting));
                datas.add(workPoint);
                adapter.setData(datas);
            }
        });

    }
}
