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
import com.smartfit.beans.UserInfo;
import com.smartfit.commons.Constants;
import com.smartfit.utils.JsonUtils;
import com.smartfit.utils.LogUtil;
import com.smartfit.utils.NetUtil;
import com.smartfit.utils.PostRequest;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach_brief);
        ButterKnife.bind(this);
        initView();
        getBriefInfo();
        addLisener();
    }

    /**
     * 获取简历信息
     */
    private void getBriefInfo() {
        PostRequest request = new PostRequest(Constants.COACH_GETRESUME, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
                LogUtil.w("dyc",response.toString());

                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage());
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

    private void doSubmit(String brief) {
        mSVProgressHUD.showWithStatus(getString(R.string.submit_ing), SVProgressHUD.SVProgressHUDMaskType.Clear);
        HashMap map = new HashMap();
        map.put("Id","");
        map.put("resumeContent",brief);
        PostRequest request = new PostRequest(Constants.COACH_UPDATERESUME, new Response.Listener<JsonObject>() {
            @Override
            public void onResponse(JsonObject response) {
               LogUtil.w("dyc",response.toString());

                mSVProgressHUD.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSVProgressHUD.showErrorWithStatus(error.getMessage());
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
