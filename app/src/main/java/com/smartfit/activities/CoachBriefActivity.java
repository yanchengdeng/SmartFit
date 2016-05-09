package com.smartfit.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.google.gson.JsonObject;
import com.smartfit.R;
import com.smartfit.beans.BriefInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author dengyancheng
 *         教练简历
 */
public class CoachBriefActivity extends BaseActivity {

    @Bind(R.id.iv_back)
    ImageView ivBack;
    @Bind(R.id.tv_tittle)
    TextView tvTittle;
    @Bind(R.id.tv_function)
    TextView tvFunction;
    @Bind(R.id.iv_function)
    ImageView ivFunction;
    @Bind(R.id.et_breif)
    EditText etBreif;
    @Bind(R.id.btn_submmit)
    Button btnSubmmit;
    @Bind(R.id.tv_status)
    TextView tvStatus;
    private String id;

    private EventBus eventBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_brief);
        eventBus = EventBus.getDefault();
        ButterKnife.bind(this);
        initView();
        getBriefInfo();
        addLisener();
    }

    /**
     * 获取简历信息
     */
    private void getBriefInfo() {
        mSVProgressHUD.showWithStatus(getString(R.string.loading), SVProgressHUD.SVProgressHUDMaskType.Clear);
        PostRequest request = new PostRequest(Constants.COACH_GETRESUME, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc", response.toString());
                BriefInfo briefInfo = JsonUtils.objectFromJson(response, BriefInfo.class);
                if (briefInfo != null) {
                    id = briefInfo.getId();
                    if (!TextUtils.isEmpty(briefInfo.getResumeContent())) {
                        etBreif.setText(briefInfo.getResumeContent());
                    }
                    /**
                     * 1待审核；2审核通过；3审核不通过
                     */
                    if (!TextUtils.isEmpty(briefInfo.getStatus())) {
                        if (briefInfo.getStatus().equals("1")){
                            tvStatus.setText("您的简历正在等待审核...");
                        }else if(briefInfo.getStatus().equals("2")){
                            tvStatus.setText("您的简历已通过审核..");
                        }else{
                            tvStatus.setText("您的简历未通过审核，请重新上传...");
                        }
                    }
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
        request.headers = NetUtil.getRequestBody(CoachBriefActivity.this);
        mQueue.add(request);
    }

    private void addLisener() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSubmmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etBreif.getEditableText().toString())) {
                    mSVProgressHUD.showInfoWithStatus(getString(R.string.please_fill_brief), SVProgressHUD.SVProgressHUDMaskType.Clear);
                } else {
                    doSubmit(etBreif.getEditableText().toString());
                }
            }
        });
    }

    private void doSubmit(final String brief) {
        mSVProgressHUD.showWithStatus(getString(R.string.submit_ing), SVProgressHUD.SVProgressHUDMaskType.Clear);
        HashMap map = new HashMap();
        if (!TextUtils.isEmpty(id)) {
            map.put("id", id);
        }
        map.put("resumeContent", brief);
        PostRequest request = new PostRequest(Constants.COACH_UPDATERESUME, map, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc", response.toString());
                if (!TextUtils.isEmpty(id)) {
                    mSVProgressHUD.showSuccessWithStatus("已提交审核", SVProgressHUD.SVProgressHUDMaskType.Clear);
                }
                BriefInfo briefInfo = JsonUtils.objectFromJson(response, BriefInfo.class);
                if (briefInfo != null) {
                    if (!TextUtils.isEmpty(briefInfo.getStatus())) {
                        if (briefInfo.getStatus().equals("1")) {
                            eventBus.post("您的简历正在等待审核..");
                        } else if (briefInfo.getStatus().equals("2")) {
                            eventBus.post("您的简历已通过认证");
                        } else {
                            eventBus.post("您的简历未通过审核，请重新认证");
                        }
                    }
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
        request.headers = NetUtil.getRequestBody(CoachBriefActivity.this);
        mQueue.add(request);
    }

    private void initView() {
        tvTittle.setText(getString(R.string.coach_brief));
    }
}
