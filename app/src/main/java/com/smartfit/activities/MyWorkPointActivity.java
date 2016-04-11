package com.smartfit.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.adpters.WorkPointAdapter;
import com.smartfit.beans.UserInfo;
import com.smartfit.beans.WorkPoint;
import com.smartfit.commons.Constants;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;
import com.smartfit.views.MyListView;

import java.util.ArrayList;
import java.util.List;

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
                        dialog.dismiss();
                        datas.remove(posiotn);
                        adapter.setData(datas);

                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_work_point);
        ButterKnife.bind(this);
        initView();
        getCoachWorkPlaces();
        addLisener();
    }

    private void getCoachWorkPlaces() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.ClearCancel);
        PostRequest request = new PostRequest(Constants.WORKSPACE_LIST, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {

                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage());
            }
        });
        request.setTag(new Object());
        request.headers = NetUtil.getRequestBody(MyWorkPointActivity.this);
        mQueue.add(request);
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
                WorkPoint workPoint = new WorkPoint();
                workPoint.setName("我的工作"+(datas.size()+1));
                workPoint.setAddress(getString(R.string.click_setting));
                workPoint.setTime(getString(R.string.click_setting));
                datas.add(workPoint);
                adapter.setData(datas);
            }
        });

    }
}
